package com.example.campusdepartment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.campusdepartment.R;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private List<AddressBean> list;
    private LayoutInflater layoutInflater;

    public AddressAdapter(Context context, List<AddressBean> list) {
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
            view = layoutInflater.inflate(R.layout.list_address, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AddressBean addressBean = (AddressBean) getItem(i);
        viewHolder.name.setText(addressBean.getName());
        viewHolder.phone.setText(addressBean.getPhonenumber());
        viewHolder.address.setText(addressBean.getCity() + addressBean.getAddress());
        return view;
    }

    class ViewHolder {
        private TextView name, phone, address;

        public ViewHolder(View view) {
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            address = view.findViewById(R.id.address);
        }
    }
}
