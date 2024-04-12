package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.support_bar2);
        }

        fillControls();
    }

    public void back(View v){
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }


    public void fillControls(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String height = document.getString("height");
                            String weight = document.getString("weight");
                            String sex = Objects.requireNonNull(document.getString("sex")).toLowerCase();

                            // Znajdź odpowiedni widok TextView i ustaw odczytaną wartość
                            TextView heightTextView = findViewById(R.id.editTextHeight);
                            heightTextView.setText(height);

                            TextView weightTextView = findViewById(R.id.editTextWeight);
                            weightTextView.setText(weight);

                            switch(sex){
                                case "f":
                                    RadioButton female = findViewById(R.id.radioButtonFemale);
                                    female.setChecked(true);
                                    break;

                                case "m":
                                    RadioButton male = findViewById(R.id.radioButtonMale);
                                    male.setChecked(true);
                                    break;

                                default:
                                    Log.d("Profile", "Unknown sex: " + sex);

                            }
                        }

                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }


    public void save(View v){

    }
}