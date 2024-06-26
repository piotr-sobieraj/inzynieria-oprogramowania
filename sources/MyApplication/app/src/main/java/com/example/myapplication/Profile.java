package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Profile extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_ui);
        fillControls();
        checkedRadioButton();
    }

    public void back(View v){
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }

    public void checkedRadioButton(){
        RadioGroup radioGroup = findViewById(R.id.sexRadioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            if (checkedId == R.id.radioButtonFemale) {
                rb.setButtonTintList(ColorStateList.valueOf(getColor(R.color.defaultColor)));
                RadioButton rb2 = findViewById(R.id.radioButtonMale);
                rb2.setButtonTintList(ColorStateList.valueOf(getColor(R.color.gray)));
            } else if (checkedId == R.id.radioButtonMale) {
                rb.setButtonTintList(ColorStateList.valueOf(getColor(R.color.defaultColor)));
                RadioButton rb2 = findViewById(R.id.radioButtonFemale);
                rb2.setButtonTintList(ColorStateList.valueOf(getColor(R.color.gray)));
            }
        });
    }


    public void fillControls(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersRef.whereEqualTo("userUID", Objects.requireNonNull(firebaseUser).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String height = document.getString("height");
                            String weight = document.getString("weight");
                            String sex = document.getString("sex").toLowerCase();
                            String name = document.getString("name");
                            String birthDate = document.getString("birthDate");
                            String targetWeight = document.getString("targetWeight");

                            // Znajdź odpowiedni widok TextView i ustaw odczytaną wartość
                            TextView heightTextView = findViewById(R.id.editTextHeight);
                            heightTextView.setText(height);

                            TextView weightTextView = findViewById(R.id.editTextWeight);
                            weightTextView.setText(weight);

                            TextView nameTextView = findViewById(R.id.editTextName);
                            nameTextView.setText(name);

                            //Ustawienie pól lokalnego w aktywności usera
                            user = new User(firebaseUser.getUid(),
                                    name,
                                    sex,
                                    birthDate,
                                    height,
                                    weight,
                                    targetWeight,
                                    "0",
                                    "");

                            user.setDailyCalorieLimit(user.calculateAndSetDailyCalorieLimit());

                            //Ustawienie kontrolki od płci
                            switch(sex){
                                case "f":
                                    RadioButton female = findViewById(R.id.radioButtonFemale);
                                    female.setChecked(true);
                                    break;

                                case "m":
                                    RadioButton male = findViewById(R.id.radioButtonMale);
                                    male.setChecked(true);
                                    break;

                                default:
                                    Log.d("Profile", "Unknown sex: " + sex);
                            }

                            //Ustaw kalendarz
                            setUpCalendar(birthDate);
                        }

                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }


    public void save(View v){

        if(!isInputDataCorrect()) return;

        String height = String.valueOf(((EditText)findViewById(R.id.editTextHeight)).getText());
        String weight = String.valueOf(((EditText)findViewById(R.id.editTextWeight)).getText());
        String name = String.valueOf(((EditText)findViewById(R.id.editTextName)).getText());
        String birtDate = ((Button)findViewById(R.id.pickDate)).getText().toString();
        String sex;

        RadioButton radioButtonFemale = findViewById(R.id.radioButtonFemale);
        sex = radioButtonFemale.isChecked() ? "f" : "m";

        user.setHeight(height);
        user.setWeight(weight);
        user.setBirthDate(birtDate);
        user.setSex(sex);
        user.setDailyCalorieLimit(user.calculateAndSetDailyCalorieLimit());
        user.calculateAndSetReachGoalDate();

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

                            document.getReference().update("height", height)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - height", e));

                            document.getReference().update("sex", sex)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - sex", e));

                            document.getReference().update("name", name)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - name", e));

                            document.getReference().update("birthDate", birtDate)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - birthDate", e));

                            document.getReference().update("dailyCalorieLimit", user.getDailyCalorieLimit())
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - dailyCalorieLimit", e));

                            document.getReference().update("reachGoalDate", user.getReachGoalDate())
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Document successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firebase", "Error updating document - reachGoalDate", e));
                        }
                    } else {
                        Log.d("Firebase", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setUpCalendar(String birthDate){
        Button date = findViewById(R.id.pickDate);
        final Calendar c = Calendar.getInstance();

        // Konwersja daty z parametru birthDate na rok, miesiąc i dzień
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(birthDate);
        } catch (ParseException e) {
            Log.d("Calendar", "Error setting calendar: " + e.getMessage());
        }
        c.setTime(parsedDate);

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String date_string = mDay + "/" + (mMonth + 1) + "/" + mYear;
        date.setText(date_string);

        date.setOnClickListener(v -> {
            SpinnerDatePickerDialog datePickerDialog = new SpinnerDatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String date_s = dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year);
                        date.setText(date_s);
                    },
                    mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private boolean isWeightCorrect(){
        try {
            String weight_s = String.valueOf(((EditText)findViewById(R.id.editTextWeight)).getText());
            BigInteger weight = new BigInteger(weight_s);

            if(weight_s.isEmpty()){
                ((TextView) findViewById(R.id.editTextWeight)).setError("Missing Weight value.");
                return false;
            }

            if (weight.compareTo(BigInteger.ZERO) <= 0 ||
                    weight.compareTo(BigInteger.valueOf(10000)) > 0) {

                ((TextView) findViewById(R.id.editTextWeight)).setError("Weight must be greater than 0 and lower than 10000.");
                return false;
            }

        } catch (NumberFormatException e) {//Literki wyłapie
            ((TextView) findViewById(R.id.editTextWeight)).setError("Invalid Weight value.");
            return false;
        }

        return true;
    }


    private boolean isHeightCorrect(){
        try {
            String height_s = String.valueOf(((EditText)findViewById(R.id.editTextHeight)).getText());
            BigInteger height = new BigInteger(height_s);

            if(height_s.isEmpty()){
                ((TextView) findViewById(R.id.editTextHeight)).setError("Missing Height value.");
                return false;
            }

            if (height.compareTo(BigInteger.ZERO) <= 0 ||
                    height.compareTo(BigInteger.valueOf(500)) > 0) {

                ((TextView) findViewById(R.id.editTextHeight)).setError("Height must be greater than 0 and lower than 500.");
                return false;
            }

        } catch (NumberFormatException e) {//Literki wyłapie
            ((TextView) findViewById(R.id.editTextHeight)).setError("Invalid Weight value.");
            return false;
        }

        return true;
    }

    private boolean isInputDataCorrect(){
        boolean isHeightCorrect = isHeightCorrect();
        boolean isWeightCorrect = isWeightCorrect();

        return isHeightCorrect && isWeightCorrect;
    }
}