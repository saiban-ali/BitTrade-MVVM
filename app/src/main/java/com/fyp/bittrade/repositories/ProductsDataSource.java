package com.fyp.bittrade.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.utils.ExploreProgressHandler;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsDataSource extends PageKeyedDataSource<Integer, Product> {

    private static final String TAG = ProductsDataSource.class.getName();

    private static final int FIRST_PAGE = 1;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Product> callback) {
        Service service = Client.getInstance().getClient().create(Service.class);
        Call<ProductsResponse> call = service.getObject(FIRST_PAGE);

        ExploreProgressHandler.showExploreProgress();
        ExploreProgressHandler.hideErrorMessage();

        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProductsResponse> call, @NotNull Response<ProductsResponse> response) {
                if (response.isSuccessful()) {

                    callback.onResult(response.body().getProductList(), null, FIRST_PAGE + 1);
                    Log.d(TAG, "loadInitial: onResponse: response successful");

                    ExploreProgressHandler.hideExploreProgress();
                    ExploreProgressHandler.hideErrorMessage();
                } else {
                    Log.d(TAG, "loadInitial: onResponse: response not successful");
                    ExploreProgressHandler.hideExploreProgress();
                    ExploreProgressHandler.showErrorMessage();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProductsResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "loadInitial: onResponse: call failed");
                ExploreProgressHandler.hideExploreProgress();
                ExploreProgressHandler.showErrorMessage();
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Product> callback) {
        Service service = Client.getInstance().getClient().create(Service.class);
        Call<ProductsResponse> call = service.getObject(params.key);

        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProductsResponse> call, @NotNull Response<ProductsResponse> response) {
                if (response.isSuccessful()) {

                    Integer key = (params.key > 1) ? params.key - 1 : null;

                    callback.onResult(response.body().getProductList(), key);
                    Log.d(TAG, "loadBefore: onResponse: response successful");
                } else {
                    Log.d(TAG, "loadBefore: onResponse: response not successful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProductsResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "loadBefore: onResponse: call failed");
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Product> callback) {
        Service service = Client.getInstance().getClient().create(Service.class);
        Call<ProductsResponse> call = service.getObject(params.key);

        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProductsResponse> call, @NotNull Response<ProductsResponse> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    Integer key = (params.key < response.body().getTotalPages()) ? params.key + 1 : null;

                    callback.onResult(response.body().getProductList(), key);
                    Log.d(TAG, "loadAfter: onResponse: response successful");
                } else {
                    Log.d(TAG, "loadAfter: onResponse: response not successful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProductsResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "loadAfter: onResponse: call failed");
            }
        });
    }
}
