package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class Reauthenticate extends AppCompatActivity {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.reauthenticate);
    }

    public void getCred(){
        setEmail(((TextView)findViewById(R.id.emailCred)).getText().toString());
        setPassword(((TextView)findViewById(R.id.passwordCred)).getText().toString());
    }

    public void deleteAcc(View v) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            getCred();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(getEmail(), getPassword());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection("users");
            usersRef.whereEqualTo("userUID", currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("users").document(
                                        document.getId()).delete().addOnCompleteListener(
                                        task2 -> Log.d("Firebase", "Document successful deleted"));
                            }
                        }
                    });
            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        Log.d(TAG, "User re-authenticated.");
                        currentUser.delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        Intent intent = new Intent(Reauthenticate.this, Start.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Reauthenticate.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    });


        }
    }
}