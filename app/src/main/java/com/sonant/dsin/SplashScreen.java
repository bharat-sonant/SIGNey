package com.sonant.dsin;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.folioreader.util.ProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.sonant.dsin.classes.ForceUpdateChecker;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class SplashScreen extends AppCompatActivity   {
    ProgressBar progressBar;
    GifImageView videoView;
    MediaController mediaController;
    private LocationManager locationManager;
    ProgressDialog dialog;
    AlertDialog alertDialog;
    boolean checkStatus = true;
    Spinner spinnerCityType;
    Button continueBtn;
    private FirebaseAuth mAuth;
    LinearLayout selectCity;
    private final static int LOCATION_REQUEST = 500;
    List<String> cityTypeList = new ArrayList<>();
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressbar);
        videoView = findViewById(R.id.imageView2);
        mediaController = new MediaController(SplashScreen.this);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("TAG", "onUpdateNeeded: check updateUrl ");
//        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
//    }

//
//    @Override
//    public void onUpdateNeeded(String updateUrl) {
//
//        Log.d("TAG", "onUpdateNeeded: check updateUrl "+updateUrl);
//        if (updateUrl == null) {
//            if (mAuth.getCurrentUser() != null) {
//                startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
//                finish();
//            }
//        } else {
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("New version available")
//                        .setCancelable(false)
//                        .setMessage("Please, update app to new version to continue service.")
//                        .setPositiveButton("Update",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        redirectStore(updateUrl);
//                                    }
//                                }).setNegativeButton("No, thanks",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (new ForceUpdateChecker(SplashScreen.this, SplashScreen.this).mustUpdate()) {
//                                            finish();
//                                        } else {
//                                            dialog.dismiss();
//                                            finishAffinity();
//
//                                        }
//                                    }
//                                }).create();
//                dialog.show();
//        }
//    }
//
//    private void redirectStore(String updateUrl) {
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }


    public void signin(View view) {

        Intent intent = new Intent(this, SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //    public void signup(View view) {
//        // change signup activity to Dashboard activity
//        Intent intent = new Intent(this, DashboardActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

}
