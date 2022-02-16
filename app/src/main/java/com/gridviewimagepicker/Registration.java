package com.gridviewimagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser userAuth;
    TextInputEditText emailInput, passwordInput, confirmpasswordInput;
    Button btnRegister;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        userAuth = firebaseAuth.getCurrentUser();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmpasswordInput = findViewById(R.id.confirm_password);

        btnRegister = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String email, password, confirm_password;
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirm_password = confirmpasswordInput.getText().toString().trim();

        if(!email.matches(emailPattern)) {
            emailInput.setError("Enter correct email!");
            return;
        } else if(email.isEmpty()) {
            emailInput.setError("Please enter email!");
            return;
        } else if(password.length()<6) {
            passwordInput.setError("Password must be more than 6 digit");
            return;
        } else if(!password.equals(confirm_password)) {
            confirmpasswordInput.setError("Password must match both fields");
            return;
        }

        progressBar.setVisibility(View.GONE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    sendUserToNextActivity();
                    Toast.makeText(Registration.this,
                            "Registration Complete!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(Registration.this,
                            "Registration Failed, Please Try Again!"+task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(Registration.this,
                Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
