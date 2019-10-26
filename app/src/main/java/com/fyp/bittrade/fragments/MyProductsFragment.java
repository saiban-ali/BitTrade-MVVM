package com.fyp.bittrade.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.FavoriteProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.viewmodels.MyProductsViewModel;

import java.util.List;

import retrofit2.Response;

public class MyProductsFragment extends Fragment {

    private IFragmentCallBack fragmentCallBack;

    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);

        final User user = ((MainActivity) getActivity()).getUser();

        final MyProductsViewModel myProductsViewModel = ViewModelProviders.of(getActivity()).get(MyProductsViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_my_products);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        final FavoriteProductsAdapter adapter = new FavoriteProductsAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FavoriteProductsAdapter.OnItemClickListener() {
            @Override
            public void onRemoveIconClick(int position, View view, View itemView) {
                myProductsViewModel.removeFromMyProducts(adapter.getProduct(position), user.getId(), new IResponseCallBack() {
                    @Override
                    public void onResponseSuccessful(Response response) {
                        Toast.makeText(getActivity(), "Product removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUnsuccessful(Response responseBody) {
                        Toast.makeText(getActivity(), "Product not removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallFailed(String message) {
                        Toast.makeText(getActivity(), "error: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onItemClick(int position, View v) {

            }
        });

        myProductsViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setFavoritesList(products);
            }
        });

        myProductsViewModel.getMyProducts(user.getId(), new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                if (response.body() instanceof ProductsResponse) {
//                    myProductsViewModel.setList(((ProductsResponse) response.body()).getProductList());
                    Toast.makeText(getActivity(), "Products Loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Products Response not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                Toast.makeText(getActivity(), "Products Response Unsuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallFailed(String message) {
                Toast.makeText(getActivity(), "error: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentCallBack) {
            fragmentCallBack = (IFragmentCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentCallBack = null;
    }


}
