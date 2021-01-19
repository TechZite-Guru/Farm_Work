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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    EditText user_mail, password;
    Button login_but;
    FirebaseAuth fAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        fAuth = FirebaseAuth.getInstance();
        user_mail = root.findViewById(R.id.et_email);
        password = root.findViewById(R.id.et_password);

        login_but = root.findViewById(R.id.btn_login);

        if (fAuth.getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()){
            Log.d("ALREADY_LOGIN", "LOGIN Success with E-mail Verification");
            Intent to_home = new Intent(getActivity(),Home.class);
            startActivity(to_home);
        }

        if (fAuth.getCurrentUser() != null && !fAuth.getCurrentUser().isEmailVerified()){
            Log.d("ALREADY_LOGIN", "LOGIN Success with E-mail Verification");
            Intent to_home = new Intent(getActivity(),Home.class);
            startActivity(to_home);
        }


        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String login_email = user_mail.getText().toString().trim();
                String login_password = password.getText().toString().trim();

                if (TextUtils.isEmpty(login_email)) {
                    user_mail.setError("E-mail is required");
                    return;
                }
                if (TextUtils.isEmpty(login_password)) {
                    password.setError("Password is required");
                    return;
                }
                if (login_password.length() < 6) {
                    password.setError("Password length should be > 6");
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
                                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Log.d("Login_Success", "Login User With Email:success");
                                Intent to_main_home = new Intent(getActivity(), Home.class);
                                startActivity(to_main_home);
                            }
                        } else {
                            Log.w("Login_Failure", "Login User With Email:failure", task.getException());
                            //login_Progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "ERROR ! " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }
}