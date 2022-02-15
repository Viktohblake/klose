package com.gridviewimagepicker.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.gridviewimagepicker.GridviewActivity;
import com.gridviewimagepicker.ImageAdapter;
import com.gridviewimagepicker.R;

import java.util.ArrayList;

public class ImagePage extends Fragment {

//    GridView gridView;
//    ImageAdapter imageAdapter;
//
//    private DatabaseReference databaseReference;
//    private FirebaseStorage firebaseStorage;
//    private ValueEventListener valueEventListener;
//    private StorageReference storageReference;
//
//    ArrayList<String> imageList;

    public ImagePage() {
        // required empty public constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
//        gridView = view.findViewById(R.id.gridview);
//
//        imageList = new ArrayList<String>();
//
//        firebaseStorage = FirebaseStorage.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
//
//        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    String imageUrl = (String) postSnapshot.getValue();
//                    imageList.add(imageUrl);
//                }
//
//                imageAdapter = new ImageAdapter(getContext(), imageList);
//                gridView.setAdapter(imageAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
        return view;

    }
}
