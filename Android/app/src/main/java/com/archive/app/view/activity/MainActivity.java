package com.archive.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.archive.app.R;

import com.archive.app.view.fragment.AchievementFragment;
import com.archive.app.view.fragment.HabitFragment;
import com.archive.app.view.fragment.ProfileFragment;
import com.archive.app.view.fragment.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;


import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    private static final String TAG = "MainActivity";

    private boolean isAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
           // startActivity(new android.content.Intent(MainActivity.this, AddEditScheduleActivity.class));
        });

        // 默认加载日程Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TaskFragment()).commit();
        }

        setupBottomNavigation();
        // 讯飞语音相关的
        getPermission();
    }

    private void getPermission(){
        XXPermissions.with(this).permission("android.permission.WRITE_EXTERNAL_STORAGE"
                , "android.permission.READ_EXTERNAL_STORAGE"
                , "android.permission.INTERNET"
                , "android.permission.MANAGE_EXTERNAL_STORAGE").request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean all) {
                Log.d(TAG,"SDK获取系统权限成功:"+all);
                for(int i=0;i<granted.size();i++){
                    Log.d(TAG,"获取到的权限有："+granted.get(i));
                }
                if(all){

                }
            }

            @Override
            public void noPermission(List<String> denied, boolean quick) {
                if(quick){
                    Log.e(TAG,"onDenied:被永久拒绝授权，请手动授予权限");
                    XXPermissions.startPermissionActivity(MainActivity.this,denied);
                }else{
                    Log.e(TAG,"onDenied:权限获取失败");
                }
            }
        });
    }



    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_schedule) {
                selectedFragment = new TaskFragment();
            } else if (itemId == R.id.nav_voice) {
                selectedFragment = new HabitFragment();
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new AchievementFragment();
            } else {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    // 添加判断是否为模拟器的方法
    // ... existing code ...
    // 添加判断是否为模拟器的方法
    private boolean isEmulator() {
        Log.d(TAG, "设备信息：MODEL=" + android.os.Build.MODEL +
                ", MANUFACTURER=" + android.os.Build.MANUFACTURER +
                ", BRAND=" + android.os.Build.BRAND +
                ", DEVICE=" + android.os.Build.DEVICE +
                ", PRODUCT=" + android.os.Build.PRODUCT);

        boolean isEmulator = android.os.Build.MODEL.contains("Emulator") ||
                android.os.Build.MODEL.contains("OPPO") ||
                android.os.Build.MANUFACTURER.contains("OPPO") ||
                (android.os.Build.BRAND.startsWith("OPPO") && android.os.Build.DEVICE.startsWith("gracelte")) ||
                android.os.Build.PRODUCT.equals("PCRT00");

        Log.d(TAG, "是否为模拟器环境: " + isEmulator);
        return isEmulator;
    }


}