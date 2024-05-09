package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            reload(currentUser);
        }
        else
            setContentView(R.layout.start);
    }

    public void loginUI(View v){
        Intent intent = new Intent(Start.this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View v){
        Intent intent = new Intent(Start.this, SignUp.class);
        startActivity(intent);
    }
    private void reload(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("userUID", user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firebase", document.getId() + " => " + document.getData());
                            Intent intent = new Intent(Start.this, Menu.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}