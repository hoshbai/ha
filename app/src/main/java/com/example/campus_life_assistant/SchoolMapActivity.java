package com.example.campus_life_assistant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RideStep;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.example.campus_life_assistant.Adapter.PlaceAdapter;
import com.example.campus_life_assistant.entry.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SchoolMapActivity extends AppCompatActivity implements AMapLocationListener, LocationSource, RouteSearch.OnRouteSearchListener {
    private static final int REQUEST_PERMISSIONS = 9527;
    private static final String TAG = "SchoolMapActivity";

    // 高德地图相关
    private MapView mapView;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;

    // 路径规划相关
    private RouteSearch routeSearch;
    private List<Polyline> polylineList = new ArrayList<>();

    // UI 控件
    private NestedScrollView bottomSheetContainer;
    private FloatingActionButton fabShowPlaces;
    private RecyclerView rvPlaceCards;
    private PlaceAdapter placeAdapter;

    // 当前定位
    private static double currentLatitude;
    private static double currentLongitude;
    private boolean isFirstLocation = true; // ✅ 新增标志位


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化隐私政策
        Context context = this;
        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        MapsInitializer.updatePrivacyShow(context, true, true);
        MapsInitializer.updatePrivacyAgree(context, true);
        ServiceSettings.updatePrivacyShow(context, true, true);
        ServiceSettings.updatePrivacyAgree(context, true);

        setContentView(R.layout.activity_school_map);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        // 初始化地图
        initMap(savedInstanceState);

        // 初始化路径规划服务
        try {
            routeSearch = new RouteSearch(this);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }
        routeSearch.setRouteSearchListener(this);

        // 初始化定位
        try {
            initLocation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 权限检查
        checkingAndroidVersion();

        // 初始化底部面板
        initPlacePanel();
    }

    // 初始化地图
    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMaxZoomLevel(14);
        // ✅ 启用定位图层
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);

        // ✅ 设置定位样式（蓝色小箭头）
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER); // 显示方向旋转的蓝色箭头
        myLocationStyle.showMyLocation(true); // 显示定位图标
        myLocationStyle.strokeColor(Color.BLACK); // 边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 255)); // 半透明蓝色圆圈
        aMap.setMyLocationStyle(myLocationStyle);

        // ✅ 启用右下角默认定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    // 初始化定位
    private void initLocation() throws Exception {
        mLocationClient = new AMapLocationClient(SchoolMapActivity.this);
        if (mLocationClient != null) {
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocationLatest(false); // ✅ 关闭单次定位
            mLocationOption.setNeedAddress(true);
            mLocationOption.setHttpTimeOut(20000);
            mLocationOption.setLocationCacheEnable(false);
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    // 激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                mLocationClient.startLocation();
            } else {
                requestPermission();
            }
        } else {
            mLocationClient.startLocation();
        }
    }

    // 停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    // 接收定位结果
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            currentLatitude = aMapLocation.getLatitude();
            currentLongitude = aMapLocation.getLongitude();
            String address = aMapLocation.getAddress();

            // ✅ 第一次定位时显示经纬度和地址
            if (isFirstLocation) {
                Toast.makeText(this, "纬度：" + currentLatitude + " 经度：" + currentLongitude, Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Toast.makeText(this, "地址：" + address, Toast.LENGTH_SHORT).show();
                }, 2000);

                // ✅ 移动地图到当前位置
                if (aMap != null) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(currentLatitude, currentLongitude), 15f));
                }

                isFirstLocation = false; // ✅ 标记为非首次定位
            } else {
                // ✅ 后续定位仅更新坐标，不弹窗
                Log.d("LocationUpdate", "更新位置：" + currentLatitude + ", " + currentLongitude);
            }

            // ✅ 通知地图更新定位图标
            if (mListener != null) {
                mListener.onLocationChanged(aMapLocation);
            }
        } else {
            Log.e("AmapError", "定位失败，错误码：" + aMapLocation.getErrorCode());
            Toast.makeText(this, "定位失败，请检查网络或权限设置", Toast.LENGTH_SHORT).show();
        }
    }

    // 请求权限
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mLocationClient.startLocation();
        } else {
            EasyPermissions.requestPermissions(this, "需要定位权限", REQUEST_PERMISSIONS, perms);
        }
    }

    // 权限结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (EasyPermissions.hasPermissions(this, permissions)) {
                mLocationClient.startLocation();
            } else {
                Toast.makeText(this, "权限不足，部分功能无法使用", Toast.LENGTH_SHORT).show();
                if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(permissions))) {
                    new AlertDialog.Builder(this)
                            .setMessage("请前往设置手动开启定位权限")
                            .setPositiveButton("去设置", (d, w) -> openAppSettings())
                            .show();
                }
            }
        }
    }

    // 打开应用设置
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    // 检查 Android 版本
    private void checkingAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            mLocationClient.startLocation();
        }
    }

    // 保存地图状态
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // 暂停地图
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // 恢复地图
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    // 销毁地图
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        mapView.onDestroy();
        if (routeSearch != null) {
            routeSearch.setRouteSearchListener(null);
        }
    }

    // 获取当前纬度（供 PlaceAdapter 使用）
    public static double getCurrentLatitude() {
        return currentLatitude;
    }

    // 获取当前经度（供 PlaceAdapter 使用）
    public static double getCurrentLongitude() {
        return currentLongitude;
    }

    // 路径规划回调：驾车
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            drawPath(result.getPaths().get(0).getSteps());
        }
    }

    // 路径规划回调：步行
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            drawPath(result.getPaths().get(0).getSteps());
        }
    }

    // 路径规划回调：骑行
    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            drawPath(result.getPaths().get(0).getSteps());
        }
    }

    // 路径规划回调：公交
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            drawPath(result.getPaths().get(0).getSteps());
        }
    }

    // 绘制路径到地图（支持多种路径类型）
    private void drawPath(List<? extends Object> steps) {
        clearPaths();
        List<com.amap.api.maps.model.LatLng> latLngs = new ArrayList<>();

        for (Object step : steps) {
            if (step instanceof DriveStep) {
                DriveStep driveStep = (DriveStep) step;
                for (LatLonPoint point : driveStep.getPolyline()) {
                    latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }
            } else if (step instanceof WalkStep) {
                WalkStep walkStep = (WalkStep) step;
                for (LatLonPoint point : walkStep.getPolyline()) {
                    latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }
            } else if (step instanceof RideStep) {
                RideStep rideStep = (RideStep) step;
                for (LatLonPoint point : rideStep.getPolyline()) {
                    latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }
            } else if (step instanceof BusStep) {
                BusStep busStep = (BusStep) step;
                for (Object subStep : busStep.getBusLines()) {
                    if (subStep instanceof WalkStep) {
                        WalkStep walkStep = (WalkStep) subStep;
                        for (LatLonPoint point : walkStep.getPolyline()) {
                            latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                        }
                    } else if (subStep instanceof DriveStep) {
                        DriveStep driveStep = (DriveStep) subStep;
                        for (LatLonPoint point : driveStep.getPolyline()) {
                            latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                        }
                    }
                }
            }
        }

        if (!latLngs.isEmpty()) {
            Polyline polyline = aMap.addPolyline(new PolylineOptions()
                    .addAll(latLngs)
                    .width(10)
                    .color(0xFF0000FF));
            polylineList.add(polyline);
        }
    }

    // 清除路径
    private void clearPaths() {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }
        polylineList.clear();
    }

    // 初始化地点列表面板
    private void initPlacePanel() {
        bottomSheetContainer = findViewById(R.id.bottom_sheet_container);
        fabShowPlaces = findViewById(R.id.fab_show_places);
        rvPlaceCards = findViewById(R.id.rv_place_cards);

        placeAdapter = new PlaceAdapter();
        rvPlaceCards.setLayoutManager(new LinearLayoutManager(this));
        rvPlaceCards.setAdapter(placeAdapter);

        // 示例数据
        List<Place> places = new ArrayList<>();
        places.add(new Place(R.drawable.library, "图书馆", "学校主图书馆，藏书丰富"));
        places.get(0).setLatitude(39.9042);  // 示例坐标
        places.get(0).setLongitude(116.4074);

        places.add(new Place(R.drawable.canteen, "食堂", "学生餐厅，提供多样餐食"));
        places.get(1).setLatitude(39.9043);
        places.get(1).setLongitude(116.4075);

        places.add(new Place(R.drawable.schoolmain, "率水校区", "率水校区大门"));
        places.get(2).setLatitude(39.9044);
        places.get(2).setLongitude(116.4076);

        places.add(new Place(R.drawable.schoolmain2, "横江校区", "横江校区大门"));
        places.get(3).setLatitude(39.9045);
        places.get(3).setLongitude(116.4077);

        placeAdapter.submitList(places);

        // 浮动按钮点击事件
        fabShowPlaces.setOnClickListener(v -> {
            if (bottomSheetContainer.getVisibility() == View.GONE) {
                bottomSheetContainer.setVisibility(View.VISIBLE);
            } else {
                bottomSheetContainer.setVisibility(View.GONE);
            }
        });
    }

    // 调用高德地图进行路径规划：驾车
    public void startDrivingRoute(double startLat, double startLon, double endLat, double endLon) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                new LatLonPoint(startLat, startLon),
                new LatLonPoint(endLat, endLon));
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(
                fromAndTo,
                RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST_AVOID_CONGESTION,
                null,
                null,
                "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    // 调用高德地图进行路径规划：步行
    public void startWalkingRoute(double startLat, double startLon, double endLat, double endLon) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                new LatLonPoint(startLat, startLon),
                new LatLonPoint(endLat, endLon));
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    // 调用高德地图进行路径规划：骑行
    public void startRidingRoute(double startLat, double startLon, double endLat, double endLon) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                new LatLonPoint(startLat, startLon),
                new LatLonPoint(endLat, endLon));
        RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RIDING_DEFAULT);
        routeSearch.calculateRideRouteAsyn(query);
    }

    // 调用高德地图进行路径规划：公交
    public void startBusRoute(double startLat, double startLon, double endLat, double endLon) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                new LatLonPoint(startLat, startLon),
                new LatLonPoint(endLat, endLon));
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT, "北京", 0);
        routeSearch.calculateBusRouteAsyn(query);
    }
}