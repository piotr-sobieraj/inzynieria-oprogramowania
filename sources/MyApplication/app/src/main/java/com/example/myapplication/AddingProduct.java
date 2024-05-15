package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddingProduct extends AppCompatActivity {

    private User user;
    private ArrayList<MealDay> listOfMealDays;
    private RecentMeal recentMeal;
    private TextView titleTextView;
    private List<Meal> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        user =(User) getIntent().getSerializableExtra("userObject");
        listOfMealDays =(ArrayList<MealDay>) getIntent().getSerializableExtra("listOfMealDayObjects");
        recentMeal = (RecentMeal) getIntent().getSerializableExtra("recentMealObject");
        setContentView(R.layout.adding_product);
        readFromObject();
        titleTextView = findViewById(R.id.typeOfMealTextView);
        titleTextView.setText(getIntent().getStringExtra("typeOfMeal"));
        TextView search = findViewById(R.id.searchProduct);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LinearLayout container = findViewById(R.id.container);
                container.removeAllViews();
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void addProduct(View view) {
        Intent intent = new Intent(AddingProduct.this, NewProduct.class);
        intent.putExtra("typeOfMeal", titleTextView.getText().toString());
        intent.putExtra("date", getIntent().getStringExtra("date"));
        intent.putExtra("userObject", user);
        intent.putExtra("listOfMealDayObjects", listOfMealDays);
        intent.putExtra("recentMealObject", recentMeal);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(AddingProduct.this, Menu.class);
        intent.putExtra("date", getIntent().getStringExtra("date"));
        intent.putExtra("userObject", user);
        intent.putExtra("listOfMealDayObjects", listOfMealDays);
        intent.putExtra("recentMealObject", recentMeal);
        startActivity(intent);
    }
    public void readFromObject(){
        Map<String, List<Meal>> map = recentMeal.getMeals();
        List<Meal> list = map.get(getIntent().getStringExtra("typeOfMeal"));
        mealList = list;
        if (list != null) {
            for(Meal meal: list){
                addRecentMeal(meal.name, meal.caloricValue, meal.fatsValue, meal.carbohydratesValue, meal.proteinsValue);
            }
        }
    }
    public void readFromDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            db.collection("users").document(document.getId()).collection("recentMeal").get()
                                    .addOnCompleteListener(task1 -> {
                                        for (QueryDocumentSnapshot documentSnapshot: task1.getResult()){
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            RecentMeal recentMeal = objectMapper.convertValue(documentSnapshot.getData(), RecentMeal.class);
                                            Map<String, List<Meal>> map = recentMeal.getMeals();
                                            List<Meal> list = map.get(getIntent().getStringExtra("typeOfMeal"));
                                            mealList = list;
                                            if (list != null) {
                                                for(Meal meal: list){
                                                    addRecentMeal(meal.name, meal.caloricValue, meal.fatsValue, meal.carbohydratesValue, meal.proteinsValue);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void addRecentMeal(String nameProduct, int cal, int fatsValue, int carbsValue, int proteinValue){
        View view = getLayoutInflater().inflate(R.layout.recent_product_card, null);
        LinearLayout layout = findViewById(R.id.container);
        Button add = view.findViewById(R.id.button11);
        add.setOnClickListener(v -> addProductToObject(nameProduct, cal, fatsValue, carbsValue, proteinValue));
        TextView name = view.findViewById(R.id.product);
        name.setText(nameProduct);
        TextView calorie = view.findViewById(R.id.calories);
        String tmp = cal + " kcal";
        calorie.setText(tmp);
        TextView fats = view.findViewById(R.id.fats);
        tmp = "fats: " + fatsValue + " g";
        fats.setText(tmp);
        TextView carbs = view.findViewById(R.id.carbohydrates);
        tmp = "carbs: " + carbsValue + " g";
        carbs.setText(tmp);
        TextView protein = view.findViewById(R.id.protein);
        tmp = "protein: " + proteinValue + " g";
        protein.setText(tmp);
        layout.addView(view);
    }

    public void addProductToObject(String nameProduct, int cal, int fatsValue, int carbsValue, int proteinValue){
        for (MealDay mealDay:listOfMealDays){
            if (Objects.equals(mealDay.getDate(), getIntent().getStringExtra("date"))){
                Map<String, List<Meal>> mapMeal = mealDay.getMeals();
                List<Meal> list = mapMeal.get(getIntent().getStringExtra("typeOfMeal"));
                if (list != null) {
                    list.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
                }
                else {
                    list = new ArrayList<>();
                    list.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
                }
                mapMeal.put(getIntent().getStringExtra("typeOfMeal"), list);
                mealDay.setMeals(mapMeal);
            }
            else {
                List<Meal> list = new ArrayList<>();
                Map<String, List<Meal>> map = new HashMap<>();
                list.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
                map.put(getIntent().getStringExtra("typeOfMeal"), list);
                listOfMealDays.add(new MealDay(getIntent().getStringExtra("date"),map));
                break;
            }
        }
        Intent intent = new Intent(AddingProduct.this, Menu.class);
        intent.putExtra("date", getIntent().getStringExtra("date"));
        intent.putExtra("userObject", user);
        intent.putExtra("listOfMealDayObjects", listOfMealDays);
        intent.putExtra("recentMealObject", recentMeal);
        startActivity(intent);
    }
    public void addProductToDatabase(String nameProduct, int cal, int fatsValue, int carbsValue, int proteinValue){
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
                                                    mealList.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
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
                                                                                                if (Objects.equals(meal.name, nameProduct) && meal.caloricValue == cal && meal.fatsValue == fatsValue && meal.carbohydratesValue == carbsValue && meal.proteinsValue == proteinValue) {
                                                                                                    find = true;
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        else {
                                                                                            list = new ArrayList<>();
                                                                                        }
                                                                                        if (!find) {
                                                                                            list.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
                                                                                            map.put(getIntent().getStringExtra("typeOfMeal"), list);
                                                                                            db.collection("users").document(document.getId()).collection("recentMeal").document(documentSnapshot2.getId()).update("meals", map)
                                                                                                    .addOnCompleteListener(task4 -> {
                                                                                                        if (task4.isSuccessful()) {
                                                                                                            Log.d("ADD", "Add document successful");
                                                                                                            Intent intent = new Intent(AddingProduct.this, Menu.class);
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
                                                                    Intent intent = new Intent(AddingProduct.this, Menu.class);
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
                                                mealList.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
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
                                                                                            if (Objects.equals(meal.name, nameProduct) && meal.caloricValue == cal && meal.fatsValue == fatsValue && meal.carbohydratesValue == carbsValue && meal.proteinsValue == proteinValue) {
                                                                                                find = true;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (!find) {
                                                                                        if (list != null) {
                                                                                            list.add(new Meal(nameProduct, cal, fatsValue, carbsValue, proteinValue));
                                                                                        }
                                                                                        map.put(getIntent().getStringExtra("typeOfMeal"), list);
                                                                                        db.collection("users").document(document.getId()).collection("recentMeal").document(documentSnapshot2.getId()).update("meals", map)
                                                                                                .addOnCompleteListener(task4 -> {
                                                                                                    if (task4.isSuccessful()) {
                                                                                                        Log.d("ADD", "Add document successful");
                                                                                                        Intent intent = new Intent(AddingProduct.this, Menu.class);
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

    public void search(String searchString){
        if (mealList != null) {
            for(Meal meal: mealList){
                if (meal.name.matches(searchString + "[A-z]*")){
                    addRecentMeal(meal.name, meal.caloricValue, meal.fatsValue, meal.carbohydratesValue, meal.proteinsValue);
                }
            }
        }
    }
}