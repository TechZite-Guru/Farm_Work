package com.example.farmwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    EditText full_name, user_mail, password, confirm_pass;
    Button register_btn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        full_name = root.findViewById(R.id.et_name);
        user_mail = root.findViewById(R.id.et_email);
        password = root.findViewById(R.id.et_password);
        confirm_pass = root.findViewById(R.id.et_repassword);
        register_btn = root.findViewById(R.id.btn_register);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String register_email = user_mail.getText().toString().trim();
                String register_password = password.getText().toString().trim();
                final String register_fullName = full_name.getText().toString().trim();

                if (TextUtils.isEmpty(register_fullName)){
                    full_name.setError("FullName is required");
                    return;
                }
                if (TextUtils.isEmpty(register_email)){
                    user_mail.setError("E-mail is required");
                    return;
                }
                if (TextUtils.isEmpty(register_password)){
                    password.setError("Password is required");
                    return;
                }
                if (register_password.length()<6){
                    password.setError("Password length should be >6");
                    return;
                }
                //registerProgress.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                //registerProgress.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(register_email,register_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Storing User data in Cloud FIRESTORE
                            userID = fAuth.getCurrentUser().getUid();
                            //registerProgress.setVisibility(View.INVISIBLE);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", register_fullName);
                            user.put("E-mail", register_email);
                            fStore.collection("users").document(userID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("User Data", "User Data Upload Success");
                                }
                            });

                            Toast.makeText(getActivity(), "Your Account has been Created", Toast.LENGTH_SHORT).show();
                            Log.d("User_Success", "createUserWithEmail:success");
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            //send verification E-mail
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Verification E-mail has been sent. Please verify your Account.", Toast.LENGTH_SHORT).show();
                                }
                            }). addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Verify E-mail","onFailure: E-mail not sent " + e.getMessage());
                                }
                            });

                            //Toast after successfull account creation

                            Intent to_login = new Intent(getActivity(),Home.class);
                            startActivity(to_login);
                        }
                        else{
                            Log.w("User_Failure", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "ERROR ! " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return root;
    }
}