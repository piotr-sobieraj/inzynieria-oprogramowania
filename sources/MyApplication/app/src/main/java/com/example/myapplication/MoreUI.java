package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MoreUI extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.more_ui);
        changeUI();
    }

    public void changeUI() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                if (checkedId == R.id.Menu) {
                    intent = new Intent(MoreUI.this, AddingMeals.class);
                    startActivity(intent);
                } else if (checkedId == R.id.Recipes) {
                    intent = new Intent(MoreUI.this, RecipesUI.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(MoreUI.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }


    public void popout(View v) {
        Intent intent = new Intent(this, popup.class);
        startActivity(intent);
    }
}