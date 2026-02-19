package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.CartAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.CartItem;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 购物车 Activity - 显示购物车项目、计算总价、结账
 */
public class CartActivity extends BaseActivity implements CartAdapter.OnCartItemListener {

    private RecyclerView cartItemsRecycler;
    private LinearLayout cartContent;
    private LinearLayout emptyCartState;
    private TextView subtotalValue;
    private TextView taxValue;
    private TextView totalValue;
    private MaterialButton checkoutBtn;
    private MaterialButton continueShoppingBtn;
    private MaterialButton emptyShoppingBtn;
    private BottomNavigationView bottomNavigation;
    private AppBarWithSearchView appBar;
    private ProgressBar loadingProgressBar;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private SessionManager sessionManager;
    private ApiService apiService;

    private static final double TAX_RATE = 0.0; // 0% tax for campus trading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();
        cartItems = new ArrayList<>();

        // 绑定视图
        bindViews();

        // 设置 RecyclerView
        setupRecyclerView();

        // 加载购物车数据
        loadCartItems();

        // 设置按钮监听器
        setupButtonListeners();

        // 设置底部导航
        setupBottomNavigation();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        appBar = findViewById(R.id.app_bar);
        cartItemsRecycler = findViewById(R.id.cart_items_recycler);
        cartContent = findViewById(R.id.cart_content);
        emptyCartState = findViewById(R.id.empty_cart_state);
        subtotalValue = findViewById(R.id.subtotal_value);
        taxValue = findViewById(R.id.tax_value);
        totalValue = findViewById(R.id.total_value);
        checkoutBtn = findViewById(R.id.checkout_btn);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        emptyShoppingBtn = findViewById(R.id.empty_continue_shopping_btn);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(cartItems, this, this);
        cartItemsRecycler.setAdapter(cartAdapter);
    }

    /**
     * 加载购物车数据
     */
    private void loadCartItems() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            showEmptyCart();
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadCartItems);
            showEmptyCart();
            return;
        }

        showProgressBar(loadingProgressBar);

        apiService.getCart(token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<CartItem> items = parseCartItems(data);
                    if (items != null && !items.isEmpty()) {
                        cartItems.clear();
                        cartItems.addAll(items);
                        cartAdapter.updateCartItems(cartItems);
                        showCartContent();
                        updateTotals();
                    } else {
                        showEmptyCart();
                    }
                } else {
                    ErrorHandler.handleApiError(CartActivity.this, response, CartActivity.this::loadCartItems);
                    showEmptyCart();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(CartActivity.this, t, CartActivity.this::loadCartItems);
                showEmptyCart();
            }
        });
    }

    /**
     * 解析购物车项目
     */
    private List<CartItem> parseCartItems(Map<String, Object> data) {
        List<CartItem> items = new ArrayList<>();
        if (data.containsKey("items")) {
            Object itemsObj = data.get("items");
            if (itemsObj instanceof List) {
                List<?> itemsList = (List<?>) itemsObj;
                for (Object obj : itemsList) {
                    if (obj instanceof Map) {
                        Map<?, ?> itemMap = (Map<?, ?>) obj;
                        CartItem item = new CartItem();
                        item.id = ((Number) itemMap.get("id")).intValue();
                        item.bookId = ((Number) itemMap.get("book_id")).intValue();
                        item.quantity = ((Number) itemMap.get("quantity")).intValue();
                        item.title = (String) itemMap.get("title");
                        item.author = (String) itemMap.get("author");
                        item.price = ((Number) itemMap.get("price")).doubleValue();
                        item.condition = (String) itemMap.get("condition");
                        items.add(item);
                    }
                }
            }
        }
        return items;
    }

    /**
     * 显示购物车内容
     */
    private void showCartContent() {
        cartContent.setVisibility(View.VISIBLE);
        emptyCartState.setVisibility(View.GONE);
    }

    /**
     * 显示空购物车状态
     */
    private void showEmptyCart() {
        cartContent.setVisibility(View.GONE);
        emptyCartState.setVisibility(View.VISIBLE);
    }

    /**
     * 更新总价
     */
    private void updateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
        }

        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalValue.setText(String.format("¥%.2f", subtotal));
        taxValue.setText(String.format("¥%.2f", tax));
        totalValue.setText(String.format("¥%.2f", total));
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                ErrorHandler.showSnackbar(this, "购物车为空");
                return;
            }
            // 跳转到结账页面
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
        });

        continueShoppingBtn.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, BrowseActivity.class));
            finish();
        });

        emptyShoppingBtn.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, BrowseActivity.class));
            finish();
        });
    }

    /**
     * 设置底部导航
     */
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_browse) {
                startActivity(new Intent(CartActivity.this, BrowseActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_sell) {
                startActivity(new Intent(CartActivity.this, CreateListingActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(CartActivity.this, DashboardActivity.class));
                finish();
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_cart);
    }

    /**
     * 购物车项目数量改变回调
     */
    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, null);
            return;
        }

        item.quantity = newQuantity;
        apiService.updateCartItem(token, item.id, item)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            updateTotals();
                        } else {
                            ErrorHandler.handleApiError(CartActivity.this, response, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        ErrorHandler.handleNetworkError(CartActivity.this, t, null);
                    }
                });
    }

    /**
     * 移除购物车项目回调
     */
    @Override
    public void onRemoveItem(CartItem item) {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, null);
            return;
        }

        apiService.removeFromCart(token, item.id)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            cartItems.remove(item);
                            cartAdapter.updateCartItems(cartItems);
                            if (cartItems.isEmpty()) {
                                showEmptyCart();
                            } else {
                                updateTotals();
                            }
                            ErrorHandler.showSnackbar(CartActivity.this, "已从购物车移除");
                        } else {
                            ErrorHandler.handleApiError(CartActivity.this, response, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        ErrorHandler.handleNetworkError(CartActivity.this, t, null);
                    }
                });
    }
}
