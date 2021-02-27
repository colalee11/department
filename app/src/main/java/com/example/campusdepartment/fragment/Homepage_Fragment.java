package com.example.campusdepartment.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.example.campusdepartment.activity.Home_ProductDetailsMainActivity;
import com.example.campusdepartment.activity.SearchMainActivity;
import com.example.campusdepartment.adapter.HomeAdapter;
import com.example.campusdepartment.adapter.HomeBen;
import com.example.campusdepartment.other.UpdateFactory;
import com.example.campusdepartment.other.loadDialogUtils;
import com.example.campusdepartment.view.RecyclerViewForScrollView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林嘉煌 on 2020/12/11.
 */

public class Homepage_Fragment extends BaseFragment {
    //首页的标志
    private static final String HOME = "home";
    public static Handler handler;
    //接收数据库数据所需要的数组，数据信息
    public static String[] getData;
    TextView tv_Add;
    RelativeLayout searchView;
    List<HomeBen> list = new ArrayList<>();
    HomeAdapter fadapter;
    View view;
    //List<HomeBen> ll_postBeans = new ArrayList<>();
    LocationClient mLocationClient; //定位客户端
    boolean isFirstLocate = true;
    BaiduMap baiduMap;
    private Dialog mDialog;
    private RecyclerViewForScrollView listView;
    private List list_data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        //动态获取定位权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listView = view.findViewById(R.id.listview);
        listView.setLayoutManager(staggeredGridLayoutManager);
        mDialog = loadDialogUtils.createLoadingDialog(getContext(), "加载中...");
        mDialog.create();
        data();

        initLocation();
        requestLocation();
        initSceneryHotBanner(view);

        return view;
    }

    private void data() {

        mDialog.show();
        UpdateFactory factory = new UpdateFactory();
        UpdateFactory.User_Head_Pic_query query = factory.Factory_Pic_query(null, HOME);
        try {
            query.user_head_pic_query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //list.clear();//刷新后清除之前的数据
                list_data = (List) msg.obj;
                if (list_data.size() > 0) {

                    for (int i = 0, j = 0; i < list_data.size(); i++, j++) {
                        HomeBen bean = new HomeBen();
                        //把泛型强制转换为byte[],再转换为bitmap类型
                        bean.setPicture((Bitmap) list_data.get(i));
                        bean.setContent((String) list_data.get(++i));
                        bean.setPrice((String) list_data.get(++i));
                        bean.setNumber((String) list_data.get(++i));
                        list.add(bean);
                        Log.e("home", "handleMessage: " + "头像-" + list_data.get(j) + ",昵称-" + list_data.get(++j) + ",发帖人账号-" + list_data.get(++j));
                        fadapter = new HomeAdapter(list);
                        listView.setAdapter(fadapter);
                        mDialog.dismiss();
                        fadapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Bitmap bitmap = (Bitmap) list.get(position).getPicture();
                                Bundle bundle = new Bundle();
                                bundle.putString("content", (String) list.get(position).getContent());
                                bundle.putParcelable("picture", bitmap);
                                bundle.putString("number", (String) list.get(position).getNumber());
                                bundle.putString("price", (String) list.get(position).getPrice());
                                Intent intent = new Intent(getActivity(), Home_ProductDetailsMainActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
//
//                    listView.setVisibility(View.GONE);
//                    loadDialogUtils.closeDialog(mDialog);
                }
            }
        };


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

    private void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
    }

//    private void data() {
//        /*工厂*/
//        final UpdateFactory factory = new UpdateFactory();
//        factory.query_posts_all(HOME);//传入首页标志
//        final int[] cc = {0};
//        handler = new Handler() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                getData = (String[]) msg.obj;
//                Bundle c = msg.getData();
//                cc[0] = c.getInt("Query_count");
//                Log.e("Query_count", "Query_count: " + cc[0]);
//                if (cc[0] > 0) {
//                    for (int i = 0; i < cc[0]; i++) {
//                        final HomeBen postBean = new HomeBen();
//                        postBean.setContent(getData[i]);
//                        postBean.setPrice(getData[++i]);
//                        postBean.setNumber(getData[++i]);
//                        postBean.setPicture(R.mipmap.welcome);
//                        ll_postBeans.add(postBean);
//                        Log.e("ccc", "cccc");
//                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        listView.setLayoutManager(staggeredGridLayoutManager);
//                        fadapter = new HomeAdapter(ll_postBeans);
//                        listView.setAdapter(fadapter);
//
//
//                    }
//                }
//            }
//        };
//    }

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


}




