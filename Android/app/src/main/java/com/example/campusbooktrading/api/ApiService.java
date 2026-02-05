package com.example.campusbooktrading.api;

import com.example.campusbooktrading.models.AuthRequest;
import com.example.campusbooktrading.models.AuthResponse;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.models.CartItem;
import com.example.campusbooktrading.models.Transaction;
import com.example.campusbooktrading.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit API 服务接口
 */
public interface ApiService {

    // ==================== 认证相关 ====================

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @GET("auth/verify")
    Call<AuthResponse> verify(@Header("Authorization") String token);

    // ==================== 书籍相关 ====================

    @GET("books")
    Call<Map<String, Object>> getBooks(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("condition") String condition,
            @Query("min_price") double minPrice,
            @Query("max_price") double maxPrice
    );

    @GET("books/search")
    Call<Map<String, Object>> searchBooks(@Query("q") String query);

    @GET("books/{id}")
    Call<Book> getBookDetail(@Path("id") int bookId);

    @POST("books")
    Call<Map<String, Object>> createBook(
            @Header("Authorization") String token,
            @Body Book book
    );

    @PUT("books/{id}")
    Call<Map<String, Object>> updateBook(
            @Header("Authorization") String token,
            @Path("id") int bookId,
            @Body Book book
    );

    @DELETE("books/{id}")
    Call<Map<String, Object>> deleteBook(
            @Header("Authorization") String token,
            @Path("id") int bookId
    );

    @GET("books/user/{userId}")
    Call<Map<String, Object>> getUserBooks(@Path("userId") int userId);

    // ==================== 购物车相关 ====================

    @GET("cart")
    Call<Map<String, Object>> getCart(@Header("Authorization") String token);

    @POST("cart")
    Call<Map<String, Object>> addToCart(
            @Header("Authorization") String token,
            @Body CartItem item
    );

    @PUT("cart/{itemId}")
    Call<Map<String, Object>> updateCartItem(
            @Header("Authorization") String token,
            @Path("itemId") int itemId,
            @Body CartItem item
    );

    @DELETE("cart/{itemId}")
    Call<Map<String, Object>> removeFromCart(
            @Header("Authorization") String token,
            @Path("itemId") int itemId
    );

    @DELETE("cart")
    Call<Map<String, Object>> clearCart(@Header("Authorization") String token);

    // ==================== 交易相关 ====================

    @POST("transactions")
    Call<Map<String, Object>> createTransaction(
            @Header("Authorization") String token,
            @Body Map<String, String> request
    );

    @GET("transactions/purchases")
    Call<Map<String, Object>> getPurchases(@Header("Authorization") String token);

    @GET("transactions/sales")
    Call<Map<String, Object>> getSales(@Header("Authorization") String token);

    @GET("transactions/{id}")
    Call<Transaction> getTransactionDetail(@Path("id") int transactionId);

    @PUT("transactions/{id}/status")
    Call<Map<String, Object>> updateTransactionStatus(
            @Header("Authorization") String token,
            @Path("id") int transactionId,
            @Body Map<String, String> status
    );

    // ==================== 用户相关 ====================

    @GET("users/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @PUT("users/profile")
    Call<Map<String, Object>> updateUserProfile(
            @Header("Authorization") String token,
            @Body User user
    );

    @PUT("users/password")
    Call<Map<String, Object>> changePassword(
            @Header("Authorization") String token,
            @Body Map<String, String> passwords
    );

    // ==================== 健康检查 ====================

    @GET("health")
    Call<Map<String, Object>> healthCheck();
}
