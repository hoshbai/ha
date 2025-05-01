//package com.example.campus_life_assistant;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.MapsInitializer;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;
//import com.amap.api.services.core.ServiceSettings;
//
//public class SchoolMapActivity extends AppCompatActivity {
//
//    private static final String TAG = "SchoolMapActivity";
//    private MapView mMapView;
//    private AMap aMap;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Context context = this;
//        //定位隐私政策同意
//        AMapLocationClient.updatePrivacyShow(context,true,true);
//        AMapLocationClient.updatePrivacyAgree(context,true);
//        //地图隐私政策同意
//        MapsInitializer.updatePrivacyShow(context,true,true);
//        MapsInitializer.updatePrivacyAgree(context,true);
//        //搜索隐私政策同意
//        ServiceSettings.updatePrivacyShow(context,true,true);
//        ServiceSettings.updatePrivacyAgree(context,true);
//        if (getWindow().getDecorView().getRootView() == null) {
//            Log.e(TAG, "RootView 为空，可能被系统优化隐藏");
//        }
//        try {
//            MapsInitializer.initialize(getApplicationContext());
//            Log.d(TAG, "高德地图 SDK 初始化成功");
//        } catch (Exception e) {
//            Log.e(TAG, "高德地图 SDK 初始化失败", e);
//        }
//
//        try {
//            setContentView(R.layout.activity_school_map);
//
//            // 初始化 MapView
//            mMapView = findViewById(R.id.map);
//            if (mMapView == null) {
//                Log.e(TAG, "onCreate: MapView 初始化失败，布局文件中未找到 map 视图");
//                return;
//            }
//
//            mMapView.onCreate(savedInstanceState);
//            Log.d(TAG, "MapView 创建成功");
//
//            // 初始化 AMap 对象
//            if (aMap == null) {
//                aMap = mMapView.getMap();
//                if (aMap != null) {
//                    Log.d(TAG, "AMap 实例创建成功");
//                    setupMap();
//                } else {
//                    Log.e(TAG, "AMap 实例创建失败，请检查 API 密钥和网络连接");
//                }
//            }
//
//            // 请求定位权限
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "请求定位权限");
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        LOCATION_PERMISSION_REQUEST_CODE);
//            } else {
//                enableLocation();
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "onCreate 发生异常: ", e);
//        }
//    }
//
//    private void setupMap() {
//        try {
//            if (aMap == null) {
//                Log.e(TAG, "setupMap: AMap 实例为空");
//                return;
//            }
//
//            // 设置地图类型：普通地图
//            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
//            Log.d(TAG, "地图类型设置为普通地图");
//
//            // 设置默认缩放级别和中心点（例如校园中心点）118.292685,29.692136
//            LatLng campusCenter = new LatLng(29.692136, 118.292685); // 替换为你的校园实际经纬度
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusCenter, 10)); // 缩放级别 10
//            // 地图加载完成监听
//            aMap.setOnMapLoadedListener(() -> {
//                Log.d(TAG, "地图加载完成");
//                aMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(29.692136, 118.292685))
//                        .title("校园中心"));
//                // 可在此执行地图加载后的操作（如添加标记、路线等）
//            });
//
//            // 设置定位样式
//            MyLocationStyle myLocationStyle = new MyLocationStyle();
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//            aMap.setMyLocationStyle(myLocationStyle);
//
//            Log.d(TAG, "地图缩放级别和中心点设置完成");
//
//        } catch (Exception e) {
//            Log.e(TAG, "setupMap 发生异常: ", e);
//        }
//    }
//
//    // 启用定位图层（显示蓝点）
//    private void enableLocation() {
//        Log.d(TAG, "尝试启用定位功能");
//
//        try {
//            if (aMap == null) {
//                Log.e(TAG, "enableLocation: AMap 实例为空");
//                return;
//            }
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_COARSE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                Log.w(TAG, "定位权限未被授予");
//                return;
//            }
//
//            aMap.setMyLocationEnabled(true); // 显示定位蓝点
//            aMap.getUiSettings().setMyLocationButtonEnabled(true); // 显示定位按钮
//            Log.d(TAG, "定位功能已启用");
//
//        } catch (SecurityException e) {
//            Log.e(TAG, "定位权限安全异常: ", e);
//        } catch (Exception e) {
//            Log.e(TAG, "enableLocation 发生异常: ", e);
//        }
//    }
//
//    // 权限请求回调
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d(TAG, "收到权限请求结果");
//
//        try {
//            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "定位权限被授予");
//                    enableLocation();
//                } else {
//                    Log.w(TAG, "定位权限被拒绝");
//                    // 提示用户手动授权
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "onRequestPermissionsResult 发生异常: ", e);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            mMapView.onResume();
//            Log.d(TAG, "MapView 恢复");
//        } catch (Exception e) {
//            Log.e(TAG, "onResume 发生异常: ", e);
//        }
//
//        // 检查当前 MapView 是否可见
//        if (mMapView != null && mMapView.isShown()) {
//            Log.d(TAG, "MapView 当前可见");
//        } else {
//            Log.e(TAG, "MapView 未显示，请检查布局和系统优化策略");
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            mMapView.onPause();
//            Log.d(TAG, "MapView 暂停");
//        } catch (Exception e) {
//            Log.e(TAG, "onPause 发生异常: ", e);
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        try {
//            mMapView.onSaveInstanceState(outState);
//            Log.d(TAG, "保存地图实例状态");
//        } catch (Exception e) {
//            Log.e(TAG, "onSaveInstanceState 发生异常: ", e);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            if (mMapView != null) {
//                mMapView.onDestroy();
//                Log.d(TAG, "MapView 销毁完成");
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "onDestroy 发生异常: ", e);
//        }
//    }
//}