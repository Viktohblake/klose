package com.gridviewimagepicker;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FragmentUserProfile extends Fragment {

    private static final int RESULT_OK = 1 ;
    private ImageView usrImage, cameraButton, userDisplayPic, mediaBtn, settingsButton;
    private TextView usrName, usrLocation, usrSex, usrAboutMe, usrPhoneNo, usrPhoneNo2, usrProfession,
            verifiedTxt, verifyBtn;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference rootReference, friendRequestRef, mUserRef;
    private static final int PICK_IMAGE = 1;
    private Uri mUri;
    String senderId;
    Button editProfileBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usrImage = view.findViewById(R.id.user_profilePic);
        usrName = view.findViewById(R.id.userNameId);
        usrSex = view.findViewById(R.id.userSexID);
        usrAboutMe = view.findViewById(R.id.userAboutID);
        usrPhoneNo = view.findViewById(R.id.userPhoneNoID);
        usrPhoneNo2 = view.findViewById(R.id.userPhoneNo2ID);
        usrProfession = view.findViewById(R.id.userProfessionID);

        editProfileBtn = view.findViewById(R.id.userProfileEditBtn);
        verifiedTxt = view.findViewById(R.id.verifiedTxtID);
        verifyBtn = view.findViewById(R.id.emailVerifyBtn);
//        cameraButton = view.findViewById(R.id.cameraBtnID);
        userDisplayPic = view.findViewById(R.id.user_profilePic);
//        mediaBtn = view.findViewById(R.id.mediaBtnId);
        usrLocation = view.findViewById(R.id.locationID);
//        settingsButton = view.findViewById(R.id.settingsBtnID);
        rootReference = FirebaseDatabase.getInstance().getReference();

        // Intent intent = new Intent();

        initViews();

        userDisplayPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,PICK_IMAGE);
//
//            }
//        });
//
//        mediaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SettingsActivity.class);
//                startActivity(intent);
//            }
//        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        senderId = mFirebaseAuth.getCurrentUser().getUid();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null) {
                mUri = data.getData();

                Picasso.get().load(mUri).into(userDisplayPic);
            }
        }catch(Exception e){

        }
    }

    void initViews(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        if(!user.isEmailVerified()){
            verifiedTxt.setText("UNVERIFIED");
            verifiedTxt.setTextColor(Color.parseColor("#E91E63"));
            verifiedTxt.setVisibility(View.VISIBLE);
            verifyBtn.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification link was sent to the email address", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("tag", "onFailure: email not sent" + e.getMessage());
                        }
                    });
                }
            });
        } else {

            verifyBtn.setVisibility(View.INVISIBLE);
            verifiedTxt.setText("VERIFIED");
            verifiedTxt.setTextColor(Color.parseColor("#1FE427"));
        }

        DatabaseReference rootReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Log.i("currentUserId1", currentUserId);
        rootReference = firebaseDatabase.getReference("Users").child(currentUserId);
        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                }else {
                    String name = snapshot.child("mName").getValue().toString();
                    usrName.setText(name);
                    String aboutMe = snapshot.child("about").getValue().toString();
                    usrAboutMe.setText(aboutMe);
                    String location = snapshot.child("location").getValue().toString();
                    usrLocation.setText(location);
                    String sex = snapshot.child("sex").getValue().toString();
                    usrSex.setText(sex);
                    String profession = snapshot.child("profession").getValue().toString();
                    usrProfession.setText(profession);
                    String phoneNo = snapshot.child("phoneNo").getValue().toString();
                    usrPhoneNo.setText(phoneNo);
                    String phoneNo2 = snapshot.child("phoneNo2").getValue().toString();
                    usrPhoneNo2.setText(phoneNo2);

                    String url = snapshot.child("Uri").getValue().toString();
                    Picasso.get().load(url).into(usrImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
