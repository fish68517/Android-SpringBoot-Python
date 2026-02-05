package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 交易数据模型
 */
public class Transaction {
    @SerializedName("id")
    public int id;

    @SerializedName("buyer_id")
    public int buyerId;

    @SerializedName("seller_id")
    public int sellerId;

    @SerializedName("book_id")
    public int bookId;

    @SerializedName("price")
    public double price;

    @SerializedName("status")
    public String status;

    @SerializedName("delivery_address")
    public String deliveryAddress;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    // 书籍信息
    @SerializedName("title")
    public String title;

    @SerializedName("author")
    public String author;

    @SerializedName("condition")
    public String condition;

    // 用户信息
    @SerializedName("buyer_name")
    public String buyerName;

    @SerializedName("seller_name")
    public String sellerName;

    @SerializedName("buyer_email")
    public String buyerEmail;

    @SerializedName("seller_email")
    public String sellerEmail;

    public Transaction() {
    }

    public Transaction(int buyerId, int sellerId, int bookId, double price, String deliveryAddress) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.bookId = bookId;
        this.price = price;
        this.deliveryAddress = deliveryAddress;
        this.status = "pending";
    }
}
