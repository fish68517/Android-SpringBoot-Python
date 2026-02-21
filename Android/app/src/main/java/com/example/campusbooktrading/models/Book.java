package com.example.campusbooktrading.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * 书籍数据模型
 */
public class Book implements Serializable {

    public int id;

    @SerializedName("seller_id")
    public int sellerId;

    public String title;
    public String author;
    public String isbn;
    public double price;
    public String condition;
    public String description;
    public String status;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("username")
    public String sellerName;

    @SerializedName("email")
    public String sellerEmail;

    // ================== 新增字段 ==================
    // 映射后端返回的 "image_url" : "/api/books/1/image"
    @SerializedName("image_url")
    public String imageUrl;
    // ============================================

    public Book() {
    }

    public Book(String title, String author, String isbn, double price, String condition, String description) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.status = "active";
    }

    /**
     * 获取完整的图片网络地址
     * @param baseUrl API 的基础地址 (如 ApiClient.BASE_URL)
     * @return 完整的图片 URL，如果没有图片则返回 null
     */
    public String getFullImageUrl(String baseUrl) {
        Log.d("BookAdapter：", "Image URL: " + imageUrl + ", Base URL: " + baseUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // 防止 baseUrl 带有尾部的 "/" 与 imageUrl 头部的 "/" 重复
            if (baseUrl.endsWith("/") && imageUrl.startsWith("/")) {
                return baseUrl + imageUrl.substring(1);
            }
            return baseUrl + imageUrl;
        }
        return null;
    }
}