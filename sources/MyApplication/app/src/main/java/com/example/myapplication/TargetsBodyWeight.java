package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class TargetsBodyWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.targets_body_weight_ui);

        fillControls();
    }

    public void back(View view) {
        Intent intent = new Intent(this, Targets.class);
        startActivity(intent);
    }

    public void save(View view) {
        String weight = String.valueOf(((EditText)findViewById(R.id.editTextCurrentWeight)).getText());
        String targetWeight = String.valueOf(((EditText)findViewById(R.id.editTextTargetWeight)).getText());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().update("weight", weight)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - weight", e));

                            document.getReference().update("targetWeight", targetWeight)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - targetWeight", e));
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void fillControls(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String weight = document.getString("weight");
                            String targetWeight = document.getString("targetWeight");

                            ((TextView)findViewById(R.id.editTextCurrentWeight)).setText(weight);
                            ((TextView)findViewById(R.id.editTextTargetWeight)).setText(targetWeight);
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }
}