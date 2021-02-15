package com.example.campusdepartment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 林嘉煌 on 2021/1/4.
 */

public class RecyclerViewForScrollView extends RecyclerView {


    public RecyclerViewForScrollView(Context context) {
        super(context);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs,
                                     int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 只需要重写这个方法即可
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}