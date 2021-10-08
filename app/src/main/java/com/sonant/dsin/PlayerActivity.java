package com.sonant.dsin;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;

    ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);

        String videoPath = "android.resource://com.sonant.dsin/" + R.raw.video;

        Uri uri = Uri.parse(videoPath);

        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();
        videoView.layout(10,10,100,10);

    }
}