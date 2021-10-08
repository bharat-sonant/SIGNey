package com.sonant.dsin.testing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivityIntentBuilder;
import com.sonant.dsin.EditablePdfActivity;
import com.sonant.dsin.R;

import java.io.File;

public class TestingActivity extends AppCompatActivity {

    VideoView viewdoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
     //   viewdoe = findViewById(R.id.videoviewid);

    }

    public void onClick(View view) {
       // try {
        //    File root = new File(String.valueOf("file://" + Environment.getExternalStorageDirectory()), "aye");
       //     if (!root.exists()) {
       //         root.mkdirs();
        //    }
        //    File wardFile = new File(root, "sample.pdf");
       //     Uri videoUri = Uri.parse(String.valueOf(wardFile));
            //  Log.d("TAG", "onClick:videoUri " + videoUri + " " + wardFile.getName());

            //   viewdoe.setVideoURI(videoUri);
            //  viewdoe.start();
            //   Log.d("TAG", "onClick:videoUri " + videoUri);
            // Log.d("Video Player", filePath);
               final Uri uri = Uri.parse("file:///android_asset/chapterfive.pdf");
            //final Intent intent = PdfActivityIntentBuilder.fromUri(this, videoUri)
            final Intent intent = PdfActivityIntentBuilder.fromUri(this, uri)
                    .configuration(new PdfActivityConfiguration.Builder(this).textSelectionPopupToolbarEnabled(false).build())
                    .activityClass(EditablePdfActivity.class)
                    .build();
            this.startActivity(intent);

//        } catch (Exception e)
//        {
//            Toast.makeText(this, "failed2" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }
}
