package com.fyp.bittrade.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    public static String TAG = Client.class.getName();
    public static Client INSTANCE = null;

    private Retrofit retrofit;

    private Client() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static Client getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Client();

        return INSTANCE;
    }

    public Retrofit getClient() {
        return retrofit;
    }
}
