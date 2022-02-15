package com.gridviewimagepicker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    Context context;
    public ArrayList<String> imageList;
    LayoutInflater layoutInflater;
    private int selectedPosition = -1;

    public ImageAdapter() {
    }

    public ImageAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = layoutInflater.from(context).inflate(R.layout.images_list, viewGroup, false);
        }

        String image = imageList.get(i);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageUploads);

        Picasso.get().load(image).fit().centerCrop().into(imageView);

        return view;
    }

    public void setSelectedPosition(int i) {
        selectedPosition = i;
    }

}