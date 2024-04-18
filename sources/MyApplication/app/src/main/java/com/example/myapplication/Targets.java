package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Targets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targerts_ui);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }

    public void bodyWeight(View view) {
        Intent intent = new Intent(this, TargetsBodyWeight.class);
        startActivity(intent);
    }

    public void rateOfWeight(View view) {
        Intent intent = new Intent(this, TargetsRateWeight.class);
        startActivity(intent);
    }
}