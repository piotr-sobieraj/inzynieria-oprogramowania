package com.example.myapplication;

import java.util.List;

public class User {
    public String name;
    public String sex;
    public String dateOfBirth;
    public int height;
    public int weight;
    public int targetWeight;
    public List<MealDay> mealCalendar;

    public User() {
        // Konstruktor domyślny wymagany dla operacji Firebase
    }

    // Konstruktor z parametrami
    public User(String name,
                String sex,
                String dateOfBirth,
                int height,
                int weight,
                int targetWeight,
                List<MealDay> mealCalendar) {
        this.name = name;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.mealCalendar = mealCalendar;
    }
}
