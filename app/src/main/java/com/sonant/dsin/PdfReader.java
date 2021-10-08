package com.sonant.dsin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;



public class PdfReader extends AppCompatActivity {
    PDFView pdfView;
    ProgressBar progressBar;
    String bookname, pgno;
    WebView webview;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

//        progressBar = findViewById(R.id.progressBar);

//        pdfView = findViewById(R.id.pdfView);
        bookname = getIntent().getStringExtra("book");
        pgno = getIntent().getStringExtra("pgno");
//        progressBar.setVisibility(View.VISIBLE);
                init();
                listener();
            }

            private void init() {
                webview = (WebView) findViewById(R.id.web_view);
                webview.getSettings().setJavaScriptEnabled(true);
                pDialog = new ProgressDialog(this);
                pDialog.setTitle("PDF");
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                webview.loadUrl("https://drive.google.com/file/d/1jp0eybkRT2lH-6DVOkC8hnpPzYOHUeZl/view?usp=sharing");

            }

            private void listener() {
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        pDialog.show();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        pDialog.dismiss();
                    }
                });
            }
//
    }

