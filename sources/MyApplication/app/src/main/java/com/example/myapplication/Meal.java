package com.example.myapplication;

public class Meal {
    public int mealNumberInDay;
    public String name;
    public int caloricValue;

    public Meal() {
        // Konstruktor domyślny
    }

    public Meal(int mealNumberInDay, String name, int caloricValue) {
        this.mealNumberInDay = mealNumberInDay;
        this.name = name;
        this.caloricValue = caloricValue;
    }

    // Gettery i settery
}
