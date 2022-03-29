package com.gridviewimagepicker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gridviewimagepicker.adapter.ImageAdapter;
import com.gridviewimagepicker.R;

import java.util.ArrayList;

public class UsersUploadsFragment extends Fragment {
    GridView gridView;
    ImageAdapter imageAdapter;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;
    private String recieverUserId;
    StorageReference storageReference;

    ArrayList<String> imageList;

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

//        recieverUserId = getActivity().getIntent().getExtras().get("id").toString();

        String receiverUserId = databaseReference.child("Users").push().getKey();

        Log.i("uid", receiverUserId);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads").child(receiverUserId);

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = (String) postSnapshot.getValue();
                    imageList.add(imageUrl);
                }

                imageAdapter = new ImageAdapter(getContext(), imageList);

                gridView.setAdapter(imageAdapter);

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

}
