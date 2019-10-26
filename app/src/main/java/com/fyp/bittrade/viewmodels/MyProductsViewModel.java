package com.fyp.bittrade.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.repositories.AddProductsRepository;
import com.fyp.bittrade.repositories.FavoritesRepository;
import com.fyp.bittrade.repositories.ProductsRepository;
import com.fyp.bittrade.utils.IResponseCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MyProductsViewModel extends ViewModel {
    private static final String TAG = MyProductsViewModel.class.getName();

    private MutableLiveData<List<Product>> mutableLiveData;
    private List<Product> list = new ArrayList<>();

    private ProductsRepository productsRepository;

    public MyProductsViewModel() {
        productsRepository = ProductsRepository.getInstance();
    }

    public MutableLiveData<List<Product>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();

        return mutableLiveData;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
        mutableLiveData.setValue(list);
    }

    public void getMyProducts(String userId, final IResponseCallBack callBack) {
        productsRepository.getAddedProducts(userId, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                if (response.body() instanceof ProductsResponse) {
                    list = ((ProductsResponse) response.body()).getProductList();
                    mutableLiveData.setValue(list);
                    callBack.onResponseSuccessful(response);
                } else {
                    callBack.onResponseUnsuccessful(response);
                }
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                callBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                callBack.onCallFailed(message);
            }
        });
    }

    public void removeFromMyProducts(final Product product, String userId, final IResponseCallBack callBack) {
        list.remove(product);
        mutableLiveData.setValue(list);

        productsRepository.removeAddedProduct(product.getId(), userId, new IResponseCallBack() {
            @Override
            public void onResponseSuccessful(Response response) {
                if (response.body() instanceof ProductsResponse) {
                    list = ((ProductsResponse) response.body()).getProductList();
                    mutableLiveData.setValue(list);
                    callBack.onResponseSuccessful(response);
                } else {
                    list.add(product);
                    mutableLiveData.setValue(list);
                    callBack.onResponseUnsuccessful(response);
                }
            }

            @Override
            public void onResponseUnsuccessful(Response responseBody) {
                list.add(product);
                mutableLiveData.setValue(list);
                callBack.onResponseUnsuccessful(responseBody);
            }

            @Override
            public void onCallFailed(String message) {
                list.add(product);
                mutableLiveData.setValue(list);
                callBack.onCallFailed(message);
            }
        });
    }
}
