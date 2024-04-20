package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TargetsRateWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targets_rate_weight_ui);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }

    public void save(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }

    public void minusRateOfWeight(View view) {
    }

    public void plusRateOfWeight(View view) {
    }
}