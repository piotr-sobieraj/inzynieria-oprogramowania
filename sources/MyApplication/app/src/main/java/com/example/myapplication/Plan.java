package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class Plan extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        if (getSupportActionBar() != null)
            Objects.requireNonNull(getSupportActionBar()).hide();
        calculateCaloric();
        setContentView(R.layout.plan_ui);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculateCaloric(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String bmr;
                            ObjectMapper objectMapper = new ObjectMapper();
                            User user = objectMapper.convertValue(document.getData(), User.class);

                            String s = user.calculateAndSetReachGoalDate();

                            ((TextView)findViewById(R.id.reachGoalDate)).setText(s);

                            saveReachGoalDateToDatabase(document, s);

                            double reduce = Double.parseDouble(user.getTargetWeight()) - Double.parseDouble(user.getWeight());

                            String string = user.getWeight() + "kg -> " + reduce + " kg -> " + user.getTargetWeight() + " kg";
                            ((TextView)findViewById(R.id.kilograms)).setText(string);

                            bmr = user.calculateAndSetDailyCalorieLimit();
                            ((TextView)findViewById(R.id.dailyCalorieLimit)).setText(bmr);

                            user.setDailyCalorieLimit(bmr);
                            saveDailyCalorieLimitToDatabase(document, bmr);
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }
    private static void saveReachGoalDateToDatabase(QueryDocumentSnapshot document, String reachGoalDate) {
        document.getReference().update("reachGoalDate", reachGoalDate)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - reachGoalDate", e));
    }
    private static void saveDailyCalorieLimitToDatabase(QueryDocumentSnapshot document, String bmr) {
        document.getReference().update("dailyCalorieLimit", bmr)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - dailyCalorieLimit", e));
    }

    public void start(View v){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}