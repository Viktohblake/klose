package com.gridviewimagepicker.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gridviewimagepicker.UploadsFragment;
import com.gridviewimagepicker.R;

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

        UploadsFragment uploadsFragment = new UploadsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.image_fragment, uploadsFragment).commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        return view;
    }
}
