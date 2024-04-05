package com.example.myapplication;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import com.example.myapplication.MealDay;

public class User implements Serializable {

    private String userUID;
    private String name;
    private String sex;
    private String birthDate;
    private String height;
    private String weight;
    private String targetWeight;
    private List<MealDay> mealCalendar;

    private User() {
        // Konstruktor domyślny wymagany dla operacji Firebase
    }

    // Konstruktor z parametrami
    public User(String userUID,
                String name,
                String sex,
                String birthDate,
                String height,
                String weight,
                String targetWeight,
                List<MealDay> mealCalendar
    ) {
        this.userUID = userUID;
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.mealCalendar = mealCalendar;
    }

    public String getName() { return name; }
    public String getSex() { return sex; }
    public String getBirthDate() { return birthDate; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public String getTargetWeight() { return targetWeight; }


    public void setName(String name) { this.name = name; }
    public void setSex(String sex) { this.sex = sex; }
    public void setBirthDate(String date) { this.birthDate = date; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setTargetWeight(String targetWeight) { this.targetWeight = targetWeight; }

    public String getUserUID() {
        return userUID;
    }
}
