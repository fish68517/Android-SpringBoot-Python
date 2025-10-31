package com.archive.app.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.archive.app.R;
import com.archive.app.view.fragment.CheckinHistoryFragment; // <-- 新增
import com.archive.app.view.fragment.CheckinNotificationFragment; // <-- 新增
import com.archive.app.view.fragment.HomeFragment;
import com.archive.app.view.fragment.MapFragment;
import com.archive.app.view.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        setupBottomNavigation();
        getPermission();
    }

    private void getPermission(){
        // ... (getPermission 方法保持不变) ...
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

            // --- 修改后的逻辑 ---
            if (itemId == R.id.nav_schedule) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_voice) { // 地图打卡
                selectedFragment = new MapFragment();
            } else if (itemId == R.id.nav_notifications) { // 签到通知 (新)
                selectedFragment = new CheckinNotificationFragment();
            } else if (itemId == R.id.nav_history) { // 签到记录 (新)
                selectedFragment = new CheckinHistoryFragment();
            } else if (itemId == R.id.nav_help) { // "我的" (Profile)
                selectedFragment = new ProfileFragment();
            } else {
                selectedFragment = new HomeFragment(); // 默认
            }
            // --- 修改结束 ---

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }

    // ... (isEmulator 方法保持不变) ...
}