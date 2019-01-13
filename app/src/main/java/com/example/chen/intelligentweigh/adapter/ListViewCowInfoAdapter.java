package com.example.chen.intelligentweigh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.House;

import java.util.List;

/**
 * @author chen
 * @date 2018/12/6.   18:59
 * description：cow列表
 */
public class ListViewCowInfoAdapter extends ArrayAdapter<Cow> {
    private Context context;
    private int resource;
    private List<Cow> list;

    public ListViewCowInfoAdapter(@NonNull Context context, int resource, @NonNull List<Cow> list) {
        super(context, resource, list);
        this.resource = resource;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cow cow = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_namearea = (TextView) view.findViewById(R.id.tv_namearea);
            viewHolder.iv_cow_icon = (ImageView) view.findViewById(R.id.iv_cow_icon);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_id = (TextView) view.findViewById(R.id.tv_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_namearea.setText(cow.getName()+"·"+cow.getFarmname());
        viewHolder.tv_type.setText(cow.getKind());
        Glide.with(context).load(R.drawable.cow).into(viewHolder.iv_cow_icon);
        viewHolder.tv_id.setText("编号:"+cow.getID());
        return view;
    }


    class ViewHolder {
        ImageView iv_cow_icon;
        TextView tv_namearea;
        TextView tv_type;
        TextView tv_id;
    }
}


