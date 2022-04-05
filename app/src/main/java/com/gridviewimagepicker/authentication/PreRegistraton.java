package com.gridviewimagepicker.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.gridviewimagepicker.R;

public class PreRegistraton extends AppCompatActivity {

    Button btnEmployer;
    Button btnWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_registration_layout);

        btnEmployer = findViewById(R.id.registerAsEmployer);
        btnWorker = findViewById(R.id.registerAsWorker);

        btnEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreRegistraton.this, RegistrationForEmployer.class);
                startActivity(intent);
            }
        });

        btnWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreRegistraton.this, Registration.class);
                startActivity(intent);
            }
        });
    }
}
