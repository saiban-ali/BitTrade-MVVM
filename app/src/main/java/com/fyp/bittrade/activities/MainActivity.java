package com.fyp.bittrade.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.fragments.AddProductImagesFragment;
import com.fyp.bittrade.fragments.CartFragment;
import com.fyp.bittrade.fragments.CheckoutFragment;
import com.fyp.bittrade.fragments.ExploreFragment;
import com.fyp.bittrade.fragments.FavoritesFragment;
import com.fyp.bittrade.fragments.MyOrdersFragment;
import com.fyp.bittrade.fragments.MyProductsFragment;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
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

    private MutableLiveData<Boolean> isCartReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFavoritesReady = new MutableLiveData<>();
    private MutableLiveData<Boolean> isResourcesReady = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsCartReady() {
        return isCartReady;
    }

    public void setIsCartReady(boolean b) {
        isCartReady.setValue(b);
    }

    public MutableLiveData<Boolean> getIsFavoritesReady() {
        return isFavoritesReady;
    }

    public void setIsFavoritesReady(boolean b) {
        isFavoritesReady.setValue(b);
    }

    public MutableLiveData<Boolean> getIsResourcesReady() {
        return isResourcesReady;
    }

    public void setIsResourcesReady(boolean b) {
        isResourcesReady.setValue(b);
    }

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
                || fragment instanceof PaymentFragment
                || fragment instanceof CheckoutFragment
                || fragment instanceof SearchFragment
                || fragment instanceof ProductDetailFragment
                || fragment instanceof MyOrdersFragment
                || fragment instanceof MyProductsFragment
        ) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

        if (
                fragment instanceof PaymentFragment
                        || fragment instanceof AddProductImagesFragment
                        || fragment instanceof CheckoutFragment
                        || fragment instanceof ProductDetailFragment
                        || fragment instanceof SearchFragment
                || fragment instanceof MyOrdersFragment
        ) {
            hideBottomNavigation();
        } else {
            showBottomNavigation();
        }
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

        setIsResourcesReady(false);
        setIsFavoritesReady(false);
        setIsCartReady(false);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        CartRepository cartRepository = CartRepository.getInstance();
        cartRepository.getCart(user.getId(), new CartRepository.ICartListResponseCallBack() {
            @Override
            public void onResponseSuccessful(CartListResponse response) {
                Toast.makeText(MainActivity.this, "CartResponseSuccessful", Toast.LENGTH_SHORT).show();
                cartViewModel.setList(response.getProductList());
                cartViewModel.setPriceLiveData(response.getTotalPrice());
                setIsCartReady(true);
//                refreshExplore();
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
                setIsFavoritesReady(true);
//                refreshExplore();
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                Toast.makeText(MainActivity.this, "FavoritesResponseNotSuccessful", Toast.LENGTH_SHORT).show();
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

        isResourcesReady.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    hideLoadingProgress();
                }
            }
        });

        final Badge badgeCart = addBadgeAt(1, cartViewModel.getList().size());

        cartViewModel.getMutableLiveData().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products.size() < 1) {
                    badgeCart.hide(true);
                } else {
                    badgeCart.setBadgeNumber(products.size());
                }
            }
        });

        final Badge badgeFav = addBadgeAt(3, favoritesViewModel.getList().size());

        favoritesViewModel.getMutableLiveData().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products.size() < 1) {
                    badgeFav.hide(true);
                } else {
                    badgeFav.setBadgeNumber(products.size());
                }
            }
        });
    }

    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNavigation.getBottomNavigationItemView(position));
    }

    private void hideLoadingProgress() {

    }

    private void refreshExplore() {
//        if (isCartReady && isFavoritesReady) {
//            ProductsViewModel productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
//            productsViewModel.refreshList();
//        }
    }

    @Override
    public void onProductClick(Product product) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        loadFragment(fragment);

//        hideBottomNavigation();
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
        if (
                getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PaymentFragment
                        || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AddProductImagesFragment
                        || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof CheckoutFragment
                        || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ProductDetailFragment
                        || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SearchFragment
        ) {
            hideBottomNavigation();
        } else {
            showBottomNavigation();
        }
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
//        hideBottomNavigation();
    }

    @Override
    public void loadExploreFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        bottomNavigation.setSelectedItemId(R.id.bottom_nav_explore);
//        showBottomNavigation();
    }

    @Override
    public void loadCheckoutFragment() {
        loadFragment(new CheckoutFragment());
//        hideBottomNavigation();
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
//        hideBottomNavigation();
    }

    @Override
    public void loadMyOrdersFragment() {
        loadFragment(new MyOrdersFragment());
    }

    @Override
    public void loadCartFragment() {
        loadFragment(new CartFragment());
    }

    @Override
    public void loadProfileFragment() {
        loadFragment(new ProfileFragment());
        bottomNavigation.getMenu().findItem(R.id.bottom_nav_user).setChecked(true);
    }

    @Override
    public void loadMyProductsFragment() {
        loadFragment(new MyProductsFragment());
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
