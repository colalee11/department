package com.example.campusdepartment.Utils;

/**
 * Created by 林嘉煌 on 2021/1/2.
 */

import android.content.Context;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.example.campusdepartment.other.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.List;

/**
 * @author xiejinbo
 * @date 2019/11/28 0028 15:20
 */
public class ImageUtils {

    /**
     * 加载图片轮播图公共方法
     * @param context 上下文
     * @param imagesList 图片资源list
     * @param radius 圆角值
     */
    /**
     * 加载图片轮播图公共方法
     *
     * @param context    上下文
     * @param imagesList 图片资源list
     * @param radius     圆角值
     */
    public static Banner initBanner(Context context, Banner banner, List<String> imagesList, final float radius) {

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader(radius));
        //防止图片切换时的直角

        banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        banner.setClipToOutline(true);

        //设置图片集合
        banner.setImages(imagesList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        return banner;
    }

}

