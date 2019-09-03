package com.fyp.bittrade.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;

public class ProductDetailFragment extends Fragment {

    private static final String TAG = ProductDetailFragment.class.getName();

    private IFragmentCallBack fragmentCallBack;
    private Product product;
    private ImageView productImage;
    private TextView title;
//    private TextView brand;
    private TextView price;
    private TextView description;
    private ImageView addToFavorite;
    private Button addToCart;
    private Button checkout;
    private ImageView back;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        Bundle bundle = getArguments();
        product = bundle.getParcelable("product");

        init(view);

        updateViews();

        setUpClickListeners();

        return view;
    }

    private void updateViews() {
        Glide.with(getActivity())
                .load(product.getImages()[0])
                .placeholder(R.drawable.ic_logo_light)
                .centerInside()
                .into(productImage);

        title.setText(product.getTitle());
        price.setText(String.format("$%.2f", product.getPrice()));
        description.setText(product.getDescription());

    }

    private void setUpClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.onBackPressed();
            }
        });
    }

    private void init(View view) {
        back = view.findViewById(R.id.img_back);

        productImage = view.findViewById(R.id.img_product);
        title = view.findViewById(R.id.txt_product_title);
//        brand = view.findViewById(R.id.txt_brand_name);
        price = view.findViewById(R.id.txt_product_price);
        description = view.findViewById(R.id.txt_product_description);
    }
}
