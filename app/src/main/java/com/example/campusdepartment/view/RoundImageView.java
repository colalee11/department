package com.example.campusdepartment.view;

/**
 * Created by 林嘉煌 on 2021/1/2.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author xiejinbo
 * @date 2019/11/28 0028 13:40
 */
@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {


    private float width,height;

    private Context context;
    /**
     * 初始化圆角为15
     */
    private float radius= 10;

    public RoundImageView(Context context) {
        this(context, null);
        this.context = context;
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > radius && height > radius) {
            Path path = new Path();
            path.moveTo(radius, 0);
            path.lineTo(width - radius, 0);
            path.quadTo(width, 0, width, radius);
            path.lineTo(width, height - radius);
            path.quadTo(width, height, width - radius, height);
            path.lineTo(radius, height);
            path.quadTo(0, height, 0, height - radius);
            path.lineTo(0, radius);
            path.quadTo(0, 0, radius, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置图片圆角dp值
     * @param context
     * @param radius
     */
    public  void setImageViewRadius(Context context, float radius){
        this.radius = dip2px(context,radius);
    }

}
