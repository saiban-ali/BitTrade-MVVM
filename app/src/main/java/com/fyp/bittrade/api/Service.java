package com.fyp.bittrade.api;

import com.fyp.bittrade.models.ProductsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

//    String BASE_URL = "https://bittradeapi.azurewebsites.net/api/";
//    String BASE_URL = "http://192.168.10.8:3000/api/";
    String BASE_URL = "https://crucial-strata-250709.appspot.com/api/";

//    @GET("products")
//    Call<ResponseBody> getObject();

    @GET("products")
    Call<ProductsResponse> getObject(@Query("page") int pageIndex);

//    @POST("user/login")
//    Call<User> sendLoginRequest(@Body User user);
}
