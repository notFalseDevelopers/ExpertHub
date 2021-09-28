package com.inchub.experthub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    AppCompatImageView backNavigation;
    AppCompatTextView resetPassword, signUp;
    MaterialButton signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextInputEditText loginEmail, loginPassword;
        backNavigation = findViewById(R.id.backToSignUp);
        resetPassword = findViewById(R.id.resetPasswordLink);
        signIn = findViewById(R.id.signInButton);
        signUp = findViewById(R.id.goToSignUp);

        loginEmail = findViewById(R.id.logEmail);
        loginPassword = findViewById(R.id.logPassword);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

        backNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(loginEmail.getText().toString()))
                {
                    loginEmail.setError("Please Enter Email");
                }
                else if (TextUtils.isEmpty(loginPassword.getText().toString()))
                {
                    loginPassword.setError("Please enter password");
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        Toast.makeText(SignIn.this, "login Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reset_Password rf = new Reset_Password();
                rf.show(getSupportFragmentManager(),Reset_Password.class.getSimpleName());
            }
        });
    }
}