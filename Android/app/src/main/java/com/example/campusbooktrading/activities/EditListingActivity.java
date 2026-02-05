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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 编辑列表 Activity - 允许用户编辑现有的书籍列表
 */
public class EditListingActivity extends BaseActivity {

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
    private MaterialButton updateButton;
    private MaterialButton deleteButton;
    private MaterialButton cancelButton;
    private ProgressBar loadingProgressBar;

    private ApiService apiService;
    private SessionManager sessionManager;
    private Book currentBook;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        // 初始化
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // 绑定视图
        bindViews();

        // 设置条件下拉菜单
        setupConditionSpinner();

        // 设置按钮监听器
        setupButtonListeners();

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
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
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
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        updateButton.setOnClickListener(v -> updateListing());
        deleteButton.setOnClickListener(v -> showDeleteConfirmation());
        cancelButton.setOnClickListener(v -> finish());
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
                    populateForm(currentBook);
                } else {
                    ErrorHandler.handleApiError(EditListingActivity.this, response, EditListingActivity.this::loadBookDetail);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(EditListingActivity.this, t, EditListingActivity.this::loadBookDetail);
            }
        });
    }

    /**
     * 填充表单数据
     */
    private void populateForm(Book book) {
        titleInput.setText(book.title);
        authorInput.setText(book.author);
        isbnInput.setText(book.isbn != null ? book.isbn : "");
        priceInput.setText(String.valueOf(book.price));
        conditionSpinner.setText(book.condition, false);
        descriptionInput.setText(book.description != null ? book.description : "");
    }

    /**
     * 更新列表
     */
    private void updateListing() {
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
            ErrorHandler.showNetworkErrorDialog(this, this::updateListing);
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

        // 调用 API 更新列表
        showLoadingDialog("正在更新列表...");
        String token = sessionManager.getAuthorizationHeader();

        apiService.updateBook(token, bookId, book).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(EditListingActivity.this, "列表更新成功");
                    // 返回到我的列表页面
                    Intent intent = new Intent(EditListingActivity.this, MyListingsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ErrorHandler.handleApiError(EditListingActivity.this, response, EditListingActivity.this::updateListing);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(EditListingActivity.this, t, EditListingActivity.this::updateListing);
            }
        });
    }

    /**
     * 显示删除确认对话框
     */
    private void showDeleteConfirmation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("删除列表")
                .setMessage("确定要删除这个列表吗？此操作无法撤销。")
                .setPositiveButton("删除", (dialog, which) -> deleteListing())
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 删除列表
     */
    private void deleteListing() {
        // 检查登录状态
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::deleteListing);
            return;
        }

        // 调用 API 删除列表
        showLoadingDialog("正在删除列表...");
        String token = sessionManager.getAuthorizationHeader();

        apiService.deleteBook(token, bookId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(EditListingActivity.this, "列表已删除");
                    // 返回到我的列表页面
                    Intent intent = new Intent(EditListingActivity.this, MyListingsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ErrorHandler.handleApiError(EditListingActivity.this, response, EditListingActivity.this::deleteListing);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(EditListingActivity.this, t, EditListingActivity.this::deleteListing);
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
