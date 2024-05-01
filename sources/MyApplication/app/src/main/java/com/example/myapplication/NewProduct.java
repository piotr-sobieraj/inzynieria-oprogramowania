package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
                   if (task.isSuccessful()){
                       for (QueryDocumentSnapshot document: task.getResult()){
                           db.collection("users").document(document.getId()).collection("mealDays").get()
                                   .addOnCompleteListener(task1 -> {
                                       if (task.isSuccessful()){
                                           boolean finded = false;
                                           for (QueryDocumentSnapshot documentSnapshot: task1.getResult()){
                                               ObjectMapper objectMapper = new ObjectMapper();
                                               MealDay mealDay = objectMapper.convertValue(documentSnapshot.getData(), MealDay.class);
                                               if (Objects.equals(mealDay.getDate(), getIntent().getStringExtra("date"))){
                                                   finded = true;
                                                   List<Meal> mealList = mealDay.getMeals().get(getIntent().getStringExtra("typeOfMeal"));
                                                   if (mealList == null){
                                                       mealList = new ArrayList<>();
                                                   }
                                                   mealList.add(new Meal(((TextView)findViewById(R.id.editText)).getText().toString(),
                                                           Integer.parseInt(((TextView)findViewById(R.id.editText2)).getText().toString()),
                                                                   Integer.parseInt(((TextView)findViewById(R.id.fatsText)).getText().toString()),
                                                                           Integer.parseInt(((TextView)findViewById(R.id.carbohydratesText)).getText().toString()),
                                                                                   Integer.parseInt(((TextView)findViewById(R.id.proteinsText)).getText().toString())));
                                                   Map<String, List<Meal>> mealMap = mealDay.getMeals();
                                                   mealMap.put(getIntent().getStringExtra("typeOfMeal"), mealList);
                                                   mealDay.setMeals(mealMap);
                                                   db.collection("users").document(document.getId()).collection("mealDays").document(documentSnapshot.getId()).update("meals", mealMap)
                                                           .addOnCompleteListener(task2 -> {
                                                               if (task2.isSuccessful()){
                                                                   db.collection("users").document(document.getId()).collection("recentMeal").get()
                                                                           .addOnCompleteListener(task3 -> {
                                                                               if (task3.isSuccessful()){
                                                                                   boolean find = false;
                                                                                   for (QueryDocumentSnapshot documentSnapshot2: task3.getResult()) {
                                                                                       RecentMeal recentMeal = objectMapper.convertValue(documentSnapshot2.getData(), RecentMeal.class);
                                                                                       Map<String, List<Meal>> map = recentMeal.getMeals();
                                                                                       List<Meal> list = map.get(getIntent().getStringExtra("typeOfMeal"));
                                                                                       if (list != null) {
                                                                                           for (Meal meal: list){
                                                                                               if (Objects.equals(meal.name, ((TextView) findViewById(R.id.editText)).getText().toString()) && meal.caloricValue == Integer.parseInt(((TextView) findViewById(R.id.editText2)).getText().toString()) && meal.fatsValue == Integer.parseInt(((TextView) findViewById(R.id.fatsText)).getText().toString()) && meal.carbohydratesValue == Integer.parseInt(((TextView) findViewById(R.id.carbohydratesText)).getText().toString()) && meal.proteinsValue == Integer.parseInt(((TextView) findViewById(R.id.proteinsText)).getText().toString())){
                                                                                                   find = true;
                                                                                               }
                                                                                           }
                                                                                       }
                                                                                       else {
                                                                                           list = new ArrayList<>();
                                                                                       }
                                                                                       if (!find) {
                                                                                           list.add(new Meal(((TextView) findViewById(R.id.editText)).getText().toString(),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.editText2)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.fatsText)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.carbohydratesText)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.proteinsText)).getText().toString())));
                                                                                           map.put(getIntent().getStringExtra("typeOfMeal"), list);
                                                                                           db.collection("users").document(document.getId()).collection("recentMeal").document(documentSnapshot2.getId()).update("meals", map)
                                                                                                   .addOnCompleteListener(task4 -> {
                                                                                                       if (task4.isSuccessful()) {
                                                                                                           Log.d("ADD", "Add document successful");
                                                                                                           Intent intent = new Intent(NewProduct.this, Menu.class);
                                                                                                           intent.putExtra("date", getIntent().getStringExtra("date"));
                                                                                                           startActivity(intent);
                                                                                                       } else {
                                                                                                           Log.d("ADD", "Document add failed" + task4.getException());
                                                                                                           Toast.makeText(this, "Document add failed",
                                                                                                                   Toast.LENGTH_SHORT).show();
                                                                                                       }
                                                                                                   });
                                                                                       }
                                                                                   }
                                                                               }
                                                                           });
                                                                   Log.d("UPDATE", "Update document successful");
                                                                   Intent intent = new Intent(NewProduct.this, Menu.class);
                                                                   intent.putExtra("date", mealDay.getDate());
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
                                           if (!finded){
                                               Map<String, List<Meal>> mealMap = new HashMap<>();
                                               List<Meal> mealList = new ArrayList<>();
                                               mealList.add(new Meal(((TextView)findViewById(R.id.editText)).getText().toString(),
                                                       Integer.parseInt(((TextView)findViewById(R.id.editText2)).getText().toString()),
                                                       Integer.parseInt(((TextView)findViewById(R.id.fatsText)).getText().toString()),
                                                       Integer.parseInt(((TextView)findViewById(R.id.carbohydratesText)).getText().toString()),
                                                       Integer.parseInt(((TextView)findViewById(R.id.proteinsText)).getText().toString())));
                                               mealMap.put(getIntent().getStringExtra("typeOfMeal"),mealList);
                                               MealDay mealDay = new MealDay(getIntent().getStringExtra("date"), mealMap);
                                               db.collection("users").document(document.getId()).collection("mealDays").add(mealDay)
                                                       .addOnCompleteListener(task2 -> {
                                                           if (task2.isSuccessful()){
                                                               db.collection("users").document(document.getId()).collection("recentMeal").get()
                                                                       .addOnCompleteListener(task3 -> {
                                                                           if (task3.isSuccessful()){
                                                                               boolean find = false;
                                                                               for (QueryDocumentSnapshot documentSnapshot2: task3.getResult()) {
                                                                                   ObjectMapper objectMapper = new ObjectMapper();
                                                                                   RecentMeal recentMeal = objectMapper.convertValue(documentSnapshot2.getData(), RecentMeal.class);
                                                                                   Map<String, List<Meal>> map = recentMeal.getMeals();
                                                                                   List<Meal> list = map.get(getIntent().getStringExtra("typeOfMeal"));
                                                                                   if (list != null) {
                                                                                       for (Meal meal: list){
                                                                                           if (Objects.equals(meal.name, ((TextView) findViewById(R.id.editText)).getText().toString()) && meal.fatsValue == Integer.parseInt(((TextView) findViewById(R.id.editText2)).getText().toString()) && meal.carbohydratesValue == Integer.parseInt(((TextView) findViewById(R.id.fatsText)).getText().toString()) && meal.proteinsValue == Integer.parseInt(((TextView) findViewById(R.id.proteinsText)).getText().toString())){
                                                                                               find = true;
                                                                                           }
                                                                                       }
                                                                                   }
                                                                                   if (!find) {
                                                                                       if (list != null) {
                                                                                           list.add(new Meal(((TextView) findViewById(R.id.editText)).getText().toString(),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.editText2)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.fatsText)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.carbohydratesText)).getText().toString()),
                                                                                                   Integer.parseInt(((TextView) findViewById(R.id.proteinsText)).getText().toString())));
                                                                                       }
                                                                                       map.put(getIntent().getStringExtra("typeOfMeal"), list);
                                                                                       db.collection("users").document(document.getId()).collection("recentMeal").document(documentSnapshot2.getId()).update("meals", map)
                                                                                               .addOnCompleteListener(task4 -> {
                                                                                                   if (task4.isSuccessful()) {
                                                                                                       Log.d("ADD", "Add document successful");
                                                                                                       Intent intent = new Intent(NewProduct.this, Menu.class);
                                                                                                       intent.putExtra("date", getIntent().getStringExtra("date"));
                                                                                                       startActivity(intent);
                                                                                                   } else {
                                                                                                       Log.d("ADD", "Document add failed" + task4.getException());
                                                                                                       Toast.makeText(this, "Document add failed",
                                                                                                               Toast.LENGTH_SHORT).show();
                                                                                                   }
                                                                                               });
                                                                                   }
                                                                               }
                                                                           }
                                                                       });
                                                           }
                                                           else {
                                                               Log.d("ADD", "Document add failed" + task2.getException());
                                                               Toast.makeText(this, "Document add failed",
                                                                       Toast.LENGTH_SHORT).show();
                                                           }
                                                       });
                                           }
                                       }
                                   });
                       }
                   }
               });
    }

    public void back(View view) {
        Intent intent = new Intent(NewProduct.this, AddingProduct.class);
        intent.putExtra("typeOfMeal", getIntent().getStringExtra("typeOfMeal"));
        intent.putExtra("date", getIntent().getStringExtra("date"));
        startActivity(intent);
    }
}