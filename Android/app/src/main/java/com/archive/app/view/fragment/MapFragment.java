package com.archive.app.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.archive.app.R;
import com.archive.app.RetrofitClient;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMapLocationListener, AMap.OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";
    private static final int PERMISSION_REQUEST_CODE = 1001;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
        try {
            mLocationClient = new AMapLocationClient(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        fabCurrentLocation = view.findViewById(R.id.fab_current_location);
        courierRecyclerView = view.findViewById(R.id.rv_courier_cards);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        fabCurrentLocation.setOnClickListener(v -> requestLocationAndMoveCamera());

    }


    private void setUpMap() {
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(new com.amap.api.maps.model.MyLocationStyle().myLocationType(com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATE));
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
        launchAmapNavigation("");
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Object obj = marker.getObject();
        launchAmapNavigation("");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // No action needed
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
}