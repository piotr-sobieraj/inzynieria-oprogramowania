package com.example.myapplication;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button date = (Button) findViewById(R.id.pickDate);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        date.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
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
        return ((Button)findViewById(R.id.pickDate)).getText().toString();
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