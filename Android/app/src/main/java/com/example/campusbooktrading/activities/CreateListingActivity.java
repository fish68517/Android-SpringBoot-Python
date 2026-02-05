package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 创建列表 Activity - 允许用户创建新的书籍列表
 */
public class CreateListingActivity extends BaseActivity {

    private TextInputLayout titleLayout;
    private TextInputEditText titleInput;
    private TextInputLayout authorLayout;
    private TextInputEditText authorInput;
    private TextInputLayout isbnLayout;
    private TextInputEditText isbnInput;
    private TextInputLayout priceLayout;
    private TextInputEditText priceInput;
    private TextInputLayout conditionLayout;
    private AutoCompleteTextView conditionSpinner;
    private TextInputLayout descriptionLayout;
    private TextInputEditText descriptionInput;
    private MaterialButton submitButton;
    private MaterialButton cancelButton;
    private ProgressBar loadingProgressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        // 初始化
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // 绑定视图
        bindViews();

        // 设置条件下拉菜单
        setupConditionSpinner();

        // 设置按钮监听器
        setupButtonListeners();

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        titleLayout = findViewById(R.id.title_layout);
        titleInput = findViewById(R.id.title_input);
        authorLayout = findViewById(R.id.author_layout);
        authorInput = findViewById(R.id.author_input);
        isbnLayout = findViewById(R.id.isbn_layout);
        isbnInput = findViewById(R.id.isbn_input);
        priceLayout = findViewById(R.id.price_layout);
        priceInput = findViewById(R.id.price_input);
        conditionLayout = findViewById(R.id.condition_layout);
        conditionSpinner = findViewById(R.id.condition_spinner);
        descriptionLayout = findViewById(R.id.description_layout);
        descriptionInput = findViewById(R.id.description_input);
        submitButton = findViewById(R.id.submit_button);
        cancelButton = findViewById(R.id.cancel_button);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
    }

    /**
     * 设置条件下拉菜单
     */
    private void setupConditionSpinner() {
        String[] conditions = {
                getString(R.string.condition_new),
                getString(R.string.condition_like_new),
                getString(R.string.condition_good),
                getString(R.string.condition_fair),
                getString(R.string.condition_poor)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                conditions
        );
        conditionSpinner.setAdapter(adapter);
        conditionSpinner.setText(conditions[2], false); // 默认选择"良好"
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        submitButton.setOnClickListener(v -> submitListing());
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * 提交列表
     */
    private void submitListing() {
        // 验证表单
        if (!validateForm()) {
            return;
        }

        // 检查登录状态
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::submitListing);
            return;
        }

        // 创建书籍对象
        Book book = new Book();
        book.title = titleInput.getText().toString().trim();
        book.author = authorInput.getText().toString().trim();
        book.isbn = isbnInput.getText().toString().trim();
        book.price = Double.parseDouble(priceInput.getText().toString().trim());
        book.condition = conditionSpinner.getText().toString();
        book.description = descriptionInput.getText().toString().trim();
        book.status = "active";

        // 调用 API 创建列表
        showLoadingDialog("正在创建列表...");
        String token = sessionManager.getAuthorizationHeader();

        apiService.createBook(token, book).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(CreateListingActivity.this, "列表创建成功");
                    // 返回到我的列表页面
                    Intent intent = new Intent(CreateListingActivity.this, MyListingsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ErrorHandler.handleApiError(CreateListingActivity.this, response, CreateListingActivity.this::submitListing);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(CreateListingActivity.this, t, CreateListingActivity.this::submitListing);
            }
        });
    }

    /**
     * 验证表单
     */
    private boolean validateForm() {
        boolean isValid = true;

        // 验证标题
        String title = titleInput.getText().toString().trim();
        if (title.isEmpty()) {
            titleLayout.setError("请输入书籍标题");
            isValid = false;
        } else {
            titleLayout.setError(null);
        }

        // 验证作者
        String author = authorInput.getText().toString().trim();
        if (author.isEmpty()) {
            authorLayout.setError("请输入作者");
            isValid = false;
        } else {
            authorLayout.setError(null);
        }

        // 验证价格
        String priceStr = priceInput.getText().toString().trim();
        if (priceStr.isEmpty()) {
            priceLayout.setError("请输入价格");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    priceLayout.setError("价格必须大于 0");
                    isValid = false;
                } else {
                    priceLayout.setError(null);
                }
            } catch (NumberFormatException e) {
                priceLayout.setError("请输入有效的价格");
                isValid = false;
            }
        }

        // 验证条件
        String condition = conditionSpinner.getText().toString().trim();
        if (condition.isEmpty()) {
            conditionLayout.setError("请选择书籍状况");
            isValid = false;
        } else {
            conditionLayout.setError(null);
        }

        return isValid;
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
