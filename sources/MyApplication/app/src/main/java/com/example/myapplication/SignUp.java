package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    public static String firstAndLastName;
    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.signup_ui);
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
                        firstAndLastName = ((TextView) findViewById(R.id.personalText)).getText().toString();
                        Intent intent = new Intent(SignUp.this, PersonalInformation.class);
                        startActivity(intent);
                    } else {
                        Log.w("signUp", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUp.this, "Creating an account failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getCred(){
        email = ((TextView)findViewById(R.id.login_email)).getText().toString();
        password = ((TextView)findViewById(R.id.login_password)).getText().toString();
    }

    public boolean signUp(){
        if(email.isEmpty()){
            ((TextView)findViewById(R.id.login_email)).setError("Missing Email");
            return false;
        }
        if (password.isEmpty()){
            ((TextView)findViewById(R.id.login_password)).setError("Missing Password");
            return false;
        }
        return true;
    }
}