package com.example.farmwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText user_phone, otp;
    Button login_but, send_otp;
    TextView to_register_but;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    ProgressDialog pd;
    String userId;
    private String phone, ph_number;
    private String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user_phone = findViewById(R.id.et_phone);
        //otp = findViewById(R.id.et_otp);

        login_but = findViewById(R.id.btn_login);
        send_otp = findViewById(R.id.request_otp);
        pd = new ProgressDialog(this);

        if (fAuth.getCurrentUser() != null) {
            Log.d("ALREADY_LOGIN", "LOGIN Success with Phone Verification");
            startActivity(new Intent(LoginActivity.this, Home.class));
        }

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph_number = user_phone.getText().toString().trim();
                if (ph_number.isEmpty()) {
                    user_phone.setError("Phone Number Required");
                    user_phone.requestFocus();
                    return;
                }
                if (ph_number.length() < 10) {
                    user_phone.setError("Invalid Phone Number");
                    user_phone.requestFocus();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Sending OTP.....", Toast.LENGTH_SHORT).show();
                sendVerificationCode();
            }
        });



        /*login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String login_email = user_phone.getText().toString().trim();
                String login_password = otp.getText().toString().trim();

                if (TextUtils.isEmpty(login_email)) {
                    user_phone.setError("E-mail is required");
                    return;
                }
                if (TextUtils.isEmpty(login_password)) {
                    otp.setError("Password is required");
                    return;
                }
                if (login_password.length() < 6) {
                    otp.setError("Password length should be > 6");
                    return;
                }
                //login_Progress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                //login_Progress.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = fAuth.getCurrentUser();
                            assert user != null;
                            if (!user.isEmailVerified()) {
                                //login_Progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(v.getContext(), "Please verify your E-mail before login.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Log.d("Login_Success", "Login User With Email:success");
                                Intent to_main_home = new Intent(LoginActivity.this, Home.class);
                                startActivity(to_main_home);
                            }
                        } else {
                            Log.w("Login_Failure", "Login User With Email:failure", task.getException());
                            //login_Progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "ERROR ! " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        to_register_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(to_register);
            }
        });*/
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "signInWithCredential:success");



                            /*Map<String, Object> user = new HashMap<>();
                            user.put("phone", phone);
                            fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("User Data", "User Data Upload Success");
                                }
                            });*/

                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                            FirebaseUser user = task.getResult().getUser();
                            Log.d("SUCCESS", "signInWithCredential:success" +user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("FAILURE", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void sendVerificationCode() {
        String india_code = "+91";
        phone = india_code+ph_number;
        Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_LONG).show();

        fAuth.useAppLanguage();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            SharedPreferences.Editor editor = getSharedPreferences("Address", MODE_PRIVATE).edit();
            editor.putString("User_Phone",phone);
            editor.apply();
            Intent to_otp_page = new Intent(LoginActivity.this, OtpActivity.class);
            to_otp_page.putExtra("OTP", codeSent);
            startActivity(to_otp_page);
        }
    };
}