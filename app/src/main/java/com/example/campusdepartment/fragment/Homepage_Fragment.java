package com.example.campusdepartment.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.example.campusdepartment.R;
import com.example.campusdepartment.Utils.ImageUtils;
import com.example.campusdepartment.activity.SearchMainActivity;
import com.example.campusdepartment.adapter.HomeAdapter;
import com.example.campusdepartment.adapter.HomeBen;
import com.example.campusdepartment.other.UpdateFactory;
import com.example.campusdepartment.view.RecyclerViewForScrollView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林嘉煌 on 2020/12/11.
 */

public class Homepage_Fragment extends BaseFragment {
    TextView tv_Add;
    RelativeLayout searchView;
    List<HomeBen> list = new ArrayList<>();
    HomeAdapter fadapter;
    View view;
    //首页的标志
    private static final String HOME = "home";
    private RecyclerViewForScrollView listView;
    public static Handler handler;
    //接收数据库数据所需要的数组，数据信息
    public static String[] getData;
    List<HomeBen> ll_postBeans = new ArrayList<>();
    LocationClient mLocationClient; //定位客户端
    boolean isFirstLocate = true;
    BaiduMap baiduMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        //动态获取定位权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        listView = view.findViewById(R.id.listview);

        data();
        initLocation();
        requestLocation();
        initSceneryHotBanner(view);

        return view;
    }

    private void requestLocation() {

        mLocationClient.start();
    }

    private void initLocation() {  //初始化

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getActivity().getApplicationContext());
        tv_Add = (TextView) view.findViewById(R.id.add);
        LocationClientOption option = new LocationClientOption();
        //设置扫描时间间隔
        option.setScanSpan(1000);
        //设置定位模式，三选一
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);*/
        //设置需要地址信息
        option.setIsNeedAddress(true);
        //保存定位参数
        mLocationClient.setLocOption(option);

        searchView = (RelativeLayout) view.findViewById(R.id.search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
            }
        });

    }

    //内部类，百度位置监听器
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            tv_Add.setText(bdLocation.getAddrStr());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }

    private void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
    }

    private void data() {
        /*工厂*/
        final UpdateFactory factory = new UpdateFactory();
        factory.query_posts_all(HOME);//传入首页标志
        final int[] cc = {0};
        handler = new Handler() {
            @SuppressLint("RestrictedApi")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                getData = (String[]) msg.obj;
                Bundle c = msg.getData();
                cc[0] = c.getInt("Query_count");
                Log.e("Query_count", "Query_count: " + cc[0]);
                if (cc[0] > 0) {
                    for (int i = 0; i < cc[0]; i++) {
                        final HomeBen postBean = new HomeBen();
                        postBean.setContent(getData[i]);
                        postBean.setPrice(getData[++i]);
                        postBean.setNumber(getData[++i]);
                        postBean.setPicture(R.mipmap.welcome);
                        ll_postBeans.add(postBean);
                        Log.e("ccc", "cccc");
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        listView.setLayoutManager(staggeredGridLayoutManager);
                        fadapter = new HomeAdapter(ll_postBeans);
                        listView.setAdapter(fadapter);


                    }
                }
            }
        };
    }


    /**
     * 加载广告banner轮播图
     *
     * @param mainLayout
     */
    private void initSceneryHotBanner(View mainLayout) {
        List<String> images = new ArrayList<>();
        images.add("https://lvchen.coding.net/p/tupianyun/git/raw/master/image20.jpg");
        images.add("https://lvchen.coding.net/p/tupianyun/git/raw/master/image25.jpg");
        images.add("https://lvchen.coding.net/p/tupianyun/git/raw/master/image26.jpg");
        images.add("https://lvchen.coding.net/p/tupianyun/git/raw/master/image27.jpg");
        images.add("https://lvchen.coding.net/p/tupianyun/git/raw/master/image30.jpg");
        Banner banner = (Banner) mainLayout.findViewById(R.id.banner01);
        ImageUtils.initBanner(getActivity(), banner, images, 10);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getActivity(), "position" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}




