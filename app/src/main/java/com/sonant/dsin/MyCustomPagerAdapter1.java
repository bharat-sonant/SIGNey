package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class MyCustomPagerAdapter1 extends PagerAdapter {
    Context context;
    String desc[] = {"Read the book or learn using sign language", "Complete challenges and earn rewards to unlock new levels!", "Keep a track of your performance on daily basis"};
    private int images[] = {R.drawable.avtaar, R.drawable.screenonce, R.drawable.screentwice};

    private String[] title = {
            "TextBook to Sign Language ", "Challenges and Rewards", "Track your performance"};

    LayoutInflater layoutInflater;


    public MyCustomPagerAdapter1(Context context) {
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return desc.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.custom_viewpager_item1, container, false);

        TextView textTitle = itemView.findViewById(R.id.pow);
        TextView textDesc = itemView.findViewById(R.id.info);
        ImageButton button = itemView.findViewById(R.id.button);

        if (position == desc.length - 1) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("initial", 1);
                    editor.apply();
                    Intent intent = new Intent(context, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
        textTitle.setText(title[position]);
        textDesc.setText(desc[position]);
        container.addView(itemView);

        //listening to image click
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}