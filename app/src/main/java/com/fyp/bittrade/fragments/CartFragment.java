package com.fyp.bittrade.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.bittrade.R;
import com.fyp.bittrade.adapters.CartProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;

import java.util.List;

public class CartFragment extends Fragment {

    private static final String TAG = CartFragment.class.getName();

    private Context context;
    private RecyclerView recyclerView;
    private CartProductsAdapter cartProductsAdapter;
    private GridLayoutManager gridLayoutManager;

    private CartViewModel cartViewModel;

    private TextView priceTextView;
    private IFragmentCallBack fragmentCallBack;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fragmentCallBack = (IFragmentCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentCallBack");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        init(view);

        setUpToolbar(view);

        setUpRecyclerView();

        setUpObservers();

//        updatePrice();

        return view;
    }

//    private void updatePrice() {
//        priceTextView.setText(String.format("$%2.f", cartViewModel.getPrice()));
//    }

    private void setUpObservers() {
        cartViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                cartProductsAdapter.setCartList(products);
            }
        });

        cartViewModel.getPriceLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                priceTextView.setText(String.format("$%.2f", aDouble));
            }
        });
    }

    private void setUpRecyclerView() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(cartProductsAdapter);

        cartProductsAdapter.setOnItemClickListener(new CartProductsAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position, View v, View itemView) {
                cartViewModel.remove(cartProductsAdapter.getProduct(position));
            }

            @Override
            public void onAddCountClick(int position, View v) {
//                cartHandler.incrementProductCount(position);
//                cartRecyclerViewAdapter.notifyItemChanged(position);
//                updatePrice();
            }

            @Override
            public void onMinusCountClick(int position, View v) {
//                if (cartHandler.getProduct(position).getProductCount() > 0) {
//                    cartHandler.decrementProductCount(position);
//                    cartRecyclerViewAdapter.notifyItemChanged(position);
//                    updatePrice();
//                }
            }
        });
    }

    private void init(View view) {
        context = getActivity();

        priceTextView = view.findViewById(R.id.txt_total_price);

        recyclerView = view.findViewById(R.id.recyclerview_cart);
        cartProductsAdapter = new CartProductsAdapter(context);
        gridLayoutManager = new GridLayoutManager(context, 1);

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
    }

    public void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cart);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                fragmentCallBack.logout();
                return true;

            default:
                return false;
        }
    }
}
