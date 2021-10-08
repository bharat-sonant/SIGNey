package com.sonant.dsin.dataEncrytion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scottyab.aescrypt.AESCrypt;
import com.sonant.dsin.CommonProgressBar;
import com.sonant.dsin.R;
import com.sonant.dsin.animtounity.AnimToUnity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;

public class DataEncryption extends AppCompatActivity {
    EditText editText123;
    String getDataForFIrebase, downloadLinkOfTxtFile;
    CommonProgressBar commonProgressBar = new CommonProgressBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_encryption);
        init();
        Log.d("TAG", "onCreate: ");
    }

    public void init() {
        editText123 = findViewById(R.id.editText123);
    }

    public void enteredDataToFirebase(View view) {
        Intent intent = new Intent(DataEncryption.this, AnimToUnity.class);
        startActivity(intent);
        //  dowloadFromFirebase();
    }

    private void dowloadFromFirebase() {
        commonProgressBar.setProgressDialog(this, this);
        getDataForFIrebase = editText123.getText().toString();
        // Log.d("TAG", "onDataChange:getDataForFIrebase " + getDataForFIrebase);
        FirebaseDatabase.getInstance().getReference().child("DataEncryptionSonant").child(getDataForFIrebase).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadLinkOfTxtFile = snapshot.getValue().toString();
                //  String namename = snapshot.child(getDataForFIrebase).getValue(String.class);
                Log.d("TAG", "DownloadLinkOfTxtFile " + downloadLinkOfTxtFile);
                downloadTxtFileFromTheStorage();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void downloadTxtFileFromTheStorage() {
        try {

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(downloadLinkOfTxtFile).child(getDataForFIrebase + ".txt");

            File localFile = File.createTempFile("input", ".txt");

            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String jsonFileFromBuffterReader = null;
                    try (BufferedReader br = new BufferedReader(new FileReader(localFile))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            jsonFileFromBuffterReader = line.toString();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    localFile.deleteOnExit();
                    try {
                        String encryptedMsg = AESCrypt.encrypt("password", jsonFileFromBuffterReader);
                        File file = new File(Environment.getExternalStorageDirectory(), getDataForFIrebase + ".txt");
                        // File file = new File(Environment.getExternalStorageDirectory(), finalMain +".json");
                        //  String data = gson.toJson(dataSnapshot.getValue());
                        try (FileOutputStream fos = new FileOutputStream(file);
                             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                            byte[] bytes = encryptedMsg.getBytes();
                            bos.write(bytes);
                            bos.close();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        commonProgressBar.closeDialog(DataEncryption.this);

                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {


                    Log.e("firebase ", "local tem file not created  created " + exception.toString());
                }
            });
        } catch (Exception e) {
        }
    }
}
