package com.example.campusbooktrading.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.models.CartItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.iconbutton.MaterialIconButton;

import java.util.List;

/**
 * 购物车适配器 - 用于 RecyclerView 显示购物车项目
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartItemListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    /**
     * 更新购物车项目列表
     */
    public void updateCartItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 类
     */
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookPrice;
        private TextView bookCondition;
        private TextView quantityText;
        private MaterialButton decreaseBtn;
        private MaterialButton increaseBtn;
        private MaterialIconButton removeBtn;
        private TextView subtotalText;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cart_item_card);
            bookImage = itemView.findViewById(R.id.cart_book_image);
            bookTitle = itemView.findViewById(R.id.cart_book_title);
            bookAuthor = itemView.findViewById(R.id.cart_book_author);
            bookPrice = itemView.findViewById(R.id.cart_book_price);
            bookCondition = itemView.findViewById(R.id.cart_book_condition);
            quantityText = itemView.findViewById(R.id.quantity_text);
            decreaseBtn = itemView.findViewById(R.id.decrease_btn);
            increaseBtn = itemView.findViewById(R.id.increase_btn);
            removeBtn = itemView.findViewById(R.id.remove_btn);
            subtotalText = itemView.findViewById(R.id.subtotal_text);
        }

        public void bind(CartItem item) {
            bookTitle.setText(item.title);
            bookAuthor.setText(item.author);
            bookPrice.setText(String.format("¥%.2f", item.price));
            bookCondition.setText(item.condition);
            quantityText.setText(String.valueOf(item.quantity));
            subtotalText.setText(String.format("¥%.2f", item.getSubtotal()));

            // 设置默认图片
            bookImage.setImageResource(R.drawable.ic_book_placeholder);

            // 减少数量按钮
            decreaseBtn.setOnClickListener(v -> {
                if (item.quantity > 1) {
                    item.quantity--;
                    quantityText.setText(String.valueOf(item.quantity));
                    subtotalText.setText(String.format("¥%.2f", item.getSubtotal()));
                    if (listener != null) {
                        listener.onQuantityChanged(item, item.quantity);
                    }
                }
            });

            // 增加数量按钮
            increaseBtn.setOnClickListener(v -> {
                item.quantity++;
                quantityText.setText(String.valueOf(item.quantity));
                subtotalText.setText(String.format("¥%.2f", item.getSubtotal()));
                if (listener != null) {
                    listener.onQuantityChanged(item, item.quantity);
                }
            });

            // 移除按钮
            removeBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(item);
                }
            });
        }
    }
}
