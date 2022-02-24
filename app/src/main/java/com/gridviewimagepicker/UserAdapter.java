package com.gridviewimagepicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View

public class UserAdapter extends FirebaseRecyclerAdapter<Users, UserAdapter.usersViewholder> {
//
    public UserAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    @NonNull
    public usersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist_layout, parent, false);
        return new UserAdapter.usersViewholder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull usersViewholder holder, int position, @NonNull Users model) {

        String imgString = model.getUri();
        Picasso.get().load(imgString).into(holder.userProfile);

        holder.userName.setText(model.getmName());
        holder.userLocation.setText(model.getLocation());
        holder.userProfession.setText(model.getProfession());
    }

    public class usersViewholder extends RecyclerView.ViewHolder {
        TextView userName, userLocation, userProfession;
        ImageView userProfile;

        public usersViewholder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userLocation = itemView.findViewById(R.id.user_location);
            userProfession = itemView.findViewById(R.id.user_profession);
        }

    }

}
