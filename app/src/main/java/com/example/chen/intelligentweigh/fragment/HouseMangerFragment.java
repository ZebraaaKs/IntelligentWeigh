package com.example.chen.intelligentweigh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.HouseAreaActivity;
import com.example.chen.intelligentweigh.adapter.ListViewHouseAdapter;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.fragment.kidFragment.HouseAreaFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/5.   15:22
 * description：牧场管理
 */
public class HouseMangerFragment extends BaseFragment {

    private ListView lv_house;
    private List<House> list = null;
    private House house = null;
    private boolean isTwoPan;
    private String TAG = "HouseMangerFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_manger_frag, container, false);
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
        new TitleBuilder(view).setTitleText("牧场管理").setRightText("添加牧场").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("添加").setEtMsg("3").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String etMsg = myDialog.getEtMsg();
                        if(!etMsg.isEmpty()){
                            OkHttpUtils.get()
                                    .url(HttpUrlUtils.ADD_NEW_HOUSE)
                                    .addParams("farm",etMsg)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            if (getActivity() != null) {
                                                Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if(getActivity()!=null) {
                                                if ("ok".equals(response.toString())) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            list.clear();
                                                            initHouseListView();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });


                        }

                    }
                }).show();

            }
        });
        lv_house = (ListView) view.findViewById(R.id.lv_house);
        initHouseListView();
        lv_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(!list.isEmpty()){
                   house = list.get(position);
                   Bundle bundle = new Bundle();
                   if(house!=null) {
                       bundle.putSerializable("houseInfo", house);
                       Log.e(TAG, "传递农场" +house );
                   }
                   if(isTwoPan){
                       HouseAreaFragment fragment = HouseAreaFragment.newInstance(house);
                       getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();

                   }else{
                       Intent intent = new Intent(getActivity(), HouseAreaActivity.class);
                       intent.putExtras(bundle);
                       startActivity(intent);

                   }
               }


            }
        });

    }

    private void initHouseListView() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if(getActivity()!=null){
                        if("error".equals(response.toString())){
                            Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                        }else {
                            Type type = new TypeToken<List<House>>() {
                            }.getType();
                            list = new Gson().fromJson(response.toString(), type);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListViewHouseAdapter adapter = new ListViewHouseAdapter(getActivity(), R.layout.item_house, list);
                                        adapter.notifyDataSetChanged();
                                        lv_house.setAdapter(adapter);

                                    }
                                });
                            }

                        }


                        }

                    }
                });
    }
}
