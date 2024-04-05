package com.example.myapplication;

import java.util.List;

public class MealDay {
    private String date;
    private List<Meal> meals;

    public MealDay() {
        // Konstruktor domyślny
    }

    public MealDay(String date, List<Meal> meals) {
        this.setDate(date);
        this.setMeals(meals);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}