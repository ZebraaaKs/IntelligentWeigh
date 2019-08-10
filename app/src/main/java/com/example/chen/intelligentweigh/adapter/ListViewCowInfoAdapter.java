package com.example.chen.intelligentweigh.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.WeightLogData;


import java.util.List;

/**
 * @author chen
 * @date 2018/12/6.   18:59
 * description：cow列表
 */
public class ListViewCowInfoAdapter extends RecyclerView.Adapter<ListViewCowInfoAdapter.ViewHolder>{

    private List<Cow> list;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView iv_cow_icon;
        TextView tv_namearea;
        TextView tv_type;
        TextView tv_id;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_namearea = (TextView) view.findViewById(R.id.tv_namearea);
            iv_cow_icon = (ImageView) view.findViewById(R.id.iv_cow_icon);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
        }
    }

    public ListViewCowInfoAdapter(List<Cow> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cowinfo_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    //私有属性
    private OnItemClickListener onItemClickListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, Cow cow, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Cow cow = list.get(i);
        viewHolder.tv_namearea.setText(cow.getName()+"·"+cow.getFarmname());
        viewHolder.tv_type.setText(cow.getKind());
        Glide.with(context).load(R.drawable.cow).into(viewHolder.iv_cow_icon);
        viewHolder.tv_id.setText("编号:"+cow.getID());
        //实现点击效果
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, cow, i);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<Cow> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<Cow> newList) {
        //刷新数据
        list.removeAll(list);
        list.addAll(newList);
        notifyDataSetChanged();
    }






}


