package com.inchub.experthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppDesc extends AppCompatActivity {

    MaterialButton gotItBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_desc);

        user = FirebaseAuth.getInstance().getCurrentUser();

        gotItBtn = findViewById(R.id.gotItButton);

        gotItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();

            }

        });


        if (user != null)
        {
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

    }
}