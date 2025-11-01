package com.archive.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.archive.app.R;

public class CustomLoginDialog extends Dialog {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnConfirm;
    private Button btnCancel;

    private OnConfirmListener onConfirmListener;

    public CustomLoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate 自定义布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom_login, null);
        setContentView(view);

        // 初始化控件
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

        // 设置 Dialog 属性（可选：无标题、可取消）
        setTitle(null); // 移除默认标题
        setCancelable(true); // 点击外部可取消
        setCanceledOnTouchOutside(true);

        // 取消按钮
        btnCancel.setOnClickListener(v -> dismiss());

        // 确定按钮
        btnConfirm.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 回调给 Activity 处理
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(username, password);
            }
            dismiss();
        });
    }

    // 设置确认监听器
    public void setOnConfirmListener(OnConfirmListener listener) {
        this.onConfirmListener = listener;
    }

    // 确认回调接口
    public interface OnConfirmListener {
        void onConfirm(String username, String password);
    }
}