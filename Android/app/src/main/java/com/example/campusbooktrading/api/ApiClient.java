package com.example.campusbooktrading.api;

import android.content.Context;

import com.example.campusbooktrading.utils.TokenInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API 客户端管理器
 */
public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:5000/api/";  // Android 模拟器访问本地服务器
    // 如果在真实设备上，改为: "http://192.168.x.x:5000/api/"

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static Context appContext = null;

    /**
     * 初始化 API 客户端（需要在应用启动时调用）
     */
    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * 获取 Retrofit 实例
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // 创建日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 创建 OkHttpClient
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor);

            // 添加令牌拦截器（如果应用上下文已初始化）
            if (appContext != null) {
                clientBuilder.addInterceptor(new TokenInterceptor(appContext));
            }

            OkHttpClient okHttpClient = clientBuilder.build();

            // 创建 Retrofit 实例
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 获取 API 服务实例
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

    /**
     * 重置 API 客户端（用于更改 BASE_URL）
     */
    public static void reset() {
        retrofit = null;
        apiService = null;
    }
}
