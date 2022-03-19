package com.gridviewimagepicker.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.gridviewimagepicker.activities.MainActivity;
import com.gridviewimagepicker.R;
import com.gridviewimagepicker.adapter.UserAdapter;
import com.gridviewimagepicker.model.Users;

public class BrickLayer extends Fragment {

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        Query query = databaseReference.orderByChild("profession").equalTo("Brick Layer");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseReference = databaseReference.child(firebaseUser.getUid());

        FirebaseRecyclerOptions<Users> usersFirebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

        userAdapter = new UserAdapter(usersFirebaseRecyclerOptions);
        recyclerView.setAdapter(userAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Brick Layer");
    }

}
