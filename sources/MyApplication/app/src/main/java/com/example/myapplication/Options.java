package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.options_ui);
    }

    public void back(View v){
        Intent intent = new Intent(this, MoreUI.class);
        startActivity(intent);
    }

    public void profile(View v){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void targets(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }
}