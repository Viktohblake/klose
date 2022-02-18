package com.gridviewimagepicker;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GridviewActivity extends AppCompatActivity {

    GridView gridView;
    ImageAdapter imageAdapter;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;
    private StorageReference storageReference;

    ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_layout);

        imageList = new ArrayList<String>();

        gridView = (GridView) findViewById(R.id.gridview);
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = (String) postSnapshot.getValue();
                    imageList.add(imageUrl);
                }

                imageAdapter = new ImageAdapter(GridviewActivity.this, imageList);
                gridView.setAdapter(imageAdapter);

                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int i, long id) {
                        PopupMenu popupMenu = new PopupMenu(GridviewActivity.this, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.delete:
                                        deleteImage(i);
                                        imageAdapter.notifyDataSetChanged();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popupMenu.inflate(R.menu.popup_menu);
                        popupMenu.show();
                        Log.e("onItemLongClick", "onItemLongClick:" + i);
                        return true;
                    }
                });

                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GridviewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void deleteImage(int position) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String selectedItem = imageAdapter.imageList.remove(position);

        Log.i("GRIDvACT", String.format("UrlToDelete: %s", selectedItem));

        firebaseStorage.getReferenceFromUrl(selectedItem).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("GRIDvACT", "got here successfully");
                Toast.makeText(GridviewActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();

                if (!task.isSuccessful()) {
                    Log.i("GRIDvACT", "deleted hopefully");
                    imageAdapter.imageList.add(position, selectedItem);
                } else {
                    databaseReference.equalTo(selectedItem);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }

                imageAdapter.notifyDataSetChanged();

            }
        });
    }
}