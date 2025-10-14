package com.archive.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.archive.app.R;
import com.archive.app.viewmodel.ProfileViewModel;

// 如果您使用图片加载库，比如Glide或Picasso，在这里导入
// import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private ImageView avatarImageView;
    private TextView nicknameTextView, emailTextView, schoolIdTextView;
    private ProgressBar progressBar;
    private View contentLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // 初始化UI
        avatarImageView = view.findViewById(R.id.image_view_avatar);
        nicknameTextView = view.findViewById(R.id.text_view_nickname);
        emailTextView = view.findViewById(R.id.text_view_email);
        schoolIdTextView = view.findViewById(R.id.text_view_school_id);
        progressBar = view.findViewById(R.id.progress_bar_profile);
        contentLayout = view.findViewById(R.id.layout_content);

        observeViewModel();
    }

    private void observeViewModel() {
        profileViewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                nicknameTextView.setText(user.getCampusNickname());
                emailTextView.setText("邮箱：" + user.getCampusEmailAddr());
                schoolIdTextView.setText("学号：" + user.getCampusSchoolId());

                // 使用Glide等库加载网络头像
                // Glide.with(this).load(user.getCampusAvatarUrl()).into(avatarImageView);
            }
        });

        profileViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                contentLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        profileViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}