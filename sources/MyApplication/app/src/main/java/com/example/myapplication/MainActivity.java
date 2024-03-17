package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    @NonNull
    private String getName(){
        return ((TextView) findViewById(R.id.editTextName)).getText().toString();
    }

    @NonNull
    private String getSex(){
        RadioButton radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        if(radioButtonFemale.isChecked())
            return "K";
        else
            return "M";
    }

    @NonNull
    private String getBirthDate() {
        return ((TextView)findViewById(R.id.editTextDate)).getText().toString();
    }

    @NonNull
    private String getTargetWeight() {
        return ((TextView)findViewById(R.id.editTextDocelowaWaga)).getText().toString();
    }

    @NonNull
    private String getWeight() {
        return ((TextView)findViewById(R.id.editTextWaga)).getText().toString();
    }

    @NonNull
    private String getHeight() {
        return ((TextView)findViewById(R.id.editTextWzrost)).getText().toString();
    }


    public void makeSummary(View v){
        String imie = getName();
        String plec = getSex();
        String dataUrodzenia = getBirthDate();
        String wzrost = getHeight();
        String waga = getWeight();
        String docelowaWaga = getTargetWeight();

        Log.d ("Podsumowanie", "{imie: \"" + imie +
                "\",\nplec: \"" + plec +
                "\",\ndataUrodzenia: \"" + dataUrodzenia +
                "\",\nwzrost: \"" + wzrost +
                "\",\nwaga: \"" + waga +
                "\",\ndocelowaWaga: \"" + docelowaWaga + "\"}");
    }
}