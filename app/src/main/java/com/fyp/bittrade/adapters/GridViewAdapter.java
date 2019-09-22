package com.fyp.bittrade.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private List<Bitmap> imagesList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    public void setImagesList(List<Bitmap> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_row, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image_view);
//        imageView.setImageBitmap(imagesList.get(position));

        Glide.with(context)
                .load(imagesList.get(position))
                .into(imageView);

        return convertView;
    }
}
