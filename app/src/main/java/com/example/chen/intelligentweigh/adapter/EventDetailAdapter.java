package com.example.chen.intelligentweigh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.EventDetail;

import java.util.List;

/**
 * @author chen
 * @date 2019/8/6.   11:45
 * description：
 */
public class EventDetailAdapter extends ArrayAdapter<EventDetail> {

    private Context context;
    private int resource;
    private List<EventDetail> list;


    public EventDetailAdapter(@NonNull Context context, int resource, @NonNull List<EventDetail> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventDetail eventDetail = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_cowdetail_id = (TextView) view.findViewById(R.id.tv_cowdetail_id);
            viewHolder.tv_cowdetail_weight = (TextView) view.findViewById(R.id.tv_detailcow_weight);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_cowdetail_id.setText("ID: "+eventDetail.getCattleid());
        viewHolder.tv_cowdetail_weight.setText("称重重量："+eventDetail.getWeight()+" 公斤");
        return view;
    }


    class ViewHolder{
        TextView tv_cowdetail_id;
        TextView tv_cowdetail_weight;
    }
}
