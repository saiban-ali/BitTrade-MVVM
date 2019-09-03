package com.fyp.bittrade.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.fyp.bittrade.models.Product;

public class ProductDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Product>> data = new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {
        ProductsDataSource productsDataSource = new ProductsDataSource();
        data.postValue(productsDataSource);
        return productsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getData() {
        return data;
    }
}
