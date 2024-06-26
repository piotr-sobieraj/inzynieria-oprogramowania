package com.example.myapplication;

import static android.content.ContentValues.TAG;

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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.math.BigInteger;

import java.util.HashMap;
import java.util.Objects;

public class PersonalInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
    }

    @Override
    public void onBackPressed() {
//         super.onBackPressed();
    }

    private void setUpUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkedRadioButton();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        Button date = findViewById(R.id.pickDate);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR) - 15;
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String date_string = mDay + "/" + (mMonth + 1) + "/" + mYear;
        date.setText(date_string);
        date.setOnClickListener(v -> {
            SpinnerDatePickerDialog datePickerDialog = new SpinnerDatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String date_s = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        date.setText(date_s);
                    },
                    mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        ((TextView) findViewById(R.id.editTextName)).setText(SignUp.firstAndLastName);
    }
    public void checkedRadioButton(){
        RadioGroup radioGroup = findViewById(R.id.sexRadioGroup2);
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

    @NonNull
    private String getNameFromView(){
        return ((TextView) findViewById(R.id.editTextName)).getText().toString();
    }

    @NonNull
    private String getSexFromView(){
        RadioButton radioButtonFemale = findViewById(R.id.radioButtonFemale);
        if(radioButtonFemale.isChecked())
            return "f";
        else
            return "m";
    }

    @NonNull
    private String getBirthDateFromView() {
        return ((Button)findViewById(R.id.pickDate)).getText().toString();
    }

    @NonNull
    private String getTargetWeightFromView() {
        return ((TextView)findViewById(R.id.editTextTargetWeight)).getText().toString();
    }

    @NonNull
    private String getWeightFromView() {
        return ((TextView)findViewById(R.id.editTextWeight)).getText().toString();
    }

    @NonNull
    private String getHeightFromView() {
        return ((TextView)findViewById(R.id.editTextHeight)).getText().toString();
    }

    private void saveUserToDatabase(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("userUID", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()){
                            Log.d("Firebase", "Successful logged user");
                            Intent intent = new Intent(PersonalInformation.this, Plan.class);
                            startActivity(intent);
                        }
                        else {
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        final Calendar c = Calendar.getInstance();
                                        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
                                        MealDay mealDay = new MealDay(date, new HashMap<>());
                                        RecentMeal recentMeal = new RecentMeal(new HashMap<>());
                                        WeightDay weightDay = new WeightDay(date, new ArrayList<>());
                                        db.collection("users").document(documentReference.getId()).collection("mealDays").add(mealDay).addOnSuccessListener(task2 -> Log.d(TAG, "DocumentSnapshot added"));
                                        db.collection("users").document(documentReference.getId()).collection("recentMeal").add(recentMeal).addOnSuccessListener(task2 -> Log.d(TAG, "DocumentSnapshot added"));
                                        db.collection("users").document(documentReference.getId()).collection("weightDays").add(weightDay).addOnSuccessListener(task2 -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()));

                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                            Intent intent = new Intent(PersonalInformation.this, Plan.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    @NonNull
    private User buildUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new User(Objects.requireNonNull(firebaseUser).getUid(),
                getNameFromView(),
                getSexFromView(),
                getBirthDateFromView(),
                getHeightFromView(),
                getWeightFromView(),
                getTargetWeightFromView(),
                null,
                null);
    }

    public void openMenu(){
        Intent intent = new Intent(this, Plan.class);
        startActivity(intent);
    }

    public void saveUserToDatabaseAndOpenMenu(View v){
        if (validateUser(buildUserData())){
            saveUserToDatabase(buildUserData());
        }
    }

    private boolean validateUser(User user) {

        boolean isBirthDateCorrect = isBirthDateCorrect(user);
        boolean isNameCorrect = isNameCorrect(user);
        boolean isSexCorrect = isSexCorrect(user);
        boolean isHeightCorrect = isHeightCorrect();
        boolean isWeightCorrect = isWeightCorrect();
        boolean isTargetWeightCorrect = isTargetWeightCorrect();


        return isBirthDateCorrect &&
               isNameCorrect &&
               isSexCorrect &&
               isHeightCorrect &&
               isWeightCorrect &&
               isTargetWeightCorrect;
    }

    private boolean isBirthDateCorrect(User user){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String[] date = user.getBirthDate().split("/");
        if (user.getBirthDate() == null || Objects.equals(user.getBirthDate(), ""))
            return false;
        else if (Integer.parseInt(date[0]) == mDay && Integer.parseInt(date[1]) == mMonth + 1 && Integer.parseInt(date[2]) >= mYear - 13) {
            ((Button) findViewById(R.id.pickDate)).setError("Too young");
            return false;
        }

        return true;
    }
    private boolean isNameCorrect(User user){
        if (user.getName() == null || Objects.equals(user.getName(), "")) {
            ((TextView) findViewById(R.id.editTextName)).setError("Missing Name");
            return false;
        }

        return true;
    }

    private boolean isSexCorrect(User user){
        return (user.getSex() != null && !Objects.equals(user.getSex(), ""));
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
            ((TextView) findViewById(R.id.editTextHeight)).setError("Invalid Height value.");
            return false;
        }

        return true;
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


    private boolean isTargetWeightCorrect(){
        try {
            String weight_s = String.valueOf(((EditText)findViewById(R.id.editTextTargetWeight)).getText());
            BigInteger weight = new BigInteger(weight_s);

            if(weight_s.isEmpty()){
                ((TextView) findViewById(R.id.editTextTargetWeight)).setError("Missing Target Weight value.");
                return false;
            }

            if (weight.compareTo(BigInteger.ZERO) <= 0 ||
                    weight.compareTo(BigInteger.valueOf(10000)) > 0) {

                ((TextView) findViewById(R.id.editTextTargetWeight)).setError("target Weight must be greater than 0 and lower than 10000.");
                return false;
            }

        } catch (NumberFormatException e) {//Literki wyłapie
            ((TextView) findViewById(R.id.editTextTargetWeight)).setError("Invalid Target Weight value.");
            return false;
        }

        return true;
    }

}