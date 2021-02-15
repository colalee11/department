package com.example.campusdepartment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.campusdepartment.R;
import com.example.campusdepartment.Utils.MysqlUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //文本框的账号，和判断成功
    public static String user_check = "0";
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    AlertDialog alertDialog;
    private Connection connection = null;
    private Button denglu, zhuce;
    private EditText phone, password;
    private CheckBox remenberpassword;
    private ImageView close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // setStatusBar();
        initview();
        //save(phone.getText().toString().trim(), user_check);
    }

    private void initview() {
        denglu = findViewById(R.id.denglu);
        zhuce = findViewById(R.id.zhuce);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        remenberpassword = findViewById(R.id.remenberpassword);
        close = findViewById(R.id.close);
        close.setOnClickListener(this);
        denglu.setOnClickListener(this);
        zhuce.setOnClickListener(this);
    }

    private void login() throws SQLException {
        String sql = "SELECT * FROM login ";
        //获取输入框的数据
        String user_phone = phone.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        // 创建用来执行sql语句的对象
        java.sql.Statement statement = connection.createStatement();
        // 执行sql查询语句并获取查询信息
        ResultSet rs = statement.executeQuery(sql);
        if (user_phone.equals("") || user_password.equals("")) {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            Looper.loop();

        }
        String user[] = new String[20];
        String passwd[] = new String[20];
        String identity[] = new String[20];
        int i = 0, j = 0, p = 0;
        while (rs.next()) {//循环数据库数据，每条数据都保存到数组user里面
            String UserName = rs.getString("user_phone");
            String UserPasswd = rs.getString("user_password")
                    .replace((char) 12288, ' ').trim();
            String UserIdentity = rs.getString("user_identity");
            user[i++] = UserName;
            passwd[j++] = UserPasswd;
            identity[p++] = UserIdentity;
        }
        int userlength = user.length - 1;
        Log.e("数组长度: ", String.valueOf(userlength));
        for (i = 0, j = 0, p = 0; i < user.length; i++, j++, p++) {
            //输入的数据和数组挨个比对，一条输入的数据和数组全部数据进行比对，判断是否注册
            if (!user_phone.equals(user[i])) {
                if (user_phone.equals("")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    Looper.loop();
                } else if (user_password.equals("")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    Looper.loop();
                } else if (i == userlength) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    Looper.loop();
                }
                continue;
            } else if (user_phone.equals(user[i])) {
                if (!user_password.equals(passwd[j])) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "密码不正确，请重试", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    Looper.loop();
                }

            }
            if (user_phone.equals(user[i]) || user_password.equals(passwd[j])) {
                Log.e("xxx", "aaaa");
                user_check = "1";
                if (user_check == "1") {
                    save(user_phone, user_check);
                }
                if (identity[p].equals("true")) {
                    //进入商家界面
                    Intent intent = new Intent(LoginActivity.this, SellerActivity.class);
                    startActivity(intent);
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    //进入买家界面
                    Intent intent = new Intent(LoginActivity.this, BuyerActivity.class);
                    startActivity(intent);
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }


        }

    }

    //将登录成功的账号，生成一个文件，放进去，便于读取
    public void save(String toString, String mkdirCk) {
        //账号文件
        FileOutputStream out = null;
        BufferedWriter writer = null;
        //判断是否登录
        FileOutputStream out_check = null;
        BufferedWriter writer_check = null;
        try {
            out = openFileOutput("user_data.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(toString);
            out_check = openFileOutput("user_check.txt", Context.MODE_PRIVATE);
            writer_check = new BufferedWriter(new OutputStreamWriter(out_check));
            writer_check.write(mkdirCk);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null && writer_check != null) {
                    writer.close();
                    writer_check.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.denglu:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("温馨提示")
                        .setMessage("正在登录中,请稍后..")
                        .create();
                alertDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //1、加载驱动
                            MysqlUtil.drive();
                            System.out.println("驱动加载成功！！！");
                            //2、获取与数据库的连接
                            connection = MysqlUtil.getConn();
                            System.out.println("连接数据库成功！！！");
                            login();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                break;
            case R.id.close:
                deleteFile("/data/data/com.example.campusdepartment/files/user_data.txt");
                deleteFile("/data/data/com.example.campusdepartment/files/user_check.txt");
                Intent intent1 = new Intent(LoginActivity.this, BuyerActivity.class);
                startActivity(intent1);
                break;
        }

    }
}
