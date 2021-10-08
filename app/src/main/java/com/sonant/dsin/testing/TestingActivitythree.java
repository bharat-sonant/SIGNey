package com.sonant.dsin.testing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sonant.dsin.EditablePdfActivity;
import com.sonant.dsin.R;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.Collections;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class TestingActivitythree extends AppCompatActivity {
    TextView txt;
    ArrayList<Integer> maxixmummatchedno = new ArrayList<Integer>();
    ArrayList<String> maximumvalue = new ArrayList<String>();
    String getvalueofString, kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_activitythree);
        txt = findViewById(R.id.seleteditqweqwe);

        comparestring();

    }

    private void comparestring() {

        Intent intent = getIntent();
        String selectedstring = intent.getStringExtra("p");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("comparestring").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String finalvalue = null;
                StringBuilder builder = new StringBuilder();
                int lastindex = 0;
                int index = selectedstring.indexOf(".");
                while (index >= 0) {
                    maxixmummatchedno.clear();
                    maximumvalue.clear();

                    Log.d("TAG", "onCreate: " + index);

                    String roar = selectedstring.substring(lastindex, index);
                    Log.d("TAG", "testingroar " + roar);

                    for (DataSnapshot snapshotdata : snapshot.getChildren()) {

                        String keyofstring = snapshotdata.getKey();
                        Log.d("TAG", "keyofstring " + keyofstring);
                        String valueofsentanceee = String.valueOf(snapshotdata.getValue());
                        Log.d("TAG", "valueofsentanceee " + valueofsentanceee);

                        int a = FuzzySearch.ratio(String.valueOf(roar), keyofstring);

                        maxixmummatchedno.add(a);
                        Log.d("TAG", "onDataChangemaxixmummatchedno: " + maxixmummatchedno);
                        maximumvalue.add(valueofsentanceee);
                        Log.d("TAG", "maximumvalue: " + maximumvalue);

                    }

                    int max = 0;
                    max = Collections.max(maxixmummatchedno);
                    Log.d("TAG", "onDataChange: max " + max);

                    int arraylistindex = 0;
                    arraylistindex = maxixmummatchedno.indexOf(max);
                    Log.d("TAG", "onDataChange: arraylistindex " + arraylistindex);

                    getvalueofString = maximumvalue.get(arraylistindex);
                    Log.d("TAG", "onDataChange: getvalueofString " + getvalueofString);

                    kit = String.valueOf(builder.append(getvalueofString + "@%"));
                    //   finalvalue = kit+"@%";
                    Log.d("TAG", "onDataChange: kit " + kit);

                    lastindex = index;
                    Log.d("TAG", "lastindex: " + lastindex);
                    index = selectedstring.indexOf(".", index + 1);
                }

                Intent intent = new Intent(TestingActivitythree.this, UnityPlayerActivity.class);
                intent.putExtra("selectedDataFromUnity", kit);
                startActivity(intent);


                //      Toast.makeText(TestingActivitythree.this, kit, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        try {
            onBackPressed();
            super.onResume();
        } catch (Exception e) {
        }

    }
}

