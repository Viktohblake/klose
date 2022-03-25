package com.gridviewimagepicker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gridviewimagepicker.activities.MainActivity;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.adapter.UserAdapter;
import com.gridviewimagepicker.model.Users;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    DatabaseReference databaseReference;
    DatabaseReference userDatabaseReference;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_allusers, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        userDatabaseReference = databaseReference.child(firebaseUser.getUid());

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Users userObject = snapshot.getValue(Users.class);
                    String currentUserLocation = userObject.getLocation();

                    Log.i("currentUserLocation", currentUserLocation);

                    Query query = databaseReference.orderByChild("location").equalTo(currentUserLocation);

                    FirebaseRecyclerOptions<Users> usersFirebaseRecyclerOptions =
                            new FirebaseRecyclerOptions.Builder<Users>()
                                    .setQuery(query, Users.class)
                                    .build();

                    userAdapter = new UserAdapter(usersFirebaseRecyclerOptions);
                    recyclerView.setAdapter(userAdapter);

                    userAdapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        userAdapter.startListening();
//    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Users");
    }

}
