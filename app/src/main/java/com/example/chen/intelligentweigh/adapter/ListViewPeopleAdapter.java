package com.example.chen.intelligentweigh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;

import java.util.List;

/**
 * @author chen
 * @date 2018/12/5.   15:32
 * description：
 */
public class ListViewPeopleAdapter extends ArrayAdapter<People> {

    private Context context;
    private int resource;
    private List<People> list;
    private String TAG = "ListViewPeopleAdapter";


    public ListViewPeopleAdapter(@NonNull Context context, int resource, @NonNull List<People> list) {
        super(context, resource, list);
        this.resource = resource;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        People people = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_touxiang = (ImageView) view.findViewById(R.id.iv_touxiang);
            viewHolder.tv_houseinfo = (TextView) view.findViewById(R.id.tv_house);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(HttpUrlUtils.TOUXIANG_URL+people.getTouxiang())
                .error(R.drawable.moren)
                .into(viewHolder.iv_touxiang);
        viewHolder.tv_phone.setText(people.getPhone());
        viewHolder.tv_name.setText(people.getName());
        String farmids = people.getFarmids();
        Log.e(TAG,"牧场:"+farmids);
        if (farmids!=null){
            viewHolder.tv_houseinfo.setText(farmids);
        }else{
            viewHolder.tv_houseinfo.setTextColor(context.getResources().getColor(R.color.txt_red));
            viewHolder.tv_houseinfo.setText("暂无绑定");
        }
        return view;
    }


}

class ViewHolder{
    ImageView iv_touxiang;
    TextView tv_name;
    TextView tv_houseinfo;
    TextView tv_phone;
}
