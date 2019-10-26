package com.fyp.bittrade.repositories;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.utils.IResponseCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRepository {
    private static final ProductsRepository ourInstance = new ProductsRepository();

    public static ProductsRepository getInstance() {
        return ourInstance;
    }

    private ProductsRepository() {
    }

    public void getAddedProducts(String userId, final IResponseCallBack callBack) {
        Client.getInstance()
                .getClient()
                .create(Service.class)
                .getMyProducts(userId)
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if (response.isSuccessful()) {
                            callBack.onResponseSuccessful(response);
                        } else {
                            callBack.onResponseUnsuccessful(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable t) {
                        callBack.onCallFailed(t.getMessage());
                    }
                });
    }

    public void removeAddedProduct(String productId, String userId, final IResponseCallBack callBack) {
        Client.getInstance()
                .getClient()
                .create(Service.class)
                .removeAddedProduct(productId, userId)
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if (response.isSuccessful()) {
                            callBack.onResponseSuccessful(response);
                        } else {
                            callBack.onResponseUnsuccessful(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable t) {
                        callBack.onCallFailed(t.getMessage());
                    }
                });
    }
}
