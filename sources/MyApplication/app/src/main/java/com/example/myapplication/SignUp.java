package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }
        setContentView(R.layout.signup_ui);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload(currentUser);
        }
    }

    public void register(View v){
        getCred();
        if(signUp()){
            createAccount(email, password);
        }
    }

    private void createAccount(String email, String password) {
        //TODO Rozwiązanie niepoprawne, trzeba zaimplementować złapanie generycznych błedów lub usunąć email protection w Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("signUp", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(SignUp.this, PersonalInformation.class);
                        startActivity(intent);
                    } else {
                        Log.w("signUp", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUp.this, "Creating an account failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void reload(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("user_uid", user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firebase", document.getId() + " => " + document.getData());
                            Intent intent = new Intent(SignUp.this, MoreUI.class);
                            startActivity(intent);
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                        Intent intent = new Intent(SignUp.this, PersonalInformation.class);
                        startActivity(intent);
                    }
                });
    }
    public void getCred(){
        email = ((TextView)findViewById(R.id.email)).getText().toString();
        password = ((TextView)findViewById(R.id.password)).getText().toString();
    }

    public boolean signUp(){
        boolean result = true;
        if(email.isEmpty()){
            ((TextView)findViewById(R.id.email)).setError("Missing Email");
            result = false;
        }
        if (password.isEmpty()){
            ((TextView)findViewById(R.id.password)).setError("Missing Password");
            result = false;
        }
        return result;
    }

}