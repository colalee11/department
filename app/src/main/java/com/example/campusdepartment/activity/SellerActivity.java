package com.example.campusdepartment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.campusdepartment.R;


public class SellerActivity extends BaseActivity {
    TextView put;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        put = (TextView) findViewById(R.id.put);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerActivity.this, PutCommoditiesActivity.class);
                startActivity(intent);
            }
        });
    }
}
