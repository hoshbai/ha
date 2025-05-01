package com.example.campus_life_assistant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.services.core.ServiceSettings;
import com.example.campus_life_assistant.Adapter.PlaceAdapter;
import com.example.campus_life_assistant.entry.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class SchoolMapActivity extends AppCompatActivity implements AMapLocationListener, LocationSource {
    private static final int REQUEST_PERMISSIONS = 9527;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //内容
//    private TextView tvContent;
    private MapView mapView;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private OnLocationChangedListener mListener;

    private NestedScrollView bottomSheetContainer;
    private FloatingActionButton fabShowPlaces;
    private RecyclerView rvPlaceCards;
    private PlaceAdapter placeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context context = this;
        //定位隐私政策同意
        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        //地图隐私政策同意
        MapsInitializer.updatePrivacyShow(context, true, true);
        MapsInitializer.updatePrivacyAgree(context, true);
        //搜索隐私政策同意
        ServiceSettings.updatePrivacyShow(context, true, true);
        ServiceSettings.updatePrivacyAgree(context, true);

        setContentView(R.layout.activity_school_map);
        mapView = findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        initLocation();
        initMap(savedInstanceState);
        checkingAndroidVersion();
        initPlacePanel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    private void initPlacePanel() {
        bottomSheetContainer = findViewById(R.id.bottom_sheet_container);
        fabShowPlaces = findViewById(R.id.fab_show_places);
        rvPlaceCards = findViewById(R.id.rv_place_cards);

        // 初始化 RecyclerView
        placeAdapter = new PlaceAdapter();
        rvPlaceCards.setLayoutManager(new LinearLayoutManager(this));
        rvPlaceCards.setAdapter(placeAdapter);

        // 示例数据（可以替换为真实数据）
        List<Place> places = new ArrayList<>();
        places.add(new Place(R.drawable.library, "图书馆", "学校主图书馆，藏书丰富"));
        places.add(new Place(R.drawable.canteen, "食堂", "学生餐厅，提供多样餐食"));
        places.add(new Place(R.drawable.schoolmain, "率水校区", "率水校区大门"));
        places.add(new Place(R.drawable.schoolmain2, "横江校区", "横江校区大门"));
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> perms = Arrays.asList(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
            if (EasyPermissions.hasPermissions(this, String.valueOf(perms))) {
                mLocationClient.startLocation();
            } else {
                requestPermission();
            }
        } else {
            mLocationClient.startLocation(); // 低版本无需动态权限
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy(); // ✅ 销毁客户端
        }
        mLocationClient = null;
    }

    /**
     * 初始化地图
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
    }

    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.d("LocationType", String.valueOf(aMapLocation.getLocationType()));
        if (aMapLocation.getErrorCode() == 0) {
            double lat = aMapLocation.getLatitude();
            double lon = aMapLocation.getLongitude();
            String address = aMapLocation.getAddress();
            // 第一个 Toast 显示经纬度
            Toast.makeText(this, "纬度：" + lat + "\n经度：" + lon, Toast.LENGTH_SHORT).show();

            // 延迟一小段时间再显示地址
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Toast.makeText(this, "地址：" + address, Toast.LENGTH_SHORT).show();
            }, 2000); // 延迟 500 毫秒
            // 停止定位（防止重复回调）
            mLocationClient.stopLocation();
            mListener.onLocationChanged(aMapLocation);

        } else {
            Log.e("AmapError", "ErrCode:" + aMapLocation.getErrorCode());
        }
    }

    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    /**
     * 请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (requestCode == REQUEST_PERMISSIONS) {
            List<String> perms = Arrays.asList(permissions);
            if (EasyPermissions.hasPermissions(this, String.valueOf(perms))) {
                mLocationClient.startLocation();
            } else {
                Toast.makeText(this, "权限不足，部分功能无法使用", Toast.LENGTH_SHORT).show();
                // 可选：检测是否永久拒绝权限并引导至设置
                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    new AlertDialog.Builder(this)
                            .setMessage("请前往设置手动开启定位权限")
                            .setPositiveButton("去设置", (d, w) -> openAppSettings())
                            .show();
                }
            }
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }



    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (EasyPermissions.hasPermissions(this, permissions)) {
            mLocationClient.startLocation(); // 权限已授予，启动定位
        } else {
            EasyPermissions.requestPermissions(this, "需要定位权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0+ 需动态权限
            requestPermission();
        } else {
            // Android 6.0以下直接定位（无需动态权限）
            mLocationClient.startLocation(); // ✅ 低版本系统安全启动定位
        }
    }

}