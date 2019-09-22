package com.fyp.bittrade.utils;

import com.fyp.bittrade.models.ProductsResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface IResponseCallBack {

    void onResponseSuccessful(Response response);
    void onResponseUnsuccessful(Response responseBody);
    void onCallFailed(String message);

}
