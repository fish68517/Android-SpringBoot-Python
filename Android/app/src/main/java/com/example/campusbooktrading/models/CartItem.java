package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 购物车项目数据模型
 */
public class CartItem {
    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("book_id")
    public int bookId;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("added_at")
    public String addedAt;

    // 书籍信息
    @SerializedName("title")
    public String title;

    @SerializedName("author")
    public String author;

    @SerializedName("price")
    public double price;

    @SerializedName("condition")
    public String condition;

    public CartItem() {
    }

    public CartItem(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return price * quantity;
    }
}
