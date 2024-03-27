package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class AddingMeals extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_meals);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        final String documentId = "wgNYXUW3ot9njNv5zqfJ";;

        db.collection("users").document(documentId)
                .collection("mealDays").add(newMealDay)
                .addOnSuccessListener(documentReference -> Log.d("AddingMeals", "MealDay successfully added!"))
                .addOnFailureListener(e -> Log.e("AddingMeals", "Error adding MealDay", e));
    }

    public void addMealToDatabase(View v){
        List<Meal> mealsForToday = Arrays.asList(
                new Meal(1, "Śniadanie", 300),
                new Meal(2, "Drugie śniadanie", 200),
                new Meal(3, "Obiad", 500)
        );

        MealDay newMealDay = new MealDay("27/3/2024", mealsForToday);
        addMealDayToDatabase(newMealDay); // Dodajemy nowy dzień posiłku do bazy
    }
}