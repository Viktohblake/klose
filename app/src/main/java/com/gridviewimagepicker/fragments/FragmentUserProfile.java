package com.gridviewimagepicker.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gridviewimagepicker.activities.EditProfileActivity;
import com.gridviewimagepicker.activities.MainActivity;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.location.SeeLocation;
import com.gridviewimagepicker.model.Users;
import com.squareup.picasso.Picasso;

public class FragmentUserProfile extends Fragment {

    private static final int RESULT_OK = 1;
    private ImageView usrImage, userDisplayPic, mediaBtn, settingsButton;
    private TextView usrName, usrLocation, usrSex, usrAboutMe, usrPhoneNo, usrPhoneNo2, usrProfession, usrAddress,
            verifiedTxt, verifyBtn, upgradePremiumBtn, locationBtn, changePasswordBtn;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference rootReference;
    private static final int PICK_FILE = 1;
    private Uri mUri;
    String senderId;
    Users userObject;
    ProgressDialog progressDialog;

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
        usrLocation = view.findViewById(R.id.locationID);
        usrAddress = view.findViewById(R.id.addressID);

        verifiedTxt = view.findViewById(R.id.verifiedTxtID);
//        verifyBtn = view.findViewById(R.id.emailVerifyBtn);

        locationBtn = view.findViewById(R.id.locationBtnID);
        changePasswordBtn = view.findViewById(R.id.changePasswordBtnID);

        userDisplayPic = view.findViewById(R.id.user_profilePic);
        mediaBtn = view.findViewById(R.id.mediaBtnId);
        settingsButton = view.findViewById(R.id.settingsBtnID);
        rootReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        initViews();

        userDisplayPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_FILE);
            }
        });

        mediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadsFragment uploadsFragment = new UploadsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, uploadsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_object", userObject );
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeeLocation.class);
                startActivity(intent);
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        senderId = mFirebaseAuth.getCurrentUser().getUid();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_FILE || resultCode == RESULT_OK || data != null || data.getData() != null) {
                mUri = data.getData();

                Picasso.get().load(mUri).into(userDisplayPic);
            }
        } catch (Exception e) {

        }
    }

    private void changePasswordDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.changepassword_dialog, null);

        TextInputEditText newPasswordEt = view.findViewById(R.id.new_password);
        TextInputEditText oldPasswordEt = view.findViewById(R.id.old_password);
        Button updatePasswordBtn = view.findViewById(R.id.update_pass);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        builder.show();

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPassword = oldPasswordEt.getText().toString();
                String newPassword = newPasswordEt.getText().toString();

                if(TextUtils.isEmpty(oldPassword)){
                    oldPasswordEt.setError("Enter your old password");
                } else if (TextUtils.isEmpty(newPassword)) {
                    newPasswordEt.setError("Enter you new password");
                } else if (oldPassword.matches(newPassword)) {
                    Toast.makeText(getActivity(), "Old and new password is still the same", Toast.LENGTH_SHORT);
                } if (oldPassword.length() < 6) {
                    oldPasswordEt.setError("Password must be more than 6 digit");
                } else if (newPassword.length() < 6) {
                    newPasswordEt.setError("Password must be more than 6 digit");
                }

                updatePassword(oldPassword, newPassword);

                dialog.dismiss();

            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {
        progressDialog.show();

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        //before changing password re-authenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), oldPassword);
        firebaseUser.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //successfully updated
                firebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed
                progressDialog.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        if (!user.isEmailVerified()) {
            verifiedTxt.setText("UNVERIFIED");
            verifiedTxt.setTextColor(Color.parseColor("#E91E63"));
            verifiedTxt.setVisibility(View.VISIBLE);
//            verifyBtn.setVisibility(View.VISIBLE);

//            verifyBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(v.getContext(), "Verification link was sent to the email address", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.i("tag", "onFailure: email not sent" + e.getMessage());
//                        }
//                    });
//                }
//            });
        } else if(user.isEmailVerified()){
            verifyBtn.setVisibility(View.GONE);
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
                if (!snapshot.exists()) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                } else {
                    userObject = snapshot.getValue(Users.class);
                    usrName.setText(userObject.getmName());
                    usrLocation.setText(userObject.getLocation());
                    usrSex.setText(userObject.getSex());
                    usrProfession.setText(userObject.getProfession());
                    usrPhoneNo.setText(userObject.getPhoneNo());
                    usrPhoneNo2.setText(userObject.getPhoneNo2());
                    usrAboutMe.setText(userObject.getAbout());
                    usrAddress.setText(userObject.getAddress());

                    Picasso.get().load(userObject.getUri()).into(usrImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Profile");
    }

}
