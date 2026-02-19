package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.BookAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页 Activity - 显示精选书籍和最新书籍
 * 包含底部导航菜单和搜索功能
 */
public class HomeActivity extends BaseActivity {

    private MaterialToolbar appBarWithSearchView;
    private RecyclerView featuredBooksCarousel;
    private RecyclerView recentBooksGrid;
    private BottomNavigationView bottomNavigation;
    private SessionManager sessionManager;
    private ApiService apiService;
    private BookAdapter featuredAdapter;
    private BookAdapter recentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();

        // 绑定视图
        appBarWithSearchView = findViewById(R.id.app_bar_with_search);
        featuredBooksCarousel = findViewById(R.id.featured_books_carousel);
        recentBooksGrid = findViewById(R.id.recent_books_grid);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // 设置 RecyclerView
        setupCarousel();
        setupGrid();

        // 加载书籍数据
        loadFeaturedBooks();
        loadRecentBooks();

        // 设置搜索功能
        setupSearch();

        // 设置底部导航
        setupBottomNavigation();
    }

    /**
     * 设置精选书籍轮播
     */
    private void setupCarousel() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        featuredBooksCarousel.setLayoutManager(layoutManager);
        featuredAdapter = new BookAdapter(new ArrayList<>(), this, true);
        featuredBooksCarousel.setAdapter(featuredAdapter);
    }

    /**
     * 设置最新书籍网格
     */
    private void setupGrid() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recentBooksGrid.setLayoutManager(layoutManager);
        recentAdapter = new BookAdapter(new ArrayList<>(), this, false);
        recentBooksGrid.setAdapter(recentAdapter);
    }

    /**
     * 加载精选书籍
     */
    private void loadFeaturedBooks() {
        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadFeaturedBooks);
            return;
        }

        apiService.getBooks(1, 5, null, 0, Double.MAX_VALUE)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> data = response.body();
                            List<Book> books = parseBooks(data);
                            if (books != null && !books.isEmpty()) {
                                featuredAdapter.updateBooks(books);
                            }
                        } else {
                            ErrorHandler.handleApiError(HomeActivity.this, response, HomeActivity.this::loadFeaturedBooks);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        ErrorHandler.handleNetworkError(HomeActivity.this, t, HomeActivity.this::loadFeaturedBooks);
                    }
                });
    }

    /**
     * 加载最新书籍
     */
    private void loadRecentBooks() {
        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadRecentBooks);
            return;
        }

        apiService.getBooks(1, 8, null, 0, Double.MAX_VALUE)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> data = response.body();
                            List<Book> books = parseBooks(data);
                            if (books != null && !books.isEmpty()) {
                                recentAdapter.updateBooks(books);
                            }
                        } else {
                            ErrorHandler.handleApiError(HomeActivity.this, response, HomeActivity.this::loadRecentBooks);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        ErrorHandler.handleNetworkError(HomeActivity.this, t, HomeActivity.this::loadRecentBooks);
                    }
                });
    }

    /**
     * 解析 API 响应中的书籍列表
     */
    private List<Book> parseBooks(Map<String, Object> data) {
        // 这是一个简化的实现，实际应用中应该使用 Gson 进行正确的反序列化
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
                        books.add(book);
                    }
                }
            }
        }
        return books;
    }

    /**
     * 设置搜索功能
     */
    private void setupSearch() {
        appBarWithSearchView.getSearchView().setOnQueryTextListener(
                new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // 跳转到搜索结果
                        Intent intent = new Intent(HomeActivity.this, BrowseActivity.class);
                        intent.putExtra("search_query", query);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
    }

    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_browse) {
                startActivity(new Intent(HomeActivity.this, BrowseActivity.class));
                return true;
            } else if (itemId == R.id.nav_sell) {
                startActivity(new Intent(HomeActivity.this, CreateListingActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, DashboardActivity.class));
                return true;
            }
            return false;
        });
    }
}
