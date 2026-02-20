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
 * 购买历史 Activity - 显示用户购买的所有书籍
 */
public class PurchaseHistoryActivity extends BaseActivity {

    private MaterialToolbar toolbar;
    private RecyclerView purchasesRecycler;
    private TextView emptyPurchasesText;
    private ProgressBar loadingProgressBar;
    private BottomNavigationView bottomNavigation;

    private SessionManager sessionManager;
    private ApiService apiService;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

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

        // 加载购买历史
        loadPurchaseHistory();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        purchasesRecycler = findViewById(R.id.purchases_recycler);
        emptyPurchasesText = findViewById(R.id.empty_purchases_text);
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
        purchasesRecycler.setLayoutManager(layoutManager);
        transactionAdapter = new TransactionAdapter(
                new ArrayList<>(),
                this,
                transaction -> {
                    // 点击交易项目时的处理
                    Toast.makeText(PurchaseHistoryActivity.this, "Transaction: " + transaction.title, Toast.LENGTH_SHORT).show();
                }
        );
        purchasesRecycler.setAdapter(transactionAdapter);
    }

    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_browse) {
                startActivity(new Intent(PurchaseHistoryActivity.this, BrowseActivity.class));
                return true;
            } else if (itemId == R.id.nav_sell) {
                startActivity(new Intent(PurchaseHistoryActivity.this, CreateListingActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(PurchaseHistoryActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(PurchaseHistoryActivity.this, DashboardActivity.class));
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }

    /**
     * 加载购买历史
     */
    private void loadPurchaseHistory() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "认证令牌缺失");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadPurchaseHistory);
            return;
        }

        showProgressBar(loadingProgressBar);

        apiService.getPurchases(token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Transaction> transactions = parseTransactions(data);

                    if (transactions != null && !transactions.isEmpty()) {
                        transactionAdapter.updateTransactions(transactions);
                        emptyPurchasesText.setVisibility(View.GONE);
                    } else {
                        emptyPurchasesText.setVisibility(View.VISIBLE);
                        emptyPurchasesText.setText("暂无购买记录");
                    }
                } else {
                    ErrorHandler.handleApiError(PurchaseHistoryActivity.this, response, PurchaseHistoryActivity.this::loadPurchaseHistory);
                    emptyPurchasesText.setVisibility(View.VISIBLE);
                    emptyPurchasesText.setText("加载购买历史失败");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(PurchaseHistoryActivity.this, t, PurchaseHistoryActivity.this::loadPurchaseHistory);
                emptyPurchasesText.setVisibility(View.VISIBLE);
                emptyPurchasesText.setText("网络错误");
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
                        transaction.sellerName = (String) transactionMap.get("seller_name");
                        transaction.sellerEmail = (String) transactionMap.get("seller_email");
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
        loadPurchaseHistory();
    }
}
