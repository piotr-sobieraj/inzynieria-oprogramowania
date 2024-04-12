package com.example.myapplication;

public class Meal {
    protected String name = "";
     protected int caloricValue = 0;
     public Meal() {}
     public Meal(String name, int caloricValue) {
        this.name = name;
        this.caloricValue = caloricValue;
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