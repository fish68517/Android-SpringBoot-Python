package com.archive.app.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.archive.app.R;
import com.archive.app.model.NotificationDTO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public interface OnCheckinButtonClickListener {
        void onCheckinButtonClick(NotificationDTO notification);
    }

    private final Context context;
    private final List<NotificationDTO> notifications;
    private final OnCheckinButtonClickListener listener;

    public NotificationAdapter(Context context, List<NotificationDTO> notifications, OnCheckinButtonClickListener listener) {
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationDTO notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    // 当ViewHolder被回收时，取消倒计时，防止内存泄漏和UI错乱
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View statusIndicator;
        TextView tvCourseName, tvTeacherName, tvExpirationTime, tvCountdown, tvCountdownLabel;
        Button btnGoToCheckin;
        CountDownTimer countDownTimer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.view_status_indicator);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);
            tvExpirationTime = itemView.findViewById(R.id.tv_expiration_time);
            tvCountdown = itemView.findViewById(R.id.tv_countdown);
            tvCountdownLabel = itemView.findViewById(R.id.tv_countdown_label);
            btnGoToCheckin = itemView.findViewById(R.id.btn_go_to_checkin);
        }

        public void bind(NotificationDTO notification) {
            tvCourseName.setText(notification.getCourseName());
            tvTeacherName.setText(notification.getTeacherName());
            SimpleDateFormat sdf = new SimpleDateFormat("截止时间: yyyy-MM-dd HH:mm", Locale.getDefault());
            tvExpirationTime.setText(sdf.format(notification.getExpirationTime()));

            // 清理上一个倒计时
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            if ("ACTIVE".equals(notification.getStatus())) {
                configureActiveState(notification);
            } else {
                configureExpiredState();
            }

            btnGoToCheckin.setOnClickListener(v -> listener.onCheckinButtonClick(notification));
        }

        private void configureActiveState(NotificationDTO notification) {
            statusIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
            tvCountdown.setVisibility(View.VISIBLE);
            tvCountdownLabel.setVisibility(View.VISIBLE);
            btnGoToCheckin.setVisibility(View.VISIBLE);

            long millisLeft = notification.getExpirationTime().getTime() - System.currentTimeMillis();
            if (millisLeft > 0) {
                countDownTimer = new CountDownTimer(millisLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                        );
                        tvCountdown.setText(timeLeft);
                    }

                    @Override
                    public void onFinish() {
                        // 倒计时结束后，自动切换为过期状态
                        configureExpiredState();
                    }
                }.start();
            } else {
                configureExpiredState();
            }
        }

        private void configureExpiredState() {
            statusIndicator.setBackgroundColor(Color.GRAY);
            tvCountdown.setText("已结束");
            tvCountdown.setTextSize(18f); // 调整字体大小
            tvCountdownLabel.setVisibility(View.GONE);
            btnGoToCheckin.setVisibility(View.GONE);
        }
    }
}