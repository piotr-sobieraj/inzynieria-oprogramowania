package com.example.myapplication;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.text.ParseException;
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
    private TextView currentCalorie;
    private TextView calorieLimit;
    private TextView currentProtein;
    private TextView proteinLimit;
    private TextView currentFats;
    private TextView fatsLimit;
    private TextView currentCarbs;
    private TextView carbsLimit;
    private ProgressBar calorieProgressBar;
    private ProgressBar proteinProgressBar;

    private ProgressBar fatsProgressBar;
    private ProgressBar carbsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        currentCalorie = findViewById(R.id.currentCalorie);
        calorieLimit = findViewById(R.id.calorieLimit);
        proteinLimit = findViewById(R.id.proteinLimit);
        currentProtein = findViewById(R.id.currentProtein);
        currentFats = findViewById(R.id.currentFats);
        fatsLimit = findViewById(R.id.fatLimit);
        currentCarbs = findViewById(R.id.currentCarbs);
        carbsLimit = findViewById(R.id.carbsLimit);
        calorieProgressBar = findViewById(R.id.calorieProgres);
        proteinProgressBar = findViewById(R.id.proteinProgres);
        fatsProgressBar = findViewById(R.id.fatsProgres);
        carbsProgressBar = findViewById(R.id.carbsProgres);
        changeUI();
        fillLimits();
        setActualDate();
        readMealDayFromDatabase();
    }


    public void changeUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(Menu.this, Menu.class);
                    startActivity(intent);
                } else if (checkedId == R.id.WeightCalendar) {
                    intent = new Intent(Menu.this, WeightCalendar.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(Menu.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setActualDate(){
        if (getIntent().getStringExtra("date") == null){
            TextView date = findViewById(R.id.date);
            Calendar c = Calendar.getInstance();
            String stringDate = c.get(DAY_OF_MONTH) + "/" + (c.get(MONTH) + 1) + "/" + c.get(YEAR);
            date.setText(stringDate);
        }
        else {
            TextView date = findViewById(R.id.date);
            date.setText(getIntent().getStringExtra("date"));
        }
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
                                        TextView dateText = findViewById(R.id.date);
                                        if (Objects.equals(mealDay.getDate(), dateText.getText().toString())){
                                            Map<String, List<Meal>> map = mealDay.getMeals();
                                            Set<String> keys = mealDay.getMeals().keySet();
                                            for (String key: keys){
                                                List<Meal> mealList = map.get(key);
                                                if (mealList == null) {
                                                    mealList = new ArrayList<>();
                                                }
                                                for (Meal meal:mealList){
                                                    addCardProduct(key, meal.name, String.valueOf(meal.caloricValue), String.valueOf(meal.fatsValue), String.valueOf(meal.carbohydratesValue), String.valueOf(meal.proteinsValue));
                                                }
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

    public void addCardProduct(String mealName, String productName, String caloricValue, String fatsValue, String carbohydratesValue, String proteinsValue){
        View view = getLayoutInflater().inflate(R.layout.product_card, null);
        setProduct(view, productName, caloricValue, fatsValue, carbohydratesValue, proteinsValue);
        Button delete = view.findViewById(R.id.delete);
        int calValueLimit = Integer.parseInt(calorieLimit.getText().toString().split(" ")[0].substring(1));
        int proValueLimit = Integer.parseInt(proteinLimit.getText().toString().split(" ")[0].substring(1).split("-")[1]);
        int fatsValueLimit = Integer.parseInt(fatsLimit.getText().toString().split(" ")[0].substring(1));
        int carbsValueLimit = Integer.parseInt(carbsLimit.getText().toString().split(" ")[0].substring(1).split("-")[1]);
        LinearLayout layout = findViewById(getResources().getIdentifier("container"+mealName, "id", getPackageName()));
        delete.setOnClickListener(v -> {
            layout.removeView(view);
            setProgressOnDeleteProduct(calValueLimit, caloricValue, proValueLimit, proteinsValue, fatsValueLimit, fatsValue, carbsValueLimit, carbohydratesValue);
            TextView cal = findViewById(getResources().getIdentifier("kcal"+mealName, "id", getPackageName()));
            String[] splited = cal.getText().toString().split(" ");
            String s = (Integer.parseInt(splited[0]) - Integer.parseInt(Objects.requireNonNull(caloricValue))) + " kcal " + (Integer.parseInt(splited[2]) - Integer.parseInt(Objects.requireNonNull(fatsValue))) + " g " + (Integer.parseInt(splited[4]) - Integer.parseInt(Objects.requireNonNull(carbohydratesValue))) + " g " + (Integer.parseInt(splited[6]) - Integer.parseInt(Objects.requireNonNull(proteinsValue)))+ " g";
            cal.setText(s);
            deleteFromDatabase(mealName, productName, caloricValue);
        });
        setProgressOnAddProduct(calValueLimit, caloricValue, proValueLimit, proteinsValue, fatsValueLimit, fatsValue, carbsValueLimit, carbohydratesValue);
        TextView cal = findViewById(getResources().getIdentifier("kcal"+mealName, "id", getPackageName()));
        String[] splited = cal.getText().toString().split(" ");
        String s = (Integer.parseInt(splited[0]) + Integer.parseInt(Objects.requireNonNull(caloricValue))) + " kcal " + (Integer.parseInt(splited[2]) + Integer.parseInt(Objects.requireNonNull(fatsValue))) + " g " + (Integer.parseInt(splited[4]) + Integer.parseInt(Objects.requireNonNull(carbohydratesValue))) + " g " + (Integer.parseInt(splited[6]) + Integer.parseInt(Objects.requireNonNull(proteinsValue)))+ " g";
        cal.setText(s);
        layout.addView(view);
    }


    public void addProductBreakfast(View v){
        Intent intent = new Intent(Menu.this, AddingProduct.class);
        TextView dateText = findViewById(R.id.date);
        intent.putExtra("typeOfMeal", "Breakfast");
        intent.putExtra("date", dateText.getText().toString());
        startActivity(intent);
    }
    public void addProductSecondBreakfast(View v){
        Intent intent = new Intent(Menu.this, AddingProduct.class);
        TextView dateText = findViewById(R.id.date);
        intent.putExtra("typeOfMeal", "SecondBreakfast");
        intent.putExtra("date", dateText.getText().toString());
        startActivity(intent);
    }
    public void addProductLunch(View v){
        Intent intent = new Intent(Menu.this, AddingProduct.class);
        TextView dateText = findViewById(R.id.date);
        intent.putExtra("typeOfMeal", "Lunch");
        intent.putExtra("date", dateText.getText().toString());
        startActivity(intent);
    }
    public void addProductSnack(View v){
        Intent intent = new Intent(Menu.this, AddingProduct.class);
        TextView dateText = findViewById(R.id.date);
        intent.putExtra("typeOfMeal", "Snack");
        intent.putExtra("date", dateText.getText().toString());
        startActivity(intent);
    }
    public void addProductDinner(View v){
        Intent intent = new Intent(Menu.this, AddingProduct.class);
        TextView dateText = findViewById(R.id.date);
        intent.putExtra("typeOfMeal", "Dinner");
        intent.putExtra("date", dateText.getText().toString());
        startActivity(intent);
    }

    public void previousDay(View view) {
        TextView date = findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
        try {
            Date dateDate = sdf.parse(date.getText().toString());
            Calendar c = Calendar.getInstance();
            if (dateDate != null)
                c.setTime(dateDate);
            c.add(DAY_OF_MONTH, -1);
            String stringDate = c.get(DAY_OF_MONTH) + "/" + (c.get(MONTH) + 1) + "/" + c.get(YEAR);
            Intent intent = new Intent(Menu.this, Menu.class);
            intent.putExtra("date", stringDate);
            startActivity(intent);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void nextDay(View view) {
        TextView date = findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
        try {
            Date dateDate = sdf.parse(date.getText().toString());
            Calendar c = Calendar.getInstance();
            if (dateDate != null)
                c.setTime(dateDate);
            c.add(DAY_OF_MONTH, 1);
            String stringDate = c.get(DAY_OF_MONTH) + "/" + (c.get(MONTH) + 1) + "/" + c.get(YEAR);
            Intent intent = new Intent(Menu.this, Menu.class);
            intent.putExtra("date", stringDate);
            startActivity(intent);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public void setProduct(View view, String productName, String caloricValue, String fatsValue, String carbohydratesValue, String proteinsValue){
        TextView product = view.findViewById(R.id.product);
        product.setText(productName);
        TextView calories = view.findViewById(R.id.calories);
        String tmp = caloricValue + " kcal";
        calories.setText(tmp);
        TextView fats = view.findViewById(R.id.fats);
        tmp = fatsValue + " g";
        fats.setText(tmp);
        TextView carbohydrates = view.findViewById(R.id.carbohydrates);
        tmp = carbohydratesValue + " g";
        carbohydrates.setText(tmp);
        TextView proteins = view.findViewById(R.id.protein);
        tmp = proteinsValue + " g";
        proteins.setText(tmp);
    }
    public void setProgressOnDeleteProduct(int calValueLimit, String caloricValue, int proValueLimit, String proteinsValue, int fatsValueLimit, String fatsValue, int carbsValueLimit, String carbsValue){
        currentCalorie.setText(String.valueOf(Integer.parseInt(currentCalorie.getText().toString()) - Integer.parseInt(Objects.requireNonNull(caloricValue))));
        calorieProgressBar.setProgress((int)((Double.parseDouble(currentCalorie.getText().toString())/calValueLimit) * 100));
        currentProtein.setText(String.valueOf(Integer.parseInt(currentProtein.getText().toString()) - Integer.parseInt(Objects.requireNonNull(proteinsValue))));
        proteinProgressBar.setProgress((int)((Double.parseDouble(currentProtein.getText().toString())/proValueLimit) * 100));
        currentFats.setText(String.valueOf(Integer.parseInt(currentFats.getText().toString()) - Integer.parseInt(Objects.requireNonNull(fatsValue))));
        fatsProgressBar.setProgress((int)((Double.parseDouble(currentFats.getText().toString())/fatsValueLimit) * 100));
        currentCarbs.setText(String.valueOf(Integer.parseInt(currentCarbs.getText().toString()) - Integer.parseInt(Objects.requireNonNull(carbsValue))));
        carbsProgressBar.setProgress((int)((Double.parseDouble(currentCarbs.getText().toString())/carbsValueLimit) * 100));
    }
    public void setProgressOnAddProduct(int calValueLimit, String caloricValue, int proValueLimit, String proteinsValue, int fatsValueLimit, String fatsValue, int carbsValueLimit, String carbsValue){
        currentCalorie.setText(String.valueOf(Integer.parseInt(currentCalorie.getText().toString()) + Integer.parseInt(Objects.requireNonNull(caloricValue))));
        calorieProgressBar.setProgress((int)((Double.parseDouble(currentCalorie.getText().toString())/calValueLimit) * 100));
        currentProtein.setText(String.valueOf(Integer.parseInt(currentProtein.getText().toString()) + Integer.parseInt(Objects.requireNonNull(proteinsValue))));
        proteinProgressBar.setProgress((int)((Double.parseDouble(currentProtein.getText().toString())/proValueLimit) * 100));
        currentFats.setText(String.valueOf(Integer.parseInt(currentFats.getText().toString()) + Integer.parseInt(Objects.requireNonNull(fatsValue))));
        fatsProgressBar.setProgress((int)((Double.parseDouble(currentFats.getText().toString())/fatsValueLimit) * 100));
        currentCarbs.setText(String.valueOf(Integer.parseInt(currentCarbs.getText().toString()) + Integer.parseInt(Objects.requireNonNull(carbsValue))));
        carbsProgressBar.setProgress((int)((Double.parseDouble(currentCarbs.getText().toString())/carbsValueLimit) * 100));
    }

    public void fillLimits(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            ObjectMapper objectMapper = new ObjectMapper();
                            User userDB = objectMapper.convertValue(documentSnapshot.getData(), User.class);
                            currentCalorie.setText("0");
                            String calLim = "/" + userDB.getDailyCalorieLimit() + " kcal";
                            calorieLimit.setText(calLim);
                            currentProtein.setText("0");
                            if (Objects.equals(userDB.getSex(), "f")){
                                String proLim = "/" + (int)(0.45 * Integer.parseInt(userDB.getWeight())) + "-" + (int)(0.75 * Integer.parseInt(userDB.getWeight())) + " g";
                                proteinLimit.setText(proLim);
                            }
                            else {
                                String proLim = "/" + (int)(0.55 * Integer.parseInt(userDB.getWeight())) + "-" + (int)(0.85 * Integer.parseInt(userDB.getWeight())) + " g";
                                proteinLimit.setText(proLim);
                            }
                            currentFats.setText("0");
                            String fatLim = "/" + (int)(0.8 * Integer.parseInt(userDB.getWeight())) + " g";
                            fatsLimit.setText(fatLim);
                            currentCarbs.setText("0");
                            String carbsLim = "/" + (int)(0.45 * Integer.parseInt(userDB.getDailyCalorieLimit())/4) + "-" + (int)(0.65 * Integer.parseInt(userDB.getDailyCalorieLimit())/4) + " g";
                            carbsLimit.setText(carbsLim);
                        }
                    }
                });
    }
}