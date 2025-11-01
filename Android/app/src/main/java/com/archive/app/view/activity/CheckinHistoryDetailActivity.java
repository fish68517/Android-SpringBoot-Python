package com.archive.app.view.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.archive.app.R;
import com.archive.app.model.CheckinRecord;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckinHistoryDetailActivity extends AppCompatActivity {

    private MapView mapView;
    private AMap aMap;
    private CheckinRecord record;

    private TextView tvStatus, tvTime, tvAddress, tvCourse, tvCoordinates;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    // 从 MapFragment 复制过来的教室范围
    private static final List<LatLng> CLASSROOM_POLYGON_POINTS = Arrays.asList(
            new LatLng(30.38480, 114.19810),
            new LatLng(30.38480, 114.19830),
            new LatLng(30.38460, 114.19830),
            new LatLng(30.38460, 114.19810)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_history_detail);

        // 1. 获取传递过来的数据
        record = (CheckinRecord) getIntent().getSerializableExtra("CHECKIN_RECORD");
        if (record == null) {
            Toast.makeText(this, "无法加载签到详情", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. 初始化Toolbar
        setupToolbar();

        // 3. 初始化视图控件
        findViews();

        // 4. 填充信息卡片
        populateInfoCard();

        // 5. 初始化地图
        mapView.onCreate(savedInstanceState);
        initializeMap();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("签到详情");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findViews() {
        mapView = findViewById(R.id.map_view_detail);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvTime = findViewById(R.id.tv_detail_time);
        tvAddress = findViewById(R.id.tv_detail_address);
        tvCourse = findViewById(R.id.tv_detail_course);
        tvCoordinates = findViewById(R.id.tv_detail_coordinates);
    }

    private void populateInfoCard() {
        // 设置状态
        setStatusStyle(tvStatus, record.getStatus());

        // 设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        tvTime.setText(sdf.format(record.getCheckinTime()));

        // 设置课程ID
        tvCourse.setText("课程ID: " + record.getCourseId());

        // 设置经纬度
        String coords = String.format(Locale.getDefault(), "经纬度: %.5f, %.5f", record.getLongitude(), record.getLatitude());
        tvCoordinates.setText(coords);

        // 异步解析地理位置
        getAddressFromLatLng(record.getLatitude(), record.getLongitude());
    }

    private void initializeMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(false); // 隐藏缩放按钮，让界面更干净

            LatLng checkinPoint = new LatLng(record.getLatitude(), record.getLongitude());

            // 移动镜头到打卡点
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(checkinPoint, 18f));

            // 添加打卡点标记
            aMap.addMarker(new MarkerOptions()
                    .position(checkinPoint)
                    .title("打卡位置")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            // 绘制教室范围
            aMap.addPolygon(new PolygonOptions()
                    .addAll(CLASSROOM_POLYGON_POINTS)
                    .strokeWidth(5)
                    .strokeColor(Color.argb(200, 255, 0, 0))
                    .fillColor(Color.argb(50, 255, 0, 0)));
        }
    }

    private void setStatusStyle(TextView textView, String status) {
        if (status == null) status = "未知";
        textView.setText(status);
        int colorResId;
        switch (status) {
            case "成功": colorResId = R.color.status_success; break;
            case "迟到": colorResId = R.color.status_late; break;
            case "缺勤": colorResId = R.color.status_absent; break;
            default: colorResId = R.color.status_default; break;
        }

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(16);
        background.setColor(ContextCompat.getColor(this, colorResId));
        textView.setBackground(background);
    }

    private void getAddressFromLatLng(double latitude, double longitude) {
        executor.execute(() -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String addressText = "无法获取位置信息";
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    addressText = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String finalAddressText = addressText;
            handler.post(() -> tvAddress.setText(finalAddressText));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 处理返回按钮点击事件
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- 必须重写地图的生命周期方法 ---
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        executor.shutdown();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}