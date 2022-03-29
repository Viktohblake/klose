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
import com.gridviewimagepicker.fragments.ByLocationFragment;
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

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.profile);

//        bottomNavigationView.getOrCreateBadge(R.id.category).setNumber(11);

    }

    FragmentUserProfile fragmentUserProfile = new FragmentUserProfile();
    ByLocationFragment byLocationFragment = new ByLocationFragment();
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

            case R.id.usersbylocation:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,  byLocationFragment).commit();
                return true;

            case R.id.uploads:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, uploadsFragment).commit();
                return true;

            case R.id.category:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, categoryFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
