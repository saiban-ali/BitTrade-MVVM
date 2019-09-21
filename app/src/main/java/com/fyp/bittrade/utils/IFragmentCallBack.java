package com.fyp.bittrade.utils;

import com.fyp.bittrade.models.Product;

public interface IFragmentCallBack {

    void onProductClick(Product product);
    void onBackPressed();
    void logout();
    void loadAddProductImagesFragment(Product product);
    void loadExploreFragment();
}
