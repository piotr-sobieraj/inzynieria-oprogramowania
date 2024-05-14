package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Start extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            reload(currentUser);
        }
        else
            setContentView(R.layout.start);
    }

    public void loginUI(View v){
        Intent intent = new Intent(Start.this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View v){
        Intent intent = new Intent(Start.this, SignUp.class);
        startActivity(intent);
    }
    private void reload(FirebaseUser currentUser) {

        Intent intent = new Intent(Start.this, Menu.class);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        if (currentUser != null) {
            usersRef.whereEqualTo("userUID", currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        for(QueryDocumentSnapshot documentUser: task.getResult()){
                            intent.putExtra("userObject", documentUser.toObject(User.class));
                            db.collection("users").document(documentUser.getId()).collection("mealDays").get()
                                    .addOnCompleteListener(task1 -> {
                                        ArrayList<MealDay> listMealDay = new ArrayList<>();
                                        for(QueryDocumentSnapshot documentMealDay: task1.getResult()){
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            listMealDay.add(objectMapper.convertValue(documentMealDay.getData(), MealDay.class));
                                            intent.putExtra("listOfMealDayObjects", listMealDay);
                                            db.collection("users").document(documentUser.getId()).collection("recentMeal").get()
                                                    .addOnCompleteListener(task2 -> {
                                                        RecentMeal recentMeal;
                                                        for(QueryDocumentSnapshot documentRecentMeal: task2.getResult()){
                                                            recentMeal = objectMapper.convertValue(documentRecentMeal.getData(), RecentMeal.class);
                                                            intent.putExtra("recentMealObject", recentMeal);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
        }
//        Database.getMealDayFromDatabase(mealDayObject -> );
//        Database.getRecentMealFromDatabase(recentMealObject -> );




    }
}