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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.CartProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.ProductsViewModel;

import java.util.List;

import okhttp3.ResponseBody;

public class CartFragment extends Fragment {

    private static final String TAG = CartFragment.class.getName();

    private Context context;
    private RecyclerView recyclerView;
    private CartProductsAdapter cartProductsAdapter;
    private GridLayoutManager gridLayoutManager;
    private Button checkoutButton;

    private CartViewModel cartViewModel;
    private ProductsViewModel productsViewModel;

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

        cartProductsAdapter.setCartList(cartViewModel.getList());

        setUpToolbar(view);

        setUpRecyclerView();

        setUpObservers();

        setUpCheckoutButton();

//        updatePrice();

        return view;
    }

    private void setUpCheckoutButton() {
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                cartViewModel.remove(cartProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new CartRepository.IResponseAddCartCallBack() {
                            @Override
                            public void onResponseSuccessful(ResponseBody response) {
                                Toast.makeText(context, "Response Successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(ResponseBody responseBody) {
                                Toast.makeText(context, "Response Not Successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onAddCountClick(int position, View v) {
                List<Product> list = cartViewModel.getList();
                Product p = list.get(position);
                if (p.getProductCount() < p.getStock()) {
                    p.incrementProductCount();

//                list.remove(position);
                    list.set(position, p);
                    cartViewModel.setList(list);
                    cartViewModel.incrementProductCount(
                            ((MainActivity) getActivity()).getUser().getId(),
                            p.getId(),
                            p.getProductCount(),
                            new CartRepository.IResponseAddCartCallBack() {
                                @Override
                                public void onResponseSuccessful(ResponseBody response) {
                                    Toast.makeText(context, "Response Successful", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponseUnsuccessful(ResponseBody responseBody) {
                                    Toast.makeText(context, "Response Not Successful", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCallFailed(String message) {
                                    Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
//                cartHandler.incrementProductCount(position);
//                cartRecyclerViewAdapter.notifyItemChanged(position);
//                updatePrice();
            }

            @Override
            public void onMinusCountClick(int position, View v) {

                List<Product> list = cartViewModel.getList();
                Product p = list.get(position);
                if (p.getProductCount() > 1) {
                    p.decrementProductCount();
//                list.remove(position);
                    list.set(position, p);
                    cartViewModel.setList(list);

                    cartViewModel.decrementProductCount(
                            ((MainActivity) getActivity()).getUser().getId(),
                            p.getId(),
                            p.getProductCount(),
                            new CartRepository.IResponseAddCartCallBack() {
                                @Override
                                public void onResponseSuccessful(ResponseBody response) {
                                    Toast.makeText(context, "Response Successful", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponseUnsuccessful(ResponseBody responseBody) {
                                    Toast.makeText(context, "Response Not Successful", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCallFailed(String message) {
                                    Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }

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
        checkoutButton = view.findViewById(R.id.btn_checkout);

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        productsViewModel = ViewModelProviders.of(getActivity()).get(ProductsViewModel.class);
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
