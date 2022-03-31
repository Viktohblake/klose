package com.gridviewimagepicker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gridviewimagepicker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullScreenViewActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenview_layout);

        String imagePath = getIntent().getStringExtra("fullimagepath");

        imageView = findViewById(R.id.fullImageView);
        Picasso.get().load(imagePath).into(imageView);

        button = findViewById(R.id.btnClose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
