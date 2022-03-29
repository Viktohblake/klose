package com.gridviewimagepicker.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.activities.UserProfileActivity;
import com.gridviewimagepicker.fragments.UsersUploadsFragment;
import com.gridviewimagepicker.model.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends FirebaseRecyclerAdapter<Users, UserAdapter.usersViewholder> {

    FirebaseAuth firebaseAuth;

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
    protected void onBindViewHolder(@NonNull usersViewholder holder, @SuppressLint("RecyclerView") int position, @NonNull Users model) {

        String imgString = model.getUri();
        Picasso.get().load(imgString).into(holder.userProfile);

        holder.userName.setText(model.getmName());
        holder.userLocation.setText(model.getLocation());
        holder.userProfession.setText(model.getProfession());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        if (firebaseUser != null) {
//            holder.itemView.setVisibility(View.GONE);
//        } else {
//            holder.itemView.setVisibility(View.VISIBLE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = getRef(position).getKey().toString();

                Intent profileIntent = new Intent(view.getContext(), UserProfileActivity.class);
                profileIntent.putExtra("user_id", user_id);
                view.getContext().startActivity(profileIntent);

                String id = getRef(position).getKey().toString();

                Intent viewImage = new Intent(view.getContext(), UsersUploadsFragment.class);
                viewImage.putExtra("id", id);
                view.getContext().startService(viewImage);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String user_id = getRef(position).getKey().toString();
//
//                Intent profileIntent = new Intent(view.getContext(), UsersUploadsFragment.class);
//                profileIntent.putExtra("user_id", user_id);
//                view.getContext().startActivity(profileIntent);
//            }
//        });
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
