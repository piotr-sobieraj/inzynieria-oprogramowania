package com.example.myapplication;

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

import java.util.Objects;

public class Targets extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targets_ui);

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

    private void fillControls() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String weight = document.getString("weight");
                            String targetWeight = document.getString("targetWeight");
                            String reachGoalDate = document.getString("reachGoalDate");

                            String currentToTargetWeight = String.format("%s kg -> %s kg", weight, targetWeight);

                            //Obliczenie i ustawienie kg/week rate
                            long weeksToGoalDate = User.WeeksToDate(reachGoalDate);
                            if (weeksToGoalDate != 0) {
                                float kgWeekValue = User.kgWeekRate(reachGoalDate, weight, targetWeight);

                                String kgWeek = String.format("%.2f kg / week", kgWeekValue);
                                ((TextView) findViewById(R.id.bodyWeightText)).setText(currentToTargetWeight);
                                ((TextView) findViewById(R.id.textViewRate)).setText(kgWeek);
                            }
                        }

                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }
}