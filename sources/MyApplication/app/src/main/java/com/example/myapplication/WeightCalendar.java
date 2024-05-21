package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WeightCalendar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weight_calendar);
        changeUI();
        readWeightDayFromDatabase();
    }

    public void addWeightToDatabase(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            db.collection("users").document(document.getId()).collection("weightDays").get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task.isSuccessful()) {
                                            boolean finded = false;
                                            for(QueryDocumentSnapshot documentSnapshot: task1.getResult()){
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                WeightDay weightDay = objectMapper.convertValue(documentSnapshot.getData(), WeightDay.class);
                                                if (Objects.equals(weightDay.getDate(), getIntent().getStringExtra("date"))){
                                                    finded = true;
                                                    List<Weight> weightList = weightDay.getWeight();
                                                    if(weightList == null){
                                                        weightList = new ArrayList<>();
                                                    }
                                                    weightList.add(new Weight(Double.parseDouble(((TextView)findViewById(R.id.weightText)).getText().toString())));
                                                    db.collection("users").document(document.getId()).collection("weightDays").document(documentSnapshot.getId()).update("weight", weightList)
                                                            .addOnCompleteListener(task2 -> {
                                                                if(task2.isSuccessful()){
                                                                    Log.d("ADD", "Add document successful");
                                                                    Intent intent = new Intent(WeightCalendar.this, WeightCalendar.class);
                                                                    intent.putExtra("date", getIntent().getStringExtra("date"));
                                                                    startActivity(intent);
                                                                } else {
                                                                    Log.d("ADD", "Document add failed" + task2.getException());
                                                                    Toast.makeText(this, "Document add failed",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            }if(!finded) {
                                                WeightDay weightDay = new WeightDay();
                                                List<Weight> weightList = new ArrayList<>();
                                                weightDay.setDate(getIntent().getStringExtra("date"));
                                                weightList.add(new Weight(Double.parseDouble(((TextView) findViewById(R.id.weightText)).getText().toString())));
                                                weightDay.setWeight(weightList);
                                                db.collection("users").document(document.getId()).collection("weightDays").add(weightDay)
                                                        .addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful()) {
                                                                Intent intent = new Intent(WeightCalendar.this, WeightCalendar.class);
                                                                intent.putExtra("date", getIntent().getStringExtra("date"));
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(this, "Document add failed",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }
    public void readWeightDayFromDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(user).getUid()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).collection("weightDays").get().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        WeightDay weightDay = objectMapper.convertValue(documentSnapshot.getData(), WeightDay.class);
                                        List<Weight> weightList = weightDay.getWeight();
                                        for (int i = 0; i <weightList.size(); i++) {
                                            if(i>0) {
                                                AddCardWeight(weightDay.getDate(), String.valueOf(weightList.get(i).weight), String.valueOf(weightList.get(i).weight - weightList.get(i-1).weight));
                                            }else {
                                                AddCardWeight(weightDay.getDate(), String.valueOf(weightList.get(i).weight), "0");
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public void AddCardWeight(String _date, String _weight, String _difference){
        View view = getLayoutInflater().inflate(R.layout.weight_card, null);
        setWeight(view, _date, _weight, _difference);
        LinearLayout layout = findViewById(getResources().getIdentifier("container", "id", getPackageName()));
        layout.addView(view);

    }
    public void setWeight(View view, String _date, String _weight, String _difference){
        TextView date = view.findViewById(R.id.date);
        date.setText(_date);
        TextView weight = view.findViewById(R.id.weight);
        String tmp = "Weight: " + _weight;
        weight.setText(tmp);
        TextView difference = view.findViewById(R.id.difference);
        tmp = _difference;
        difference.setText(tmp);
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
