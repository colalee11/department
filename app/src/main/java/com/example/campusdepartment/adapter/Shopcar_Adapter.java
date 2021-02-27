package com.example.campusdepartment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.campusdepartment.R;

import java.util.List;

public class Shopcar_Adapter extends BaseAdapter {
    private Context mContext;
    private List<ShopcarBean> list;
    private LayoutInflater layoutInflater;
    //    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    private View.OnClickListener mDelClickListener;

    //, View.OnClickListener delClickListener
    public Shopcar_Adapter(Context context, List<ShopcarBean> list, View.OnClickListener delClickListener) {
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        mDelClickListener = delClickListener;
    }

    public Shopcar_Adapter(Context context, List<ShopcarBean> list) {
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    //    public void addItem(DataHolder item) {
//        mDataList.add(item);
//        notifyDataSetChanged();
//    }
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


        shopcarBean.rootView = (LinearLayout) view.findViewById(R.id.lin_root);
        shopcarBean.rootView.scrollTo(0, 0);
        TextView delTv = (TextView) view.findViewById(R.id.del);
        delTv.setOnClickListener(mDelClickListener);
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
//    public static class DataHolder {
//        public String title;
//        public LinearLayout rootView;
//    }
}
