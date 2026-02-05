package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 书籍数据模型
 */
public class Book {
    @SerializedName("id")
    public int id;

    @SerializedName("seller_id")
    public int sellerId;

    @SerializedName("title")
    public String title;

    @SerializedName("author")
    public String author;

    @SerializedName("isbn")
    public String isbn;

    @SerializedName("price")
    public double price;

    @SerializedName("condition")
    public String condition;

    @SerializedName("description")
    public String description;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("username")
    public String sellerName;

    @SerializedName("email")
    public String sellerEmail;

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
}
