package com.fyp.bittrade.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.bittrade.R;

public class SellFragment extends Fragment {

    private static final String TAG = SellFragment.class.getName();

    public SellFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sell);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        return view;
    }
}
