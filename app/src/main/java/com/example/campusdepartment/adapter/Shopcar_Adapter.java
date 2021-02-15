package com.example.campusdepartment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campusdepartment.R;

import java.util.List;

public class Shopcar_Adapter extends BaseAdapter {
    private List<ShopcarBean> list;
    private LayoutInflater layoutInflater;

    public Shopcar_Adapter(Context context, List<ShopcarBean> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_shopcat, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ShopcarBean shopcarBean = (ShopcarBean) getItem(i);
        viewHolder.content.setText(shopcarBean.getContent());
        viewHolder.price.setText(shopcarBean.getPrice());
        viewHolder.number.setText(shopcarBean.getNumber());
        viewHolder.photo.setImageBitmap(shopcarBean.getPicture());
        return view;

    }

    class ViewHolder {
        private TextView content, price, number;
        private ImageView photo;

        public ViewHolder(View view) {
            content = view.findViewById(R.id.tiitle);
            price = view.findViewById(R.id.price);
            number = view.findViewById(R.id.number);
            photo = view.findViewById(R.id.photo);
        }
    }
}
