package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    private String email = "";
    private String password = "";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin_ui);
    }
    public void getCred(){
        email = ((TextView)findViewById(R.id.login_email)).getText().toString();
        password = ((TextView)findViewById(R.id.login_password)).getText().toString();
    }
    public boolean signIn(){
        boolean result = true;
        if(email.isEmpty()){
            ((TextView)findViewById(R.id.login_email)).setError("Missing Email");
            result = false;
        }
        if (password.isEmpty()){
            ((TextView)findViewById(R.id.login_password)).setError("Missing Password");
            result = false;
        }
        return result;
    }



    public void signIn(View v){
        getCred();
        if (signIn()){
            //TODO Rozwiązanie niepoprawne, trzeba zaimplementować złapanie generycznych błedów lub usunąć email protection w Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("signIn", "signInWithEmail:success");
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("users");
                            usersRef.whereEqualTo("userUID", Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task.isSuccessful()) {
                                            if (!task2.getResult().isEmpty()){
                                                Log.d("Firebase", "Successful logged user");
                                                Intent intent = new Intent(SignIn.this, Menu.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Log.d("Firebase", "User without document");
                                                Intent intent = new Intent(SignIn.this, PersonalInformation.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                        else {
                            Log.d("signIn", "signInWithEmail:failure");
                            Toast.makeText(SignIn.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
