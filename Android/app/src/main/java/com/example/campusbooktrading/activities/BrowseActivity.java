package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.BookAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 浏览书籍 Activity - 显示书籍列表，支持搜索、筛选和排序
 */
public class BrowseActivity extends BaseActivity {

    private RecyclerView booksRecyclerView;
    private ProgressBar loadingProgressBar;
    private BookAdapter bookAdapter;
    private ApiService apiService;
    private ChipGroup conditionChipGroup;
    private ChipGroup sortChipGroup;

    private List<Book> allBooks = new ArrayList<>();
    private int currentPage = 1;
    private int pageSize = 10;
    private String currentSearchQuery = "";
    private String selectedCondition = null;
    private String selectedSort = "newest";
    private double minPrice = 0;
    private double maxPrice = Double.MAX_VALUE;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        // 初始化
        apiService = ApiClient.getApiService();

        // 绑定视图
        booksRecyclerView = findViewById(R.id.books_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        conditionChipGroup = findViewById(R.id.condition_chip_group);
        sortChipGroup = findViewById(R.id.sort_chip_group);

        // 设置 RecyclerView
        setupRecyclerView();

        // 设置筛选和排序
        setupFiltersAndSort();

        // 检查是否有搜索查询
        String searchQuery = getIntent().getStringExtra("search_query");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            currentSearchQuery = searchQuery;
            loadBooks(true);
        } else {
            loadBooks(true);
        }

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        booksRecyclerView.setLayoutManager(layoutManager);
        bookAdapter = new BookAdapter(new ArrayList<>(), this, false);
        booksRecyclerView.setAdapter(bookAdapter);

        // 设置无限滚动
        booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2) {
                        currentPage++;
                        loadBooks(false);
                    }
                }
            }
        });
    }

    /**
     * 设置筛选和排序选项
     */
    /**
     * 设置筛选和排序选项
     */
    private void setupFiltersAndSort() {
        // 设置条件筛选
        conditionChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String newCondition = null;
            if (!checkedIds.isEmpty()) {
                Chip chip = findViewById(checkedIds.get(0));
                newCondition = chip.getText().toString();
            }

            // 【关键修复】判断新选中的状态和之前的是否一样。如果一样，直接 return 结束，不发请求
            if (selectedCondition == null) {
                if (newCondition == null) return;
            } else if (selectedCondition.equals(newCondition)) {
                return;
            }

            System.out.println("Condition 真正发生改变: " + newCondition);
            selectedCondition = newCondition;
            currentPage = 1;
            allBooks.clear();
            loadBooks(true);
        });

        // 设置排序选项
        sortChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String newSort = "newest"; // 默认排序
            if (!checkedIds.isEmpty()) {
                Chip chip = findViewById(checkedIds.get(0));
                String sortText = chip.getText().toString();

                if (sortText.equals("最新")) {
                    newSort = "newest";
                } else if (sortText.equals("价格低到高")) {
                    newSort = "price_asc";
                } else if (sortText.equals("价格高到低")) {
                    newSort = "price_desc";
                }
            }

            // 【关键修复】判断排序方式是否真正改变
            if (selectedSort.equals(newSort)) {
                return;
            }

            System.out.println("Sort 真正发生改变: " + newSort);
            selectedSort = newSort;
            currentPage = 1;
            allBooks.clear();
            loadBooks(true);
        });
    }

    /**
     * 加载书籍列表
     */
    private void loadBooks(boolean isRefresh) {
        if (isLoading) return;

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, () -> loadBooks(isRefresh));
            return;
        }

        isLoading = true;
        loadingProgressBar.setVisibility(android.view.View.VISIBLE);

        Call<Map<String, Object>> call;

        System.out.println("Current Search Query: " + currentSearchQuery);
        if (!currentSearchQuery.isEmpty()) {
            call = apiService.searchBooks(currentSearchQuery);
        } else {
            if (selectedCondition == null  || selectedCondition.equals("全部")) {
                call = apiService.getBooks(1, pageSize, null, 0, Double.MAX_VALUE);

            } else {
                call = apiService.getBooks(1, pageSize, selectedCondition, minPrice, maxPrice);
            }

        }

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                isLoading = false;
                loadingProgressBar.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Book> books = parseBooks(data);

                    System.out.println("Books siez: " + books.size());
                    allBooks.clear();

                    if (books != null ) {
                        // 应用排序
                        sortBooks(books);
                        allBooks.addAll(books);
                        bookAdapter.updateBooks(allBooks);
                    }
                } else {
                    ErrorHandler.handleApiError(BrowseActivity.this, response, () -> loadBooks(isRefresh));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                isLoading = false;
                loadingProgressBar.setVisibility(android.view.View.GONE);
                ErrorHandler.handleNetworkError(BrowseActivity.this, t, () -> loadBooks(isRefresh));
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
     * 根据选定的排序方式排序书籍
     */
    private void sortBooks(List<Book> books) {
        if (selectedSort.equals("price_asc")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                books.sort((b1, b2) -> Double.compare(b1.price, b2.price));
            }
        } else if (selectedSort.equals("price_desc")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                books.sort((b1, b2) -> Double.compare(b2.price, b1.price));
            }
        }
        // "newest" 是默认排序，不需要额外处理
    }

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                currentPage = 1;
                allBooks.clear();
                loadBooks(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    /**
     * 处理菜单项点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
