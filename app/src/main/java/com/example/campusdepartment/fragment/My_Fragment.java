package com.example.campusdepartment.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.campusdepartment.R;
import com.example.campusdepartment.Utils.MysqlUtil;
import com.example.campusdepartment.activity.BuyerActivity;
import com.example.campusdepartment.activity.LoginActivity;
import com.example.campusdepartment.activity.Receiving_address_Activity;
import com.example.campusdepartment.activity.SettingActivity;
import com.example.campusdepartment.other.ACache;
import com.example.campusdepartment.other.ReadFile;
import com.example.campusdepartment.other.UpdateFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 林嘉煌 on 2020/12/11.
 */

public class My_Fragment extends Fragment implements View.OnClickListener {
    public static Handler handler;
    public static String u_id, user_check;
    private final String MYFRAGMENT = "MyFragment";
    ImageView imageView, setting, updata_name;
    TextView name;
    View view;
    RelativeLayout tuichu, updataInfo;
    //缓存框架-头像
    ACache aCache_headPic;
    String newname;//修改后的昵称
    private List list = new ArrayList();
    private Connection connection = null;
    private Connection connection1 = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        return view;

    }

    private void initView() {
        name = view.findViewById(R.id.name);
        imageView = view.findViewById(R.id.imagea);
        tuichu = view.findViewById(R.id.more3);
        updataInfo = view.findViewById(R.id.more4);
        updata_name = view.findViewById(R.id.updata_name);
        setting = view.findViewById(R.id.set);
        imageView.setOnClickListener(this);
        updata_name.setOnClickListener(this);
        tuichu.setOnClickListener(this);
        setting.setOnClickListener(this);
        updataInfo.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取登录状态
        u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
        user_check = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_check.txt");
        if (!TextUtils.isEmpty(u_id)) {
            Log.e("cdc", "11");
            if (user_check.equals("1")) {
                //自动从数据库里获取图片blob类型，然后转换显示出来,缓存头像
                try {
                    //使用缓存框架ACache
                    aCache_headPic = ACache.get(getActivity());
                    Log.e("cdc", "22");
                    //一来就获取缓存内容，查看是否为空,为空就执行缓存，不为空就直接获取缓存内容
                    user_pic();
                    if (aCache_headPic.get(getActivity()).getAsBitmap("head_pic") != null) {
                        Log.e("this", "onCreateView: 1");
                        aCache_headPic.get(getActivity());
                        Bitmap headPic = aCache_headPic.getAsBitmap("head_pic");
                        imageView.setImageBitmap(headPic);
                    } else {
                        Log.e("cdc", "444");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("cdc", "333");
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    connection = MysqlUtil.getConn();
                    String sql = "SELECT user_realname FROM login where user_phone=" + u_id;
                    java.sql.Statement statement = null;
                    statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()) {
                        String UserName = rs.getString("user_realname");
                        Log.e("cdc", UserName);
                        name.setText(UserName);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }).start();


    }


    private void user_pic() throws SQLException {
        //尝试数据库获取图片
        UpdateFactory factory = new UpdateFactory();
        UpdateFactory.User_Head_Pic_query query = factory.Factory_Pic_query(BuyerActivity.u_id, MYFRAGMENT);
        query.user_head_pic_query();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if ("1".equals(user_check)) {
                    list = (List) msg.obj;
                    Log.e("bt: ", msg.obj + "");
                    //  Log.e("bt: ", "查看获取的obj" + list.get(0));
                    //把读取到的图片数据,进行缓存,都是Bitmap格式
                    aCache_headPic.put("head_pic", (Bitmap) list.get(0));//后面加,10代表缓存时间为10秒,
                    //拿到缓存内容
                    aCache_headPic.get(getActivity());
                    Bitmap headPic = aCache_headPic.getAsBitmap("head_pic");
                    //设置到头像上去
                    imageView.setImageBitmap(headPic);
                } else {
                    Log.e("cdc", "666");
                }

            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagea:

                break;
            case R.id.more3:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.set:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.more4:
                startActivity(new Intent(getActivity(), Receiving_address_Activity.class));
                break;
            case R.id.updata_name:
                setUpdata_name();
                break;
        }

    }

    /**
     * 用户修改昵称
     */
    private void setUpdata_name() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.updata_name, null);
// 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view1);
//这个位置十分重要，只有位于这个位置逻辑才是正确的
        final AlertDialog dialog = builder.show();
        final EditText et_Threshold = view1.findViewById(R.id.edThreshold);
        //  et_Threshold.setText(mGamePadBitmap.setThresholdValue);
        view1.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newname = et_Threshold.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MysqlUtil.drive();
                        try {
                            connection1 = MysqlUtil.getConn();
                            String sql = "update login set user_realname=? where user_phone=" + u_id;
                            PreparedStatement pst = connection.prepareStatement(sql);
                            pst.setString(1, newname);
                            pst.executeUpdate();
                            pst.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }).start();
                name.setText(newname);
                dialog.dismiss();
            }
        });
        view1.findViewById(R.id.btn_openNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
