package com.gridviewimagepicker.pager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gridviewimagepicker.fragments.UploadsFragment;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.fragments.UsersUploadsFragment;

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

        UsersUploadsFragment usersUploadsFragment = new UsersUploadsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.image_fragment, usersUploadsFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        return view;
    }
}
