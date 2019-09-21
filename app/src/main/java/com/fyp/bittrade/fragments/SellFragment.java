package com.fyp.bittrade.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;

public class SellFragment extends Fragment {

    private static final String TAG = SellFragment.class.getName();

    private Spinner spinner;
    private EditText title;
    private EditText stock;
    private EditText price;
    private EditText description;
    private Button nextButton;

    private IFragmentCallBack fragmentCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentCallBack");
        }
    }

    public SellFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Product Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        init(view);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();

                if (validateFields()) {
                    product.setTitle(title.getText().toString());
                    product.setDescription(description.getText().toString());
                    product.setPrice(Double.parseDouble(price.getText().toString()));
                    product.setStock(Integer.parseInt(stock.getText().toString()));
                    product.setCategory(spinner.getSelectedItem().toString());

                    fragmentCallBack.loadAddProductImagesFragment(product);
                }
            }
        });

        return view;
    }

    private boolean validateFields() {
        if (title.getText().toString().isEmpty()) {
            title.setError("Input Title");
            return false;
        }

        if (stock.getText().toString().isEmpty()) {
            stock.setError("Input stock");
            return false;
        }

        if (price.getText().toString().isEmpty()) {
            price.setError("Input price");
            return false;
        }

        if (description.getText().toString().isEmpty()) {
            description.setError("Input Description");
            return false;
        }

        return true;
    }

    private void init(View view) {
        spinner = view.findViewById(R.id.category);
        title = view.findViewById(R.id.title);
        stock = view.findViewById(R.id.stock);
        price = view.findViewById(R.id.price);
        description = view.findViewById(R.id.description);
        nextButton = view.findViewById(R.id.btn_next);
    }
}
