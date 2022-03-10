package com.gridviewimagepicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.gridviewimagepicker.authentication.Login;

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

        bottomNavigationView.getOrCreateBadge(R.id.category).setNumber(2);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
