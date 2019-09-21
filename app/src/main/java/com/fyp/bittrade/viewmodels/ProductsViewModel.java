package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.repositories.ProductsDataSourceFactory;

public class ProductsViewModel extends ViewModel {
    private static final String TAG = ProductsViewModel.class.getName();

    private LiveData<PagedList<Product>> pagedListLiveData;
    private LiveData<PageKeyedDataSource<Integer, Product>> dataSourceLiveData;
    private ProductsDataSourceFactory productsDataSourceFactory;
    public ProductsViewModel() {

        productsDataSourceFactory = new ProductsDataSourceFactory();
        dataSourceLiveData = productsDataSourceFactory.getData();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        pagedListLiveData = new LivePagedListBuilder<Integer, Product>(productsDataSourceFactory, config).build();
    }

    public LiveData<PagedList<Product>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    public void refreshList() {
        productsDataSourceFactory.getData().getValue().invalidate();
    }
}
