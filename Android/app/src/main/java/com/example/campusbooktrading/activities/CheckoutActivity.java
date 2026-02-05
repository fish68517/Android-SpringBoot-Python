package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.adapters.CheckoutItemAdapter;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.CartItem;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.appbarwithsearchview.AppBarWithSearchView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 结账 Activity - 显示订单确认、收货地址、支付方式选择
 */
public class CheckoutActivity extends BaseActivity {

    private AppBarWithSearchView appBar;
    private RecyclerView orderItemsRecycler;
    private TextView orderTotalValue;
    private TextInputEditText deliveryAddressInput;
    private RadioGroup paymentMethodGroup;
    private MaterialButton cancelBtn;
    private MaterialButton placeOrderBtn;

    private CheckoutItemAdapter checkoutAdapter;
    private List<CartItem> orderItems;
    private SessionManager sessionManager;
    private ApiService apiService;

    private static final double TAX_RATE = 0.0; // 0% tax for campus trading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();
        orderItems = new ArrayList<>();

        // 绑定视图
        bindViews();

        // 设置 RecyclerView
        setupRecyclerView();

        // 加载购物车数据
        loadCartItems();

        // 设置按钮监听器
        setupButtonListeners();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        appBar = findViewById(R.id.app_bar);
        orderItemsRecycler = findViewById(R.id.order_items_recycler);
        orderTotalValue = findViewById(R.id.order_total_value);
        deliveryAddressInput = findViewById(R.id.delivery_address_input);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        cancelBtn = findViewById(R.id.cancel_btn);
        placeOrderBtn = findViewById(R.id.place_order_btn);
    }

    /**
     * 设置 RecyclerView
     */
    private void setupRecyclerView() {
        checkoutAdapter = new CheckoutItemAdapter(orderItems);
        orderItemsRecycler.setAdapter(checkoutAdapter);
    }

    /**
     * 加载购物车数据
     */
    private void loadCartItems() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "未登录");
            finish();
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadCartItems);
            finish();
            return;
        }

        showLoadingDialog("正在加载购物车...");

        apiService.getCart(token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    List<CartItem> items = parseCartItems(data);
                    if (items != null && !items.isEmpty()) {
                        orderItems.clear();
                        orderItems.addAll(items);
                        checkoutAdapter.updateItems(orderItems);
                        updateOrderTotal();
                    } else {
                        ErrorHandler.showSnackbar(CheckoutActivity.this, "购物车为空");
                        finish();
                    }
                } else {
                    ErrorHandler.handleApiError(CheckoutActivity.this, response, CheckoutActivity.this::loadCartItems);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(CheckoutActivity.this, t, CheckoutActivity.this::loadCartItems);
                finish();
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
     * 更新订单总价
     */
    private void updateOrderTotal() {
        double total = 0;
        for (CartItem item : orderItems) {
            total += item.getSubtotal();
        }
        orderTotalValue.setText(String.format("¥%.2f", total));
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        placeOrderBtn.setOnClickListener(v -> {
            placeOrder();
        });
    }

    /**
     * 下单
     */
    private void placeOrder() {
        // 验证收货地址
        String deliveryAddress = deliveryAddressInput.getText().toString().trim();
        if (deliveryAddress.isEmpty()) {
            ErrorHandler.showSnackbar(this, "请输入收货地址");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::placeOrder);
            return;
        }

        // 获取支付方式
        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "cash";
        if (selectedPaymentId == R.id.payment_alipay) {
            paymentMethod = "alipay";
        } else if (selectedPaymentId == R.id.payment_wechat) {
            paymentMethod = "wechat";
        }

        // 创建订单
        createTransactions(deliveryAddress, paymentMethod);
    }

    /**
     * 创建交易
     */
    private void createTransactions(String deliveryAddress, String paymentMethod) {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "未登录");
            return;
        }

        showLoadingDialog("正在处理订单...");

        // 为每个购物车项目创建交易
        for (CartItem item : orderItems) {
            Map<String, String> request = new HashMap<>();
            request.put("book_id", String.valueOf(item.bookId));
            request.put("delivery_address", deliveryAddress);
            request.put("payment_method", paymentMethod);

            apiService.createTransaction(token, request)
                    .enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            if (response.isSuccessful()) {
                                // 交易创建成功，继续处理下一个
                                handleTransactionSuccess();
                            } else {
                                hideLoadingDialog();
                                ErrorHandler.handleApiError(CheckoutActivity.this, response, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            hideLoadingDialog();
                            ErrorHandler.handleNetworkError(CheckoutActivity.this, t, null);
                        }
                    });
        }
    }

    /**
     * 处理交易成功
     */
    private void handleTransactionSuccess() {
        // 清空购物车
        String token = sessionManager.getAuthorizationHeader();
        if (!token.isEmpty()) {
            apiService.clearCart(token).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    hideLoadingDialog();
                    showOrderSuccessDialog();
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    hideLoadingDialog();
                    showOrderSuccessDialog();
                }
            });
        } else {
            hideLoadingDialog();
            showOrderSuccessDialog();
        }
    }

    /**
     * 显示订单成功对话框
     */
    private void showOrderSuccessDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.order_success)
                .setMessage(R.string.order_placed_successfully)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    // 导航到仪表板
                    Intent intent = new Intent(CheckoutActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}

