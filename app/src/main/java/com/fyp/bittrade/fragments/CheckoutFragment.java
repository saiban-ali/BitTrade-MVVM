package com.fyp.bittrade.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.CartProductsAdapter;
import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.CheckoutResponse;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.VerifyPermissions;
import com.fyp.bittrade.viewmodels.CartViewModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {


    private User user;

    private TextView address;
    private TextView cityZip;
    private TextView country;
    private TextView phone;
    private Button editAddress;
    private ProgressBar progressBar;

    private IFragmentCallBack fragmentCallBack;
    private Button proceedPaymentButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentCallBack");
        }
    }

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        setUpToolbar(view);

        init(view);
        progressBar.setVisibility(View.GONE);

        setUpCartList(view);

        setUpDeliveryDetails();

        setUpClickListeners();

        ((MainActivity) Objects.requireNonNull(getActivity())).hideBottomNavigation();

        return view;
    }

    private void setUpClickListeners() {
        proceedPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                proceedPaymentButton.setVisibility(View.GONE);
                getPaymentUrl();
            }
        });
    }

    private void getPaymentUrl() {
        VerifyPermissions.verifyInternetPermission(getActivity());

        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        api.checkout(user.getId())
                .enqueue(new Callback<CheckoutResponse>() {
                    @Override
                    public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Response Successful", Toast.LENGTH_SHORT).show();
                            proceedPaymentButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            fragmentCallBack.loadPaymentFragment(response.body().getUrl());
                        } else {
                            Toast.makeText(getActivity(), "Response Not Successful", Toast.LENGTH_SHORT).show();
                            proceedPaymentButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                        proceedPaymentButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setUpDeliveryDetails() {
        if (user.getContact() == null) {
            address.setText("");
            cityZip.setText("");
            country.setText("");

            phone.setText("");

            AlertDialog.Builder alertDialgBuilder = new AlertDialog.Builder(getActivity());
            alertDialgBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).loadProfileFragment();
                }
            })
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).loadCartFragment();
                }
            });
            alertDialgBuilder.setTitle("Add Address");
            alertDialgBuilder.setMessage("Please add contact details before checkout");
            AlertDialog dialog = alertDialgBuilder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return;
        }

        address.setText(user.getContact().getAddress());
        cityZip.setText(String.format("%s, %s", user.getContact().getCity(), user.getContact().getZip()));
        country.setText(user.getContact().getCountry());

        phone.setText(user.getPhoneNumber());
    }

    private void init(View view) {
        user = ((MainActivity) getActivity()).getUser();

        address = view.findViewById(R.id.address);
        cityZip = view.findViewById(R.id.city_zip);
        country = view.findViewById(R.id.country);
        phone = view.findViewById(R.id.phone_number);
        editAddress = view.findViewById(R.id.btn_edit_address);
        proceedPaymentButton = view.findViewById(R.id.btn_proceed_to_payment);
        progressBar = view.findViewById(R.id.progress);
    }

    private void setUpCartList(View view) {
        CartViewModel cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_checkout);
        CartProductsAdapter cartProductsAdapter = new CartProductsAdapter(getActivity(), true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(cartProductsAdapter);
        cartProductsAdapter.setCartList(cartViewModel.getList());
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Checkout");
        toolbar.setTitleTextColor(Color.RED);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
