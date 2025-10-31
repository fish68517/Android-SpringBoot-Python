package com.archive.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class FaceApiClient {

    // !!! 关键: 替换成你Python Flask服务的IP地址
    public static final String BASE_URL = "http://192.168.222.145:5000";

    private static Retrofit retrofit;
    private static FaceApiService apiService;

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS) // AI计算可能较慢，延长超时
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder().create();
    }

    public static FaceApiService getApiService() {
        if (apiService == null) {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(getClient())
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .build();
            }
            apiService = retrofit.create(FaceApiService.class);
        }
        return apiService;
    }
}