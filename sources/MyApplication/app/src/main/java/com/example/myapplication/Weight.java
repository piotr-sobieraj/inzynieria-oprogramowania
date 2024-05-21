package com.example.myapplication;

public class Weight {
    protected double weight = 0;

    public Weight() {}

    public Weight(double weight){
        this.weight = weight;
    }

    public double getWeight() { return weight; }
    public void setWeight(double weight){ this.weight = weight; }
}
