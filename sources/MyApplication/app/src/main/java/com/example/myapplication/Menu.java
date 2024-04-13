package com.example.myapplication;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Menu extends AppCompatActivity {
    public static String typeOfMeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        changeUI();
        getTypeOfDay();
        readMealDayFromDatabase();
    }


    public void changeUI() {
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(Menu.this, Menu.class);
                    startActivity(intent);
                } else if (checkedId == R.id.Recipes) {
                    intent = new Intent(Menu.this, RecipesUI.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(Menu.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }
    public void getTypeOfDay(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE", new Locale("en"));
        int radioButtonID = getResources().getIdentifier("radioButton" + simpleDateFormat.format(date), "id", getPackageName());
        RadioButton radioButton = (RadioButton) findViewById(radioButtonID);
        radioButton.setChecked(true);
    }
    public void deleteFromDatabase(String mealName, String nameProduct, String caloricValue){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).collection("mealDays").get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()){
                                            for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                MealDay mealDay = objectMapper.convertValue(documentSnapshot.getData(), MealDay.class);
                                                List<Meal> mealList = mealDay.getMeals().get(mealName);
                                                if (mealList == null) {
                                                    mealList = new ArrayList<>();
                                                }
                                                for(Meal meal:mealList){
                                                    if (Objects.equals(meal.name, nameProduct) && meal.caloricValue == Integer.parseInt(caloricValue)){
                                                        mealList.remove(meal);
                                                        break;
                                                    }
                                                }
                                                Map<String, List<Meal>> mealMap = mealDay.getMeals();
                                                Set<String> keys = mealMap.keySet();
                                                for (String key: keys){
                                                    if (Objects.equals(key, mealName)){
                                                        mealMap.put(key, mealList);
                                                    }
                                                    else {
                                                        mealMap.put(key, mealMap.get(key));
                                                    }
                                                }
                                                mealDay.setMeals(mealMap);
                                                db.collection("users").document(document.getId()).collection("mealDays").document(documentSnapshot.getId()).update("meals", mealMap).addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()){
                                                        Log.d("UPDATE", "Update document successful");
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
                    else{
                        Log.d("SELECT", "Failed to search for user" + task.getException());
                        Toast.makeText(this, "Failed to search for user",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void readMealDayFromDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).collection("mealDays").get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        MealDay mealDay = objectMapper.convertValue(documentSnapshot.getData(), MealDay.class);
                                        Map<String, List<Meal>> map = mealDay.getMeals();
                                        Set<String> keys = mealDay.getMeals().keySet();
                                        for (String key: keys){
                                            List<Meal> mealList = map.get(key);
                                            if (mealList == null) {
                                                mealList = new ArrayList<>();
                                            }
                                            for (Meal meal:mealList){
                                                addCardProduct(key, meal.name, String.valueOf(meal.caloricValue));
                                            }
                                        }
                                    }
                                }
                                else {
                                    Log.d("SELECT", "Failed to search for meals " + task1.getException());
                                    Toast.makeText(this, "Failed to search for meals",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else {
                        Log.d("SELECT", "Failed to search for user " + task.getException());
                        Toast.makeText(this, "Failed to search for user",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void addCardProduct(String mealName, String productName, String caloricValue){
        View view = getLayoutInflater().inflate(R.layout.product_card, null);
        TextView product = view.findViewById(R.id.product);
        product.setText(productName);
        TextView calories = view.findViewById(R.id.calories);
        calories.setText(caloricValue);
        Button delete = view.findViewById(R.id.delete);
        LinearLayout layout = findViewById(getResources().getIdentifier("container"+mealName, "id", getPackageName()));
        delete.setOnClickListener(v -> {
            layout.removeView(view);
            TextView cal = findViewById(getResources().getIdentifier("kcal"+mealName, "id", getPackageName()));
            String s = (Integer.parseInt(cal.getText().toString().split(" ")[0]) - Integer.parseInt(Objects.requireNonNull(caloricValue))) + " kcal";
            cal.setText(s);
            deleteFromDatabase(mealName, productName, caloricValue);
        });
        TextView cal = findViewById(getResources().getIdentifier("kcal"+mealName, "id", getPackageName()));
        String s = (Integer.parseInt(cal.getText().toString().split(" ")[0]) + Integer.parseInt(Objects.requireNonNull(caloricValue))) + " kcal";
        cal.setText(s);
        layout.addView(view);
    }


    public void addProductBreakfast(View v){
        typeOfMeal = "Breakfast";
        Intent intent = new Intent(Menu.this, NewProduct.class);
        startActivity(intent);
    }
    public void addProductSecondBreakfast(View v){
        typeOfMeal = "SecondBreakfast";
        Intent intent = new Intent(Menu.this, NewProduct.class);
        startActivity(intent);
    }
}