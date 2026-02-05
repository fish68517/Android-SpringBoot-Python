package com.example.campusbooktrading.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.activities.BookDetailActivity;
import com.example.campusbooktrading.models.Book;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * 书籍适配器 - 用于 RecyclerView 显示书籍列表
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books;
    private Context context;
    private boolean isCarousel;

    public BookAdapter(List<Book> books, Context context, boolean isCarousel) {
        this.books = books;
        this.context = context;
        this.isCarousel = isCarousel;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isCarousel ? R.layout.item_book_carousel : R.layout.item_book_grid;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    /**
     * 更新书籍列表
     */
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 类
     */
    public class BookViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookPrice;
        private TextView bookCondition;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.book_card);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookCondition = itemView.findViewById(R.id.book_condition);
        }

        public void bind(Book book) {
            bookTitle.setText(book.title);
            bookAuthor.setText(book.author);
            bookPrice.setText(String.format("¥%.2f", book.price));
            bookCondition.setText(book.condition);

            // 设置点击监听器，使用 Material 过渡
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("book_id", book.id);

                // 创建 Material 过渡动画
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (android.app.Activity) context,
                        bookImage,
                        ViewCompat.getTransitionName(bookImage)
                );

                context.startActivity(intent, options.toBundle());
            });

            // 设置默认图片（实际应用中应使用 Glide 加载网络图片）
            bookImage.setImageResource(R.drawable.ic_book_placeholder);
            ViewCompat.setTransitionName(bookImage, "book_image_" + book.id);
        }
    }
}
