package com.example.campusdepartment.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.campusdepartment.R;


public class WelcomeActivity extends BaseActivity {
    // 声明控件对象
    private TextView textView, pass;
    //声明时间有多少;
    private int count = 3;
    private Animation animation;
    //进行一个消息的处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                textView.setText(getCount() + "");
                handler.sendEmptyMessageDelayed(0, 1000);
                animation.reset();
                textView.startAnimation(animation);
            }

        }

        ;

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 下面的话就是去除标题的方法
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        // 初始化控件对象textView
        textView = (TextView) findViewById(R.id.textView);
        pass = (TextView) findViewById(R.id.pass);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_text);
        handler.sendEmptyMessageDelayed(0, 1000);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                handler.removeCallbacksAndMessages(null);//停止handler
            }
        });
    }

    //咱在写一个计算Welcome界面的广告时间结束后进入主界面的方法
    private int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
}
