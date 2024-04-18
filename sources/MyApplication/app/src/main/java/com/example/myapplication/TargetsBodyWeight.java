package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TargetsBodyWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targets_body_weight_ui);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }

    public void save(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }
}