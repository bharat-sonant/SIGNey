package com.sonant.dsin.animtounity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sonant.dsin.R;

import java.util.ArrayList;

public class GetDataInToRecyclerViewFromFirebase extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    UserAdapter userAdapter;
    ArrayList<UserListModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_in_to_recycler_view_from_firebase);

        recyclerView = findViewById(R.id.userList);
        database = FirebaseDatabase.getInstance().getReference("zstorageRefrance");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        userAdapter = new UserAdapter(this, list);
        recyclerView.setAdapter(userAdapter);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        list.add(new UserListModel(dataSnapshot.getKey()));
                    } catch (Exception e) {
                        Toast.makeText(GetDataInToRecyclerViewFromFirebase.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}