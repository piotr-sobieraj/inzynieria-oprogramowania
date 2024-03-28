package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PersonalInformation extends AppCompatActivity {
    private User user;
    final String documentId = "wgNYXUW3ot9njNv5zqfJ";//Dokument na sztywno!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
        //readDataFromDatabase();
    }

    private void setViewData() {
        /* @TODO wypełnić kontrolki danymi z bazy*/
    }

    private void readDataFromDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user = document.toObject(User.class);
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                setViewData();//Ustawia wartości kontrolek na dane odczytane z bazy
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void setUpUI() {
        EdgeToEdge.enable(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        Button date = (Button) findViewById(R.id.pickDate);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        date.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformation.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    @NonNull
    private String getNameFromView(){
        return ((TextView) findViewById(R.id.editTextName)).getText().toString();
    }

    @NonNull
    private String getSexFromView(){
        RadioButton radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        if(radioButtonFemale.isChecked())
            return "K";
        else
            return "M";
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
        return ((TextView)findViewById(R.id.editTextWaga)).getText().toString();
    }

    @NonNull
    private String getHeightFromView() {
        return ((TextView)findViewById(R.id.editTextWzrost)).getText().toString();
    }


    private void saveUserToDatabase(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @NonNull
    private User buildUserData() {
        return new User(getNameFromView(),getSexFromView(),getBirthDateFromView(),getHeightFromView(),
                getWeightFromView(),getTargetWeightFromView(), null);
    }

    public void openAddingMeals(View v){
        Intent addingMeals = new Intent(PersonalInformation.this, AddingMeals.class);
        addingMeals.putExtra("userKey", user);
        startActivity(addingMeals);
    }

    public void openMore(){
        Intent intent = new Intent(this, MoreUI.class);
        startActivity(intent);
    }

    public void saveUserToDatabaseAndOpenAddingMeals(View v){
        if (validateUser(buildUserData())){
            saveUserToDatabase(buildUserData()); //<- wyłączone, bo w tej chwili tylko odczytuję dane z bazy
            openMore();
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
        else if(Integer.parseInt(date[0]) == mDay && Integer.parseInt(date[1]) == mMonth - 1 && Integer.parseInt(date[2]) >= mYear - 13){
            ((Button)findViewById(R.id.pickDate)).setError("Too young");
            result = false;
        }
        else if (user.getHeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextWzrost)).setError("Height contains forbidden characters");
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
            ((TextView) findViewById(R.id.editTextWzrost)).setError("Missing Height");
            result = false;
        }
        else if (user.getHeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextWzrost)).setError("Height contains forbidden characters");
            result = false;
        }
        if (user.getWeight() == null || Objects.equals(user.getWeight(), "")) {
            ((TextView) findViewById(R.id.editTextWaga)).setError("Missing Weight");
            result = false;
        }
        else if (user.getWeight().matches("[a-zA-Z\\W]*[0-9]*[a-zA-Z\\W]+[0-9]*[a-zA-Z\\W]*")) {
            ((TextView) findViewById(R.id.editTextWaga)).setError("Weight contains forbidden characters");
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