package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addProductToDatabase(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).collection("mealDays").get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task1.getResult()){
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        MealDay mealDay = objectMapper.convertValue(documentSnapshot.getData(), MealDay.class);
                                        List<Meal> mealList = mealDay.getMeals().get(Menu.typeOfMeal);
                                        if (mealList == null) {
                                            mealList = new ArrayList<>();
                                        }
                                        mealList.add(
                                                new Meal(
                                                        ((TextView)findViewById(R.id.editText)).getText().toString(),
                                                        Integer.parseInt(((TextView)findViewById(R.id.editText2)).getText().toString())));
                                        Map<String, List<Meal>> mealMap = mealDay.getMeals();
                                        mealMap.put(Menu.typeOfMeal, mealList);
                                        mealDay.setMeals(mealMap);
                                        db.collection("users").document(document.getId()).collection("mealDays").document(documentSnapshot.getId()).update("meals", mealMap).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                Log.d("UPDATE", "Update document successful");
                                                Intent intent = new Intent(NewProduct.this, Menu.class);
                                                intent.putExtra("createProductCard", true);
                                                intent.putExtra("nameProduct", ((TextView)findViewById(R.id.editText)).getText().toString());
                                                intent.putExtra("caloricProduct", ((TextView)findViewById(R.id.editText2)).getText().toString());
                                                startActivity(intent);
                                            }
                                            else {
                                                Log.d("UPDATE", "Document update failed" + task2.getException());
                                                Toast.makeText(this, "Document update failed",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                else {
                                    Log.d("SELECT", "Failed to search for meals" + task1.getException());
                                    Toast.makeText(this, "Failed to search for meals",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        Log.d("SELECT", "Failed to search for user" + task.getException());
                        Toast.makeText(this, "Failed to search for user",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}