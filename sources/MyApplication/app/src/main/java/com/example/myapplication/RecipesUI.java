package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RecipesUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (getSupportActionBar() != null)
            Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.recipes_ui);
        changeUI();
    }
    public void changeUI(){
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(RecipesUI.this, AddingMeals.class);
                    startActivity(intent);
                } else if (checkedId == R.id.Recipes) {
                    intent = new Intent(RecipesUI.this, RecipesUI.class);
                    startActivity(intent);
                }
                else if (checkedId == R.id.More) {
                    intent = new Intent(RecipesUI.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }
}