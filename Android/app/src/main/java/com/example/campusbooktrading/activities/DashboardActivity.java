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
import com.example.campusbooktrading.models.User;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户仪表板 Activity - 显示用户个人资料、统计数据和最近交易
 */
public class DashboardActivity extends BaseActivity {

    private MaterialToolbar appBar;
    private MaterialCardView profileCard;
    private TextView profileUsername;
    private TextView profileEmail;
    private TextView profileStudentId;
    private TextView activeListingsCount;
    private TextView purchasesCount;
    private TextView salesCount;
    private RecyclerView recentTransactionsRecycler;
    private TextView emptyTransactionsText;
    private MaterialButton myListingsBtn;
    private MaterialButton purchaseHistoryBtn;
    private MaterialButton salesHistoryBtn;
    private MaterialButton editProfileBtn;
    private MaterialButton logoutBtn;
    private ProgressBar loadingProgressBar;
    private BottomNavigationView bottomNavigation;

    private SessionManager sessionManager;
    private ApiService apiService;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

        // 设置 RecyclerView
        setupRecyclerView();

        // 设置按钮监听器
        setupButtonListeners();

        // 设置底部导航
        setupBottomNavigation();

        // 加载数据
        loadUserProfile();
        loadRecentTransactions();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        appBar = findViewById(R.id.app_bar);
        profileCard = findViewById(R.id.profile_card);
        profileUsername = findViewById(R.id.profile_username);
        profileEmail = findViewById(R.id.profile_email);
        profileStudentId = findViewById(R.id.profile_student_id);
        activeListingsCount = findViewById(R.id.active_listings_count);
        purchasesCount = findViewById(R.id.purchases_count);
        salesCount = findViewById(R.id.sales_count);
        recentTransactionsRecycler = findViewById(R.id.recent_transactions_recycler);
        emptyTransactionsText = findViewById(R.id.empty_transactions_text);
        myListingsBtn = findViewById(R.id.my_listings_btn);
        purchaseHistoryBtn = findViewById(R.id.purchase_history_btn);
        salesHistoryBtn = findViewById(R.id.sales_history_btn);
        editProfileBtn = findViewById(R.id.edit_profile_btn);
        logoutBtn = findViewById(R.id.logout_btn);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recentTransactionsRecycler.setLayoutManager(layoutManager);
        transactionAdapter = new TransactionAdapter(
                new ArrayList<>(),
                this,
                transaction -> {
                    // 点击交易项目时的处理
                    Toast.makeText(DashboardActivity.this, "Transaction: " + transaction.title, Toast.LENGTH_SHORT).show();
                }
        );
        recentTransactionsRecycler.setAdapter(transactionAdapter);
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        myListingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyListingsActivity.class);
            startActivity(intent);
        });

        purchaseHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PurchaseHistoryActivity.class);
            startActivity(intent);
        });

        salesHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SalesHistoryActivity.class);
            startActivity(intent);
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            performLogout();
        });
    }

    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_browse) {
                startActivity(new Intent(DashboardActivity.this, BrowseActivity.class));
                return true;
            } else if (itemId == R.id.nav_sell) {
                startActivity(new Intent(DashboardActivity.this, CreateListingActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(DashboardActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }

    /**
     * 加载用户个人资料
     */
    private void loadUserProfile() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "认证令牌缺失");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadUserProfile);
            return;
        }

        apiService.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    displayUserProfile(user);
                } else {
                    ErrorHandler.handleApiError(DashboardActivity.this, response, DashboardActivity.this::loadUserProfile);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorHandler.handleNetworkError(DashboardActivity.this, t, DashboardActivity.this::loadUserProfile);
            }
        });
    }

    /**
     * 显示用户个人资料
     */
    private void displayUserProfile(User user) {
        profileUsername.setText(user.username);
        profileEmail.setText(user.email);
        profileStudentId.setText("学号: " + user.studentId);

        // 更新统计数据
        activeListingsCount.setText(String.valueOf(user.activeListings));
        purchasesCount.setText(String.valueOf(user.purchases));
        salesCount.setText(String.valueOf(user.sales));
    }

    /**
     * 加载最近交易
     */
    private void loadRecentTransactions() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "认证令牌缺失");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadRecentTransactions);
            return;
        }

        showProgressBar(loadingProgressBar);

        // 获取购买历史
        apiService.getPurchases(token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<Transaction> transactions = parseTransactions(data);

                    if (transactions != null && !transactions.isEmpty()) {
                        // 只显示最近 5 条交易
                        List<Transaction> recentTransactions = transactions.subList(
                                0,
                                Math.min(5, transactions.size())
                        );
                        transactionAdapter.updateTransactions(recentTransactions);
                        emptyTransactionsText.setVisibility(View.GONE);
                    } else {
                        emptyTransactionsText.setVisibility(View.VISIBLE);
                        emptyTransactionsText.setText("暂无交易");
                    }
                } else {
                    ErrorHandler.handleApiError(DashboardActivity.this, response, DashboardActivity.this::loadRecentTransactions);
                    emptyTransactionsText.setVisibility(View.VISIBLE);
                    emptyTransactionsText.setText("加载交易失败");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(DashboardActivity.this, t, DashboardActivity.this::loadRecentTransactions);
                emptyTransactionsText.setVisibility(View.VISIBLE);
                emptyTransactionsText.setText("网络错误");
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
        loadUserProfile();
        loadRecentTransactions();
    }
}
