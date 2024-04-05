package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }
        setContentView(R.layout.start);
    }

    public void loginUI(View v){
        Intent intent = new Intent(Start.this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View v){
        Intent intent = new Intent(Start.this, SignUp.class);
        startActivity(intent);
    }
}