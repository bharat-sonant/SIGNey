package com.sonant.dsin.animtounity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scottyab.aescrypt.AESCrypt;
import com.sonant.dsin.R;
import com.sonant.dsin.User;
import com.sonant.dsin.dataEncrytion.DataEncryption;
import com.unity3d.player.UnityPlayerActivity;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    GetDataInToRecyclerViewFromFirebase getDataInToRecyclerViewFromFirebase;

    ArrayList<UserListModel> list;

    public UserAdapter(Context context, ArrayList<UserListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserListModel userListModel = list.get(position);
        holder.textView.setText(userListModel.getItemdetail());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clickedString = userListModel.getItemdetail();
                Log.d("TAG", "onClick:clickedString " + clickedString);

                try {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://dsiapp-103c4.appspot.com/DataEncryptionSonant")
                            .child(clickedString + ".txt");

                    //  File localFile = File.createTempFile("input", ".txt");
                    File file = new File(Environment.getExternalStorageDirectory(), "Sonant Secure files");

                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    final File localFile = new File(file, clickedString + ".txt");

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
                            try {
                                String messageAfterDecrypt = AESCrypt.decrypt("password", jsonFileFromBuffterReader);
                                Log.d("TAG", "onSuccess:+messageAfterDecrypt " + messageAfterDecrypt);

                                Intent intent = new Intent(context, UnityPlayerActivity.class);
                                //   intent.putExtra("selectedDataFromUnity", messageAfterDecrypt);
                                context.startActivity(intent);

                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            localFile.deleteOnExit();
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
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn);
            textView = itemView.findViewById(R.id.textViewOfUserList);
        }
    }
}
