package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class PersonalInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
    }

    private void setUpUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkedRadioButton();
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
                        String date_s = dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year);
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
        return ((TextView)findViewById(R.id.editTextDocelowaWaga)).getText().toString();
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
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    final Calendar c = Calendar.getInstance();
                    String date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
                    MealDay mealDay = new MealDay(date, new HashMap<>());
                    RecentMeal recentMeal = new RecentMeal(new HashMap<>());
                    db.collection("users").document(documentReference.getId()).collection("mealDays").add(mealDay).addOnSuccessListener(task -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()));
                    db.collection("users").document(documentReference.getId()).collection("recentMeal").add(recentMeal).addOnSuccessListener(task -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()));
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
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

    public void saveUserToDatabaseAndOpenAddingMeals(View v){
        if (validateUser(buildUserData())){
            saveUserToDatabase(buildUserData());
            openMenu();
        }
    }

    private boolean validateUser(User user){
        boolean result = true;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String[] date = user.getBirthDate().split("/");
        if(user.getBirthDate() == null || Objects.equals(user.getBirthDate(), ""))
            result = false;
        else if(Integer.parseInt(date[0]) == mDay && Integer.parseInt(date[1]) == mMonth + 1 && Integer.parseInt(date[2]) >= mYear - 13){
            ((Button)findViewById(R.id.pickDate)).setError("Too young");
            result = false;
        }
        else if (user.getHeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextHeight)).setError("Height contains forbidden characters");
            result = false;
        }
        if (user.getSex() == null || Objects.equals(user.getSex(), ""))
            result = false;
        if (user.getName() == null || Objects.equals(user.getName(), "")){
            ((TextView) findViewById(R.id.editTextName)).setError("Missing Name");
            result = false;
        }
        else if (user.getName().matches("\\W*[a-zA-Z0-9]*\\W+[a-zA-Z0-9]*\\W*")) {
            ((TextView) findViewById(R.id.editTextName)).setError("Name contains forbidden characters");
            result = false;
        }
        if (user.getHeight() == null || Objects.equals(user.getHeight(), "")) {
            ((TextView) findViewById(R.id.editTextHeight)).setError("Missing Height");
            result = false;
        }
        else if (user.getHeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextHeight)).setError("Height contains forbidden characters");
            result = false;
        }
        if (user.getWeight() == null || Objects.equals(user.getWeight(), "")) {
            ((TextView) findViewById(R.id.editTextWeight)).setError("Missing Weight");
            result = false;
        }
        else if (user.getWeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextWeight)).setError("Weight contains forbidden characters");
            result = false;
        }
        if (user.getTargetWeight() == null || Objects.equals(user.getTargetWeight(), "")) {
            ((TextView) findViewById(R.id.editTextDocelowaWaga)).setError("Missing Target Weight");
            result = false;
        }
        else if (user.getTargetWeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextDocelowaWaga)).setError("Target Weight contains forbidden characters");
            result = false;
        }
        return result;
    }
}