package com.example.campusdepartment.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.campusdepartment.R;
import com.example.campusdepartment.activity.BuyerActivity;
import com.example.campusdepartment.adapter.ShopcarBean;
import com.example.campusdepartment.adapter.Shopcar_Adapter;
import com.example.campusdepartment.other.UpdateFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 林嘉煌 on 2020/12/11.
 */

public class ShoppingCar_Fragment extends Fragment implements View.OnClickListener {
    public static Handler handler_shop;
    private final String ShoppingCar_Fragment = "ShoppingCar_Fragment";
    ListView listView;
    private List<ShopcarBean> list = new ArrayList<>();
    private List list_data = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    Shopcar_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoppingcar, container, false);
        listView = view.findViewById(R.id.listview);
        refreshLayout = view.findViewById(R.id.swipe_refresh_fans);
        user_pic();

//        adapter = new Shopcar_Adapter(getActivity(),list, this);
//        listView.setAdapter(adapter);
        fresh();
        return view;

    }

    @SuppressLint("ResourceAsColor")
    private void fresh() {
        refreshLayout.setColorSchemeColors(R.color.black);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新内容,获取数据库的用户头像
                user_pic();
//              adapter = new Shopcar_Adapter(getActivity(),list);
//              listView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void user_pic() {
        UpdateFactory factory = new UpdateFactory();
        UpdateFactory.User_Head_Pic_query query = factory.Factory_Pic_query(BuyerActivity.u_id, ShoppingCar_Fragment);
        try {
            query.user_head_pic_query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler_shop = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                list.clear();//刷新后清除之前的数据
                list_data = (List) msg.obj;
                if (list_data.size() > 0) {
                    for (int i = 0, j = 0; i < list_data.size(); i++, j++) {
                        ShopcarBean bean = new ShopcarBean();
                        //把泛型强制转换为byte[],再转换为bitmap类型
                        bean.setPicture((Bitmap) list_data.get(i));
                        bean.setContent((String) list_data.get(++i));
                        bean.setPrice((String) list_data.get(++i));
                        bean.setNumber((String) list_data.get(++i));
                        list.add(bean);
                        Log.e("New_Fans", "handleMessage: " + "头像-" + list_data.get(j) + ",昵称-" + list_data.get(++j) + ",发帖人账号-" + list_data.get(++j));
                        // adapter = new Shopcar_Adapter(getActivity(),list, (View.OnClickListener) getActivity());
                        adapter = new Shopcar_Adapter(getActivity(), list);
                        listView.setAdapter(adapter);
                    }


                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.del) {
            int position = listView.getPositionForView(view);
            adapter.removeItem(position);
            Log.e("xxx", "购物车删除" + position);
        }
    }

}
