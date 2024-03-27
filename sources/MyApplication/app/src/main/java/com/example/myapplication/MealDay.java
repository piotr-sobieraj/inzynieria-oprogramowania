package com.example.myapplication;

import java.util.List;
import com.example.myapplication.Meal;

public class MealDay {
    public String date;
    public List<Meal> meals;

    public MealDay() {
        // Konstruktor domyślny
    }

    public MealDay(String date, List<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }

    // Gettery i settery
}