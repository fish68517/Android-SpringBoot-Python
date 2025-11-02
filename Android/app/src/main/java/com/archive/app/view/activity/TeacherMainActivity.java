package com.archive.app.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.archive.app.MyApplication;
import com.archive.app.R;
import com.archive.app.model.User;
import com.archive.app.view.fragment.CourseManagementFragment;
import com.archive.app.view.fragment.CourseMgmtFragment;
import com.archive.app.view.fragment.StartCheckinFragment;
import com.archive.app.view.fragment.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;

public class TeacherMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_teacher);
        navigationView.setNavigationItemSelectedListener(this);

        // 设置 DrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 设置导航栏头部信息
        User currentUser = MyApplication.curUser;
        if (currentUser != null) {
            TextView headerName = navigationView.getHeaderView(0).findViewById(R.id.nav_header_teacher_name);
            TextView headerEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_header_teacher_email);
            headerName.setText("教师: " + currentUser.getUsername());
            headerEmail.setText(currentUser.getEmail()); // 假设 User 有 getEmail
        }

        // 默认加载第一个 Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_teacher,
                    new CourseManagementFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_course_mgmt);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_course_mgmt) {
            selectedFragment = new CourseManagementFragment();
        } else if (itemId == R.id.nav_start_checkin) {
            selectedFragment = new StartCheckinFragment();
        } else if (itemId == R.id.nav_statistics) {
            selectedFragment = new StatisticsFragment();
        } else if (itemId == R.id.nav_logout) {
            logout();
            return true;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_teacher, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        // 清除登录信息并返回登录页
        MyApplication.curUser = null;
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply(); // 清除"记住密码"

        Intent intent = new Intent(TeacherMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}