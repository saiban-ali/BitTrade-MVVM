package com.fyp.bittrade.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;

import java.util.ArrayList;
import java.util.List;

public class SellFragment extends Fragment {

    private static final String TAG = SellFragment.class.getName();

    private Spinner spinner;
    private EditText title;
    private EditText brand;
    private EditText stock;
    private EditText price;
    private EditText description;
    private Button nextButton;
    private TextView descriptionWordCount;

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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        init(view);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();

                if (validateFields()) {

                    if (!((MainActivity) getActivity()).getUser().isPaymentMethodAvailable()) {
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("")
//                                .setMessage("Please add payment method first")
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .show();
//                        return;
                    }

                    product.setTitle(title.getText().toString());
                    product.setBrand(brand.getText().toString());
                    product.setDescription(description.getText().toString());
                    product.setPrice(Double.parseDouble(price.getText().toString()));
                    product.setStock(Integer.parseInt(stock.getText().toString()));
                    product.setCategory(spinner.getSelectedItem().toString());

                    fragmentCallBack.loadAddProductImagesFragment(product);
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descriptionWordCount.setText(Integer.toString(description.getText().toString().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private boolean validateFields() {
        if (title.getText().toString().isEmpty()) {
            title.setError("Input Title");
            return false;
        }

        if (brand.getText().toString().isEmpty()) {
            brand.setError("Input Brand Name");
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
        brand = view.findViewById(R.id.brand);
        stock = view.findViewById(R.id.stock);
        price = view.findViewById(R.id.price);
        description = view.findViewById(R.id.description);
        nextButton = view.findViewById(R.id.btn_next);
        descriptionWordCount = view.findViewById(R.id.txt_char_count);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("title", title.getText().toString());
        outState.putString("brand", brand.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("wordcount", descriptionWordCount.getText().toString());
        outState.putString("stock", stock.getText().toString());
        outState.putString("price", price.getText().toString());
        outState.putString("category", spinner.getSelectedItem().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            title.setText(savedInstanceState.getString("title"));
            brand.setText(savedInstanceState.getString("brand"));
            description.setText(savedInstanceState.getString("description"));
            descriptionWordCount.setText(savedInstanceState.getString("wordcount"));
            stock.setText(savedInstanceState.getString("stock"));
            price.setText(savedInstanceState.getString("price"));

            String[] categories = getActivity().getResources().getStringArray(R.array.categories);

            for (int i = 0; i <categories.length; i++) {
                if (categories[i].equals(savedInstanceState.getString("category"))) {
                    spinner.setSelection(i);
                }
            }
        }
    }
}
