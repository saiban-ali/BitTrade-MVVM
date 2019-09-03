package com.fyp.bittrade.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fyp.bittrade.R;
import com.fyp.bittrade.fragments.CartFragment;
import com.fyp.bittrade.fragments.ExploreFragment;
import com.fyp.bittrade.fragments.FavoritesFragment;
import com.fyp.bittrade.fragments.ProductDetailFragment;
import com.fyp.bittrade.fragments.ProfileFragment;
import com.fyp.bittrade.fragments.SellFragment;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.utils.IFragmentCallBack;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements IFragmentCallBack {

    private static final String TAG = MainActivity.class.getName();

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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    @Override
    public void onProductClick(Product product) {
        findViewById(R.id.bottom_navigation).setVisibility(View.GONE);

        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
