package com.fyp.bittrade.repositories;

import com.fyp.bittrade.api.Client;
import com.fyp.bittrade.api.Service;
import com.fyp.bittrade.models.CartListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {

    private static final String TAG = "CartRepository";
    private static CartRepository INSTANCE = null;

    public static CartRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CartRepository();
        }

        return INSTANCE;
    }

    public void getCart(String userId, final ICartListResponseCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        Call<CartListResponse> call = api.getCart(userId);
        call.enqueue(new Callback<CartListResponse>() {
            @Override
            public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response.body());
                } else {
                    responseCallBack.onResponseUnsuccessful(response.body());
                }
            }

            @Override
            public void onFailure(Call<CartListResponse> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }

    public void addToCart(String productId, String userId, final IResponseAddCartCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        Call<ResponseBody> call = api.addToCart(new CartResponse(productId, userId));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            responseCallBack.onResponseSuccessful(response.body());
                        } else {
                            responseCallBack.onResponseUnsuccessful(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        responseCallBack.onCallFailed(t.getMessage());
                    }
                });
    }

    public void removeFromCart(String productId, String userId, final IResponseAddCartCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        Call<ResponseBody> call = api.removeFromCart(productId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response.body());
                } else {
                    responseCallBack.onResponseUnsuccessful(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }

    public void incrementProductCount(String productId, String userId, int newQty, final IResponseAddCartCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        CartProduct cartProduct = new CartProduct(userId, productId, newQty);

        Call<ResponseBody> call = api.incrementCount(cartProduct);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response.body());
                } else {
                    responseCallBack.onResponseUnsuccessful(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }

    public void decrementProductCount(String productId, String userId, int newQty, final IResponseAddCartCallBack responseCallBack) {
        Service api = Client.getInstance()
                .getClient()
                .create(Service.class);

        CartProduct cartProduct = new CartProduct(userId, productId, newQty);

        Call<ResponseBody> call = api.decrementCount(cartProduct);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    responseCallBack.onResponseSuccessful(response.body());
                } else {
                    responseCallBack.onResponseUnsuccessful(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallBack.onCallFailed(t.getMessage());
            }
        });
    }


    public class CartProduct {

        String userId;
        String productId;
        int newqty;

        CartProduct(String userId, String productId, int newQty) {
            this.userId = userId;
            this.productId = productId;
            this.newqty = newQty;
        }
    }


    public class CartResponse {
        private String productId;
        private String userId;

        CartResponse(String productId, String userId) {
            this.productId = productId;
            this.userId = userId;
        }
    }

    public interface ICartListResponseCallBack {
        void onResponseSuccessful(CartListResponse response);
        void onResponseUnsuccessful(CartListResponse responseBody);
        void onCallFailed(String message);
    }

    public interface IResponseAddCartCallBack {
        void onResponseSuccessful(ResponseBody response);
        void onResponseUnsuccessful(ResponseBody responseBody);
        void onCallFailed(String message);
    }

}
