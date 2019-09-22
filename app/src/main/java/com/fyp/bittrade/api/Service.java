package com.fyp.bittrade.api;

import androidx.core.provider.FontsContractCompat;

import com.fyp.bittrade.models.CartListResponse;
import com.fyp.bittrade.models.CheckoutResponse;
import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.Product;
import com.fyp.bittrade.models.ProductsResponse;
import com.fyp.bittrade.models.User;
import com.fyp.bittrade.models.UserResponse;
import com.fyp.bittrade.repositories.CartRepository;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

//    String BASE_URL = "https://bittradeapi.azurewebsites.net/api/";
    String BASE_URL = "http://192.168.10.11:3000/api/";
//    String BASE_URL = "https://crucial-strata-250709.appspot.com/api/";
//    String BASE_URL = "https://bittrade-252909.appspot.com/api/";
//    @GET("products")
//    Call<ResponseBody> getObject();

    @GET("products")
    Call<ProductsResponse> getObject(@Query("page") int pageIndex);

    @POST("user/login")
    Call<UserResponse> sendLoginRequest(@Body User user);

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

    @POST("user")
    Call<UserResponse> registerUser(@Body User user);

    @Multipart
    @PUT("user/uploadimage/{userId}")
    Call<UserResponse> uploadImage(@Part MultipartBody.Part imageFile, @Path("userId") String userId);

    @PUT("user/contactinfo/{userId}")
    Call<UserResponse> addAddress(@Body Contact contact, @Path("userId") String userId);

    @Multipart
    @POST("products/{userId}")
    Call<ResponseBody> uploadProduct(
            @Path("userId") String userId,
            @PartMap Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> productimages
    );

    @GET("checkout/{userId}")
    Call<CheckoutResponse> checkout(@Path("userId") String userId);

    @GET("products/favorite/{userId}")
    Call<ProductsResponse> getFavorites(@Path("userId") String userId);

    @POST("products/favorite/{userId}/{productId}")
    Call<ResponseBody> addToFavorite(@Path("userId") String userId, @Path("productId") String productId);

    @DELETE("products/favorite/{userId}/{productId}")
    Call<ResponseBody> removeFromFavorites(@Path("userId") String userId, @Path("productId") String productId);

    @GET("products/search")
    Call<ProductsResponse> search(@Query("keyword") String keyword, @Query("is_category") boolean isCategory);
}
