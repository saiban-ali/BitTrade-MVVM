package com.fyp.bittrade.repositories;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.utils.IResponseCallBack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesRepository {

    private static final String TAG = "FavoritesRepository";

    private static FavoritesRepository INSTANCE = null;

    public static FavoritesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FavoritesRepository();
        }

        return INSTANCE;
    }

    public void addToFavorites(String productId, String userId, final IResponseCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        Call<ResponseBody> call = api.addToFavorite(userId, productId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response);
                } else {
                    responseCallBack.onResponseUnsuccessful(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }

    public void removeFromFavorites(String productId, String userId, final IResponseCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        Call<ResponseBody> call = api.removeFromFavorites(userId, productId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response);
                } else {
                    responseCallBack.onResponseUnsuccessful(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }

    public void getFavorites(String userId, final IResponseCallBack responseCallBack) {
        Client.getInstance()
                .getClient()
                .create(Service.class)
                .getFavorites(userId)
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
