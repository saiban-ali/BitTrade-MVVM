package com.fyp.bittrade.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.ExploreProductsAdapter;
import com.fyp.bittrade.adapters.SearchAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.repositories.SearchRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchBox;
    private RecyclerView recyclerView;
    private SearchAdapter productsAdapter;
    private ExploreProductsAdapter productsAdapterExplore;

    private GridLayoutManager gridLayoutManager;

    private FavoritesViewModel favoritesViewModel;
    private CartViewModel cartViewModel;

    private ProgressBar progressBar;
    private TextView noProduct;

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

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Bundle bundle = null;
        if (getArguments() != null) {
            bundle = getArguments();
        }

        progressBar = view.findViewById(R.id.progress);
        noProduct = view.findViewById(R.id.no_products);

        searchBox = view.findViewById(R.id.searchbox);

        if (bundle != null && bundle.getString("searchId") != null) {
            searchBox.setText(bundle.getString("searchId"));
            progressBar.setVisibility(View.VISIBLE);
            performSearch(searchBox.getText().toString(), true);
        } else {
            progressBar.setVisibility(View.GONE);
            noProduct.setVisibility(View.GONE);
            searchBox.requestFocus();
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBox.getText().toString().length() > 0) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressBar.setVisibility(View.VISIBLE);
                    productsAdapter.submitList(new ArrayList<Product>());
                    performSearch(searchBox.getText().toString(), false);
                }

                return true;
            }
        });

        favoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoritesViewModel.class);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productsAdapter = new SearchAdapter(
                getActivity(),
                new ArrayList<Product>(),
                favoritesViewModel.getList(),
                cartViewModel.getList()
        );
        recyclerView.setAdapter(productsAdapter);
//        productsAdapterExplore = new ExploreProductsAdapter(
//                getActivity(),
//                favoritesViewModel.getList(),
//                cartViewModel.getList()
//        );
//        recyclerView.setAdapter(productsAdapterExplore);

        productsAdapter.setOnItemClickListener(new ExploreProductsAdapter.OnItemClickListener() {
            @Override
            public void onAddToFavoritesClick(int position, final View v, final View itemView) {
                Toast.makeText(getActivity(), "Add favorite clicked", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.VISIBLE);
                favoritesViewModel.add(
                        productsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new IResponseCallBack() {
                            @Override
                            public void onResponseSuccessful(Response response) {
                                Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(Response responseBody) {
                                Toast.makeText(getActivity(), "Not Added to favorite, Something went wrong", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(getActivity(), "Call failed: " + message, Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.GONE);
                            }
                        }
                );
            }

            @Override
            public void onRemoveFromFavoritesClick(int position, final View v, final View itemView) {
                Toast.makeText(getActivity(), "remove favorite clicked", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon).setVisibility(View.VISIBLE);
                favoritesViewModel.remove(
                        productsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new IResponseCallBack() {
                            @Override
                            public void onResponseSuccessful(Response response) {
                                Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(Response responseBody) {
                                Toast.makeText(getActivity(), "Not Removed from favorites,  Something went wrong", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(getActivity(), "Call failed: " + message, Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon).setVisibility(View.GONE);
                            }
                        }
                );
            }

            @Override
            public void onProductClick(int position, View v, View itemView) {
                Product product = productsAdapter.getProduct(position);

                fragmentCallBack.onProductClick(product);
            }

            @Override
            public void onAddToCartClick(int position, final View v, final View itemView) {
//                v.setVisibility(View.GONE);

                Product p = productsAdapter.getProduct(position);

                if (cartViewModel.hasProduct(p)) {
                    Toast.makeText(getActivity(), "Already in Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.added_to_cart).setVisibility(View.VISIBLE);

                cartViewModel.add(
                        productsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new CartRepository.IResponseAddCartCallBack() {
                            @Override
                            public void onResponseSuccessful(ResponseBody response) {
                                Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(ResponseBody responseBody) {
                                Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.added_to_cart).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.added_to_cart).setVisibility(View.GONE);
                            }
                        });
            }

            @Override
            public void onRemoveFromCartClick(int position, final View v, final View itemView) {
                Product p = productsAdapter.getProduct(position);

                if (!cartViewModel.hasProduct(p)) {
                    Toast.makeText(getActivity(), "Not in Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.VISIBLE);

                cartViewModel.remove(
                        productsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new CartRepository.IResponseAddCartCallBack() {
                            @Override
                            public void onResponseSuccessful(ResponseBody response) {
                                Toast.makeText(getActivity(), "removed from cart", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(ResponseBody responseBody) {
                                Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.GONE);
                            }
                        });
            }
        });

        ((MainActivity) getActivity()).hideBottomNavigation();

        return view;
    }

    private void performSearch(String keyword, boolean isCategory) {
        noProduct.setVisibility(View.GONE);
        SearchRepository.getInstance().search(keyword, isCategory, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                Toast.makeText(getActivity(), "ResponseSuccessful", Toast.LENGTH_SHORT).show();

                ProductsResponse productsResponse = (ProductsResponse) response.body();
                if (productsResponse != null) {
                    if (productsResponse.getProductList() == null || productsResponse.getProductList().size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        noProduct.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        productsAdapter.submitList(productsResponse.getProductList());
                    }
                }
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                Toast.makeText(getActivity(), "Response Not Successful", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCallFailed(String message) {
                Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
