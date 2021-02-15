package com.example.campusdepartment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campusdepartment.R;
import com.example.campusdepartment.Utils.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    // 主线程响应方法，用于显示提示气泡
    public Runnable runShowResult = new Runnable() {

        @Override
        public void run() {

            // 弹出气泡
            if (resultCount == -1) {
                Toast.makeText(getApplicationContext(), "用户已注册，注册失败", Toast.LENGTH_SHORT).show();
            } else if (resultCount == 1) {
                // Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, Register_info_activity.class);
                intent.putExtra("user", phone.getText().toString());
                startActivity(intent);
                finish();
            }

        }
    };
    private Connection connection = null;
    private EditText phone;
    private EditText password2, check_password;
    private CheckBox checkBox;
    private ImageView back;
    private int resultCount;
    Button button;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        password2 = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.user);
        check_password = (EditText) findViewById(R.id.check_password);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        button = (Button) findViewById(R.id.zhuce);
        button.setOnClickListener(this);
    }

    private int insert() throws java.sql.SQLException {
        int Count = 1;
        String user_phone = phone.getText().toString();
        String user_password = password2.getText().toString();
        String sql_queryALL = "SELECT * FROM login ";
        String sql_insert = "insert into login (user_phone,user_password,user_identity,user_realname,user_name,user_photo,user_sex) values (?,?,?,?,?,?,?)";
        //String sql_delete = "delete from test where phone=" + editText.getText().toString().trim();
        java.sql.Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql_queryALL);
        Log.e("insert: ", "连接成功");
        String user[] = new String[20];
        String passwd[] = new String[20];
        int i = 0, j = 0;
        while (rs.next()) {//循环数据库数据，每条数据都保存到数组user里面
            String UserName = rs.getString("user_phone");
            String UserPasswd = rs.getString("user_password");
            user[i++] = UserName;
            passwd[j++] = UserPasswd;
        }
        //注册提示算法
        for (i = 0; i < user.length; i++) {
            //输入的数据和数组挨个比对，一条输入的数据和数组全部数据进行比对，判断是否注册
            if (!user_phone.equals(user[i])) {

                if (user_phone.equals("")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                } else if (checkUsername(user_phone) == false) {

                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else if (user_password.length() < 6 && !user_password.equals("")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "密码太简单，请重试", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                } else if (user_password.equals("")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                } else if (!check_password.getText().toString().trim().equals(user_password)) {
                    if (check_password.getText().toString().trim().equals("")) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    }
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                continue;
            }
            //比对成功则执行else，显示已注册账号
            else if (user_phone.equals(user[i])) {
                //返回注册失败的值
                return Count = -1;
            }
        }
        if (checkBox.isChecked()) {
            //商家注册成功
            PreparedStatement pst = connection.prepareStatement(sql_insert);
            pst.setString(1, user_phone);
            pst.setString(2, user_password);
            pst.setString(3, "true");
            pst.setString(4, "");
            pst.setString(5, "");
            pst.setString(6, "");
            pst.setString(7, "");
            pst.executeUpdate();

        } else {
            //买家注册成功
            PreparedStatement pst = connection.prepareStatement(sql_insert);
            pst.setString(1, user_phone);
            pst.setString(2, user_password);
            pst.setString(3, "false");
            pst.setString(4, "");
            pst.setString(5, "");
            pst.setString(6, "");
            pst.setString(7, "");
            pst.executeUpdate();
        }
        return Count;
    }

    //判断手机号是否正确
    private boolean checkUsername(String username) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce:
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

                            resultCount = insert();
                            Log.e("run: ", String.valueOf(resultCount));
                            // 使用handler，使主线程响应并执行runShowResult方法
                            handler.post(runShowResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                try {
                                    connection.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
                break;
            case R.id.back:
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
        }
    }
}


