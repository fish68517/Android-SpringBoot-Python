package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.BookAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.models.CartItem;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 书籍详情 Activity - 显示书籍完整信息、卖家资料、相关书籍和评价
 */
public class BookDetailActivity extends BaseActivity {

    private ImageView bookImage;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookPrice;
    private Chip bookCondition;
    private TextView bookIsbn;
    private TextView bookDescription;
    private TextView sellerName;
    private TextView sellerEmail;
    private RatingBar sellerRating;
    private MaterialButton addToCartButton;
    private RecyclerView relatedBooksRecyclerView;
    private ProgressBar loadingProgressBar;

    private ApiService apiService;
    private SessionManager sessionManager;
    private BookAdapter relatedBooksAdapter;
    private Book currentBook;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // 初始化
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // 绑定视图
        bindViews();

        // 获取书籍 ID
        bookId = getIntent().getIntExtra("book_id", -1);

        if (bookId != -1) {
            loadBookDetail();
        } else {
            ErrorHandler.showSnackbar(this, "书籍 ID 无效");
            finish();
        }

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        bookImage = findViewById(R.id.book_image);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookPrice = findViewById(R.id.book_price);
        bookCondition = findViewById(R.id.book_condition);
        bookIsbn = findViewById(R.id.book_isbn);
        bookDescription = findViewById(R.id.book_description);
        sellerName = findViewById(R.id.seller_name);
        sellerEmail = findViewById(R.id.seller_email);
        sellerRating = findViewById(R.id.seller_rating);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        relatedBooksRecyclerView = findViewById(R.id.related_books_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);

        // 设置相关书籍 RecyclerView
        setupRelatedBooksRecyclerView();

        // 设置加入购物车按钮
        addToCartButton.setOnClickListener(v -> addToCart());
    }

    /**
     * 设置相关书籍 RecyclerView
     */
    private void setupRelatedBooksRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        relatedBooksRecyclerView.setLayoutManager(layoutManager);
        relatedBooksAdapter = new BookAdapter(new ArrayList<>(), this, true);
        relatedBooksRecyclerView.setAdapter(relatedBooksAdapter);
    }

    /**
     * 加载书籍详情
     */
    private void loadBookDetail() {
        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadBookDetail);
            return;
        }

        showProgressBar(loadingProgressBar);

        apiService.getBookDetail(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    currentBook = response.body();
                    displayBookDetail(currentBook);
                    loadRelatedBooks(currentBook.author);
                } else {
                    ErrorHandler.handleApiError(BookDetailActivity.this, response, BookDetailActivity.this::loadBookDetail);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(BookDetailActivity.this, t, BookDetailActivity.this::loadBookDetail);
            }
        });
    }

    /**
     * 显示书籍详情
     */
    private void displayBookDetail(Book book) {
        bookTitle.setText("书籍名称：" + book.title);
        bookAuthor.setText("作者：" + book.author);
        bookPrice.setText(String.format("¥%.2f", book.price));
        bookCondition.setText(book.condition);
        bookIsbn.setText(book.isbn != null ? book.isbn : "N/A");
        bookDescription.setText(book.description != null ? book.description : "暂无描述");

        // 显示卖家信息
        sellerName.setText(book.sellerName != null ? book.sellerName : "Unknown");
        sellerEmail.setText(book.sellerEmail != null ? book.sellerEmail : "N/A");

        // 设置默认图片（实际应用中应使用 Glide 加载网络图片）
        bookImage.setImageResource(R.drawable.ic_book_placeholder);

        // 设置默认图片（实际应用中应使用 Glide 加载网络图片）
        // 在 Adapter 的 onBindViewHolder 中：
        String fullUrl = book.getFullImageUrl(ApiClient.BASE_URL_Image);
        Log.d("BookAdapter：", "Full URL: " + fullUrl);
        if (fullUrl != null) {
            Glide.with(this)
                    .load(fullUrl)
                    .placeholder(R.drawable.ic_book_placeholder) // 占位图
                    .into(bookImage);
        } else {
            // 如果没有图片，显示默认图
            bookImage.setImageResource(R.drawable.ic_book_placeholder);
        }
    }

    /**
     * 加载相关书籍（同一作者的其他书籍）
     */
    private void loadRelatedBooks(String author) {
        apiService.searchBooks(author).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Book> relatedBooks = parseBooks(data);

                    // 过滤掉当前书籍
                    if (relatedBooks != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            relatedBooks.removeIf(b -> b.id == currentBook.id);
                        }
                        // 只显示前 5 本相关书籍
                        if (relatedBooks.size() > 5) {
                            relatedBooks = relatedBooks.subList(0, 5);
                        }
                        relatedBooksAdapter.updateBooks(relatedBooks);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // 相关书籍加载失败不影响主要功能
            }
        });
    }

    /**
     * 解析 API 响应中的书籍列表
     */
    private List<Book> parseBooks(Map<String, Object> data) {
        List<Book> books = new ArrayList<>();
        if (data.containsKey("books")) {
            Object booksObj = data.get("books");
            if (booksObj instanceof List) {
                List<?> booksList = (List<?>) booksObj;
                for (Object obj : booksList) {
                    if (obj instanceof Map) {
                        Map<?, ?> bookMap = (Map<?, ?>) obj;
                        Book book = new Book();
                        book.id = ((Number) bookMap.get("id")).intValue();
                        book.title = (String) bookMap.get("title");
                        book.author = (String) bookMap.get("author");
                        book.price = ((Number) bookMap.get("price")).doubleValue();
                        book.condition = (String) bookMap.get("condition");
                        book.description = (String) bookMap.get("description");
                        book.status = (String) bookMap.get("status");
                        book.sellerId = ((Number) bookMap.get("seller_id")).intValue();
                        book.imageUrl = (String) bookMap.get("image_url");
                        books.add(book);
                    }
                }
            }
        }
        return books;
    }

    /**
     * 加入购物车
     */
    private void addToCart() {
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        if (currentBook == null) {
            ErrorHandler.showSnackbar(this, "书籍信息加载失败");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::addToCart);
            return;
        }

        // 创建购物车项目
        CartItem cartItem = new CartItem(currentBook.id, 1);

        // 调用 API 添加到购物车
        String token = sessionManager.getAuthorizationHeader();
        apiService.addToCart(token, cartItem).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(BookDetailActivity.this, "已添加到购物车");
                    // 可选：导航到购物车页面
                    // Intent intent = new Intent(BookDetailActivity.this, CartActivity.class);
                    // startActivity(intent);
                } else {
                    ErrorHandler.handleApiError(BookDetailActivity.this, response, BookDetailActivity.this::addToCart);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                ErrorHandler.handleNetworkError(BookDetailActivity.this, t, BookDetailActivity.this::addToCart);
            }
        });
    }

    /**
     * 处理返回按钮
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
