package com.fyp.bittrade.api;

import com.fyp.bittrade.models.CartListResponse;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.repositories.CartRepository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

//    String BASE_URL = "https://bittradeapi.azurewebsites.net/api/";
    String BASE_URL = "http://192.168.10.9:3000/api/";
//    String BASE_URL = "https://crucial-strata-250709.appspot.com/api/";
//    String BASE_URL = "https://bittrade-252909.appspot.com/api/";
//    @GET("products")
//    Call<ResponseBody> getObject();

    @GET("products")
    Call<ProductsResponse> getObject(@Query("page") int pageIndex);

    @POST("user/login")
    Call<User> sendLoginRequest(@Body User user);

    @POST("cart")
    Call<ResponseBody> addToCart(@Body CartRepository.CartResponse cartResponse);

    @GET("cart/getproducts/{userId}")
    Call<CartListResponse> getCart(@Path("userId") String userId);

    @DELETE("cart/remove")
    Call<ResponseBody> removeFromCart(@Query("productId") String productId, @Query("userId") String userId);

    @PUT("cart/incqty")
    Call<ResponseBody> incrementCount(@Body CartRepository.CartProduct cartProduct);

    @PUT("cart/decqty")
    Call<ResponseBody> decrementCount(@Body CartRepository.CartProduct cartProduct);
}
