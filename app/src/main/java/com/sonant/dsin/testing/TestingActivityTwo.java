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
import com.sonant.dsin.R;

import java.util.ArrayList;
import java.util.Collections;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class TestingActivityTwo extends AppCompatActivity {
    TextView textviewqwe;
    StringBuilder text = new StringBuilder();
    ArrayList<Integer> maximumno = new ArrayList<Integer>();
    ArrayList<String> maximumvalue = new ArrayList<String>();
    String getvalueofString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_two);
        textviewqwe = findViewById(R.id.selectedtext123);

        //Todo in this method string is getting trim and in lower case also get seperate after dot
        splitedString();
        //Todo in this method string is getting trim and in lower case also get seperate after dot
        compareString();


    }

    private void splitedString() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("p");
        Toast.makeText(TestingActivityTwo.this, "" + str, Toast.LENGTH_LONG).show();
        String ok = str.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\\s", "");
        String lowecasestring = ok.toLowerCase();
        Log.d("TAG", "onCreate:2 " + lowecasestring);
        String[] arrOfStr = lowecasestring.split("\\.");
        for (String message : arrOfStr) {
            Log.d("TAG", "splitedString: sdfsdvf" + message);
            text.append(message + "\n");
            Log.d("TAG", "splitedString: sdfvadfvadfv" + text);
        }
    }

    private void compareString() {

        DatabaseReference referance = FirebaseDatabase.getInstance().getReference();
        referance.child("comparestring").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder buffer = new StringBuilder();
                //  Log.d("TAG", "snapshotvalue213 "+snapshot);
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String keysofsentance = snapshot1.getKey();
                    String valueofsentance = String.valueOf(snapshot1.getValue());
                    //   int intvaluewofsentance = Integer.parseInt(valueofsentance
                    // Log.d("TAG", "onDataChange:789 " + keysofsentance+"-----"+valueofsentance);
                    int a = FuzzySearch.ratio(String.valueOf(text), keysofsentance);
                    maximumno.add(a);
                    maximumvalue.add(valueofsentance);
                    //todo add a in array list and find max
//                    buffer.append(snapshot1.getKey());
//                    Log.d("TAG", "onDataChange:456 "+buffer);
                }

                int max = Collections.max(maximumno);

                int arraylistindex = maximumno.indexOf(max);

                getvalueofString = maximumvalue.get(arraylistindex);
                //  Toast.makeText(TestingActivityTwo.this, "" + getvalueofString, Toast.LENGTH_SHORT).show();

                Log.d("TAG", "onDataChange:753 " + getvalueofString);
                textviewqwe.setText(getvalueofString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
