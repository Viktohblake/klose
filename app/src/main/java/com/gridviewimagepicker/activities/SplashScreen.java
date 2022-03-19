package com.gridviewimagepicker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.authentication.Login;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser() != null) {
                    final Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    firebaseAuth.getCurrentUser();
                    final Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                }
//                final Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
