package com.example.campusbooktrading.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.activities.EditListingActivity;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.Book;
import com.example.campusbooktrading.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的列表适配器 - 显示用户的书籍列表
 */
public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.ViewHolder> {

    private List<Book> listings;
    private Context context;
    private ApiService apiService;
    private SessionManager sessionManager;
    private OnListingDeletedListener deleteListener;

    public interface OnListingDeletedListener {
        void onListingDeleted();
    }

    public MyListingsAdapter(List<Book> listings, Context context) {
        this.listings = listings;
        this.context = context;
        this.apiService = ApiClient.getApiService();
        this.sessionManager = new SessionManager(context);
    }

    public void setOnListingDeletedListener(OnListingDeletedListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = listings.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    /**
     * 更新列表数据
     */
    public void updateListings(List<Book> newListings) {
        this.listings = newListings;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookPrice;
        private Chip statusChip;
        private MaterialButton editButton;
        private MaterialButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            statusChip = itemView.findViewById(R.id.status_chip);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(Book book) {
            bookTitle.setText(book.title);
            bookAuthor.setText(book.author);
            bookPrice.setText(String.format("¥%.2f", book.price));

            // 设置状态芯片
            if ("sold".equals(book.status)) {
                statusChip.setText(context.getString(R.string.sold));
                statusChip.setChipBackgroundColorResource(R.color.gray_medium);
            } else {
                statusChip.setText(context.getString(R.string.active));
                statusChip.setChipBackgroundColorResource(R.color.md_theme_primary);
            }

            // 设置默认图片
            bookImage.setImageResource(R.drawable.ic_book_placeholder);

            // 设置编辑按钮
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditListingActivity.class);
                intent.putExtra("book_id", book.id);
                context.startActivity(intent);
            });

            // 设置删除按钮 - 显示确认对话框
            deleteButton.setOnClickListener(v -> showDeleteConfirmation(book));
        }

        /**
         * 显示删除确认对话框
         */
        private void showDeleteConfirmation(Book book) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.delete_listing))
                    .setMessage(context.getString(R.string.delete_listing_confirmation))
                    .setPositiveButton(context.getString(R.string.delete), (dialog, which) -> deleteListing(book))
                    .setNegativeButton(context.getString(R.string.cancel), null)
                    .show();
        }

        /**
         * 删除列表
         */
        private void deleteListing(Book book) {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(context, context.getString(R.string.please_login), Toast.LENGTH_SHORT).show();
                return;
            }

            String token = sessionManager.getAuthorizationHeader();

            apiService.deleteBook(token, book.id).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, context.getString(R.string.listing_deleted), Toast.LENGTH_SHORT).show();
                        listings.remove(book);
                        notifyDataSetChanged();
                        if (deleteListener != null) {
                            deleteListener.onListingDeleted();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.network_error) + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
