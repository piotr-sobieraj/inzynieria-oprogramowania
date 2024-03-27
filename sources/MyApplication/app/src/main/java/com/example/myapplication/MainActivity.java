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
import java.util.Map;
import java.util.HashMap;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private User user;
    final String documentId = "wgNYXUW3ot9njNv5zqfJ";//Dokument na sztywno!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
        readDataFromDatabase();
    }

    private void setViewData() {

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


    public void saveUserToDatabase(){
        Map<String, Object> user = buildUserData();
        saveUserToDatabase(user);
    }

    private static void saveUserToDatabase(Map<String, Object> user) {
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
    private Map<String, Object> buildUserData() {
        Map<String, Object> user = new HashMap<>();
        String name = getNameFromView();
        String sex = getSexFromView();
        String birthDate = getBirthDateFromView();
        String height = getHeightFromView();
        String weight = getWeightFromView();
        String targetWeight = getTargetWeightFromView();

        user.put("name", name);
        user.put("sex", sex);
        user.put("birthDate", birthDate);
        user.put("height", height);
        user.put("weight", weight);
        user.put("targetWeight", targetWeight);
        return user;
    }

    public void openAddingMeals(View v){
        Intent addingMeals = new Intent(this, AddingMeals.class);
        startActivity(addingMeals);
    }

    public void saveUserToDatabaseAndOpenAddingMeals(View v){
        saveUserToDatabase();
        openAddingMeals(v);
    }
}