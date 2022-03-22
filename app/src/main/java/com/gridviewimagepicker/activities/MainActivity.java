package com.gridviewimagepicker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.authentication.Login;
import com.gridviewimagepicker.fragments.CategoryFragment;
import com.gridviewimagepicker.fragments.FragmentUserProfile;
import com.gridviewimagepicker.fragments.GalleryFragment;
import com.gridviewimagepicker.fragments.UploadsFragment;
import com.gridviewimagepicker.fragments.UsersFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

//        ViewGroup container = binding.container;
//
//        container.addView(new View(this) {
//            @Override
//            protected void onConfigurationChanged(Configuration newConfig) {
//                super.onConfigurationChanged(newConfig);
//                computeWindowSizeClasses();
//            }
//        });

//        computeWindowSizeClasses();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.getOrCreateBadge(R.id.category).setNumber(11);

    }

    FragmentUserProfile fragmentUserProfile = new FragmentUserProfile();
    GalleryFragment galleryFragment = new GalleryFragment();
    UsersFragment usersFragment = new UsersFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    UploadsFragment uploadsFragment = new UploadsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragmentUserProfile).commit();
                return true;

            case R.id.users:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, usersFragment).commit();
                return true;

            case R.id.uploads:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, uploadsFragment).commit();
                return true;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT);
                return true;

            case R.id.category:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, categoryFragment).commit();
                return true;
        }
        return false;
    }

//    private void computeWindowSizeClasses() {
//        WindowMetrics metrics = WindowMetricsCalculator.getOrCreate()
//                .computeCurrentWindowMetrics(this);
//
//        float widthDp = metrics.getBounds().width() /
//                getResources().getDisplayMetrics().density;
//        WindowSizeClass widthWindowSizeClass;
//
//        if (widthDp < 600f) {
//            widthWindowSizeClass = WindowSizeClass.COMPACT;
//        } else if (widthDp < 840f) {
//            widthWindowSizeClass = WindowSizeClass.MEDIUM;
//        } else {
//            widthWindowSizeClass = WindowSizeClass.EXPANDED;
//        }
//
//        float heightDp = metrics.getBounds().height() /
//                getResources().getDisplayMetrics().density;
//        WindowSizeClass heightWindowSizeClass;
//
//        if (heightDp < 480f) {
//            heightWindowSizeClass = WindowSizeClass.COMPACT;
//        } else if (heightDp < 900f) {
//            heightWindowSizeClass = WindowSizeClass.MEDIUM;
//        } else {
//            heightWindowSizeClass = WindowSizeClass.EXPANDED;
//        }
//
//        // Use widthWindowSizeClass and heightWindowSizeClass
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
