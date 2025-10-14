package com.archive.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.1.10:8081/";

    private static Retrofit userRetrofit;
    private static Retrofit mainRetrofit;
    private static ApiService mainApiService;

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }


    public static ApiService getMainApiService() {
        if (mainApiService == null) {
            if (mainRetrofit == null) {
                mainRetrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(getClient())
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .build();
            }
            mainApiService = mainRetrofit.create(ApiService.class);
        }
        return mainApiService;
    }
}
