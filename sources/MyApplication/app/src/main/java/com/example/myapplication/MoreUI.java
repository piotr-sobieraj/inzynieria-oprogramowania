package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MoreUI extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (getSupportActionBar() != null)
            Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.more_ui);
        changeUI();
    }

    public void changeUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(MoreUI.this, Menu.class);
                    startActivity(intent);
                } else if (checkedId == R.id.WeightCalendar) {
                    intent = new Intent(MoreUI.this, WeightCalendar.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(MoreUI.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }


    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MoreUI.this, Start.class);
        startActivity(intent);
    }


    public void reAuth(View v) {
        Intent intent = new Intent(this, Reauthenticate.class);
        startActivity(intent);
    }
    public void options(View v){
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }

    public void authors(View v){
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v1 -> new AlertDialog.Builder(v1.getContext())
                .setTitle("Authors")
                .setMessage("Bartosz Siedlecki" + System.lineSeparator() + "Piotr Sobieraj" + System.lineSeparator() + "Wiktor Szczepanik")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {})
                .show());
    }
}