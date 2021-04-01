package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText full_name, user_mail, phone, confirm_pass, fare;
    TextView fare_title;
    LinearLayout register_screen;
    Button register_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    ProgressDialog pd;
    RadioGroup radioGroup;
    RadioButton radioButton, worker_radioButton, farmer_radioButton;
    private String userID, phone_number, AlphaNumericString, random_string_generated;
    private String register_email, register_fullName, search_name, register_phone, role, fare_amount;
    int n = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        register_screen = findViewById(R.id.register_activity);
        full_name = findViewById(R.id.et_name);
        //user_mail = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        register_btn = findViewById(R.id.btn_register);
        radioGroup = findViewById(R.id.radio_group_role);
        worker_radioButton = findViewById(R.id.worker_role_radiobtn);
        farmer_radioButton = findViewById(R.id.farmer_role_radiobtn);
        fare = findViewById(R.id.fare);
        fare_title = findViewById(R.id.fare_title);

        userID = fAuth.getCurrentUser().getUid();

        SharedPreferences preferences = this.getSharedPreferences("Address", Context.MODE_PRIVATE);
        phone_number = preferences.getString("User_Phone", "");
        Log.d("Phone Number",": "+phone_number);

        phone.setText(phone_number);

        AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
            random_string_generated = sb.toString();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (worker_radioButton.isChecked()) {
                    fare_title.setVisibility(View.VISIBLE);
                    fare.setVisibility(View.VISIBLE);
                }
                int visibility = fare.getVisibility();
                int visibility2 = fare_title.getVisibility();
                if (farmer_radioButton.isChecked()) {
                    if (visibility == View.VISIBLE && visibility2 == View.VISIBLE) {
                        fare_title.setVisibility(View.GONE);
                        fare.setVisibility(View.GONE);
                    }
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selected_radio_id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_radio_id);

                role = radioButton.getText().toString().trim();
                Toast.makeText(getApplicationContext(), role, Toast.LENGTH_LONG).show();
                //register_email = user_mail.getText().toString().trim();
                register_phone = phone.getText().toString().trim();
                register_fullName = full_name.getText().toString().trim();
                search_name = full_name.getText().toString().toLowerCase().trim();
                fare_amount = fare.getText().toString().trim();

                Log.d("User Phone", ": "+register_phone);

                if (TextUtils.isEmpty(register_fullName)) {
                    full_name.setError("FullName is required");
                    full_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(register_phone)){
                    user_mail.setError("Phone Number is required");
                    user_mail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(role)){
                    user_mail.setError("Select Who you are");
                    user_mail.requestFocus();
                    return;
                }
                if (role.equals("Worker")) {
                    if (TextUtils.isEmpty(fare_amount)){
                        user_mail.setError("Enter your Fare");
                        user_mail.requestFocus();
                        return;
                    }
                }
                //registerProgress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                //registerProgress.setVisibility(View.VISIBLE);
                storeUserData();
            }
        });
    }

    public void storeUserData() {
        //Storing User data in Cloud FIRESTORE
        //registerProgress.setVisibility(View.INVISIBLE);
        Map<String, Object> user = new HashMap<>();
        user.put("name", register_fullName);
        //user.put("email", register_email);
        user.put("phone", register_phone);
        user.put("roles", role);
        user.put("search_name", search_name);
        user.put("random_string", random_string_generated);
        Log.d("Random String : ", random_string_generated);
        if (role.equals("Worker")){
            user.put("fare", fare_amount);
        }
        fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("User Data", "User Data Upload Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User Data", "User Data Upload UnSuccess");
            }
        });

        if (role.equals("Worker")) {
            Map<String, Object> date = new HashMap<>();
            date.put("booking_date", "10/12/1999");
            fStore.collection("Worker").document(userID).collection("BookedBy").document("test").set(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("User Data", "User Data Upload Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("User Data", "User Data Upload UnSuccess");
                }
            });
        }

        Toast.makeText(getApplicationContext(), "Your Account has been Created", Toast.LENGTH_SHORT).show();
        Log.d("User_Success", "createUserWithEmail:success");

        //Toast after successfull account creation

        Intent to_login = new Intent(RegisterActivity.this, Home.class);
        startActivity(to_login);
    }
}