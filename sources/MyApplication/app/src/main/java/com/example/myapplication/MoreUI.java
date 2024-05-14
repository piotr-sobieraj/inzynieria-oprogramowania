package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MoreUI extends AppCompatActivity {
    private User user;
    private ArrayList<MealDay> listOfMealDays;
    private RecentMeal recentMeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        user =(User) getIntent().getSerializableExtra("userObject");
        listOfMealDays =(ArrayList<MealDay>) getIntent().getSerializableExtra("listOfMealDayObjects");
        recentMeal = (RecentMeal) getIntent().getSerializableExtra("recentMealObject");
        setContentView(R.layout.more_ui);
        changeUI();
    }

    public void changeUI() {
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Intent intent;
            if (checkedId == R.id.Menu) {
                intent = new Intent(MoreUI.this, Menu.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            } else if (checkedId == R.id.WeightCalendar) {
                intent = new Intent(MoreUI.this, WeightCalendar.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            } else if (checkedId == R.id.More) {
                intent = new Intent(MoreUI.this, MoreUI.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            }
        });
    }


    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MoreUI.this, Start.class);
        startActivity(intent);
    }


    public void reAuth(View v) {
        Intent intent = new Intent(this, Reauthenticate.class);
        startActivity(intent);
    }
    public void options(View v){
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }
}