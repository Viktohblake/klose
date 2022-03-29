package com.gridviewimagepicker.pager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.activities.MainActivity;

public class PagerHome extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        // setting up the adapter
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.viewpager);

        // add the fragments
        pagerAdapter.add(new ImagePage(), "Uploads");
//        pagerAdapter.add(new VideoPage(), "Video");

        // Set the adapter
        viewPager.setAdapter(pagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

}
