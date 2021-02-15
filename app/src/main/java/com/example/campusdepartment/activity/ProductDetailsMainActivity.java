package com.example.campusdepartment.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusdepartment.R;
import com.example.campusdepartment.SQLite.ContentSQLiteHelper;
import com.example.campusdepartment.Utils.MysqlUtil;
import com.example.campusdepartment.other.ReadFile;
import com.example.campusdepartment.other.UpdateFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ProductDetailsMainActivity extends BaseActivity implements View.OnClickListener {
    Cursor cursor1;
    TextView content, price, shopcar;
    ImageView picture, back;
    Connection connection;
    String name, price1, content1, price2, number1, phone1;
    Bitmap ing;
    String u_id;
    boolean check = false;
    ContentSQLiteHelper searchSqliteHelper;
    private byte[] user_pic;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_main);
        initview();
        u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
        Bundle b = getIntent().getExtras();
        name = b.getString("content");//商品名
        ing = b.getParcelable("picture");//商品照片
        price1 = b.getString("price");//商品价格
        picture.setImageBitmap(ing);
        Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
        //上传该图片到服务器,传入图片资源，转换为二进制形式保存在byte[]数组里
        user_pic = bmpToByteArray(bitmap);
        //Bitmap to byte[] ,存图片
        content.setText(name);
        price.setText(price1);
    }

    //Bitmap to byte[] ,存图片
    public byte[] bmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void initview() {
        content = findViewById(R.id.content);
        price = findViewById(R.id.price);
        picture = findViewById(R.id.picture);
        shopcar = findViewById(R.id.shopcar);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        shopcar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shopcar:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MysqlUtil.drive();
                            connection = MysqlUtil.getConn();
                            String sql = "SELECT * FROM shop_car where user_phone=" + u_id;
                            Statement statement = null;
                            statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery(sql);
                            String phone[] = new String[20];
                            String content[] = new String[20];
                            String price[] = new String[20];
                            String number[] = new String[20];
                            int i = 0, j = 0, p = 0, c = 0;
                            while (rs.next()) {
                                phone1 = rs.getString("user_phone");
                                content1 = rs.getString("content");
                                price2 = rs.getString("price");
                                number1 = rs.getString("number");
                                phone[c++] = phone1;
                                content[i++] = content1;
                                price[j++] = price2;
                                number[p++] = number1;
                            }

                            for (i = 0, j = 0, p = 0; i < phone.length; i++, j++, p++) {
                                if (name.equals(content[i]) && price1.equals(price[j])) {
                                    String sql_insert = "update shop_car set picture=?,content=?,price=? ,number=? where price =" + price[j];

                                    PreparedStatement pst = connection.prepareStatement(sql_insert);
                                    pst.setBytes(1, user_pic);
                                    pst.setString(2, name);
                                    pst.setString(3, price1);
                                    pst.setInt(4, Integer.parseInt(number[p]) + 1);
                                    pst.executeUpdate();
                                    pst.close();
                                    Looper.prepare();
                                    check = true;
                                    Toast.makeText(ProductDetailsMainActivity.this, "购物车中已存在，数量加一", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                            if (check == false) {
                                UpdateFactory updateFactory = new UpdateFactory();
                                updateFactory.shop_car_register(u_id, user_pic, name, price1, "1");
                                Looper.prepare();
                                Toast.makeText(ProductDetailsMainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.back:
                startActivity(new Intent(ProductDetailsMainActivity.this, SearchMainActivity.class));
                finish();
                break;
        }
    }

}
