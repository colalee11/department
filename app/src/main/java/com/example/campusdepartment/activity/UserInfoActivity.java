package com.example.campusdepartment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusdepartment.R;
import com.example.campusdepartment.adapter.JDCityPicker;
import com.example.campusdepartment.other.ReadFile;
import com.example.campusdepartment.other.UpdateFactory;


public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    TextView mTvSlect, mTvCity;
    EditText set_name, set_phone, set_address;
    Button save;
    ImageView back;
    JDCityPicker mJDCityPicker;
    public static String u_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
        initView();
    }

    private void initView() {
        mTvSlect = findViewById(R.id.tv_select);
        mTvCity = findViewById(R.id.tv_city_pick);
        set_name = findViewById(R.id.set_name);
        set_phone = findViewById(R.id.set_phone);
        set_address = findViewById(R.id.set_address);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);

        back.setOnClickListener(this);
        mTvSlect.setOnClickListener(this);
        set_name.setOnClickListener(this);
        set_phone.setOnClickListener(this);
        set_address.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    //背景变暗
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                bgAlpha(0.7f);
                mJDCityPicker = new JDCityPicker(UserInfoActivity.this, new JDCityPicker.onCitySelect() {
                    @Override
                    public void onSelect(String province, String city, String area) {
                        mTvCity.setText(province + "   " + city + "   " + area);
                    }
                });
                mJDCityPicker.showAtLocation(mTvSlect, Gravity.BOTTOM, 0, 0);
                mJDCityPicker.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bgAlpha(1.0f);
                    }
                });
                break;
            case R.id.save:
                String name = set_name.getText().toString().trim();
                String phone = set_phone.getText().toString().trim();
                String city = mTvCity.getText().toString().trim();
                String address = set_address.getText().toString().trim();
                UpdateFactory updateFactory = new UpdateFactory();
                if (!name.equals("") && !phone.equals("") && !city.equals("") && !address.equals("")) {
                    updateFactory.information_address(u_id, name, phone, city, address);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } else if (name.equals("")) {
                    Toast.makeText(this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
                } else if (phone.equals("")) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (city.equals("")) {
                    Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                startActivity(new Intent(UserInfoActivity.this, Receiving_address_Activity.class));
                finish();
                break;
        }
    }
}