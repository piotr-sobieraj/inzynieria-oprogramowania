package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {
    public static String typeOfMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    public void changeUI() {
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(Menu.this, Menu.class);
                    startActivity(intent);
                } else if (checkedId == R.id.Recipes) {
                    intent = new Intent(Menu.this, RecipesUI.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(Menu.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void addProductBreakfast(View v){
        typeOfMeal = "Breakfast";
        Intent intent = new Intent(Menu.this, NewProduct.class);
        startActivity(intent);
    }
    public void addProductSecondBreakfast(View v){
        typeOfMeal = "SecondBreakfast";
        Intent intent = new Intent(Menu.this, NewProduct.class);
        startActivity(intent);
    }
}