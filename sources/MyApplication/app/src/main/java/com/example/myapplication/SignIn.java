package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private String email = "";
    private String password = "";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }
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
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(SignIn.this, MoreUI.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SignIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
