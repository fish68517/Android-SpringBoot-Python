package com.example.campusbooktrading.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.models.CartItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * 结账项目适配器
 */
public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.CheckoutItemViewHolder> {

    private List<CartItem> items;

    public CheckoutItemAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cardView = (MaterialCardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout, parent, false);
        return new CheckoutItemViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    /**
     * 结账项目 ViewHolder
     */
    static class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView authorView;
        private final TextView priceView;
        private final TextView quantityView;
        private final TextView subtotalView;

        public CheckoutItemViewHolder(@NonNull MaterialCardView itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.item_title);
            authorView = itemView.findViewById(R.id.item_author);
            priceView = itemView.findViewById(R.id.item_price);
            quantityView = itemView.findViewById(R.id.item_quantity);
            subtotalView = itemView.findViewById(R.id.item_subtotal);
        }

        public void bind(CartItem item) {
            titleView.setText(item.title);
            authorView.setText(item.author);
            priceView.setText(String.format("¥%.2f", item.price));
            quantityView.setText(String.format("×%d", item.quantity));
            subtotalView.setText(String.format("¥%.2f", item.getSubtotal()));
        }
    }
}

