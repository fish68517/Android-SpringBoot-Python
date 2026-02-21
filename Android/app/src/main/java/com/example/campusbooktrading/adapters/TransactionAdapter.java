package com.example.campusbooktrading.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.models.Transaction;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 交易适配器 - 用于 RecyclerView 显示交易项目
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private Context context;
    private OnTransactionClickListener listener;

    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }

    public TransactionAdapter(List<Transaction> transactions, Context context, OnTransactionClickListener listener) {
        this.transactions = transactions;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions != null ? transactions.size() : 0;
    }

    /**
     * 更新交易列表
     */
    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 类
     */
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView bookTitle;
        private TextView price;
        private Chip statusChip;
        private TextView date;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.transaction_book_title);
            price = itemView.findViewById(R.id.transaction_price);
            statusChip = itemView.findViewById(R.id.transaction_status);
            date = itemView.findViewById(R.id.transaction_date);
            statusChip.setVisibility(View.GONE);

        }

        public void bind(Transaction transaction) {
            bookTitle.setText(transaction.title != null ? transaction.title : "Unknown Book");
            price.setText(String.format("¥%.2f", transaction.price));
            statusChip.setText(getStatusText(transaction.status));
            statusChip.setChipBackgroundColorResource(getStatusColor(transaction.status));
            date.setText(formatDate(transaction.createdAt));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTransactionClick(transaction);
                }
            });
        }

        /**
         * 获取状态文本
         */
        private String getStatusText(String status) {
            if (status == null) return "Unknown";
            switch (status.toLowerCase()) {
                case "pending":
                    return "Pending";
                case "completed":
                    return "Completed";
                case "cancelled":
                    return "Cancelled";
                default:
                    return status;
            }
        }

        /**
         * 获取状态颜色
         */
        private int getStatusColor(String status) {
            if (status == null) return R.color.md_theme_primary_container;
            switch (status.toLowerCase()) {
                case "pending":
                    return R.color.md_theme_tertiary_container;
                case "completed":
                    return R.color.md_theme_secondary_container;
                case "cancelled":
                    return R.color.md_theme_error_container;
                default:
                    return R.color.md_theme_primary_container;
            }
        }

        /**
         * 格式化日期
         */
        private String formatDate(String dateString) {
            if (dateString == null || dateString.isEmpty()) return "Unknown";
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                return outputFormat.format(date);
            } catch (Exception e) {
                return dateString;
            }
        }
    }
}
