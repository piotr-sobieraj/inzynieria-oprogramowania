package com.example.myapplication;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable {

    private String userUID;
    private String name;
    private String sex;
    private String birthDate;
    private String height;
    private String weight;
    private String targetWeight;
    private String calorieLimit;
    private String reachGoalDate;

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
                String dailyCalorieLimit,
                String reachGoalDate) {
        this.userUID = userUID;
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.calorieLimit = dailyCalorieLimit;
        this.reachGoalDate = reachGoalDate;
    }

    public String getName() { return name; }
    public String getSex() { return sex.toLowerCase(); }
    public String getBirthDate() { return birthDate; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public String getTargetWeight() { return targetWeight; }
    public String getDailyCalorieLimit() {
        return calorieLimit;
    }
    public String getReachGoalDate() {
        return reachGoalDate;
    }


    public String getUserUID() {
        return userUID;
    }


    public void setName(String name) { this.name = name; }
    public void setSex(String sex) { this.sex = sex; }
    public void setBirthDate(String date) { this.birthDate = date; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setTargetWeight(String targetWeight) { this.targetWeight = targetWeight; }
    public void setDailyCalorieLimit(String calorieLimit) { this.calorieLimit = calorieLimit; }
    public void setReachGoalDate(String reachGoalDate) { this.reachGoalDate = reachGoalDate; }


    public static long WeeksBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return (end - start) / (24 * 60 * 60 * 1000 * 7);  // Milisekundy w tygodniu
    }

    public static long WeeksToDate(String reachGoalDate){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date goalDate = null;
        try {
            goalDate = formatter.parse(reachGoalDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar today = Calendar.getInstance();

        Calendar target = Calendar.getInstance();
        if (goalDate != null) {
            target.setTime(goalDate);
        }

        return WeeksBetween(today, target);
    }
    public static float kgWeekRate(String reachGoalDate, String weight, String targetWeight) {
        long weeksToGoalDate = WeeksToDate(reachGoalDate);
        if (weeksToGoalDate != 0)
            return (Float.parseFloat(weight) - Float.parseFloat(targetWeight)) / (float) weeksToGoalDate;
        else return -1;
    }

    public String calculateAndSetDailyCalorieLimit(){
        //Muszą być ustawione: birthDate, sex, weight i height
        double bmr;
        String bmr_s;
        String[] date = getBirthDate().split("/");
        int birthYear = Integer.parseInt(date[2]);

        // Pobierz aktualny rok
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);

        // Oblicz wiek
        int age = currentYear - birthYear;

        if (Objects.equals(getSex(), "m")){
            bmr = 66.473 + (13.752 * Double.parseDouble(getWeight()) + (5.003 * Integer.parseInt(getHeight())) - (6.775 * age));
        }
        else {
            bmr = 655.1 + (9.563 * Double.parseDouble(getWeight()) + (1.85 * Integer.parseInt(getHeight())) - (4.676 * age));
        }

        bmr_s = String.valueOf((int)Math.round(bmr));
        calorieLimit = bmr_s;
        return bmr_s;
    }

    public String calculateAndSetReachGoalDate(){
        double reduce = Double.parseDouble(getTargetWeight()) - Double.parseDouble(getWeight());
        LocalDate localDate = LocalDate.now();

        localDate = localDate.plusDays((long) (Math.abs(reduce) * 7700 / 500));
        String s = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();
        setReachGoalDate(s);

        return s;
    }
}
