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
import com.example.chen.intelligentweigh.bean.CowType;
import com.example.chen.intelligentweigh.bean.House;

import java.util.List;

/**
 * @author chen
 * @date 2018/12/6.   18:59
 * description：种类列表
 */
public class ListViewChooseTypeAdapter extends ArrayAdapter<CowType> {
    private Context context;
    private int resource;
    private List<CowType> list;

    public ListViewChooseTypeAdapter(@NonNull Context context, int resource, @NonNull List<CowType> list) {
        super(context, resource, list);
        this.resource = resource;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CowType type = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_cow_typeName = (TextView) view.findViewById(R.id.tv_cow_typeName);
            viewHolder.tv_cow_typeId = (TextView) view.findViewById(R.id.tv_cow_typeId);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_cow_typeId.setText(type.getID());
        viewHolder.tv_cow_typeName.setText(type.getName());
        return view;
    }


    class ViewHolder {
        TextView tv_cow_typeName;
        TextView tv_cow_typeId;
    }
}


