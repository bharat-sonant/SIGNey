package com.sonant.dsin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class NationalAnthem extends AppCompatActivity {
    PDFView pdfnationalanthem;
    ProgressBar progressBarra;
    ImageButton imganthem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_anthem);
        progressBarra = findViewById(R.id.progressanthem);
        progressBarra.setVisibility(View.VISIBLE);

        pdfnationalanthem = findViewById(R.id.pdfnationalanthem);
        imganthem = findViewById(R.id.imageanthem);
        pdfnationalanthem.fromAsset("diversitycrop.pdf")
                .enableSwipe(true)
                .pages(0)
                .swipeHorizontal(false)
                .defaultPage(0)
                .enableDoubletap(true)
                .fitEachPage(true)
                .pageFitPolicy(FitPolicy.WIDTH)
                .pageSnap(true)
                .load();
        progressBarra.setVisibility(View.GONE);


    }

    public void gotovideoview(View view) {
        imganthem.setImageDrawable(getResources().getDrawable(R.drawable.croppedhandselected));
        Intent intent = new Intent(NationalAnthem.this, UnityHandlerActivity.class);
        startActivity(intent);
    }
}