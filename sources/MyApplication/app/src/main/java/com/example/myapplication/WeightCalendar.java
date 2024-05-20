package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;
public class WeightCalendar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weight_calendar);
        changeUI();
    }

    public void addWeightToDatabase(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            db.collection("users").document(document.getId()).collection("mealDays").get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task.isSuccessful()) {
                                            for(QueryDocumentSnapshot documentSnapshot: task1.getResult()){

                                            }
                                        }
                                    });
                        }
                    }
                });

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
                    intent = new Intent(WeightCalendar.this, Menu.class);
                    startActivity(intent);
                } else if (checkedId == R.id.WeightCalendar) {
                    intent = new Intent(WeightCalendar.this, WeightCalendar.class);
                    startActivity(intent);
                } else if (checkedId == R.id.More) {
                    intent = new Intent(WeightCalendar.this, MoreUI.class);
                    startActivity(intent);
                }
            }
        });
    }
}
