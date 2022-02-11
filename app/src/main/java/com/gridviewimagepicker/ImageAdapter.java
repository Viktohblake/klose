package com.gridviewimagepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> imageList;
    LayoutInflater layoutInflater;

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

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // arg2 = the id of the item in our view (List/Grid) that we clicked
        // arg3 = the id of the item that we have clicked
        // if we didn't assign any id for the Object (Book) the arg3 value is 0
        // That means if we comment, aBookDetail.setBookIsbn(i); arg3 value become 0
        Toast.makeText(context.getApplicationContext(), "You clicked on position : " + arg2 + " and id : " + arg3, Toast.LENGTH_LONG).show();
    }
}