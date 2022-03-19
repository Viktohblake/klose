package com.gridviewimagepicker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.pager.PagerHome;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {


    private String recieverUserId;
    Toolbar toolbar;
    TextView userName, userPhoneNo, userPhoneNo2, userLocation, userProfession, userSex, userAbout;
    ImageView userImage;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbarUserProfile);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayout = findViewById(R.id.goToImagePage);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, PagerHome.class);
                startActivity(intent);
            }
        });

        linearLayout2 = findViewById(R.id.goToVideoPage);

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, PagerHome.class);
                startActivity(intent);
            }
        });

        userName = findViewById(R.id.userProfileName);
        userPhoneNo = findViewById(R.id.userProfilePhoneNo);
        userPhoneNo2 = findViewById(R.id.userProfilePhoneNo2);
        userLocation = findViewById(R.id.userProfileLocation);
        userProfession = findViewById(R.id.userProfileProfession);
        userSex = findViewById(R.id.userProfileSex);
        userAbout = findViewById(R.id.userAboutMe);

        userImage = findViewById(R.id.userProfileImage);

        recieverUserId = getIntent().getExtras().get("user_id").toString();

        Toast.makeText(this, "This user id is" + recieverUserId, Toast.LENGTH_SHORT).show();

        initView();

    }

    public void initView() {
        DatabaseReference databaseReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Users").child(recieverUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }else {
                    String name = snapshot.child("mName").getValue().toString();
                    userName.setText(name);
                    String location = snapshot.child("location").getValue().toString();
                    userLocation.setText(location);
                    String sex = snapshot.child("sex").getValue().toString();
                    userSex.setText(sex);
                    String profession = snapshot.child("profession").getValue().toString();
                    userProfession.setText(profession);
                    String phoneNo = snapshot.child("phoneNo").getValue().toString();
                    userPhoneNo.setText(phoneNo);
                    String phoneNo2 = snapshot.child("phoneNo2").getValue().toString();
                    userPhoneNo2.setText(phoneNo2);
                    String aboutMe = snapshot.child("about").getValue().toString();
                    userAbout.setText(aboutMe);

                    String url = snapshot.child("uri").getValue().toString();
                    Picasso.get().load(url).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
