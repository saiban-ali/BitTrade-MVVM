package com.fyp.bittrade.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private String[] imageURLs;

    public ImageSliderAdapter(Context context, String[] imageURLs) {
        this.context = context;
        this.imageURLs = imageURLs;
    }

    @Override
    public int getCount() {
        return imageURLs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        Glide.with(context)
                .load(imageURLs[position])
                .centerInside()
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }
}
