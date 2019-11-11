package com.fyp.bittrade.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.fyp.bittrade.R;
import com.fyp.bittrade.activities.MainActivity;
import com.fyp.bittrade.adapters.ImageSlideAdapter;
import com.fyp.bittrade.adapters.ImageSliderAdapter;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    private static final String TAG = ProductDetailFragment.class.getName();

    private IFragmentCallBack fragmentCallBack;
    private Product product;
    private ImageView productImage;
    private TextView title;
    private TextView brand;
    private TextView price;
    private TextView description;
    private TextView category;
    private TextView stock;
    private ImageView addToFavorite;
    private ImageView removeFromFavorite;
    private Button addToCart;
    private Button removeFromCart;
    private Button checkout;
    private ImageView back;
    private SliderView sliderView;
    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private FavoritesViewModel favoritesViewModel;
    private CartViewModel cartViewModel;

    public ProductDetailFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        Bundle bundle = getArguments();
        product = bundle.getParcelable("product");

        init(view);

        updateViews();

        setUpClickListeners();

        if (cartViewModel.hasProduct(product)) {
            addToCart.setVisibility(View.GONE);
            removeFromCart.setVisibility(View.VISIBLE);
        } else {
            addToCart.setVisibility(View.VISIBLE);
            removeFromCart.setVisibility(View.GONE);
        }

        if (favoritesViewModel.hasProduct(product)) {
            addToFavorite.setVisibility(View.GONE);
            removeFromFavorite.setVisibility(View.VISIBLE);
        } else {
            addToFavorite.setVisibility(View.VISIBLE);
            removeFromFavorite.setVisibility(View.GONE);
        }

        ((MainActivity) Objects.requireNonNull(getActivity())).hideBottomNavigation();

        return view;
    }

    private void updateViews() {
        viewPager.setAdapter(imageSliderAdapter);

        title.setText(product.getTitle());
        price.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        brand.setText(
                (product.getBrand() == null || product.getBrand().equals("")) ?
                        "No Brand" :
                        product.getBrand()
        );
        description.setText(product.getDescription());
        category.setText(product.getCategory());
        stock.setText(String.format(Locale.US, "%d", product.getStock()));
    }

    private void setUpClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.onBackPressed();
            }
        });

        addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                removeFromFavorite.setVisibility(View.VISIBLE);
                addToFavorites();
            }
        });

        removeFromFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                addToFavorite.setVisibility(View.VISIBLE);
                removeFromFavorite();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                removeFromCart.setVisibility(View.VISIBLE);
                addToCart();
            }
        });

        removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                addToCart.setVisibility(View.VISIBLE);
                removeFromCart();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallBack.loadCheckoutFragment();
            }
        });
    }

    private void removeFromCart() {
//        cartViewModel.remove(product);
        cartViewModel.remove(
                product,
                ((MainActivity) getActivity()).getUser().getId(),
                new CartRepository.IResponseAddCartCallBack() {
                    @Override
                    public void onResponseSuccessful(ResponseBody response) {
                        Toast.makeText(getActivity(), "Removed from cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUnsuccessful(ResponseBody responseBody) {
                        addToCart.setVisibility(View.GONE);
                        removeFromCart.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallFailed(String message) {
                        addToCart.setVisibility(View.GONE);
                        removeFromCart.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void addToCart() {
//        cartViewModel.add(product);
        cartViewModel.add(
                product,
                ((MainActivity) getActivity()).getUser().getId(),
                new CartRepository.IResponseAddCartCallBack() {
                    @Override
                    public void onResponseSuccessful(ResponseBody response) {
                        Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUnsuccessful(ResponseBody responseBody) {
                        addToCart.setVisibility(View.VISIBLE);
                        removeFromCart.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallFailed(String message) {
                        addToCart.setVisibility(View.VISIBLE);
                        removeFromCart.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void removeFromFavorite() {
//        favoritesViewModel.remove(product);
        favoritesViewModel.remove(
                product,
                ((MainActivity) getActivity()).getUser().getId(),
                new IResponseCallBack() {
                    @Override
                    public void onResponseSuccessful(Response response) {
                        Toast.makeText(getActivity(), "Removed From favorites", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUnsuccessful(Response responseBody) {
                        addToFavorite.setVisibility(View.GONE);
                        removeFromFavorite.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallFailed(String message) {
                        addToFavorite.setVisibility(View.GONE);
                        removeFromFavorite.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void addToFavorites() {
//        favoritesViewModel.add(product);
        favoritesViewModel.add(
                product,
                ((MainActivity) getActivity()).getUser().getId(),
                new IResponseCallBack() {
                    @Override
                    public void onResponseSuccessful(Response response) {
                        Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseUnsuccessful(Response responseBody) {
                        addToFavorite.setVisibility(View.VISIBLE);
                        removeFromFavorite.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallFailed(String message) {
                        addToFavorite.setVisibility(View.VISIBLE);
                        removeFromFavorite.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Call Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void init(View view) {

//        sliderView = view.findViewById(R.id.imageSlider);

        viewPager = view.findViewById(R.id.imageSlider);
        imageSliderAdapter = new ImageSliderAdapter(getActivity(), product.getImages());

        back = view.findViewById(R.id.img_back);

//        productImage = view.findViewById(R.id.img_product);
        title = view.findViewById(R.id.txt_product_title);
        brand = view.findViewById(R.id.txt_product_brand);
        price = view.findViewById(R.id.txt_product_price);
        description = view.findViewById(R.id.txt_product_description);
        addToFavorite = view.findViewById(R.id.img_favorite);
        removeFromFavorite = view.findViewById(R.id.img_favorite_fill);
        addToCart = view.findViewById(R.id.btn_add_to_cart);
        removeFromCart = view.findViewById(R.id.btn_remove_from_cart);
        checkout = view.findViewById(R.id.btn_checkout);
        category = view.findViewById(R.id.txt_product_category);
        stock = view.findViewById(R.id.txt_product_stock);

        favoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoritesViewModel.class);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
    }
}
