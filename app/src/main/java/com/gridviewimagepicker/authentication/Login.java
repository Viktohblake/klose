package com.gridviewimagepicker.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.gridviewimagepicker.MainActivity;
import com.gridviewimagepicker.R;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextInputEditText emailInput, passwordInput;
    Button btnLogin;
    ProgressBar progressBar;
    TextView regPage, forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        regPage = findViewById(R.id.regPage);
        forgetPassword = findViewById(R.id.forgetPassword);

        if(firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        regPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword();
            }
        });
    }

    private void loginUser() {
        String email, password;
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if (email.isEmpty()) {
            emailInput.setError("Please enter your email!");
            return;
        } else if (password.isEmpty()) {
            passwordInput.setError("Please enter your password!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Sign-in existing user
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(Login.this,
                                        "Login Successful", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    private void forgetPassword() {
        Intent intent = new Intent(Login.this, ForgetPassword.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}