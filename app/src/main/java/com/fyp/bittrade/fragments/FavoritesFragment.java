package com.fyp.bittrade.fragments;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.FavoriteProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;

import java.util.List;

import retrofit2.Response;

public class FavoritesFragment extends Fragment {

    private static final String TAG = FavoritesFragment.class.getName();

//    private List<Product> productList = new ArrayList<>();

    private Context context;

    private RecyclerView recyclerView;
    private FavoriteProductsAdapter favoriteProductsAdapter;
    private GridLayoutManager gridLayoutManager;

    private FavoritesViewModel favoritesViewModel;

    private IFragmentCallBack fragmentCallBack;

    public FavoritesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

        setUpToolbar(view);

        setUpRecyclerView();

        setUpFavoritesObserver();

        return view;
    }

    private void setUpFavoritesObserver() {
        favoritesViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                favoriteProductsAdapter.setFavoritesList(products);
            }
        });
    }

    private void setUpRecyclerView() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(3);
        } else {
            gridLayoutManager.setSpanCount(2);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(favoriteProductsAdapter);

        favoriteProductsAdapter.setOnItemClickListener(new FavoriteProductsAdapter.OnItemClickListener() {
            @Override
            public void onRemoveIconClick(int position, View view, View itemView) {
//                favoritesViewModel.remove(favoriteProductsAdapter.getProduct(position));

                Toast.makeText(context, "remove favorite clicked", Toast.LENGTH_SHORT).show();
                favoritesViewModel.remove(
                        favoriteProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new IResponseCallBack() {
                            @Override
                            public void onResponseSuccessful(Response response) {
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(Response responseBody) {
                                Toast.makeText(context, "Not Removed from favorites,  Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call failed: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }

            @Override
            public void onItemClick(int position, View v) {
                fragmentCallBack.onProductClick(favoriteProductsAdapter.getProduct(position));
            }
        });
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.favorites);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void init(View view) {
        context = getActivity();

        recyclerView = view.findViewById(R.id.recycler_view_products_favorites);
        gridLayoutManager = new GridLayoutManager(context, 2);
        favoriteProductsAdapter = new FavoriteProductsAdapter(context);

        favoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoritesViewModel.class);
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
