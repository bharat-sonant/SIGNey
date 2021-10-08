package com.sonant.dsin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.player.UnityPlayerActivity;

public class UnityHandlerActivity extends AppCompatActivity {

    EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_handler);

        edittext = findViewById(R.id.unityeditbtn);
    }

    public void unity(View view) {
        if (edittext == null) {

            edittext.setError("Fill the blanks");
        }
        Intent intent = new Intent(UnityHandlerActivity.this, UnityPlayerActivity.class);
        startActivity(intent);
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
        myEdit.putString("name", edittext.getText().toString());

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.commit();


    }
}