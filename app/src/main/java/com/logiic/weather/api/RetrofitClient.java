package com.logiic.weather.api;

import com.logiic.weather.services.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.forecast.io/forecast/";
    private static final String TAG = RetrofitClient.class.getSimpleName();

    private static RetrofitClient mInstance;
    private static Retrofit retrofit;
    OkHttpClient client;

    public RetrofitClient() {

        client = new OkHttpClient.Builder()
            .build();

        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
