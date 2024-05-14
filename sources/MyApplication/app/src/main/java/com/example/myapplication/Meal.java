package com.example.myapplication;

import java.io.Serializable;

public class Meal implements Serializable {
    protected String name = "";
     protected int caloricValue = 0;
     protected int fatsValue = 0;
    protected int carbohydratesValue = 0;
     protected int proteinsValue = 0;
     public Meal() {}
     public Meal(String name, int caloricValue, int fatsValue, int carbohydratesValue, int proteinsValue) {
        this.name = name;
        this.caloricValue = caloricValue;
        this.fatsValue = fatsValue;
        this.carbohydratesValue = carbohydratesValue;
        this.proteinsValue = proteinsValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public int getCaloricValue() {
        return caloricValue;
    }
    public void setCaloricValue(int caloricValue) {
        this.caloricValue = caloricValue;
    }
    public int getFatsValue() {
        return fatsValue;
    }

    public void setFatsValue(int fatsValue) {
        this.fatsValue = fatsValue;
    }

    public int getCarbohydratesValue() {
        return carbohydratesValue;
    }

    public void setCarbohydratesValue(int carbohydratesValue) {
        this.carbohydratesValue = carbohydratesValue;
    }

    public int getProteinsValue() {
        return proteinsValue;
    }

    public void setProteinsValue(int proteinsValue) {
        this.proteinsValue = proteinsValue;
    }
}

class Breakfast extends Meal {
    public Breakfast(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public int getCaloricValue() {
        return super.getCaloricValue();
    }

    @Override
    public void setCaloricValue(int caloricValue) {
        super.setCaloricValue(caloricValue);
    }
}


class SecondBreakfast extends Meal {
    public SecondBreakfast(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
    }
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public int getCaloricValue() {
        return super.getCaloricValue();
    }

    @Override
    public void setCaloricValue(int caloricValue) {
        super.setCaloricValue(caloricValue);
    }
}
class Lunch extends Meal {
    public Lunch(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
    }
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public int getCaloricValue() {
        return super.getCaloricValue();
    }

    @Override
    public void setCaloricValue(int caloricValue) {
        super.setCaloricValue(caloricValue);
    }
}

class Snack extends Meal {
    public Snack(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
    }
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public int getCaloricValue() {
        return super.getCaloricValue();
    }

    @Override
    public void setCaloricValue(int caloricValue) {
        super.setCaloricValue(caloricValue);
    }
}

class Dinner extends Meal {
    public Dinner(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
    }
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public int getCaloricValue() {
        return super.getCaloricValue();
    }

    @Override
    public void setCaloricValue(int caloricValue) {
        super.setCaloricValue(caloricValue);
    }
}