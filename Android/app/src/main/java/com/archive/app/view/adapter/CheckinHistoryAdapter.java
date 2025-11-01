package com.archive.app.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.archive.app.R;
import com.archive.app.model.CheckinRecord;
import com.archive.app.view.activity.CheckinHistoryDetailActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckinHistoryAdapter extends RecyclerView.Adapter<CheckinHistoryAdapter.ViewHolder> {

    private final List<CheckinRecord> records;
    private final Context context;
    // 创建一个线程池用于执行地理位置解析任务
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public CheckinHistoryAdapter(Context context, List<CheckinRecord> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_checkin_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckinRecord record = records.get(position);

        // 设置课程ID (在真实应用中你可能需要根据courseId查询课程名称)
        holder.tvCourse.setText("课程ID: " + record.getCourseId());

        // --- 新增：为列表项设置点击事件 ---
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CheckinHistoryDetailActivity.class);
            // 将 CheckinRecord 对象通过 Intent 传递过去
            // CheckinRecord 必须实现 Serializable 接口 (你的代码已经实现了)
            intent.putExtra("CHECKIN_RECORD", record);
            context.startActivity(intent);
        });

        // 格式化并设置签到时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.tvTime.setText(sdf.format(record.getCheckinTime()));

        // 设置签到状态和颜色
        setStatus(holder.tvStatus, record.getStatus());

        // 解析并设置地理位置
        getAddressFromLatLng(holder.tvLocation, record.getLatitude(), record.getLongitude());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private void setStatus(TextView tvStatus, String status) {
        if (status == null) status = "未知";
        tvStatus.setText(status);
        int colorResId;
        switch (status) {
            case "成功":
                colorResId = R.color.status_success;
                break;
            case "迟到":
                colorResId = R.color.status_late;
                break;
            case "缺勤":
                colorResId = R.color.status_absent;
                break;
            default:
                colorResId = R.color.status_default;
                break;
        }

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(16); // 设置圆角
        background.setColor(ContextCompat.getColor(context, colorResId));
        tvStatus.setBackground(background);
    }

    private void getAddressFromLatLng(TextView tvLocation, double latitude, double longitude) {
        tvLocation.setText("正在解析地理位置...");
        // 在后台线程中执行网络请求
        executorService.execute(() -> {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            String addressText = "无法获取位置信息";
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // 构建更详细的地址字符串
                    addressText = address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            final String finalAddressText = addressText;
            // 回到主线程更新UI
            new Handler(Looper.getMainLooper()).post(() -> tvLocation.setText(finalAddressText));
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourse, tvTime, tvLocation, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}