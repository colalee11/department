package com.example.campusdepartment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.campusdepartment.R;

import java.util.List;


/**
 * Created by 林嘉煌 on 2020/5/3.
 */

public class RecordAdapter extends BaseAdapter {
    private List<RecordBen> list;
    private LayoutInflater layoutInflater;

    public RecordAdapter(Context context, List<RecordBen> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list==null?0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if ( convertView==null ){
            convertView=layoutInflater.inflate(R.layout.search_list,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        RecordBen noteInfo= (RecordBen) getItem(position);

        viewHolder.name.setText(noteInfo.getName());

        return convertView;
    }
    class ViewHolder{
        private TextView name;
        public ViewHolder(View view){
           name= (TextView) view.findViewById(R.id.text1);


        }
    }
}
