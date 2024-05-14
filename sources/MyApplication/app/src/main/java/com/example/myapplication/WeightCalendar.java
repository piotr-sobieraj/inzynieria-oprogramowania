package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
public class WeightCalendar extends AppCompatActivity {
    private User user;
    private ArrayList<MealDay> listOfMealDays;
    private RecentMeal recentMeal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        user =(User) getIntent().getSerializableExtra("userObject");
        listOfMealDays =(ArrayList<MealDay>) getIntent().getSerializableExtra("listOfMealDayObjects");
        recentMeal = (RecentMeal) getIntent().getSerializableExtra("recentMealObject");
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
        RadioGroup radioGroup = findViewById(R.id.mainMenu);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Intent intent;
            if (checkedId == R.id.Menu) {
                intent = new Intent(WeightCalendar.this, Menu.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            } else if (checkedId == R.id.WeightCalendar) {
                intent = new Intent(WeightCalendar.this, WeightCalendar.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            } else if (checkedId == R.id.More) {
                intent = new Intent(WeightCalendar.this, MoreUI.class);
                intent.putExtra("userObject", user);
                intent.putExtra("listOfMealDayObjects", listOfMealDays);
                intent.putExtra("recentMealObject", recentMeal);
                startActivity(intent);
            }
        });
    }
}
