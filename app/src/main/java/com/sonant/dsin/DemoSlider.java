package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class DemoSlider extends AppCompatActivity {

    int currentPage = 0;
    VideoView videoView;
    ViewPager viewPager1;
    ImageButton left, right;
    TextView tv;
    RelativeLayout rela;
    TabLayout tabLayout;
    private int images[] = {R.raw.video8, R.raw.screen2, R.raw.screen3};
    int position = 0;

    MyCustomPagerAdapter myCustomPagerAdapter;
    MyCustomPagerAdapter1 myCustomPagerAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_slider);
        tv = findViewById(R.id.skip);
        videoView = (VideoView) findViewById(R.id.videoView);
        viewPager1 = (ViewPager) findViewById(R.id.viewPager1);
        Context context = getApplicationContext();
        left = findViewById(R.id.button_left);
        right = findViewById(R.id.button_right);

        myCustomPagerAdapter = new MyCustomPagerAdapter(context);
        rela = findViewById(R.id.rell);
        myCustomPagerAdapter1 = new MyCustomPagerAdapter1(context);
        setvideo(position);
        viewPager1.setAdapter(myCustomPagerAdapter1);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager1, true);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });


    }

    private void setvideo(int position) {
        viewPager1.setCurrentItem(position, true);
        if (position == images.length - 1) {
            tv.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.VISIBLE);
            rela.setVisibility(View.VISIBLE);
        } else if (position >= images.length - 1) {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.INVISIBLE);
        } else {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            rela.setVisibility(View.INVISIBLE);
        }
        if (position == images.length) {
            skip();
        } else {
            String video_url = "android.resource://" + getPackageName() + "/" + images[position];
            Uri videoUri = Uri.parse(video_url);
            videoView.setVideoURI(videoUri);
          //  videoView.requestFocus();
          //  videoView.stopPlayback();
            if (position == 0) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            } else if (position == 1) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            } else if (position == 2) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            }

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void skip() {
        videoView.stopPlayback();
        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("initial", 1);
        editor.apply();
        videoView.stopPlayback();
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }

    public void left(View view) {
        currentPage = viewPager1.getCurrentItem();

        if (currentPage > 0) {
            currentPage = currentPage - 1;
        }
        videoView.stopPlayback();
        Log.d("clicked", "left" + currentPage);

        setvideo(currentPage);
    }

    public void right(View view) {

        currentPage = viewPager1.getCurrentItem();
        if (currentPage < 2) {
            currentPage = currentPage + 1;
        }
        videoView.stopPlayback();
        Log.d("clicked", "Right" + currentPage + position);
        setvideo(currentPage);

    }
}