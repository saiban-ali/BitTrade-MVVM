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
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fyp.bittrade.R;
import com.fyp.bittrade.fragments.AddProductImagesFragment;
import com.fyp.bittrade.fragments.CartFragment;
import com.fyp.bittrade.fragments.ExploreFragment;
import com.fyp.bittrade.fragments.FavoritesFragment;
import com.fyp.bittrade.fragments.ProductDetailFragment;
import com.fyp.bittrade.fragments.ProfileFragment;
import com.fyp.bittrade.fragments.SellFragment;
import com.fyp.bittrade.models.CartListResponse;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.repositories.CartRepository;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.fyp.bittrade.utils.PreferenceUtil;
import com.fyp.bittrade.viewmodels.CartViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import okhttp3.ResponseBody;

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

    private boolean loggedIn = false;

    private void loadFragment(Fragment fragment) {

        if (fragment instanceof ProfileFragment) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);

            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if(fragment instanceof AddProductImagesFragment) {
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
        CartRepository cartRepository = CartRepository.getInstance();
        cartRepository.getCart(user.getId(), new CartRepository.ICartListResponseCallBack() {
            @Override
            public void onResponseSuccessful(CartListResponse response) {
                Toast.makeText(MainActivity.this, "ResponseSuccessful", Toast.LENGTH_SHORT).show();
                cartViewModel.setList(response.getProductList());
                cartViewModel.setPriceLiveData(response.getTotalPrice());
            }

            @Override
            public void onResponseUnsuccessful(CartListResponse responseBody) {
                Toast.makeText(MainActivity.this, "ResponseNotSuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallFailed(String message) {
                Toast.makeText(MainActivity.this, "CallFailed", Toast.LENGTH_SHORT).show();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
