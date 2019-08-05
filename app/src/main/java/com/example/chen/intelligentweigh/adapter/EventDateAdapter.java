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
import com.example.chen.intelligentweigh.bean.EventDate;

import java.util.List;

/**
 * @author chen
 * @date 2019/8/5.   21:40
 * description：
 */
public class EventDateAdapter extends ArrayAdapter<EventDate> {

    private Context context;
    private List<EventDate> eventDates;
    private int resource;


    public EventDateAdapter(@NonNull Context context, int resource, @NonNull List<EventDate> eventDates) {
        super(context, resource, eventDates);
        this.context = context;
        this.resource = resource;
        this.eventDates = eventDates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        EventDate eventDate = getItem(position);
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_eventDate = (TextView) view.findViewById(R.id.tv_eventdate);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_eventDate.setText(eventDate.getWtime()+" 称重未填写今日肉价!");
        return view;
    }


    class ViewHolder{
        TextView tv_eventDate;
    }
}
