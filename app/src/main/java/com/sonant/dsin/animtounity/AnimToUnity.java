package com.sonant.dsin.animtounity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.sonant.dsin.CommonProgressBar;
import com.sonant.dsin.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimToUnity extends AppCompatActivity {
    CommonProgressBar commonProgressBar = new CommonProgressBar();
    ArrayList<String> myList = new ArrayList<String>();
    ArrayList<String> copiedList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_to_unity);

        intialMethod();
    }

    private void intialMethod() {
        commonProgressBar.setProgressDialog(this, this);

        Log.d("TAG", "intialMethod:1 ");


        File root = new File(Environment.getExternalStorageDirectory(), "upload encrypted data to storage");
        if (!root.exists()) {
            Toast.makeText(AnimToUnity.this, "file not Exist", Toast.LENGTH_SHORT).show();
        }
        Log.d("TAG", "intialMethod:2 ");
        Log.d("TAG", "onCreate:path " + root);

        File list[] = root.listFiles();
        for (int i = 0; i < list.length; i++) {
            Log.d("TAG", "intialMethod:3 ");
            myList.add(list[i].getName());

            String folderItem = String.valueOf(myList);

            int lenghtOfFolderItemString = folderItem.length();
            String trimedString = folderItem.substring(1, lenghtOfFolderItemString - 5);
            HashMap<String, Object> userDetails = new HashMap<>();

            userDetails.put(trimedString, "gs://dsiapp-103c4.appspot.com/DataEncryptionSonant");

            FirebaseDatabase.getInstance().getReference().child("zstorageRefrance").updateChildren(userDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(AnimToUnity.this, "success", Toast.LENGTH_SHORT).show();
                            commonProgressBar.closeDialog(AnimToUnity.this);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AnimToUnity.this, "failed", Toast.LENGTH_SHORT).show();

                        }
                    });
            myList.clear();

        }
    }

    public void buttonclick(View view) {
        Intent intent = new Intent(AnimToUnity.this,GetDataInToRecyclerViewFromFirebase.class);
        startActivity(intent);
    }
}