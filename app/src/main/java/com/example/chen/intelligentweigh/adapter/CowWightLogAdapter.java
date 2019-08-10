package com.example.chen.intelligentweigh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.WeightLogData;

import java.util.List;

/**
 * @author chen
 * @date 2019/8/9.   15:21
 * description：
 */
public class CowWightLogAdapter extends RecyclerView.Adapter<CowWightLogAdapter.ViewHolder> {

    private List<WeightLogData> list;


    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView tv_sumweight;
        TextView tv_cownum;
        TextView tv_weighttime;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_sumweight = (TextView) view.findViewById(R.id.tv_sumweight);
            tv_cownum = (TextView) view.findViewById(R.id.tv_cownum);
            tv_weighttime = (TextView) view.findViewById(R.id.tv_weighttime);
        }
    }

    public CowWightLogAdapter(List<WeightLogData> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cowweight_log,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //将数据绑定到控件上
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeightLogData weightLogData = list.get(position);
        holder.tv_sumweight.setText("总重量："+weightLogData.getSumweigh()+"公斤");
        holder.tv_cownum.setText("数量："+weightLogData.getCattlenum()+"头");
        holder.tv_weighttime.setText(weightLogData.getWtime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<WeightLogData> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<WeightLogData> newList) {
        //刷新数据
        list.removeAll(list);
        list.addAll(newList);
        notifyDataSetChanged();
    }

}
