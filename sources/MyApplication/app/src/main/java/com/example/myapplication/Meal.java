package com.example.myapplication;

public class Meal {
    private int mealNumberInDay;
    private String name;
    private int caloricValue;

    public Meal() {
        // Konstruktor domyślny
    }

    public Meal(int mealNumberInDay, String name, int caloricValue) {
        this.setMealNumberInDay(mealNumberInDay);
        this.setName(name);
        this.setCaloricValue(caloricValue);
    }

    public int getMealNumberInDay() {
        return mealNumberInDay;
    }

    public void setMealNumberInDay(int mealNumberInDay) {
        this.mealNumberInDay = mealNumberInDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloricValue() {
        return caloricValue;
    }

    public void setCaloricValue(int caloricValue) {
        this.caloricValue = caloricValue;
    }
}
