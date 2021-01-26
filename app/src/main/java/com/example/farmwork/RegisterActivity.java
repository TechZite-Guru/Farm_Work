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

    EditText full_name, user_mail, phone, confirm_pass;
    LinearLayout register_screen;
    Button register_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    ProgressDialog pd;
    private String userID, phone_number;
    private String register_email, register_fullName, search_name, register_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        register_screen = findViewById(R.id.register_activity);
        full_name = findViewById(R.id.et_name);
        user_mail = findViewById(R.id.et_email);
        phone = findViewById(R.id.et_phone);
        register_btn = findViewById(R.id.btn_register);

        userID = fAuth.getCurrentUser().getUid();

        SharedPreferences preferences = this.getSharedPreferences("Address", Context.MODE_PRIVATE);
        phone_number = preferences.getString("User_Phone", "");
        Log.d("Phone Number",": "+phone_number);

        phone.setText(phone_number);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_email = user_mail.getText().toString().trim();
                register_phone = phone.getText().toString().trim();
                register_fullName = full_name.getText().toString().trim();
                search_name = full_name.getText().toString().toLowerCase().trim();

                Log.d("User Phone", ": "+register_phone);

                if (TextUtils.isEmpty(register_fullName)) {
                    full_name.setError("FullName is required");
                    full_name.requestFocus();
                    return;
                }
                /*if (TextUtils.isEmpty(register_email)){
                    user_mail.setError("E-mail is required");
                    user_mail.requestFocus();
                    return;
                }*/
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
            user.put("email", register_email);
            user.put("phone", register_phone);
            user.put("search_name", search_name);
            fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("User Data", "User Data Upload Success");
                }
            });

            Toast.makeText(getApplicationContext(), "Your Account has been Created", Toast.LENGTH_SHORT).show();
            Log.d("User_Success", "createUserWithEmail:success");

            //Toast after successfull account creation

            Intent to_login = new Intent(RegisterActivity.this, Home.class);
            startActivity(to_login);
    }
}