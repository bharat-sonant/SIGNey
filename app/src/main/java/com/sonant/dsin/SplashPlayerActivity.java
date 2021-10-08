package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sonant.dsin.Utils.GifImageView;

public class SplashPlayerActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashplayer);
        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.appdeflogo);

        textView = findViewById(R.id.appname);


        SharedPreferences sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("key", 0);
        final int k = sharedpreferences.getInt("initial", 0);

        //  Log.d("jkivalue", "hi" + k + j);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (j > 0) {
                  SharedPreferences sh = getSharedPreferences("getTheUserName", MODE_PRIVATE);
                   int getTheUserName = sh.getInt("avaiablebooks", 0);
                    if (getTheUserName == 1) {
                        Intent activity = new Intent(getApplicationContext(), AvailableBooksActivity.class);
                        startActivity(activity);
                    } else {
                    Intent activity = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(activity);
                }
//                    Intent activity = new Intent(getApplicationContext(), DashboardActivity.class);
//                    startActivity(activity);
                } else {
                    if (k > 0) {
                        Intent intent = new Intent(SplashPlayerActivity.this, SplashScreen.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashPlayerActivity.this, DemoSlider.class);
                        startActivity(intent);
                    }
                }
            }
        }, 3000);


    }
}