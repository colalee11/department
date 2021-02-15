package com.example.campusdepartment.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.campusdepartment.Utils.MysqlUtil;
import com.example.campusdepartment.activity.Receiving_address_Activity;
import com.example.campusdepartment.fragment.Homepage_Fragment;
import com.example.campusdepartment.fragment.My_Fragment;
import com.example.campusdepartment.fragment.ShoppingCar_Fragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林嘉煌 on 2020/12/14.
 */

public class UpdateFactory {
    private Connection cn =null;
    private Context mContext;
    public static String[] query_values_all = new String[999];
    public static int Query_count=0;
    public static byte[] query_picture;
    public static String query_content,query_price,query_number;

    /**
     * 注册的信息
     * @param user_name
     * @param user_sex
     * @param user_realname
     * @param user_photo
     * @param user_phone
     */
    public void information_register(final String user_name, final String user_sex, final String user_realname, final byte[] user_photo, final String user_phone){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    cn = MysqlUtil.getConn();
                    Log.e("数据库", "information_register: "+"连接成功" );
                    insert_information_register(user_name,user_sex,user_realname,user_photo,user_phone);
                }catch (Exception e){
                    Log.e("数据库", "information_register: "+"连接失败" );
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void insert_information_register(String name_e, String sex_e, String nc_e, byte[] user_head_pic_e, String user_e) throws SQLException {
        String sql_insert = "update login set user_name=?,user_sex=?,user_realname=?,user_photo=? where user_phone=?";
        PreparedStatement pst = cn.prepareStatement(sql_insert);
        pst.setString(1, name_e);
        pst.setString(2, sex_e);
        pst.setString(3, nc_e);
        pst.setBytes(4, user_head_pic_e);
        pst.setString(5, user_e);
        pst.executeUpdate();
        Log.e("SDS","更新成功");
        pst.close();
    }



    /**
   * 上传商品信息
   *
   * */
  public void information_Commodities(final byte[] picture, final String content, final String price, final String number , final String user_phone){
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  MysqlUtil.drive();
                  cn = MysqlUtil.getConn();
                  Log.e("数据库", "information_Commodities: "+"连接成功" );
                  insert_information_Commodities(picture,content,price,number,user_phone);
              }catch (Exception e){
                  Log.e("数据库", "information_Commodities: "+"连接失败" );
                  e.printStackTrace();
              }
          }
      }).start();
  }
    private void insert_information_Commodities(final byte[] picture, final String content, final String price, final String number, final String user_phone) throws SQLException {
        String sql_insert = "insert commodities set picture=?,content=?,price=? ,number=?";
        PreparedStatement pst = cn.prepareStatement(sql_insert);
        pst.setBytes(1, picture);
        pst.setString(2, content);
        pst.setString(3, price);
        pst.setString(4, number);
        pst.executeUpdate();
        Log.e("Commoditie","更新成功");
        pst.close();
    }


    /**
     * 首页商品加载
     *
     * */
    public void query_posts_all(final String sign) {//传入的是标志,每个碎片布局都要自定义一个标志,比如我穿的是首页标志,代表你现在在首页要请求数据库数据再展示
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    cn = MysqlUtil.getConn();
                    Log.e("数据库", "information_register: "+"首页连接成功" );
                    //开始查询操作
                    query_values_all = query_all();//把返回的数据给了query_values_all的数组
                    //用handle发送数据给活动，不能直接调用返回的值，因为当线程销毁，数据也将消失
                    if (sign == "home") {//首页标志,返回handler请求
                        Message msg = Message.obtain();
                        Bundle c = new Bundle();
                        //这个count代表的是对应用户数据库查找的行数，7是代表限制recyclerView的条数
                        c.putInt("Query_count", Query_count*3);
                        msg.setData(c);
                        msg.obj = query_values_all;
                        //记得在活动里声明实例化一个全局的handler，获取值
                       Homepage_Fragment.handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    Log.e("query_posts: ","连接失败" );
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //查询所有商品的方法
    private String[] query_all() throws SQLException {
        Query_count = 0;
        String sql_query_posts = "select * FROM commodities ";
        Statement statement = cn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql_query_posts);
        int i=-1;
        String[] query_s = new String[999];
        while (resultSet.next()) {
            Query_count++;
           // query_picture=resultSet.getBytes("picture");
            query_content = resultSet.getString("content");
            query_price = resultSet.getString("price");
            query_number = resultSet.getString("number");
          //  Bitmap bmpout_pic_head = BitmapFactory.decodeByteArray(query_picture, 0, query_picture.length);
            query_s[++i]=query_content;
            query_s[++i]=query_price;
            query_s[++i]=query_number;
            Log.e("update_count", "query_all: "+Query_count);
        }
        return query_s;
    }


    /**
     * 添加收货地址的信息
     *
     */
    public void information_address(final String user_phone, final String consignee, final String phonenumber, final String city,final String address){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    cn = MysqlUtil.getConn();
                    Log.e("数据库", "information_register: "+"连接成功" );
                    insert_information_address(user_phone,consignee,phonenumber,city,address);
                }catch (Exception e){
                    Log.e("数据库", "information_register: "+"连接失败" );
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void insert_information_address(final String user_phone, final String consignee, final String phonenumber, final String city,final String address) throws SQLException {
        String sql_insert = "insert receiving_address set user_phone=?,consignee=?,phonenumber=? ,city=?,address=?";
        PreparedStatement pst = cn.prepareStatement(sql_insert);
        pst.setString(1, user_phone);
        pst.setString(2, consignee);
        pst.setString(3, phonenumber);
        pst.setString(4, city);
        pst.setString(5, address);
        pst.executeUpdate();
        Log.e("SDS","更新成功");
        pst.close();
    }


    /**
     * 获取保存的收货地址信息
     */
    public static String[] query_address_all = new String[999];
    public static int Query_address_count=0;
    public static String query_name,query_phone,query_city,query_address;
    public void query_address_all(String u_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    cn = MysqlUtil.getConn();
                    query_address_all = getQuery_address_all(u_id);//把返回的数据给了query_values_all的数组
                    //用handle发送数据给活动，不能直接调用返回的值，因为当线程销毁，数据也将消失
                        Message msg = Message.obtain();
                        Bundle c = new Bundle();
                        //这个count代表的是对应用户数据库查找的行数，7是代表限制recyclerView的条数
                        c.putInt("Query_address_count", Query_address_count*3);
                        msg.setData(c);
                        msg.obj = query_address_all;
                        //记得在活动里声明实例化一个全局的handler，获取值
                        Receiving_address_Activity.handler.sendMessage(msg);

                }catch (Exception e){
                    Log.e("query_posts: ","连接失败" );
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //查询所有商品的方法
    private String[] getQuery_address_all(String u_id) throws SQLException {
        Query_address_count = 0;
        String sql_query_posts = "select consignee,phonenumber,city,address FROM receiving_address where user_phone="+u_id;
        Statement statement = cn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql_query_posts);
        int i=-1;
        String[] query_s = new String[999];
        while (resultSet.next()) {
            Query_address_count++;
            query_name = resultSet.getString("consignee");
            query_phone = resultSet.getString("phonenumber");
            query_city = resultSet.getString("city");
            query_address = resultSet.getString("address");

            query_s[++i]=query_name;
            query_s[++i]=query_phone;
            query_s[++i]=query_city;
            query_s[++i]=query_address;
        }
        return query_s;
    }


    /**
     * 购物车添加
     *
     */
    public  void shop_car_register(String user_phone, byte[] picture, String content, String price, String number){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MysqlUtil.drive();
                    cn = MysqlUtil.getConn();
                    Log.e("数据库", "information_register: "+"连接成功" );
                    insert_shop_car(user_phone,picture,content,price,number);
                }catch (Exception e){
                    Log.e("数据库", "information_register: "+"连接失败" );
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void insert_shop_car(String user_phone, byte[] picture, String content, String price, String number) throws SQLException {
        String sql_insert = "insert shop_car set user_phone=?,picture=?,content=?,price=?,number=?";
        PreparedStatement pst = cn.prepareStatement(sql_insert);
        pst.setString(1, user_phone);
        pst.setBytes(2, picture);
        pst.setString(3, content);
        pst.setString(4, price);
        pst.setString(5, number);
        pst.executeUpdate();
        Log.e("SDS","更新成功");
        pst.close();
    }

    /**
     * 显示购物车信息
     */









    /*-----------------------------------------从数据库里查询图片---------------------------------------------*/
    private byte[] User_head_query;
    private String User_query;
    private List bitmap = new ArrayList();
    private String user_nick_name,user_phone_attention,price,number;
    private String sign_my;
    private List list = new ArrayList<>();
    //定义接口六,insert的，查询显示用户头像
    public interface User_Head_Pic_query{
        public void user_head_pic_query() throws SQLException;
    }
    //建立工厂模式
    public User_Head_Pic_query Factory_Pic_query(String user,String sign){ //传入标志，复用代码
        User_query = user;
        sign_my = sign;
        return new Pic_query();
    }
    private class Pic_query implements User_Head_Pic_query {
        @Override
        public void user_head_pic_query() throws SQLException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MysqlUtil.drive();
                        cn = MysqlUtil.getConn();
                        list.clear();
                        Log.e("head_pic_query: ","连接成功" );
                        if (sign_my == "MyFragment") {
                            Log.e("MyFragment", "run: MyFragment" );
                            String sql = "select * from login where user_phone = ";
                            bitmap = query_pic(User_query,sql);//传入账号
                            Message msg = Message.obtain();
                            msg.obj = bitmap;
                            My_Fragment.handler.sendMessage(msg);
                      }
                        else if (sign_my == "ShoppingCar_Fragment"){
                            Log.e("ShoppingCar_Fragment", "run: ShoppingCar_Fragment" );
                            String sql = "select * from shop_car where user_phone = ";
                            bitmap = query_pic(User_query,sql);//传入账号
                            Message msg = Message.obtain();
                            msg.obj = bitmap;
                            ShoppingCar_Fragment.handler_shop.sendMessage(msg);
                        }
                    }catch (Exception e){
                        Log.e("head_pic_query: ","连接失败" );
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        private List query_pic(String account,String sql) throws SQLException {
            Log.e("数据库,", "用户头像是否为空- "+",账号："+account);
            String sql_query_pic = sql + account;
            Statement statement = cn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_query_pic);
            while (resultSet.next()) {
               if (sign_my == "MyFragment") {
                    if (account.equals(resultSet.getString("user_phone"))) {
                        User_head_query = null;
                        user_nick_name = null;
                        user_phone_attention = null;
                        Log.e("2222222", "query_pic: ");
                        User_head_query = resultSet.getBytes("user_photo");
                        Bitmap bmpout_pic_head = BitmapFactory.decodeByteArray(User_head_query, 0, User_head_query.length);
                        list.add(bmpout_pic_head); //头像
                        return list;
                    }
               }
               else if (sign_my=="ShoppingCar_Fragment"){
                   if (account.equals(resultSet.getString("user_phone"))) {
                       User_head_query = null;
                       user_nick_name = null;
                       price = null;
                       number=null;
                       Log.e("3333", "query_pic: ");
                       User_head_query = resultSet.getBytes("picture");
                       user_nick_name = resultSet.getString("content");
                       price = resultSet.getString("price");
                       number = resultSet.getString("number");
                       Bitmap bmpout_pic_head = BitmapFactory.decodeByteArray(User_head_query, 0, User_head_query.length);
                       list.add(bmpout_pic_head);
                       list.add(user_nick_name);
                       list.add(price);
                       list.add(number);
                   }
               }
            }
            statement.close();
            resultSet.close();
            return list;
        }
    }
}
