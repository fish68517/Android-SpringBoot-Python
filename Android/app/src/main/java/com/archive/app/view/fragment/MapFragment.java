package com.archive.app.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.archive.app.ApiService;
import com.archive.app.FaceApiClient;
import com.archive.app.MyApplication;
import com.archive.app.R;
import com.archive.app.RetrofitClient;


import com.archive.app.model.CheckPresenceRequest;
import com.archive.app.model.CheckPresenceResponse;
import com.archive.app.model.CheckinRecord;
import com.archive.app.model.NotificationDTO;
import com.archive.app.model.Point;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMapLocationListener, AMap.OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private FloatingActionButton fabCheckIn; // <-- 新增：打卡按钮

    // --- 新增：地理围栏常量 ---
    private static final double PRESENCE_THRESHOLD_METERS = 10.0; // 10米内算在场
    // 根据你的坐标模拟一个教室多边形
 /*   private static final List<LatLng> CLASSROOM_POLYGON_POINTS = Arrays.asList(
            new LatLng(30.38480, 114.19810), // 西北角
            new LatLng(30.38480, 114.19830), // 东北角
            new LatLng(30.38460, 114.19830), // 东南角
            new LatLng(30.38460, 114.19810)  // 西南角
    );*/
    // (你的点 30.384723, 114.19816 在这个多边形内部)


        private static final List<LatLng> CLASSROOM_POLYGON_POINTS = new ArrayList<>();

    // --- 新增：用于测试的坐标点 ---
    // (你的点，在教室内)
    private static final LatLng TEST_POINT_INSIDE = new LatLng(30.384723, 114.19816);
    // (约 50 米外，向北移动约 0.00045 度)
    private static final LatLng TEST_POINT_50M_AWAY = new LatLng(30.385173, 114.19816);
    // (约 100 米外，向北移动约 0.00090 度)
    private static final LatLng TEST_POINT_100M_AWAY = new LatLng(30.385623, 114.19816);
    // (约 500 米外，向北移动约 0.00450 度)
    private static final LatLng TEST_POINT_500M_AWAY = new LatLng(30.389223, 114.19816);

    private MapView mapView;
    private AMap aMap;
    private FloatingActionButton fabCurrentLocation;
    private RecyclerView courierRecyclerView;
    private final Map<String, Marker> markerMap = new HashMap<>();

    private LatLng mCurrentLocation;
    private Polyline currentPolyline;
    private Marker mTargetMarkerForRouting = null;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private ApiService springBootApiService;
    private NotificationDTO notification;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "地图日志 onCreate: ");
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
        try {
            mLocationClient = new AMapLocationClient(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getArguments() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notification = getArguments().getSerializable("notification", NotificationDTO.class);
            }
            Log.d(TAG, "onCreate: notification=" + notification);
            // [{"latitude":30.48195175333713,"longitude":114.53529981534143},{"latitude":30.48178070232253,"longitude":114.5335027353322},{"latitude":30.481292974130486,"longitude":114.53437981766507}]
            // 转换成 CLASSROOM_POLYGON_POINTS
            // 获取教室多边形JSON字符串
            String polygonJson = notification.getClassroomPolygon();

            // 使用Gson解析JSON
            Gson gson = new Gson();

            Point[] points = gson.fromJson(polygonJson, Point[].class);

            // 创建新的CLASSROOM_POLYGON_POINTS
            List<LatLng> classroomPolygonPoints = new ArrayList<>();
            for (Point point : points) {
                classroomPolygonPoints.add(new LatLng(point.latitude, point.longitude));
            }
            // 更新CLASSROOM_POLYGON_POINTS
            CLASSROOM_POLYGON_POINTS.clear();
            CLASSROOM_POLYGON_POINTS.addAll(classroomPolygonPoints);

        } else {

            CLASSROOM_POLYGON_POINTS.clear();
        }
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);// 改为 false 持续定位，方便测试
        mLocationOption.setInterval(1000*60); // 60秒一次
        mLocationClient.setLocationOption(mLocationOption);

        // --- 新增：初始化 Spring Boot ApiService ---
        springBootApiService = RetrofitClient.getMainApiService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        fabCurrentLocation = view.findViewById(R.id.fab_current_location);
        courierRecyclerView = view.findViewById(R.id.rv_courier_cards);
        fabCheckIn = view.findViewById(R.id.fab_check_in);


        Log.d(TAG, "地图日志 onCreateView: ");
        if (notification == null) {
            fabCheckIn.setVisibility(View.GONE);
        } else {
            fabCheckIn.setVisibility(View.VISIBLE);
        }
        // --- 新增：打卡按钮点击事件 ---
        fabCheckIn.setOnClickListener(v -> {
            if (mCurrentLocation == null) {
                Toast.makeText(getContext(), "正在获取当前位置，请稍后...", Toast.LENGTH_SHORT).show();
                requestLocationAndMoveCamera(); // 尝试重新定位
                return;
            }
            // 发起打卡
            performCheckIn(mCurrentLocation);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "地图日志 onViewCreated: ");
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        } else {
            setUpMap();
        }

        fabCurrentLocation.setOnClickListener(v -> requestLocationAndMoveCamera());

    }


    private void setUpMap() {
        Log.d(TAG, "地图日志 onViewCreated: " + "描绘电子围栏");
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(new com.amap.api.maps.model.MyLocationStyle().myLocationType(com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATE));
        // --- 新增：绘制教室多边形 ---
        drawClassroomPolygon();
    }

    private void drawClassroomPolygon() {
        if (aMap != null) {
            aMap.addPolygon(new PolygonOptions()
                    .addAll(CLASSROOM_POLYGON_POINTS)
                    .strokeWidth(5)
                    .strokeColor(Color.argb(200, 255, 0, 0)) // 红色描边
                    .fillColor(Color.argb(50, 255, 0, 0))); // 半透明红色填充
        }
    }

    private void moveCheckPoint(LatLng point) {
        // 移动地图到测试点
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 18f));
        // 给测试点增加一个标记
        aMap.addMarker(new MarkerOptions()
                .position(point)
                .title("签到打卡点")
                .snippet("这是你需要打卡的点")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }


    // --- 新增：打卡核心逻辑 ---
    private void performCheckIn(LatLng userLocation) {
        Toast.makeText(getContext(), "正在发起打卡...", Toast.LENGTH_SHORT).show();

        // 1. 准备请求体
       // userLocation = TEST_POINT_INSIDE; // 测试用，强制使用教室内坐标
       // userLocation = TEST_POINT_50M_AWAY; // 测试用，强制使用教室内坐标
      //  userLocation = TEST_POINT_100M_AWAY; // 测试用，强制使用教室内坐标
        // userLocation = TEST_POINT_500M_AWAY; // 测试用，强制使用教室内坐标
        CheckPresenceRequest.LatLngs userLoc = new CheckPresenceRequest.LatLngs(userLocation.latitude, userLocation.longitude);
        List<CheckPresenceRequest.LatLngs> polygon = new ArrayList<>();
        for (LatLng p : CLASSROOM_POLYGON_POINTS) {
            polygon.add(new CheckPresenceRequest.LatLngs(p.latitude, p.longitude));
        }

        for (LatLng p : CLASSROOM_POLYGON_POINTS) {
            Log.d("PerformCheckIn", "polygon point: " + p.latitude + ", " + p.longitude);
            moveCheckPoint(p);
            break;
        }
        // 延时1秒再继续，确保地图移动完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CheckPresenceRequest request = new CheckPresenceRequest(userLoc, polygon, PRESENCE_THRESHOLD_METERS);

        // 2. 调用 Python API
        LatLng finalUserLocation = userLocation;
        FaceApiClient.getApiService().checkPresence(request).enqueue(new Callback<CheckPresenceResponse>() {
            @Override
            public void onResponse(Call<CheckPresenceResponse> call, Response<CheckPresenceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CheckPresenceResponse body = response.body();
                    Log.i(TAG, "打卡结果: " + body.getStatus());
                    if ("success".equals(body.getStatus())) {
                        // 成功，显示Python返回的消息
                        Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "打卡成功: " + body.getMessage());
                        saveCheckinToSpringBoot(finalUserLocation, body);
                    } else {
                        // 成功，但Python返回了逻辑失败
                        Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // HTTP 错误
                    Toast.makeText(getContext(), "打卡失败: 服务器响应错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckPresenceResponse> call, Throwable t) {
                // 网络错误
                Toast.makeText(getContext(), "打卡失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "打卡网络错误", t);
            }
        });
    }


    private void requestLocationAndMoveCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            mLocationClient.startLocation();
            Toast.makeText(getContext(), "正在获取当前位置...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationClient.startLocation();
            Toast.makeText(getContext(), "定位权限已授予", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "定位权限被拒绝", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mCurrentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                if (mTargetMarkerForRouting != null) {
                    drawRouteAndZoom(mCurrentLocation, mTargetMarkerForRouting);
                    mTargetMarkerForRouting = null;
                } else {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15f));
                }
                // 定位成功打印经纬度  定位成功: 30.384723, 114.19816
                Log.i(TAG, "定位成功: " + amapLocation.getLatitude() + ", " + amapLocation.getLongitude());
            } else {
                mTargetMarkerForRouting = null;
                // 定位失败: 7, KEY错误 请到http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/查看错误码说明,
                // 错误详细信息:auth fail:INVALID_USER_SCODE#SHA1AndPackage#66:99:CA:9A:AA:E0:F8:DE:B7:48:55:79:B6:EC:45:47:B4:DF:40:DD:com.archive.app#gsid#033040110065176187911983400030650663549#csid#87de83a295e34a24901561c158693b81#0701#pm110011
                Log.e(TAG, "定位失败: " + amapLocation.getErrorCode() + ", " + amapLocation.getErrorInfo());
                Toast.makeText(getContext(), "定位失败: " + amapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object obj = marker.getObject();
       // launchAmapNavigation("");
        // 点击 Marker 显示路径
        showRouteAndZoom(marker);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Object obj = marker.getObject();
        // 示例：从 marker 获取地址
        // String address = marker.getTitle();
        // launchAmapNavigation(address);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // No action needed
        // 点击地图时清除路径
        if (currentPolyline != null) {
            currentPolyline.remove();
            currentPolyline = null;
        }
    }


    private void showRouteAndZoom(Marker marker) {
        if (mCurrentLocation == null) {
            Toast.makeText(getContext(), "正在获取当前位置...", Toast.LENGTH_SHORT).show();
            mTargetMarkerForRouting = marker;
            requestLocationAndMoveCamera();
            return;
        }
        drawRouteAndZoom(mCurrentLocation, marker);
    }

    private void drawRouteAndZoom(LatLng startPoint, Marker marker) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        LatLng endPoint = marker.getPosition();

        currentPolyline = aMap.addPolyline(new PolylineOptions()
                .add(startPoint, endPoint)
                .width(10)
                .color(ContextCompat.getColor(requireContext(), R.color.purple_500)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));

        marker.showInfoWindow();
    }

    private void launchAmapNavigation(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String url = "amapuri://route/plan/?dname=" + encodedAddress + "&dev=0&t=0";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "高德地图App未安装或打开失败", Toast.LENGTH_LONG).show();
            try {
                String webUrl = "https://uri.amap.com/navigation?to=" + URLEncoder.encode(address, "UTF-8") + "&mode=car&policy=0";
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                startActivity(webIntent);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // --- 新增方法：将签到记录保存到 Spring Boot ---
    private void saveCheckinToSpringBoot(LatLng location, CheckPresenceResponse presenceResponse) {
        if (MyApplication.curUser == null) {
            Toast.makeText(getContext(), "保存失败：用户未登录", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckinRecord record = new CheckinRecord();
        record.setUserId((long) MyApplication.curUser.getId());
        record.setLatitude(location.latitude);
        record.setLongitude(location.longitude);
        record.setStatus(presenceResponse.isPresent() ? "成功" : "失败");
        if (notification == null) {
            record.setCourseId(1L); // TODO: 你需要一个逻辑来获取当前签到的课程ID
        } else
            record.setCourseId(notification.getCourseId());


        springBootApiService.saveCheckinRecord(record).enqueue(new Callback<CheckinRecord>() {
            @Override
            public void onResponse(Call<CheckinRecord> call, Response<CheckinRecord> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "签到记录已保存到服务器！", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Spring Boot 保存成功");
                } else {
                    Toast.makeText(getContext(), "签到记录保存失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Spring Boot 保存失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CheckinRecord> call, Throwable t) {
                Toast.makeText(getContext(), "签到记录保存失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Spring Boot 保存网络错误", t);
            }
        });
    }
}