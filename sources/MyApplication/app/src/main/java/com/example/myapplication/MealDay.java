package com.example.myapplication;

import java.util.List;
import java.util.Map;

public class MealDay {
    private String date;
    private Map<String, List<Meal>> meals;

    public MealDay() {
        // Konstruktor domyślny
    }

    public MealDay(String date, Map<String, List<Meal>> meals) {
        this.setDate(date);
        this.setMeals(meals);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, List<Meal>> getMeals() {
        return meals;
    }

    public void setMeals(Map<String, List<Meal>> meals) {
        this.meals = meals;
    }
}