package com.fyp.bittrade.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.ExploreProductsAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.ExploreProgressHandler;
import com.fyp.bittrade.utils.IExploreeProgressCallBack;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;
import com.fyp.bittrade.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ExploreFragment extends Fragment implements IExploreeProgressCallBack {

    private static final String TAG = ExploreFragment.class.getName();

    private List<Product> productList = new ArrayList<>();

    private Context context;
    private RecyclerView recyclerView;
    private ExploreProductsAdapter exploreProductsAdapter;
    private GridLayoutManager gridLayoutManager;
//    private RelativeLayout errorLayout;
    private TextView categorySeeAll;

//    private ProductsDataSource productsRepository;
    private ProductsViewModel productsViewModel;
    private FavoritesViewModel favoritesViewModel;
    private CartViewModel cartViewModel;

    private IFragmentCallBack fragmentCallBack;

    private LinearLayout categoryMen;
    private LinearLayout categoryWomen;
    private LinearLayout categoryElectronics;
    private LinearLayout categoryGadgets;
    private LinearLayout categoryGaming;
    private ProgressBar progressBar;
    private TextView errorText;

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

        ExploreProgressHandler.callBack = this;
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

//        progressBar.setVisibility(View.VISIBLE);
//        errorText.setVisibility(View.GONE);

        setUpListeners();

        setUpRecyclerView();

        setUpProductsObserver();

        return view;
    }

    private void setUpListeners() {
        categorySeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });

        categoryMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadSearchFragment("Men's Fashion");
            }
        });
        categoryWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadSearchFragment("Women's Fashion");
            }
        });
        categoryElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadSearchFragment("Electronics Devices");
            }
        });
        categoryGadgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadSearchFragment("Gadgets");
            }
        });
        categoryGaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadSearchFragment("Gaming");
            }
        });
    }

    private void showCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a Category");

        final String[] categories = getActivity().getResources().getStringArray(R.array.categories);

        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragmentCallBack.loadSearchFragment(categories[which]);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setUpProductsObserver() {

        productsViewModel.getPagedListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
//                progressBar.setVisibility(View.GONE);
                exploreProductsAdapter.submitList(products);
            }
        });

        favoritesViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
//                exploreProductsAdapter.notifyDataSetChanged();
                exploreProductsAdapter.submitFavoritesList(favoritesViewModel.getList());
            }
        });

        cartViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
//                exploreProductsAdapter.notifyDataSetChanged();
                exploreProductsAdapter.submitCartList(cartViewModel.getList());
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
            public void onAddToFavoritesClick(int position, final View v, final View itemView) {
                Toast.makeText(context, "Add favorite clicked", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.VISIBLE);
                favoritesViewModel.add(
                        exploreProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new IResponseCallBack() {
                            @Override
                            public void onResponseSuccessful(Response response) {
                                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(Response responseBody) {
                                Toast.makeText(context, "Not Added to favorite, Something went wrong", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call failed: " + message, Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon_fill).setVisibility(View.GONE);
                            }
                        }
                );
            }

            @Override
            public void onRemoveFromFavoritesClick(int position, final View v, final View itemView) {
                Toast.makeText(context, "remove favorite clicked", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.favorites_icon).setVisibility(View.VISIBLE);
                favoritesViewModel.remove(
                        exploreProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new IResponseCallBack() {
                            @Override
                            public void onResponseSuccessful(Response response) {
                                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(Response responseBody) {
                                Toast.makeText(context, "Not Removed from favorites,  Something went wrong", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call failed: " + message, Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.favorites_icon).setVisibility(View.GONE);
                            }
                        }
                );
            }

            @Override
            public void onProductClick(int position, View v, View itemView) {
                fragmentCallBack.onProductClick(exploreProductsAdapter.getProduct(position));
            }

            @Override
            public void onAddToCartClick(int position, final View v, final View itemView) {
//                v.setVisibility(View.GONE);

                Product p = exploreProductsAdapter.getProduct(position);

                if (cartViewModel.hasProduct(p)) {
                    Toast.makeText(context, "Already in Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.added_to_cart).setVisibility(View.VISIBLE);

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
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.added_to_cart).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.added_to_cart).setVisibility(View.GONE);
                            }
                        });
            }

            @Override
            public void onRemoveFromCartClick(int position, final View v, final View itemView) {
                Product p = exploreProductsAdapter.getProduct(position);

                if (!cartViewModel.hasProduct(p)) {
                    Toast.makeText(context, "Not in Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                v.setVisibility(View.GONE);
                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.VISIBLE);

                cartViewModel.remove(
                        exploreProductsAdapter.getProduct(position),
                        ((MainActivity) getActivity()).getUser().getId(),
                        new CartRepository.IResponseAddCartCallBack() {
                            @Override
                            public void onResponseSuccessful(ResponseBody response) {
                                Toast.makeText(context, "removed from cart", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseUnsuccessful(ResponseBody responseBody) {
                                Toast.makeText(context, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCallFailed(String message) {
                                Toast.makeText(context, "Call Failed", Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.add_to_cart_icon).setVisibility(View.GONE);
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
        exploreProductsAdapter = new ExploreProductsAdapter(context, favoritesViewModel, cartViewModel);

        categorySeeAll = container.findViewById(R.id.txt_see_all);
        categoryMen = container.findViewById(R.id.layout_cat_man);
        categoryWomen = container.findViewById(R.id.layout_cat_woman);
        categoryElectronics = container.findViewById(R.id.layout_cat_electronics);
        categoryGadgets = container.findViewById(R.id.layout_cat_gadgets);
        categoryGaming = container.findViewById(R.id.layout_cat_gaming);

        progressBar = container.findViewById(R.id.progress);
        errorText = container.findViewById(R.id.error_text);

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

            case R.id.app_bar_search:
                fragmentCallBack.loadSearchFragment(null);
                return true;

            default:
                return false;
        }

    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void showErrorMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorText.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void hideErrorMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorText.setVisibility(View.GONE);
            }
        });

    }
}
