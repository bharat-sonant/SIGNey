package com.sonant.dsin;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    private int images[] = {R.raw.video8, R.raw.screen2, R.raw.screen3};
    String desc[] = {"Read the book or learn using sign language", "Complete challenges and earn rewards to unlock new levels!", "Keep a track of your performance "};

    private String[] title = {
            "TextBook to Sign Language ", "Challenges and Rewards", "Track your performance"};

    LayoutInflater layoutInflater;


    public MyCustomPagerAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.custom_viewpager_item, container, false);

        VideoView videoView = (VideoView) itemView.findViewById(R.id.imv);
        RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rell);

        if (position > 0) {
            relativeLayout.setVisibility(View.INVISIBLE);

        }
        String video_url = "android.resource://" + context.getPackageName() + "/" + images[position];
        Uri videoUri = Uri.parse(video_url);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
        container.addView(itemView);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}