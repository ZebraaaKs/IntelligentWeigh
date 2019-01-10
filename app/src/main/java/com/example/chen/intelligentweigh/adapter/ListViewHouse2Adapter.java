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
import com.example.chen.intelligentweigh.bean.House;

import java.util.List;

/**
 * @author chen
 * @date 2018/12/6.   18:59
 * description：牧场列表
 */
public class ListViewHouse2Adapter extends ArrayAdapter<House> {
    private Context context;
    private int resource;
    private List<House> list;

    public ListViewHouse2Adapter(@NonNull Context context, int resource, @NonNull List<House> list) {
        super(context, resource, list);
        this.resource = resource;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        House house = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_house_name = (TextView) view.findViewById(R.id.tv_house_name);
            viewHolder.tv_house_id = (TextView) view.findViewById(R.id.tv_house_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_house_id.setText(house.getID());
        viewHolder.tv_house_name.setText(house.getName());
        return view;
    }


    class ViewHolder {
        TextView tv_house_name;
        TextView tv_house_id;
        TextView tv_house_addr;
    }
}


