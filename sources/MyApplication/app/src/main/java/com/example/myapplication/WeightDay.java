package com.example.myapplication;

import java.util.List;
import java.util.Map;

public class WeightDay {
    private String date;
    private List<Weight> weights;
    public WeightDay(){
        //Domyślny
    }
    public WeightDay(String date, List<Weight> weights){
        this.setDate(date);
        this.setWeight(weights);
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public List<Weight> getWeight() {
        return weights;
    }
    public void setWeight(List<Weight> weights) {
        this.weights = weights;
    }

}
