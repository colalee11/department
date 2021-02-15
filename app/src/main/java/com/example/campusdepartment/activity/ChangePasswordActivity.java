package com.example.campusdepartment.activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdepartment.R;
import com.example.campusdepartment.Utils.MysqlUtil;
import com.example.campusdepartment.other.ReadFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    public static String u_id, user_check;
    EditText phone, password, check_password;
    Button button;
    String UserName;
    private ImageView back;
    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initview();

    }

    private void initview() {
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        check_password = (EditText) findViewById(R.id.check_password);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        button = (Button) findViewById(R.id.zhuce);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zhuce:
                u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MysqlUtil.drive();
                            connection = MysqlUtil.getConn();
                            String sql = "SELECT user_password FROM login where user_phone=" + u_id;
                            Statement statement = null;
                            statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery(sql);
                            while (rs.next()) {
                                UserName = rs.getString("user_password");
                                Log.e("cdc", UserName);
                            }
                            if (phone.getText().toString().trim().equals(UserName)) {
                                if (password.getText().toString().trim().equals(check_password.getText().toString().trim()) && !password.getText().toString().trim().equals("") && !check_password.getText().toString().trim().equals("")) {
                                    String sql_insert = "update login set user_password=? where user_phone=" + u_id;
                                    PreparedStatement pst = connection.prepareStatement(sql_insert);
                                    pst.setString(1, password.getText().toString().trim());
                                    pst.executeUpdate();
                                    pst.close();
                                    Looper.prepare();
                                    Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(ChangePasswordActivity.this, "两次密码不一样或输入不能为空，请重新输入", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                Looper.prepare();
                                Toast.makeText(ChangePasswordActivity.this, "旧密码错误请重新输入", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}