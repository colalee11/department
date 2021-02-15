package com.example.campusdepartment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campusdepartment.R;
import com.example.campusdepartment.fragment.Homepage_Fragment;
import com.example.campusdepartment.fragment.Message_Fragment;
import com.example.campusdepartment.fragment.My_Fragment;
import com.example.campusdepartment.fragment.ShoppingCar_Fragment;
import com.example.campusdepartment.other.BottomNavigationViewHelper;
import com.example.campusdepartment.other.ReadFile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BuyerActivity extends BaseActivity {
    public static String u_id, user_check;
    protected boolean useStatusBarColor = true;
    AlertDialog alertDialog;
    int lastSelectedPosition = 0;
    Homepage_Fragment homepage_fragment;
    Message_Fragment message_fragment;
    My_Fragment my_fragment;
    ShoppingCar_Fragment shoppingCar_fragment;
    private BottomNavigationView navigationBar;
    private FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        initView();
        user();
        getFragment();
        showFragment(homepage_fragment);

    }

    private void addFragemnt(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void hildFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    private void initView() {
        navigationBar = findViewById(R.id.bottom_navigation_bar);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        BottomNavigationViewHelper.removeNavigationShiftMode(navigationBar);
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        hile();
                        showFragment(homepage_fragment);
                        break;
                    case R.id.message:
                        user();
                        if (u_id.equals("")) {
                            check_login_alertDialog();

                        } else {
                            hile();
                            showFragment(message_fragment);
                        }
                        break;
                    case R.id.shoppingcar:
                        user();
                        if (u_id.equals("")) {
                            check_login_alertDialog();
                        } else {
                            hile();
                            showFragment(shoppingCar_fragment);
                        }
                        break;
                    case R.id.my:
                        user();
                        if (u_id.equals("")) {
                            check_login_alertDialog();
                        } else {
                            hile();
                            showFragment(my_fragment);
                            break;
                        }
                }
                return true;
            }
        });
    }

    private void check_login_alertDialog() {
        alertDialog = new AlertDialog.Builder(BuyerActivity.this)
                .setTitle("温馨提示")
                .setMessage("你还没有登录，请先登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1 = new Intent(BuyerActivity.this, LoginActivity.class);
                        startActivity(intent1);
                    }
                }).create();
        alertDialog.show();
    }

    private void getFragment() {
        homepage_fragment = new Homepage_Fragment();
        message_fragment = new Message_Fragment();
        shoppingCar_fragment = new ShoppingCar_Fragment();
        my_fragment = new My_Fragment();
        addFragemnt(homepage_fragment);
        addFragemnt(message_fragment);
        addFragemnt(shoppingCar_fragment);
        addFragemnt(my_fragment);

        hildFragment(homepage_fragment);
        hildFragment(message_fragment);
        hildFragment(shoppingCar_fragment);
        hildFragment(my_fragment);
    }

    private void hile() {
        hildFragment(homepage_fragment);
        hildFragment(message_fragment);
        hildFragment(shoppingCar_fragment);
        hildFragment(my_fragment);
    }

    private void user() {
        u_id = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_data.txt");
        user_check = ReadFile.ReadTxtFile("/data/data/com.example.campusdepartment/files/user_check.txt");
        if (!TextUtils.isEmpty(u_id)) {

        } else if ("".equals(u_id) || u_id == null) {
            u_id = "";
            Log.e("user", "u_id-MainActivity: " + u_id);
        }
    }


}
