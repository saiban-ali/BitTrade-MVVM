package com.fyp.bittrade.repositories;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.utils.IResponseCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private static final String TAG = "SearchRepository";

    private static SearchRepository INSTANCE;

    private SearchRepository() {

    }

    public static SearchRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchRepository();
        }

        return INSTANCE;
    }

    public void search(String keyword, boolean isCategory, final IResponseCallBack responseCallBack) {
        Client.getInstance()
                .getClient()
                .create(Service.class)
                .search(keyword, isCategory)
                .enqueue(new Callback<ProductsResponse>() {
                    @Override
                    public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                        if (response.isSuccessful()) {
                            responseCallBack.onResponseSuccessful(response);
                        } else {
                            responseCallBack.onResponseUnsuccessful(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductsResponse> call, Throwable t) {
                        responseCallBack.onCallFailed(t.getMessage());
                    }
                });
    }
}
