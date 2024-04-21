package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Targets extends AppCompatActivity {
    private String currentToTargetWeight;
    private String kgWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targets_ui);

        // Odebranie przesłanych wartości
        Intent intent = getIntent();
        currentToTargetWeight = intent.getStringExtra("currentToTargetWeight");
        kgWeek = intent.getStringExtra("kgWeek");

        fillControls();
    }

    public void back(View view) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }

    public void bodyWeight(View view) {
        Intent intent = new Intent(this, TargetsBodyWeight.class);
        startActivity(intent);
    }

      private void fillControls(){
        ((TextView) findViewById(R.id.bodyWeightText)).setText(currentToTargetWeight);
        ((TextView) findViewById(R.id.textViewRate)).setText(kgWeek);
    }
}