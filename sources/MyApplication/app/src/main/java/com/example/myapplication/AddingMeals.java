package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddingMeals extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_meals);
        changeUI();
        fillUserData();
    }

    private void fillUserData() {
        // Odbieranie intencji, która uruchomiła tę aktywność
        Intent intent = getIntent();
        if (intent != null) {
            // Próba odczytania obiektu User przekazanego z poprzedniej aktywności
            User user = (User) intent.getSerializableExtra("userKey");
            if (user != null) {
                Log.d("AddingMeals", user.getName());
            }
        }
    }


    public void addMealToDatabase(View v) {
        Map<String, List<Meal>> dayMeal = new HashMap<>();
        List<Meal> meals = new ArrayList<>();
        dayMeal.put("Breakfast", meals);
        meals = new ArrayList<>();
        meals.add(new Lunch("Kebab", 250));
        dayMeal.put("Lunch", meals);
        meals = new ArrayList<>();
        meals.add(new Dinner("Biscuit", 500));
        dayMeal.put("Dinner", meals);
        MealDay newMealDay = new MealDay("04/04/2024", dayMeal);
        addOrUpdateMeal(newMealDay); // Dodajemy nowy dzień posiłku do bazy
    }

        public void changeUI () {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mainMenu);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Intent intent;
                    if (checkedId == R.id.Menu) {
                        intent = new Intent(AddingMeals.this, AddingMeals.class);
                        startActivity(intent);
                    } else if (checkedId == R.id.Recipes) {
                        intent = new Intent(AddingMeals.this, RecipesUI.class);
                        startActivity(intent);
                    } else if (checkedId == R.id.More) {
                        intent = new Intent(AddingMeals.this, MoreUI.class);
                        startActivity(intent);
                    }
                }
            });
        }


    private void addOrUpdateMeal(MealDay newMealDay) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId())
                                    .collection("mealDays").add(newMealDay)
                                    .addOnSuccessListener(documentReference -> Log.d("AddingMeals",
                                            "MealDay successfully added!"))
                                    .addOnFailureListener(e -> Log.e("AddingMeals", "Error adding MealDay", e));
                        }
                    }
                });
    }

    }

