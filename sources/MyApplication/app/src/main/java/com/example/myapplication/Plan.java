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

import java.time.LocalDate;
import java.util.Calendar;
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
                            double bmr;
                            ObjectMapper objectMapper = new ObjectMapper();
                            User user = objectMapper.convertValue(document.getData(), User.class);
                            double reduce = Double.parseDouble(user.getTargetWeight()) - Double.parseDouble(user.getWeight());
                            LocalDate localDate = LocalDate.now();
                            localDate = localDate.plusDays((long) (Math.abs(reduce)*7700/ 500));
                            String s = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear();
                            ((TextView)findViewById(R.id.reachGoalDate)).setText(s);
                            String string = user.getWeight() + "kg -> " + reduce + " kg -> " + user.getTargetWeight() + " kg";
                            ((TextView)findViewById(R.id.kilograms)).setText(string);
                            final Calendar c = Calendar.getInstance();
                            String[] date = user.getBirthDate().split("/");
                            int age = c.get(Calendar.YEAR) - Integer.parseInt(date[2]);
                            if (Objects.equals(user.getSex(), "M")){
                                bmr = 66.473 + (13.752 * Double.parseDouble(user.getWeight()) + (5.003 * Integer.parseInt(user.getHeight())) - (6.775 * age));
                            }
                            else {
                                bmr = 655.1 + (9.563 * Double.parseDouble(user.getWeight()) + (1.85 * Integer.parseInt(user.getHeight())) - (4.676 * age));
                            }
                            ((TextView)findViewById(R.id.calorieLimit)).setText(String.valueOf((int)Math.floor(bmr)));
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void start(View v){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}