package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.MyListingsAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的列表 Activity - 显示用户的所有书籍列表
 */
public class MyListingsActivity extends BaseActivity {

    private RecyclerView listingsRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView emptyStateTextView;
    private FloatingActionButton createListingFab;

    private ApiService apiService;
    private SessionManager sessionManager;
    private MyListingsAdapter listingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        // 初始化
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // 绑定视图
        bindViews();

        // 设置 RecyclerView
        setupRecyclerView();

        // 设置 FAB 监听器
        createListingFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateListingActivity.class);
            startActivity(intent);
        });

        // 加载用户的列表
        loadUserListings();

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        listingsRecyclerView = findViewById(R.id.listings_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        emptyStateTextView = findViewById(R.id.empty_state_text_view);
        createListingFab = findViewById(R.id.create_listing_fab);
    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listingsRecyclerView.setLayoutManager(layoutManager);
        listingsAdapter = new MyListingsAdapter(new ArrayList<>(), this);
        listingsRecyclerView.setAdapter(listingsAdapter);
    }

    /**
     * 加载用户的列表
     */
    private void loadUserListings() {
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadUserListings);
            emptyStateTextView.setVisibility(android.view.View.VISIBLE);
            return;
        }

        showProgressBar(loadingProgressBar);
        int userId = sessionManager.getUserId();

        apiService.getUserBooks(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Book> books = parseBooks(data);

                    if (books != null && !books.isEmpty()) {
                        listingsAdapter.updateListings(books);
                        emptyStateTextView.setVisibility(android.view.View.GONE);
                    } else {
                        emptyStateTextView.setVisibility(android.view.View.VISIBLE);
                        emptyStateTextView.setText("暂无列表");
                    }
                } else {
                    ErrorHandler.handleApiError(MyListingsActivity.this, response, MyListingsActivity.this::loadUserListings);
                    emptyStateTextView.setVisibility(android.view.View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(MyListingsActivity.this, t, MyListingsActivity.this::loadUserListings);
                emptyStateTextView.setVisibility(android.view.View.VISIBLE);
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
     * 处理返回按钮
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * 刷新列表
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadUserListings();
    }
}
