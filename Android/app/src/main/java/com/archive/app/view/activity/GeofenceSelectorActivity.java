package com.archive.app.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.IReGeoLocationCallback;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.archive.app.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GeofenceSelectorActivity extends AppCompatActivity implements AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    private static final int PERMISSION_REQUEST_CODE = 1002;
    public static final String RESULT_POLYGON_STRING = "polygonData";
    public static final String RESULT_THRESHOLD = "thresholdData";

    private MapView mapView;
    private AMap aMap;
    private EditText etSearch;
    private Button btnSearch, btnClear, btnConfirm;

    private AMapLocationClient locationClient;
    private GeocodeSearch geocodeSearch;
    private Marker searchResultMarker;

    // 围栏绘制相关
    private List<LatLng> polygonPoints = new ArrayList<>();
    private Polygon currentPolygon;
    private List<Marker> polygonMarkers = new ArrayList<>();

    // 默认阈值
    private final double THRESHOLD = 10.0; // 10米
    private TextView instruct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_selector);

        mapView = findViewById(R.id.map_view_geofence);
        instruct = findViewById(R.id.tv_instruction);
        mapView.onCreate(savedInstanceState);

        etSearch = findViewById(R.id.et_location_search);
        btnSearch = findViewById(R.id.btn_location_search);
        btnClear = findViewById(R.id.btn_clear_polygon);
        btnConfirm = findViewById(R.id.btn_confirm_geofence);

        initMap();
        initSearch();
        initLocation();
        setupListeners();
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(true);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
        }

        // 地图点击事件：添加多边形顶点
        aMap.setOnMapClickListener(latLng -> {
            addPolygonPoint(latLng);
        });
    }

    private void initSearch() {
        try {
            geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void initLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            startLocation();
        }
    }

    private void startLocation() {
        try {
            locationClient = new AMapLocationClient(this);
            locationClient.setLocationListener(this);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true); // 只需要定位一次
            locationClient.setLocationOption(option);
            locationClient.startLocation();
            locationClient.setReGeoLocationCallback(new IReGeoLocationCallback() {
                @Override
                public void onReGeoLocation(AMapLocation aMapLocation) {
                    Log.d("D", "onReGeoLocation调试  1: " + aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
                    if (aMapLocation != null) {
                        Log.d("D", "onReGeoLocation: " + aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 15));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        // 搜索
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                hideKeyboard();
                GeocodeQuery query = new GeocodeQuery(keyword, ""); // 第二个参数传 "" 表示全国搜索
                geocodeSearch.getFromLocationNameAsyn(query);
            }
        });

        // 清除
        btnClear.setOnClickListener(v -> {
            clearPolygon();
        });

        // 确认
        btnConfirm.setOnClickListener(v -> {
            if (polygonPoints.size() < 3) {
                Toast.makeText(this, "请至少选择4个点来构成一个多边形", Toast.LENGTH_SHORT).show();
                return;
            }

           /* // 1. 格式化多边形数据
            // 格式: "lat1,lon1;lat2,lon2;..."
            StringBuilder polygonString = new StringBuilder();
            for (LatLng point : polygonPoints) {
                polygonString.append(point.latitude)
                        .append(",")
                        .append(point.longitude)
                        .append(";");
            }
            // 去掉最后一个分号
            polygonString.deleteCharAt(polygonString.length() - 1);*/


            String jsonOutput = new Gson().toJson(polygonPoints);

            // 打印结果
            System.out.println(jsonOutput);
            // 输出: [{"latitude":30.xxxx,"longitude":114.xxxx},{"latitude":...,"longitude":...}]

            // 2. 将数据打包回传
            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESULT_POLYGON_STRING, jsonOutput);
            resultIntent.putExtra(RESULT_THRESHOLD, THRESHOLD);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void addPolygonPoint(LatLng latLng) {
        polygonPoints.add(latLng);

        // 添加标记点
        polygonMarkers.add(aMap.addMarker(new MarkerOptions().position(latLng).icon(
                // 使用一个小蓝点作为顶点标记
                com.amap.api.maps.model.BitmapDescriptorFactory.defaultMarker(com.amap.api.maps.model.BitmapDescriptorFactory.HUE_AZURE)
        )));

        // 绘制多边形
        if (currentPolygon != null) {
            currentPolygon.remove();
        }

        if (polygonPoints.size() >= 3) {
            currentPolygon = aMap.addPolygon(new PolygonOptions()
                    .addAll(polygonPoints)
                    .strokeWidth(5)
                    .strokeColor(Color.argb(200, 255, 0, 0))
                    .fillColor(Color.argb(50, 255, 0, 0)));
        }
    }

    private void clearPolygon() {
        for (Marker marker : polygonMarkers) {
            marker.remove();
        }
        if (currentPolygon != null) {
            currentPolygon.remove();
        }
        polygonMarkers.clear();
        polygonPoints.clear();
        currentPolygon = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // 打印log
        // 将经纬度转换成 地理位置
        String location = amapLocation.getLongitude() + "\n" + amapLocation.getLatitude();
        Log.d("D", "onReGeoLocation调试 2: " + location);
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            // 定位成功，移动到当前位置
            LatLng currentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            String city = amapLocation.getCity();
            // 请点击地图设置教室多边形（至少3个点）
            Log.d("GeofenceSelectorActivity", "当前城市: " + city);
            instruct.setText("请点击地图设置教室多边形（至少3个点）\n当前经纬度: " + location);
        } else {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS && result != null && result.getGeocodeAddressList().size() > 0) {
            LatLonPoint point = result.getGeocodeAddressList().get(0).getLatLonPoint();
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());

            // 114.190243,30.392116  2025-11-08 22:09:39.111 23530-23530
            // 114.536951,30.479961  2025-11-08 22:09:14.673
            Log.d("GeofenceSelectorActivity", "onReGeoLocation调试 3: " + point.getLongitude() + "," + point.getLatitude());
            // 移动到搜索结果
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));


            // 添加标记
            if (searchResultMarker != null) searchResultMarker.remove();
            searchResultMarker = aMap.addMarker(new MarkerOptions().position(latLng).title(etSearch.getText().toString()));
            searchResultMarker.showInfoWindow();
        } else {
            Toast.makeText(this, "搜索失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {}

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // --- 地图生命周期管理 ---
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
        if (locationClient != null) {
            locationClient.onDestroy();
        }
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            } else {
                Toast.makeText(this, "需要定位权限才能使用地图功能", Toast.LENGTH_SHORT).show();
            }
        }
    }
}