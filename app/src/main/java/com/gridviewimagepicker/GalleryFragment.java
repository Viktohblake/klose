package com.gridviewimagepicker;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gridviewimagepicker.pager.PagerHome;

import java.io.IOException;
import java.util.UUID;

public class GalleryFragment extends Fragment {

    Button btnViewImage;
    Button btnChooseImage;
    Button btnUploadImage;
    Button btnGoToPager;

    ImageView imageView;

    Uri filePath;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private DatabaseReference databaseReference;

    private static final int PICK_FILE = 25;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        btnViewImage = view.findViewById(R.id.viewImage);
        btnChooseImage = view.findViewById(R.id.chooseImage);
        btnUploadImage = view.findViewById(R.id.saveImage);
        btnGoToPager = view.findViewById(R.id.goToPager);

        imageView = view.findViewById(R.id.selectedImage);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

//        btnViewImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UploadsFragment uploadsFragment = new UploadsFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.container, uploadsFragment).commit();
//            }
//        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryPick();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        btnGoToPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PagerHome.class);
                startActivity(intent);
            }
        });
    }

    public void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String imageUrl = task.getResult().toString();
                                    Log.i("IMAGE URL", String.format("%s", imageUrl));
                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(imageUrl);
                                }
                            });

                            UploadsFragment uploadsFragment = new UploadsFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, uploadsFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + progress + "%...");
                        }
                    });
        }
    }

}
