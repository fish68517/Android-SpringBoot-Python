package com.archive.app.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archive.app.R;
import com.archive.app.model.Achievement;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievements = new ArrayList<>();

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement currentAchievement = achievements.get(position);
        holder.name.setText(currentAchievement.getAchieveNameText());
        holder.description.setText(currentAchievement.getAchieveDescriptionText());

        Glide.with(holder.itemView.getContext())
                .load(currentAchievement.getAchieveIconUrl())
                .placeholder(R.mipmap.ic_launcher) // 默认图片
                .into(holder.icon);

        // 如果徽章未获得，设置灰度效果
        // if (!currentAchievement.isEarned()) {
        //     holder.itemView.setAlpha(0.5f);
        // } else {
        //     holder.itemView.setAlpha(1.0f);
        // }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
        notifyDataSetChanged();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView name;
        private final TextView description;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_view_ach_icon);
            name = itemView.findViewById(R.id.text_view_ach_name);
            description = itemView.findViewById(R.id.text_view_ach_description);
        }
    }
}