package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import com.example.myapplication.User;


public class Options extends AppCompatActivity {

    private String currentToTargetWeight;
    private String kgWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.options_ui);
        fillControls();
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

    private void fillControls(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String height = document.getString("height");
                            String birthDate = document.getString("birthDate");
                            String sex = document.getString("sex").toLowerCase();

                            String weight = document.getString("weight");
                            String targetWeight = document.getString("targetWeight");
                            String reachGoalDate = document.getString("reachGoalDate");

                            if(sex.equals("f"))
                                sex = "Female";
                            else if (sex.equals("m"))
                                 sex = "Male";
                            else sex = "unknown sex: " + sex;

                            ((TextView)findViewById(R.id.textViewProfileInfo)).setText(
                                    String.format("%s, %s, %s cm", sex, birthDate, height)
                            );

                            currentToTargetWeight = String.format("%s kg -> %s kg", weight, targetWeight);

                            //Obliczenie i ustawienie kg/week rate
                            long weeksToGoalDate = User.WeeksToDate(reachGoalDate);
                            if (weeksToGoalDate != 0){
                                float kgWeekValue = User.kgWeekRate(reachGoalDate, weight, targetWeight);

                                kgWeek = String.format("%.2f kg / week", kgWeekValue);
                                ((TextView)findViewById(R.id.textViewTargets)).setText(
                                    String.format("%s, %s", currentToTargetWeight, kgWeek));
                            }
                        }

                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }




}