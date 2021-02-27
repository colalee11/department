package com.example.campusdepartment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.campusdepartment.R;

import java.util.List;

/**
 * Created by 林嘉煌 on 2021/1/5.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    View view;
    private List<HomeBen> list;
    private Context context = null;
    private OnItemClickListener mOnItemClickListener;

    public HomeAdapter(List<HomeBen> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeBen fruit = list.get(position);
        holder.picture.setImageBitmap(fruit.getPicture());
        holder.content.setText(fruit.getContent());
        holder.price.setText(fruit.getPrice());
        holder.number.setText(fruit.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView content, price, number;

        public ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) view.findViewById(R.id.picture);
            content = (TextView) view.findViewById(R.id.content);
            price = (TextView) view.findViewById(R.id.price);
            number = (TextView) view.findViewById(R.id.number);
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //确保position值有效
                    if (position != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onItemClick(view, position);
                    }

                }
            });
        }
    }

}
