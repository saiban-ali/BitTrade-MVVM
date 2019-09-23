package com.fyp.bittrade.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.fragments.AddProductImagesFragment;
import com.fyp.bittrade.fragments.CartFragment;
import com.fyp.bittrade.fragments.CheckoutFragment;
import com.fyp.bittrade.fragments.ExploreFragment;
import com.fyp.bittrade.fragments.FavoritesFragment;
import com.fyp.bittrade.fragments.PaymentFragment;
import com.fyp.bittrade.fragments.ProductDetailFragment;
import com.fyp.bittrade.fragments.ProfileFragment;
import com.fyp.bittrade.fragments.SearchFragment;
import com.fyp.bittrade.fragments.SellFragment;
import com.fyp.bittrade.models.CartListResponse;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.IResponseCallBack;
import com.fyp.bittrade.utils.PreferenceUtil;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.fyp.bittrade.viewmodels.FavoritesViewModel;
import com.fyp.bittrade.viewmodels.ProductsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IFragmentCallBack {

    private static final String TAG = MainActivity.class.getName();
    private static final int LOGIN_CODE = 1;

    private BottomNavigationViewEx bottomNavigation;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.bottom_nav_explore:
                    fragment = new ExploreFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_nav_cart:
                    fragment = new CartFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_nav_sell:
                    if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SellFragment) {
                        return true;
                    }
                    fragment = new SellFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_nav_favorites:
                    fragment = new FavoritesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bottom_nav_user:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private User user;

    public User getUser() {
        return user;
    }

    private CartViewModel cartViewModel;
    private FavoritesViewModel favoritesViewModel;

    private boolean isCartReady = false;
    private boolean isFavoritesReady = false;
    private boolean readyToRefreash = false;

    private void loadFragment(Fragment fragment) {

        if (fragment instanceof ProfileFragment) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);

            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (
                fragment instanceof AddProductImagesFragment
                        || fragment instanceof CheckoutFragment
                        || fragment instanceof PaymentFragment
                || fragment instanceof SearchFragment
        ) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("userBundle")) {
            Bundle bundle = getIntent().getBundleExtra("userBundle");
            user = bundle.getParcelable("user");
            Contact contact = bundle.getParcelable("contact");
            user.setContact(contact);
        }

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        CartRepository cartRepository = CartRepository.getInstance();
        cartRepository.getCart(user.getId(), new CartRepository.ICartListResponseCallBack() {
            @Override
            public void onResponseSuccessful(CartListResponse response) {
                Toast.makeText(MainActivity.this, "CartResponseSuccessful", Toast.LENGTH_SHORT).show();
                cartViewModel.setList(response.getProductList());
                cartViewModel.setPriceLiveData(response.getTotalPrice());
                isCartReady = true;
                refreshExplore();
            }

            @Override
            public void onResponseUnsuccessful(CartListResponse responseBody) {
                Toast.makeText(MainActivity.this, "CartResponseNotSuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallFailed(String message) {
                Toast.makeText(MainActivity.this, "CartCallFailed", Toast.LENGTH_SHORT).show();
            }
        });

        favoritesViewModel.loadFavoritesList(user.getId(), new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                Toast.makeText(MainActivity.this, "FavoritesResponseSuccessful", Toast.LENGTH_SHORT).show();
                isFavoritesReady = true;
                refreshExplore();
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                Toast.makeText(MainActivity.this, "FavoritesResponseNotsuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallFailed(String message) {
                Toast.makeText(MainActivity.this, "Favorites Call Failed", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setTextSize(10);

        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.bottom_nav_explore);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(android.R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.RED);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ProductDetailFragment) {
            bottomNavigation.setVisibility(View.GONE);
        } else {
            bottomNavigation.setVisibility(View.VISIBLE);
        }

//        loadFragment(new AddProductImagesFragment());
    }

    private void refreshExplore() {
        if (isCartReady && isFavoritesReady) {
            ProductsViewModel productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
            productsViewModel.refreshList();
        }
    }

    @Override
    public void onProductClick(Product product) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        hideBottomNavigation();
    }

    public void hideBottomNavigation() {
        findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == LOGIN_CODE && resultCode == RESULT_OK) {
//
//            assert data != null;
//            user = data.getParcelableExtra("user");
//
//            Toast.makeText(this, "Coming from Login", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    @Override
    public void logout() {
        PreferenceUtil.deleteUser(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void loadAddProductImagesFragment(Product product) {
        Fragment fragment = new AddProductImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        fragment.setArguments(bundle);

        loadFragment(fragment);
        hideBottomNavigation();
    }

    @Override
    public void loadExploreFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        bottomNavigation.setSelectedItemId(R.id.bottom_nav_explore);
        showBottomNavigation();
    }

    @Override
    public void loadCheckoutFragment() {
        loadFragment(new CheckoutFragment());
        hideBottomNavigation();
    }

    @Override
    public void loadPaymentFragment(String url) {
        Fragment fragment = new PaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        fragment.setArguments(bundle);

        loadFragment(fragment);
    }

    @Override
    public void loadSearchFragment(String searchId) {
        Fragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        if (searchId != null) {
            bundle.putString("searchId", searchId);
        }
        fragment.setArguments(bundle);
        loadFragment(fragment);
        hideBottomNavigation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
