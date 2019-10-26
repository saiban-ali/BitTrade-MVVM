package com.fyp.bittrade.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {


    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Payment");
//        toolbar.setTitleTextColor(Color.RED);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Bundle bundle = getArguments();

        String url = null;

        if (bundle != null) {
            url = bundle.getString("url");
        }

        WebView webView = view.findViewById(R.id.webview_payment);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (url != null) {
            webView.loadUrl(url);
        }

        ((MainActivity) Objects.requireNonNull(getActivity())).hideBottomNavigation();

        return view;
    }

}
