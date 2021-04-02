package com.example.farmwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OtpActivity extends AppCompatActivity {

    EditText otp;
    Button login_but;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userID, codeSent, phone;
    ProgressDialog pd;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        otp = findViewById(R.id.et_otp);
        pd = new ProgressDialog(this);

        login_but = findViewById(R.id.btn_login);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            codeSent = intent.getStringExtra("OTP");
        }
        Log.d("OTP",""+codeSent);

        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = otp.getText().toString();
                if (code.isEmpty()) {
                    otp.setError("OTP Required");
                    otp.requestFocus();
                    return;
                }
                if (code.length()<6) {
                    otp.setError("Enter 6 digits OTP");
                    otp.requestFocus();
                    return;
                }
                pd.setTitle("Logging in...");
                pd.show();
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //pd.setContentView(R.layout.progress_dialog);
                //pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                verifySignInCode();
            }
        });
    }

    private void checkAuth() {
        userID = fAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    pd.dismiss();
                    if (document.exists()) {
                        Log.d("TAG", "Document found" +userID);
                        Intent to_home = new Intent(OtpActivity.this,Home.class);
                        startActivity(to_home);
                    } else {
                        Log.d("TAG", "Document Not found. Staying in this Activity" +userID);
                        Intent to_register = new Intent(OtpActivity.this, RegisterActivity.class);
                        to_register.putExtra("phone_number", phone);
                        startActivity(to_register);
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });
    }

    private void verifySignInCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "signInWithCredential:success");

                            userID = fAuth.getCurrentUser().getUid();

                            /*Map<String, Object> user = new HashMap<>();
                            user.put("phone", phone);
                            fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("User Data", "User Data Upload Success");
                                }
                            });*/
                            FirebaseUser user = task.getResult().getUser();
                            Log.d("SUCCESS", "signInWithCredential:success" +user);
                            checkAuth();
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
}