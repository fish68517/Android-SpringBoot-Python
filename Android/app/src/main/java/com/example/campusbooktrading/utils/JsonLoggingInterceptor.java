package com.example.campusbooktrading.utils;

import android.util.Log;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 自定义拦截器：支持中文展示并以通俗格式打印请求与响应日志
 */
public class JsonLoggingInterceptor implements Interceptor {
    private static final String TAG = "NetworkLog";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 1. 打印请求信息
        Log.i(TAG, "------------------ 发送请求 ------------------");
        // 【新增 URL 解码逻辑】将 %E8%89%AF%E5%A5%BD 转换回直观的中文
        String originalUrl = request.url().toString();
        try {
            String decodedUrl = URLDecoder.decode(originalUrl, "UTF-8");
            Log.i(TAG, "URL    : " + decodedUrl);
        } catch (Exception e) {
            // 如果极少数情况下解码报错，则回退打印原始的 URL
            Log.i(TAG, "URL    : " + originalUrl);
        }
        Log.i(TAG, "METHOD : " + request.method());

        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            Log.i(TAG, "BODY   : " + buffer.readString(StandardCharsets.UTF_8));
        }

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        // 2. 打印响应信息
        Log.i(TAG, "------------------ 接收响应 ------------------");
        Log.i(TAG, String.format("状态码  : %s (耗时 %.1fms)", response.code(), (t2 - t1) / 1e6d));

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // 读取全部字节
            Buffer buffer = source.getBuffer();

            // 关键点：使用 UTF-8 编码读取，解决中文乱码问题
            String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
            Log.i(TAG, "JSON数据: " + bodyString);
        }
        Log.i(TAG, "--------------------------------------------");

        return response;
    }
}