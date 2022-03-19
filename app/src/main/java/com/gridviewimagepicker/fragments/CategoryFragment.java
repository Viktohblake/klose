package com.gridviewimagepicker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gridviewimagepicker.activities.MainActivity;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.categories.BrickLayer;
import com.gridviewimagepicker.categories.Carpentry;
import com.gridviewimagepicker.categories.ContentWriter;
import com.gridviewimagepicker.categories.Electrician;
import com.gridviewimagepicker.categories.Furniture;
import com.gridviewimagepicker.categories.Laundry;
import com.gridviewimagepicker.categories.Painter;
import com.gridviewimagepicker.categories.Plumber;
import com.gridviewimagepicker.categories.Programmer;
import com.gridviewimagepicker.categories.Teacher;
import com.gridviewimagepicker.categories.WebDesigner;

public class CategoryFragment extends Fragment {

    CardView painter, carpentry, laundry, contentwriter, electrician,
            programmer, webdesigner, plumber, bricklayer, furniture, teacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        painter = (CardView) getView().findViewById(R.id.painterCategory);
        carpentry = (CardView) getView().findViewById(R.id.carpentryCategory);
        laundry = (CardView) getView().findViewById(R.id.laundryCategory);
        contentwriter = (CardView) getView().findViewById(R.id.contentWriterCategory);
        electrician = (CardView) getView().findViewById(R.id.electricianCategory);
        programmer = (CardView) getView().findViewById(R.id.programmerCategory);
        webdesigner = (CardView) getView().findViewById(R.id.webDesignerCategory);
        plumber = (CardView) getView().findViewById(R.id.plumberCategory);
        bricklayer = (CardView) getView().findViewById(R.id.brickLayerCategory);
        furniture = (CardView) getView().findViewById(R.id.furnitureCategory);
        teacher = (CardView) getView().findViewById(R.id.teacherCategory);

        painter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Painter painter = new Painter();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, painter);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        carpentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carpentry carpentry = new Carpentry();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, carpentry);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Laundry laundry = new Laundry();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, laundry);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        contentwriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentWriter contentWriter = new ContentWriter();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, contentWriter);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Electrician electrician = new Electrician();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, electrician);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        programmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Programmer programmer = new Programmer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, programmer);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        webdesigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebDesigner webDesigner = new WebDesigner();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, webDesigner);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plumber plumber = new Plumber();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, plumber);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        bricklayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrickLayer brickLayer = new BrickLayer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();;
                transaction.replace(R.id.container, brickLayer);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Furniture furniture = new Furniture();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, furniture);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Teacher teacher = new Teacher();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, teacher);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Categories");
    }
}
