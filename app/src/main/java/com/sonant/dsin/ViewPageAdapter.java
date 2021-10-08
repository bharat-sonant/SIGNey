package com.sonant.dsin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.sonant.dsin.Utils.GifImageView;


public class ViewPageAdapter extends PagerAdapter {
    int count = 0;
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] titlle = {"TextBook to Sign Language", "Challenges and Rewards", "Track your performance"};
    private String[] desciption = {"Read the book or learn using sign language", "Complete challenges and earn rewards to unlock new levels!", "Keep a track of your performance "};
    private Integer[] images = {R.drawable.avtaar, R.drawable.screenonce, R.drawable.screentwice};

    public ViewPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = view.findViewById(R.id.imv);
//        GifImageView gifImageView = view.findViewById(R.id.GifView);
        TextView title = view.findViewById(R.id.pow);
        TextView desc = view.findViewById(R.id.info);
        RelativeLayout rell = view.findViewById(R.id.rell);
        Button button = view.findViewById(R.id.button);

        if (position > 0) {
            if (position == images.length-1) {
                button.setVisibility(View.VISIBLE);
            }
            rell.setVisibility(View.INVISIBLE);
            title.setText(titlle[position]);
            desc.setText(desciption[position]);
//            gifImageView.setVisibility(View.VISIBLE);
//            gifImageView.setGifImageResource(images[position]);
//            imageView.setVisibility(View.GONE);
            Glide.with(context).load(images[position]).into(imageView);
        }
        Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);

        count++;
        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}