package com.gridviewimagepicker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

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
import com.gridviewimagepicker.fragments.UploadsFragment;
import com.gridviewimagepicker.model.Users;
import com.gridviewimagepicker.pager.PagerHome;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {


    private String receiverUserId;
    Toolbar toolbar;
    TextView userName, userPhoneNo, userPhoneNo2, userLocation, userProfession, userSex, userAbout, userAddress;
    ImageView userImage;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;

    LinearLayout addressLinearLayout, locationLinearLayout, phoneLinearLayout, aboutMeLinearLayout,
            professionLinearLayout, sexLinearLayout;

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

        linearLayout2 = findViewById(R.id.goToVideoPage);

        userName = findViewById(R.id.userProfileName);
        userPhoneNo = findViewById(R.id.userProfilePhoneNo);
        userPhoneNo2 = findViewById(R.id.userProfilePhoneNo2);
        userLocation = findViewById(R.id.userProfileLocation);
        userProfession = findViewById(R.id.userProfileProfession);
        userSex = findViewById(R.id.userProfileSex);
        userAbout = findViewById(R.id.userAboutMe);
        userImage = findViewById(R.id.userProfileImage);
        userAddress = findViewById(R.id.userProfileAddress);

        addressLinearLayout = findViewById(R.id.addressLinearLayout);
        locationLinearLayout = findViewById(R.id.locationLinearLayout);
        phoneLinearLayout = findViewById(R.id.phoneLinearLayout);
        aboutMeLinearLayout = findViewById(R.id.aboutMeLinearLayout);
        professionLinearLayout = findViewById(R.id.professionLinearLayout);
        sexLinearLayout = findViewById(R.id.sexLinearLayout);

        receiverUserId = getIntent().getExtras().get("user_id").toString();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PagerHome.class);
                intent.putExtra("user_id", receiverUserId);
                startActivity(intent);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PagerHome.class);
                intent.putExtra("user_id", receiverUserId);
                startActivity(intent);
            }
        });

//        Toast.makeText(this, "This user id is" + recieverUserId, Toast.LENGTH_SHORT).show();

        initView();

    }

    public void initView() {
        DatabaseReference databaseReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Users").child(receiverUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }else if(snapshot.child("role").exists()) {
                    addressLinearLayout.setVisibility(View.GONE);
                    professionLinearLayout.setVisibility(View.GONE);
                    sexLinearLayout.setVisibility(View.GONE);

                    String name = snapshot.child("mName").getValue().toString();
                    userName.setText(name);
                    String location = snapshot.child("location").getValue().toString();
                    userLocation.setText(location);
                    String phoneNo = snapshot.child("phoneNo").getValue().toString();
                    userPhoneNo.setText(phoneNo);
                    String phoneNo2 = snapshot.child("phoneNo2").getValue().toString();
                    userPhoneNo2.setText(phoneNo2);

                    String url = snapshot.child("uri").getValue().toString();
                    Picasso.get().load(url).into(userImage);
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
                    String address = snapshot.child("address").getValue().toString();
                    userAddress.setText(address);

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
