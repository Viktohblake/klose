package com.gridviewimagepicker.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gridviewimagepicker.R;
import com.gridviewimagepicker.fragments.UploadsFragment;
import com.gridviewimagepicker.fragments.UsersUploadsFragment;

public class VideoPage extends Fragment {

    public VideoPage() {
        // required empty public constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UsersUploadsFragment usersUploadsFragment = new UsersUploadsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.video_fragment, usersUploadsFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }
}
