package com.sonant.dsin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AvailableBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_books);
    }

    public void gondolier(View view) {
        Intent intent = new Intent(AvailableBooksActivity.this, SubjectActivity.class);
        startActivity(intent);
    }
}