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

        // Przekazanie danych do wyswietlenia w labelkach
        intent.putExtra("currentToTargetWeight", currentToTargetWeight);
        intent.putExtra("kgWeek", kgWeek);

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

                            long weeksToGallDate = WeeksToDate(reachGoalDate);
                            if (weeksToGallDate != 0){
                                float kgWeekValue = (Float.parseFloat(weight) - Float.parseFloat(targetWeight)) / (float) weeksToGallDate;

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

    private long WeeksToDate(String reachGoalDate){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date goalDate = null;
        try {
            goalDate = formatter.parse(reachGoalDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar today = Calendar.getInstance();

        Calendar target = Calendar.getInstance();
        if (goalDate != null) {
            target.setTime(goalDate);
        }

        return WeeksBetween(today, target);
    }

    private static long WeeksBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return (end - start) / (24 * 60 * 60 * 1000 * 7);  // Milisekundy w tygodniu
    }
}