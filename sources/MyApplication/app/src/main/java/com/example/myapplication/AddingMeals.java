package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class AddingMeals extends AppCompatActivity {
    private User user;
    final String documentId = "wgNYXUW3ot9njNv5zqfJ"; //<- dokument (user) na sztywno!!!


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
            user = (User) intent.getSerializableExtra("userKey");
            if (user != null) {
                Log.d("Adding meal activity", user.getName());
            }
        }
    }

    private void addMealDayToDatabase(MealDay newMealDay) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(documentId)
                .collection("mealDays").add(newMealDay)
                .addOnSuccessListener(documentReference -> Log.d("AddingMeals", "MealDay successfully added!"))
                .addOnFailureListener(e -> Log.e("AddingMeals", "Error adding MealDay", e));
    }

    public void addMealToDatabase(View v) {
        List<Meal> mealsForToday = Arrays.asList(
                new Meal(1, "Śniadanie", 280),
                new Meal(2, "Lunch", 250),
                new Meal(3, "Podwieczorek", 500)
        );

        MealDay newMealDay = new MealDay("28/3/2024", mealsForToday);
        addMealDayToDatabase(newMealDay); // Dodajemy nowy dzień posiłku do bazy
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


    private void addOrUpdateMeal() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(documentId)
            .collection("mealDays")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        return;
                    } else {
                        return;
                    }
                }
            });
    }

    }

