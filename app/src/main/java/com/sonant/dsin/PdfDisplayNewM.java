package com.sonant.dsin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfDisplayNewM extends AppCompatActivity {
    PDFView pdfbook;
    String bookurlpdff = "";
    String pagenourldf = "";
    ProgressBar progressBarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_display_new_m);
        pdfbook = findViewById(R.id.id_pdf_view22);
        progressBarr = findViewById(R.id.progressbars);
        progressBarr.setVisibility(View.VISIBLE);

        bookurlpdff = getIntent().getStringExtra("bookurlpdffff");
        pagenourldf = getIntent().getStringExtra("bookpagepdffff");

        Log.d("TAG", "onCreate: sdgargsrg" + pagenourldf + bookurlpdff);
        new RetrivePDFfromUrl().execute(bookurlpdff);


    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();

                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.

            try {
                pdfbook.fromStream(inputStream)
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .defaultPage(Integer.parseInt(pagenourldf))
                        .enableDoubletap(true)
                        .fitEachPage(true)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .pageSnap(true)
                        .nightMode(true)
                        .load();
                progressBarr.setVisibility(View.GONE);
            } catch (Exception e) {
                Toast.makeText(PdfDisplayNewM.this, "Can't find the page", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }


        }
    }

    @Override
    public void onBackPressed() {
        bookurlpdff = null;
        super.onBackPressed();
    }
}