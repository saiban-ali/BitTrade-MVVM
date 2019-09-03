package com.fyp.bittrade.utils;

import com.fyp.bittrade.models.ProductsResponse;

import okhttp3.ResponseBody;

public interface IResponseCallBack {

    void onResponseSuccessful(ProductsResponse response);
    void onResponseUnsuccessful(ResponseBody responseBody);
    void onCallFailed(String message);

}
