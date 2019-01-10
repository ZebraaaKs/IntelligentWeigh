package com.example.chen.intelligentweigh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowMangerAreaActivity;
import com.example.chen.intelligentweigh.adapter.ListViewHouse2Adapter;
import com.example.chen.intelligentweigh.adapter.ListViewHouseAdapter;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.kidFragment.CowManagerAreaFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;

/**
 * author : chen
 * date   : 2018/11/27  17:17
 * desc   : 牛  管理
 */
public class CowManageFragment extends BaseFragment {


    private ListView lv_cow_manager;
    private User user;
    private List<House> list;
    private ListViewHouse2Adapter adapter;
    private boolean isTwoPan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_frag, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            isTwoPan = true;
        } else {
            isTwoPan = false;
        }

    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("肉牛管理").build();
        lv_cow_manager = (ListView) view.findViewById(R.id.lv_cow_manager);
        initListView();

    }

    private void initListView() {
        if(getActivity()!=null){
            String phone = SharedUtils.getPhone(getActivity());
            if(!phone.isEmpty()){
                List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                if(users!=null&&users.size()>0){
                    user = users.get(0);
                    OkHttpUtils.get()
                            .url(HttpUrlUtils.HOUSE_CHOOSE_URL)
                            .addParams("bindstr",user.getFarmids())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {

                                }

                                @Override
                                public void onResponse(String response) {
                                    if("error".equals(response)){
                                        Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Type type = new TypeToken<List<House>>(){}.getType();
                                        Gson gson = new Gson();
                                        list = (List<House>)gson.fromJson(response, type);
                                        adapter = new ListViewHouse2Adapter(getActivity(),R.layout.item_house,list);
                                        lv_cow_manager.setAdapter(adapter);
                                        lv_cow_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if(isTwoPan){
                                                    CowManagerAreaFragment fragment = CowManagerAreaFragment.newInstance(list.get(position).getID(),list.get(position).getName());
                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                                }else{
                                                    Intent intent = new Intent(getActivity(), CowMangerAreaActivity.class);
                                                    intent.putExtra("mangerId",list.get(position).getID());
                                                    intent.putExtra("mangerName",list.get(position).getName());
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                }
            }
        }
    }


}
