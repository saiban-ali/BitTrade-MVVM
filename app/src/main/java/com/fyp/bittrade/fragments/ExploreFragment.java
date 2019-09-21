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
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.ExploreProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;
import com.fyp.bittrade.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class ExploreFragment extends Fragment {

    private static final String TAG = ExploreFragment.class.getName();

    private List<Product> productList = new ArrayList<>();

    private Context context;
    private RecyclerView recyclerView;
    private ExploreProductsAdapter exploreProductsAdapter;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout errorLayout;

//    private ProductsDataSource productsRepository;
    private ProductsViewModel productsViewModel;
    private FavoritesViewModel favoritesViewModel;
    private CartViewModel cartViewModel;

    private IFragmentCallBack fragmentCallBack;

//    private boolean hasNextPage = false;
//    private boolean isLastPage = false;


    public ExploreFragment() {
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
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        init(view);
        setUpToolBar(view);

        setUpRecyclerView();

        setUpProductsObserver();

        return view;
    }

    private void setUpProductsObserver() {

        productsViewModel.getPagedListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                exploreProductsAdapter.submitList(products);
            }
        });

        favoritesViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {

            }
        });

        cartViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {

            }
        });


//        productsViewModel = ViewModelProviders.of(getActivity()).get(ProductsViewModel.class);
//        productsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
//            @Override
//            public void onChanged(List<Product> products) {
//                exploreProductsAdapter.removeLoadingFooter();
////                if (productList.size() > 0) {
////                    int i = productList.size() - 1;
////                    productList.remove(i);
////                    exploreProductsAdapter.notifyItemRemoved(i);
////                }
//                Toast.makeText(context, "Loading Removed", Toast.LENGTH_SHORT).show();
//                exploreProductsAdapter.addAll(products);
////                int a = productList.size();
////                productList.addAll(products);
////                exploreProductsAdapter.notifyItemRangeInserted(a, products.size());
//                Toast.makeText(context, "products added", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        productsViewModel.getHasNextPage().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                hasNextPage = aBoolean;
//                if (aBoolean) {
//                    exploreProductsAdapter.addLoadingFooter();
////                    productList.add(new Product());
////                    exploreProductsAdapter.notifyItemInserted(productList.size() - 1);
//                    Toast.makeText(context, "Loading added", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        productsViewModel.getIsLastPage().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                isLastPage = aBoolean;
//            }
//        });
    }

    private void setUpRecyclerView() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(3);
        } else {
            gridLayoutManager.setSpanCount(2);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(exploreProductsAdapter);

        exploreProductsAdapter.setOnItemClickListener(new ExploreProductsAdapter.OnItemClickListener() {
            @Override
            public void onAddToFavoritesClick(int position, View v, View itemView) {
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.VISIBLE);
                favoritesViewModel.add(exploreProductsAdapter.getProduct(position));
            }

            @Override
            public void onRemoveFromFavoritesClick(int position, View v, View itemView) {
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon).setVisibility(View.VISIBLE);
                favoritesViewModel.remove(exploreProductsAdapter.getProduct(position));
            }

            @Override
            public void onProductClick(int position, View v, View itemView) {
                fragmentCallBack.onProductClick(exploreProductsAdapter.getProduct(position));
            }

            @Override
            public void onAddToCartClick(int position, View v, View itemView) {
//                v.setVisibility(View.GONE);

                Product p = exploreProductsAdapter.getProduct(position);

                if (cartViewModel.hasProduct(p)) {
                    Toast.makeText(context, "Already in Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                cartViewModel.add(
                        exploreProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new CartRepository.IResponseAddCartCallBack() {
                            @Override
                            public void onResponseSuccessful(ResponseBody response) {
                                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(ResponseBody responseBody) {
                                Toast.makeText(context, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (position == exploreProductsAdapter.getItemCount() - 1) {
//                    return 2;
//                } else {
//                    return 1;
//                }
//            }
//        });

//        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
//            @Override
//            protected void loadMoreItem() {
//                productsViewModel.fetchMoreProducts();
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean hasNextPage() {
//                return hasNextPage;
//            }
//        });

    }

    private void setUpToolBar(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
    }

    private void init(View container) {
        context = getActivity();

        productsViewModel = ViewModelProviders.of(getActivity()).get(ProductsViewModel.class);
        favoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoritesViewModel.class);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        recyclerView = container.findViewById(R.id.recycler_view_products_main);
        gridLayoutManager = new GridLayoutManager(context, 2);
        exploreProductsAdapter = new ExploreProductsAdapter(context, favoritesViewModel.getList(), cartViewModel.getList());
//        productsRepository = ProductsDataSource.getInstance();
//        errorLayout = container.findViewById(R.id.layout_error);
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

            case R.id.refresh:
                productsViewModel.refreshList();
                return true;

            default:
                return false;
        }

    }
}
