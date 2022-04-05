package com.gridviewimagepicker.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.model.Users;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileForEmployer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView profileImage, usrImage;
    private Button saveBtn;
    private EditText editName, editPhoneNo, editPhoneNo2;
    private TextView role;
    private Uri mUri;
    private ProgressBar mProgressbar;
    private static final int PICK_IMAGE = 1;
    private UploadTask mUploadTask;
    private StorageReference mStorageReference;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    private Users user_member;
    private String currentUserId;
    private Spinner spinnerLocation;
    boolean isNewUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_employer);

        user_member = new Users();
        profileImage = findViewById(R.id.profilePic);
        saveBtn = findViewById(R.id.userProfileSaveBtn);
        editName = findViewById(R.id.userNameField);
        editPhoneNo = findViewById(R.id.userPhoneNoField);
        editPhoneNo2 = findViewById(R.id.userPhoneNoField2);
        mProgressbar = findViewById(R.id.userProfileProgressBar);
        usrImage = findViewById(R.id.profilePic);

        role = findViewById(R.id.role);

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

        spinnerLocation = findViewById(R.id.locationSpinnerID);
        ArrayAdapter<CharSequence> locationAdapter =
                ArrayAdapter.createFromResource(this, R.array.location, R.layout.spinner_list_item);
        locationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);
        spinnerLocation.setOnItemSelectedListener(this);

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
            editPhoneNo.setText(user_member.getPhoneNo());
            editPhoneNo2.setText(user_member.getPhoneNo2());

            role.setText(user_member.getRole());

            String location = user_member.getLocation();
            ArrayAdapter locationSpinnerAdapter = (ArrayAdapter) spinnerLocation.getAdapter();
            int locationSpinnerPosition = locationSpinnerAdapter.getPosition(location);
            spinnerLocation.setSelection(locationSpinnerPosition);

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

        String mName = editName.getText().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String phoneNo = editPhoneNo.getText().toString();
        String phoneNo2 = editPhoneNo2.getText().toString();

        String role = "Employer";

        Picasso.get().load(mUri).into(usrImage);

        if ((!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(phoneNo)
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
                            profile.put("location", location);
                            profile.put("privacy", "public");
                            profile.put("Uri", downloadUri != null ? downloadUri.toString() : "");
                            profile.put("uid", currentUserId);
                            profile.put("profile images", "default");
                            profile.put("phoneNo", phoneNo);
                            profile.put("phoneNo2", phoneNo2);
                            profile.put("role", role);

                            user_member.setmName(mName);
                            user_member.setLocation(location);
                            user_member.setPhoneNo(phoneNo);
                            user_member.setPhoneNo2(phoneNo2);
                            user_member.setUri(downloadUri.toString());
                            user_member.setUserid(currentUserId);
                            user_member.setRole(role);

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
                                                    Intent intent = new Intent(EditProfileForEmployer.this,
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
                user_member.setLocation(location);
                user_member.setPhoneNo(phoneNo);
                user_member.setPhoneNo2(phoneNo2);
                user_member.setRole(role);
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
                                        Intent intent = new Intent(EditProfileForEmployer.this,
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
