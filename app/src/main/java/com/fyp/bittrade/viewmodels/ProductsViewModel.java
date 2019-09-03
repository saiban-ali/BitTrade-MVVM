package com.fyp.bittrade.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.repositories.ProductDataSourceFactory;
import com.fyp.bittrade.repositories.ProductsDataSource;
import com.fyp.bittrade.utils.IResponseCallBack;

import java.util.List;

import okhttp3.ResponseBody;

public class ProductsViewModel extends ViewModel {
    private static final String TAG = ProductsViewModel.class.getName();

    private LiveData<PagedList<Product>> pagedListLiveData;
    private LiveData<PageKeyedDataSource<Integer, Product>> dataSourceLiveData;

    public ProductsViewModel() {

        ProductDataSourceFactory productDataSourceFactory = new ProductDataSourceFactory();
        dataSourceLiveData = productDataSourceFactory.getData();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        pagedListLiveData = new LivePagedListBuilder<Integer, Product>(productDataSourceFactory, config).build();
    }

    public LiveData<PagedList<Product>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
