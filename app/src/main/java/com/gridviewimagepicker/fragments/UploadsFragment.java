package com.gridviewimagepicker.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.gridviewimagepicker.activities.UserProfileActivity;
import com.gridviewimagepicker.adapter.ImageAdapter;
import com.gridviewimagepicker.activities.MainActivity;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.pager.PagerHome;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class UploadsFragment extends Fragment {
    GridView gridView;
    ImageAdapter imageAdapter;

    private static final int PICK_FILE = 25;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;
    StorageReference storageReference;

    ArrayList<String> imageList;

    ImageView imageView;
    Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.gridview_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        imageAdapter = new ImageAdapter();

        imageList = new ArrayList<String>();

        gridView = (GridView) view.findViewById(R.id.gridview);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads").child(currentUserId);

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = (String) postSnapshot.getValue();
                    imageList.add(imageUrl);
                }

                imageAdapter = new ImageAdapter(getContext(), imageList);

                gridView.setAdapter(imageAdapter);

                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int position, long id) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.delete:
                                        deleteImage(position);
                                        imageAdapter.notifyDataSetChanged();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popupMenu.inflate(R.menu.popup_menu);
                        popupMenu.show();
                        Log.e("onItemLongClick", "onItemLongClick:" + position);
                        return true;
                    }
                });

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("onItemClick", "onItemClick:" + i);
                        viewImage(i);
                    }
                });

                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void viewImage(int position) {
        String selectedImage = imageAdapter.imageList.get(position);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedImage));
        getActivity().startActivity(intent);
    }

    public void deleteImage(int position) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String selectedItem = imageAdapter.imageList.remove(position);

        Log.i("GRIDvACT", String.format("UrlToDelete: %s", selectedItem));

        firebaseStorage.getReferenceFromUrl(selectedItem).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("GRIDvACT", "got here successfully");
                Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();

                if (!task.isSuccessful()) {
                    Log.i("GRIDvACT", "Deleted hopefully");
                    imageAdapter.imageList.add(position, selectedItem);
                    imageAdapter.notifyDataSetChanged();
                } else {
                    databaseReference.equalTo(selectedItem);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }

                imageAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_uploads_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("*/*");
                String[] mimeTypes = {"image/*"};
//                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, PICK_FILE);

                return true;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String imageUrl = task.getResult().toString();
                                    Log.i("IMAGE URL", String.format("%s", imageUrl));
                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(imageUrl);
                                }
                            });

                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            UploadsFragment uploadsFragment = new UploadsFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, uploadsFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                            imageAdapter.notifyDataSetChanged();

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

//    @Override
//    public void onResume(){
//        super.onResume();
//        ((MainActivity) getActivity()).setActionBarTitle("Gallery");
//    }

}
