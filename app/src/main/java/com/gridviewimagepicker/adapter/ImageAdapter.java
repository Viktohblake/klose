package com.gridviewimagepicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gridviewimagepicker.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    Context context;
    public ArrayList<String> imageList;
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

//        else {
//            view = layoutInflater2.from(context).inflate(R.layout.video_list, viewGroup, false);
//
//            String image = imageList.get(i);
//
//            VideoView videoView = (VideoView) view.findViewById(R.id.videoUploads);
//            videoView.setVideoURI(Uri.parse(image));
//            videoView.getDuration();
//            videoView.setBackgroundColor(Color.TRANSPARENT);
//            videoView.setZOrderOnTop(true);
//            videoView.start();
//            videoView.requestFocus();
//        }

        return view;
    }

}
