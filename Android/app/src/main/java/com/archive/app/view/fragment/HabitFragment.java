package com.archive.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archive.app.R;
import com.archive.app.view.adapter.HabitAdapter;
import com.archive.app.viewmodel.HabitViewModel;


public class HabitFragment extends Fragment {

    private HabitViewModel habitViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HabitAdapter habitAdapter; // 需要您创建HabitAdapter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_habit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        recyclerView = view.findViewById(R.id.recycler_view_habits);
        progressBar = view.findViewById(R.id.progress_bar_habits);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        habitAdapter = new HabitAdapter(); // 假设HabitAdapter已创建
        recyclerView.setAdapter(habitAdapter);
    }

    private void observeViewModel() {
        habitViewModel.getUserHabits().observe(getViewLifecycleOwner(), habits -> {
            if (habits != null) {
                habitAdapter.setHabits(habits);
            }
        });

        habitViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        habitViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}