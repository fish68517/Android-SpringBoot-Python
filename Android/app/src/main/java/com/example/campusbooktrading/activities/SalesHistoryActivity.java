package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.TransactionAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Transaction;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 销售历史 Activity - 显示用户销售的所有书籍
 */
public class SalesHistoryActivity extends BaseActivity {

    private MaterialToolbar toolbar;
    private RecyclerView salesRecycler;
    private TextView emptySalesText;
    private ProgressBar loadingProgressBar;
    private BottomNavigationView bottomNavigation;

    private SessionManager sessionManager;
    private ApiService apiService;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();

        // 检查登录状态
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 绑定视图
        bindViews();

        // 设置 Toolbar
        setupToolbar();

        // 设置 RecyclerView
        setupRecyclerView();

        // 设置底部导航
        setupBottomNavigation();

        // 加载销售历史
        loadSalesHistory();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        salesRecycler = findViewById(R.id.sales_recycler);
        emptySalesText = findViewById(R.id.empty_sales_text);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    /**
     * 设置 Toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        salesRecycler.setLayoutManager(layoutManager);
        transactionAdapter = new TransactionAdapter(
                new ArrayList<>(),
                this,
                transaction -> {
                    // 点击交易项目时的处理
                    Toast.makeText(SalesHistoryActivity.this, "Transaction: " + transaction.title, Toast.LENGTH_SHORT).show();
                }
        );
        salesRecycler.setAdapter(transactionAdapter);
    }

    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_browse) {
                startActivity(new Intent(SalesHistoryActivity.this, BrowseActivity.class));
                return true;
            } else if (itemId == R.id.nav_sell) {
                startActivity(new Intent(SalesHistoryActivity.this, CreateListingActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(SalesHistoryActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(SalesHistoryActivity.this, DashboardActivity.class));
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }

    /**
     * 加载销售历史
     */
    private void loadSalesHistory() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "认证令牌缺失");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadSalesHistory);
            return;
        }

        showProgressBar(loadingProgressBar);

        apiService.getSales(token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Transaction> transactions = parseTransactions(data);

                    if (transactions != null && !transactions.isEmpty()) {
                        transactionAdapter.updateTransactions(transactions);
                        emptySalesText.setVisibility(View.GONE);
                    } else {
                        emptySalesText.setVisibility(View.VISIBLE);
                        emptySalesText.setText("暂无销售记录");
                    }
                } else {
                    ErrorHandler.handleApiError(SalesHistoryActivity.this, response, SalesHistoryActivity.this::loadSalesHistory);
                    emptySalesText.setVisibility(View.VISIBLE);
                    emptySalesText.setText("加载销售历史失败");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(SalesHistoryActivity.this, t, SalesHistoryActivity.this::loadSalesHistory);
                emptySalesText.setVisibility(View.VISIBLE);
                emptySalesText.setText("网络错误");
            }
        });
    }

    /**
     * 解析 API 响应中的交易列表
     */
    private List<Transaction> parseTransactions(Map<String, Object> data) {
        List<Transaction> transactions = new ArrayList<>();
        if (data.containsKey("transactions")) {
            Object transactionsObj = data.get("transactions");
            if (transactionsObj instanceof List) {
                List<?> transactionsList = (List<?>) transactionsObj;
                for (Object obj : transactionsList) {
                    if (obj instanceof Map) {
                        Map<?, ?> transactionMap = (Map<?, ?>) obj;
                        Transaction transaction = new Transaction();
                        transaction.id = ((Number) transactionMap.get("id")).intValue();
                        transaction.buyerId = ((Number) transactionMap.get("buyer_id")).intValue();
                        transaction.sellerId = ((Number) transactionMap.get("seller_id")).intValue();
                        transaction.bookId = ((Number) transactionMap.get("book_id")).intValue();
                        transaction.price = ((Number) transactionMap.get("price")).doubleValue();
                        transaction.status = (String) transactionMap.get("status");
                        transaction.deliveryAddress = (String) transactionMap.get("delivery_address");
                        transaction.createdAt = (String) transactionMap.get("created_at");
                        transaction.title = (String) transactionMap.get("title");
                        transaction.author = (String) transactionMap.get("author");
                        transaction.condition = (String) transactionMap.get("condition");
                        transaction.buyerName = (String) transactionMap.get("buyer_name");
                        transaction.buyerEmail = (String) transactionMap.get("buyer_email");
                        transactions.add(transaction);
                    }
                }
            }
        }
        return transactions;
    }

    /**
     * 刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadSalesHistory();
    }
}
