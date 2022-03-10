package com.gridviewimagepicker;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView profileImage, usrImage;
    private Button saveBtn;
    private EditText editName, editProfession, editAboutMe, editPhoneNo, editPhoneNo2;
    private Uri mUri;
    private ProgressBar mProgressbar;
    private static final int PICK_IMAGE = 1;
    private UploadTask mUploadTask;
    private StorageReference mStorageReference;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    private Users user_member;
    private String currentUserId;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButtonMale, radioButtonFemale;
    private Spinner spinner;
    boolean isNewUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user_member = new Users();
        profileImage = findViewById(R.id.profilePic);
        saveBtn = findViewById(R.id.userProfileSaveBtn);
        editName = findViewById(R.id.userNameField);
        editProfession = findViewById(R.id.userProfessionField);
        editAboutMe = findViewById(R.id.userAboutMeField);
        editPhoneNo = findViewById(R.id.userPhoneNoField);
        editPhoneNo2 = findViewById(R.id.userPhoneNoField2);
        mProgressbar = findViewById(R.id.userProfileProgressBar);
        usrImage = findViewById(R.id.profilePic);
        radioGroup = findViewById(R.id.radioGroupID);

        radioButtonMale = (RadioButton) findViewById(R.id.radioBtnMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioBtnFemale);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Users");
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = fUser.getUid();
        Log.i("currentUserId0", currentUserId);

        mStorageReference = FirebaseStorage.getInstance().getReference("Profile images");

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        spinner = findViewById(R.id.spinnerID);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressbar.setVisibility(View.VISIBLE);
                uploadData();
            }
        });

        try {
            Bundle bundle = getIntent().getExtras();

            user_member = (Users) bundle.getSerializable("user_object");

            profileImage = findViewById(R.id.profilePic);
            Picasso.get().load(user_member.getUri()).into(profileImage);

            editName.setText(user_member.getmName());
            editProfession.setText(user_member.getProfession());
            editAboutMe.setText(user_member.getAbout());
            editPhoneNo.setText(user_member.getPhoneNo());
            editPhoneNo2.setText(user_member.getPhoneNo2());
            if (user_member.getSex().equalsIgnoreCase("male")) {
                radioButtonMale.setChecked(true);
            } else {
                radioButtonFemale.setChecked(true);
            }
            String location = user_member.getLocation();
            ArrayAdapter spinnerAdapter = (ArrayAdapter) spinner.getAdapter();
            int spinnerPosition = spinnerAdapter.getPosition(location);
            spinner.setSelection(spinnerPosition);

        } catch (Exception e) {

        }

    }

    private String getFileExt(Uri uri) {
        Log.i("getFileExt", uri.toString());
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null) {
                isNewUpload = true;
                mUri = data.getData();
                Picasso.get().load(mUri).into(profileImage);
            }
        } catch (Exception e) {

        }
    }

    private void uploadData() {
        int selectID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectID);
        String sex = radioButton.getText().toString();

//        if (sex == null) {
//            return;
//        }

        String mName = editName.getText().toString();
        String location = spinner.getSelectedItem().toString();
        String profession = editProfession.getText().toString();
        String about = editAboutMe.getText().toString();
        String phoneNo = editPhoneNo.getText().toString();
        String phoneNo2 = editPhoneNo2.getText().toString();

        Picasso.get().load(mUri).into(usrImage);

        if ((!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(profession)
                && !TextUtils.isEmpty(about) && !TextUtils.isEmpty(phoneNo)
                && !TextUtils.isEmpty(location))
                && (isNewUpload ? mUri != null : true)) {

            if (isNewUpload) {
                final StorageReference reference =
                        mStorageReference.child(System.currentTimeMillis() + "." + getFileExt(mUri));
                mUploadTask = reference.putFile(mUri);
                mUploadTask.addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double uploadProgress = snapshot.getBytesTransferred() / snapshot.getTotalByteCount() * 100;
                        Log.i("addOnProgressListener", String.format("Percentage Done %2f", uploadProgress));
                    }
                });
                Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();

                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            Map<String, String> profile = new HashMap<>();
                            profile.put("mName", mName);
                            profile.put("sex", sex);
                            profile.put("location", location);
                            profile.put("profession", profession);
                            profile.put("about", about);
                            profile.put("privacy", "public");
                            profile.put("Uri", downloadUri != null ? downloadUri.toString() : "");
                            profile.put("uid", currentUserId);
                            profile.put("profile images", "default");
                            profile.put("phoneNo", phoneNo);
                            profile.put("phoneNo2", phoneNo2);

                            user_member.setmName(mName);
                            user_member.setSex(sex);
                            user_member.setLocation(location);
                            user_member.setProfession(profession);
                            user_member.setPhoneNo(phoneNo);
                            user_member.setPhoneNo2(phoneNo2);
                            user_member.setAbout(about);
                            user_member.setUri(downloadUri.toString());
                            user_member.setUserid(currentUserId);

                            Log.i("NEW_IMG_URI", user_member.getUri());

                            isNewUpload = false;
                            mDatabaseReference.child(currentUserId).setValue(user_member)/*;
                            mDatabaseReference.child(currentUserId).setValue(profile)*/
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(getApplicationContext(),
                                                    "Profile Created Successfully", Toast.LENGTH_LONG);

                                            mProgressbar.setVisibility(View.GONE);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(EditProfileActivity.this,
                                                            MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }, 100);

                                        }
                                    });
                        }
                    }
                });
            } else {

                user_member.setmName(mName);
                user_member.setSex(sex);
                user_member.setLocation(location);
                user_member.setProfession(profession);
                user_member.setPhoneNo(phoneNo);
                user_member.setPhoneNo2(phoneNo2);
                user_member.setAbout(about);
//                user_member.setUrl(downloadUri.toString());
                user_member.setUserid(currentUserId);
                Log.i("CURRENT_IMG_URI", user_member.getUri());

                mDatabaseReference.child(currentUserId).setValue(user_member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(),
                                        "Profile Created Successfully", Toast.LENGTH_LONG);

                                mProgressbar.setVisibility(View.GONE);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(EditProfileActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 100);

                            }
                        });
            }

        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
