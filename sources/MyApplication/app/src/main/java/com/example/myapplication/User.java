package com.example.myapplication;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import com.example.myapplication.MealDay;

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


    @NonNull
    public String toString(){
        @SuppressLint("DefaultLocale") String user = String.format("%s %d", name, height);
        return user;
    }
}
