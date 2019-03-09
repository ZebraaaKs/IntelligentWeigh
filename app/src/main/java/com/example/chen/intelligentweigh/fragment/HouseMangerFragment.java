package com.example.chen.intelligentweigh.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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

    private SwipeMenuListView lv_house;
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
                final AlertDialog myDialog = new AlertDialog(getActivity()).et2Builder();
                myDialog.setEtGone().setTitle("添加").setEtMsg("3").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String etMsg = myDialog.getEtMsg();
                        String etMsg2 = myDialog.getEt2Msg();
                        if(!etMsg.isEmpty()&&!etMsg2.isEmpty()){
                            OkHttpUtils.get()
                                    .url(HttpUrlUtils.ADD_NEW_HOUSE)
                                    .addParams("farm",etMsg)
                                    .addParams("addr",etMsg2)
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
        lv_house = (SwipeMenuListView) view.findViewById(R.id.lv_house);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getActivity());

                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(255,
                        170, 37)));
                // set item width
                editItem.setWidth(dp2px(90));

                editItem.setIcon(R.drawable.ic_edit);
                // add to menu
                menu.addMenuItem(editItem);


            }
        };
        lv_house.setMenuCreator(creator);
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

        lv_house.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, final SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        final AlertDialog myDialog1 = new AlertDialog(getActivity()).et2Builder();
                        myDialog1.setEtGone().setTitle("修改信息").setEtMsg("3").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                House house = list.get(position);
                                if (!myDialog1.getEtMsg().isEmpty()&&!myDialog1.getEt2Msg().isEmpty()){
                                OkHttpUtils.get()
                                        .addParams("farmid",house.getID())
                                        .addParams("name",myDialog1.getEtMsg().toString())
                                        .addParams("addr",myDialog1.getEt2Msg().toString())
                                        .url(HttpUrlUtils.FARMMODIFYINFO)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Request request, Exception e) {
                                                Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                if("ok".equals(response)){
                                                    Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                                    list.clear();
                                                    initHouseListView();
                                                }else{
                                                    Toast.makeText(getContext(),"修改失败",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                }
                            }
                        }).show();
                        break;

                }
                return false;
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
                            Log.e(TAG,"牧场详情 ："+response);
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
