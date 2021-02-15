package com.example.campusdepartment.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campusdepartment.R;
import com.example.campusdepartment.adapter.AddressAdapter;
import com.example.campusdepartment.adapter.AddressBean;
import com.example.campusdepartment.other.ReadFile;
import com.example.campusdepartment.other.UpdateFactory;

import java.util.ArrayList;
import java.util.List;

public class Receiving_address_Activity extends BaseActivity implements View.OnClickListener {
    TextView add_receiving;
    ImageView back;
    ListView listView;
    List<AddressBean> mList = new ArrayList<>();
    public static Handler handler;
    //接收数据库数据所需要的数组，数据信息
    public static String[] getData;
    private String  u_id;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_address);
        u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
        initView();
        data();


    }
    private void initView(){
        listView = findViewById(R.id.listview);
        add_receiving=findViewById(R.id.receiving);
        back=findViewById(R.id.back);
        add_receiving.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void data(){
        final UpdateFactory factory = new UpdateFactory();
        factory.query_address_all(u_id);
        final int[] cc = {0};
        handler = new Handler() {
            @SuppressLint("RestrictedApi")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                getData = (String[]) msg.obj;
                Log.e("bbb", getData+"");
                Bundle c = msg.getData();
                cc[0] = c.getInt("Query_address_count");
                Log.e("Query_address_count", "Query_address_count: " + cc[0]);
                if (cc[0] > 0) {
                    for (int i = 0; i < cc[0]; i++) {
                        final AddressBean postBean = new AddressBean();
                        postBean.setName(getData[i]);
                        postBean.setPhonenumber(getData[++i]);
                        postBean.setCity(getData[++i]);
                        postBean.setAddress(getData[++i]);
                        mList.add(postBean);
                        Log.e("bbb", "cccc");
                        AddressAdapter myAdapter = new AddressAdapter(Receiving_address_Activity.this, mList);
                        listView.setAdapter(myAdapter);
                       // listView.notifyAll();
                    }
                }
            }
        };
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.receiving:
                startActivity(new Intent(Receiving_address_Activity.this,UserInfoActivity.class));
                finish();
                break;
            case R.id.back:
               finish();
                break;
        }
    }
}
