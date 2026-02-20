package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 创建列表 Activity - 允许用户创建新的书籍列表
 */
public class CreateListingActivity extends BaseActivity {

    private static final String TAG = "CreateListingActivity";
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

    private MaterialButton  selectImageButton;
    private ImageView bookImagePreview;

    private ApiService apiService;
    private SessionManager sessionManager;

    private Uri selectedImageUri = null; // 用于保存用户选择的图片URI

    // 新的 API：用于注册启动相册的启动器
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    bookImagePreview.setImageURI(uri);
                    Log.d(TAG, "成功选择图片 URI: " + uri.toString());
                } else {
                    Log.d(TAG, "用户取消了选择图片");
                }
            });



    private void setupListeners() {
        // 点击选择图片
        selectImageButton.setOnClickListener(v -> {
            Log.d(TAG, "打开相册选择图片");
            mGetContent.launch("image/*");
        });

        // 提交按钮
        submitButton.setOnClickListener(v -> {
            if (validateInput()) {
                submitListing();
            }
        });

        // 取消按钮
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * 将 Uri 复制到应用的临时缓存目录，以便 Retrofit 可以作为 File 读取
     */
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        if (fileUri == null) return null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            File tempFile = File.createTempFile("upload_", ".jpg", getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            inputStream.close();

            Log.d(TAG, "临时文件创建成功: " + tempFile.getAbsolutePath());

            // 获取 MimeType
            String mimeType = getContentResolver().getType(fileUri);
            if (mimeType == null) mimeType = "image/jpeg";

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), tempFile);
            return MultipartBody.Part.createFormData(partName, tempFile.getName(), requestFile);
        } catch (Exception e) {
            Log.e(TAG, "图片处理失败，无法生成文件", e);
            return null;
        }
    }

    // 辅助方法：将 String 转换为 RequestBody
    private RequestBody createPartFromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    private void submitListing() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showSnackbar(this, "网络不可用");
            return;
        }

        submitButton.setEnabled(false);
        submitButton.setText("正在发布...");
        Log.d(TAG, "开始准备发布书籍数据...");

        // 获取文本输入
        String title = titleInput.getText().toString().trim();
        String author = authorInput.getText().toString().trim();
        String isbn = isbnInput != null && isbnInput.getText() != null ? isbnInput.getText().toString().trim() : "";
        String price = priceInput.getText().toString().trim();
        String condition = conditionSpinner.getText().toString().trim();
        String description = descriptionInput != null && descriptionInput.getText() != null ? descriptionInput.getText().toString().trim() : "";

        // 1. 构建文本部分的 RequestBody
        RequestBody rTitle = createPartFromString(title);
        RequestBody rAuthor = createPartFromString(author);
        RequestBody rIsbn = createPartFromString(isbn);
        RequestBody rPrice = createPartFromString(price);
        RequestBody rCondition = createPartFromString(condition);
        RequestBody rDesc = createPartFromString(description);

        // 2. 构建图片部分的 MultipartBody.Part
        MultipartBody.Part imagePart = prepareFilePart("image", selectedImageUri);

        Log.d(TAG, "发起网络请求 API -> createBookWithImage");

        // 3. 发送请求
        apiService.createBookWithImage(rTitle, rAuthor, rIsbn, rPrice, rCondition, rDesc, imagePart)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        submitButton.setEnabled(true);
                        submitButton.setText(getString(R.string.submit));

                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "发布成功，服务器返回: " + response.body().toString());
                            Toast.makeText(CreateListingActivity.this, "书籍发布成功！", Toast.LENGTH_SHORT).show();
                            finish(); // 返回上一页
                        } else {
                            Log.e(TAG, "发布失败，HTTP 状态码: " + response.code());
                            ErrorHandler.showSnackbar(CreateListingActivity.this, "发布失败，请重试");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        submitButton.setEnabled(true);
                        submitButton.setText(getString(R.string.submit));
                        Log.e(TAG, "发布请求发生异常: ", t);
                        ErrorHandler.showSnackbar(CreateListingActivity.this, "网络错误: " + t.getMessage());
                    }
                });
    }

    private boolean validateInput() {
        // ... (保留您原来的表单非空验证逻辑) ...
        return true;
    }
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

        setupListeners();
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

        // 新增的图片UI
        bookImagePreview = findViewById(R.id.book_image_preview);
        selectImageButton = findViewById(R.id.select_image_button);
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
