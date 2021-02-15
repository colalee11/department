package com.example.campusdepartment.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.campusdepartment.R;
import com.example.campusdepartment.other.UpdateFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 林嘉煌 on 2020/12/14.
 */

public class Register_info_activity extends BaseActivity implements View.OnClickListener {
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PICTURE = 2;
    public static Uri tempUri;
    private EditText name, nc, sex;
    private TextView ensure_register;
    private ProgressDialog progressDialog;
    //选择图片处理
    private CircleImageView imageView_head;
    private Bitmap mBitmap;
    private byte[] user_pic;
    private String register_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        Intent intent = getIntent();
        register_user = intent.getStringExtra("user");
        initView();
        initPhotoError();
    }

    //id
    private void initView() {
        name = (EditText) findViewById(R.id.ll_name_information);
        nc = (EditText) findViewById(R.id.ll_nc_information);
        sex = (EditText) findViewById(R.id.ll_sex_information);
        ensure_register = (TextView) findViewById(R.id.ll_register_information);
        imageView_head = (CircleImageView) findViewById(R.id.register_head_pic);
        ensure_register.setOnClickListener(this);
        imageView_head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_head_pic:
                showChoosePicDialog();
                break;
            case R.id.ll_register_information:
                String name_data = name.getText().toString();
                String sex_data = sex.getText().toString();
                String nc_data = nc.getText().toString();
                UpdateFactory updateFactory = new UpdateFactory();
                if (!name_data.equals("") && !sex_data.equals("") && !nc_data.equals("") && user_pic != null | sex_data.equals("男") | sex_data.equals("女")) {
                    //传入姓名，性别，昵称，图片资源
                    updateFactory.information_register(name_data, sex_data, nc_data, user_pic, register_user);
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT)
                            .show();
                    finish();
                } else if (name_data.equals("")) {
                    Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT)
                            .show();
                } else if (sex_data.equals("")) {
                    Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT)
                            .show();
                } else if (nc_data.equals("")) {
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT)
                            .show();
                } else if (!sex_data.equals("男") | !sex_data.equals("女")) {
                    Toast.makeText(this, "请输入正确性别", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请点击上方圆头，设置头像", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
        }
    }

    /**
     * 显示修改图片的对话框
     */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地图片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //选择本地图片
                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));

                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    //图片处理过程的数据返回操作
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {//resultCode意思是结果代码
            switch (requestCode) { //requestCode意思是请求代码
                case TAKE_PICTURE:
                    cutImage(tempUri);//图片裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        try {
                            setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    //图片裁剪
    private void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("检查路径", "路径不存在");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     * 上传图片到数据库里
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void setImageToView(Intent data) throws SQLException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            imageView_head.setImageBitmap(mBitmap);//显示图片
            //得到图片资源
            Bitmap bitmap = ((BitmapDrawable) imageView_head.getDrawable()).getBitmap();
            //上传该图片到服务器,传入图片资源，转换为二进制形式保存在byte[]数组里
            user_pic = bmpToByteArray(bitmap);
            //传入图片资源的byte形式，传入要上传的账号
        }
    }

    //Bitmap to byte[] ,存图片
    public byte[] bmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //禁用返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;//不执行父类点击事件
        }
        return false;//继续执行父类其他点击事件
    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}

