package com.fyp.bittrade.utils;

import com.fyp.bittrade.models.ProductsResponse;

import okhttp3.ResponseBody;

public interface IAddProductCallBack {
    void onResponseSuccessful(ResponseBody response);
    void onResponseUnsuccessful(ResponseBody responseBody);
    void onCallFailed(String message);
}
