package com.archive.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archive.app.R;
import com.archive.app.model.Course;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * 用于在 CourseManagementFragment 中显示课程列表的 RecyclerView.Adapter。
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    /**
     * 定义一个点击事件的回调接口。
     * Fragment/Activity 将实现此接口来接收点击事件。
     */
    public interface OnCourseClickListener {
        /**
         * 当整个课程卡片被点击时调用。
         * @param course 被点击的课程对象
         */
        void onCourseClick(Course course);

        /**
         * 当 "发起签到" 按钮被点击时调用。
         * @param course 对应的课程对象
         */
        void onStartCheckinClick(Course course);
    }

    private final Context context;
    private final List<Course> courseList;
    private final OnCourseClickListener listener;

    /**
     * 构造函数。
     * @param context 上下文
     * @param courseList 课程数据列表
     * @param listener 点击事件的监听器 (通常是 Fragment 本身)
     */
    public CourseAdapter(Context context, List<Course> courseList, OnCourseClickListener listener) {
        this.context = context;
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 从 XML 布局文件 list_item_course.xml 创建 ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取当前位置的课程数据
        Course course = courseList.get(position);
        // 将数据绑定到 ViewHolder 的视图上
        holder.bind(course, listener);
    }

    @Override
    public int getItemCount() {
        // 返回列表中的项目总数
        return courseList.size();
    }

    /**
     * ViewHolder 类，持有列表项的视图。
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // 视图组件的引用
        TextView tvCourseName;
        TextView tvCourseCode;
        MaterialButton btnStartCheckin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 链接 XML 布局中的视图
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvCourseCode = itemView.findViewById(R.id.tv_course_code);
            btnStartCheckin = itemView.findViewById(R.id.btn_start_checkin);
        }

        /**
         * 将课程数据绑定到视图，并设置点击监听器。
         * @param course 要显示的课程对象
         * @param listener 用于处理点击事件的回调接口
         */
        public void bind(final Course course, final OnCourseClickListener listener) {
            // 填充数据
            tvCourseName.setText(course.getCourseName());
            tvCourseCode.setText(course.getCourseCode());
            btnStartCheckin.setVisibility(View.GONE);

            // --- 设置点击事件 ---

            // 1. 整个卡片的点击事件
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            });

            // 2. "发起签到" 按钮的点击事件
            btnStartCheckin.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStartCheckinClick(course);
                }
            });
        }
    }
}