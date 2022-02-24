package com.gridviewimagepicker.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gridviewimagepicker.R;

public class ForgetPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button resetPassword;
    TextInputEditText resetMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        firebaseAuth = FirebaseAuth.getInstance();
        resetPassword = findViewById(R.id.reset_pass);
        resetMail = findViewById(R.id.reset_mail);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String mail = resetMail.getText().toString();

        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this,
                            "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgetPassword.this,
                            "Reset Password Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
