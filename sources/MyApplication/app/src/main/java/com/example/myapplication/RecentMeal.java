package com.example.myapplication;

import java.util.List;
import java.util.Map;

public class RecentMeal {
    private Map<String, List<Meal>> meals;

    public RecentMeal() {
        // Konstruktor domyślny
    }

    public RecentMeal(Map<String, List<Meal>> meals) {
        this.setMeals(meals);
    }

    public Map<String, List<Meal>> getMeals() {
        return meals;
    }

    public void setMeals(Map<String, List<Meal>> meals) {
        this.meals = meals;
    }
}
