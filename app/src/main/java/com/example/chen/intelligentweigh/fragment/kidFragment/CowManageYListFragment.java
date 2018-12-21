package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.LoginActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageExitsActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageYListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditCowInfoActivty;
import com.example.chen.intelligentweigh.activity.kidActivity.ShowCowInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewCowInfoAdapter;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.WeighData;
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
 * @date 2018/12/16.   16:45
 * description：健在牛list列表展示
 */
public class CowManageYListFragment extends BaseFragment {

    private SwipeMenuListView lv_y_list;
    private String TAG = "CowManageYListFragment";
    private List<Cow> list ;
    private ListViewCowInfoAdapter adapter ;
    private String Yidd;
    private String Yname;
    private Boolean isTwoPan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_y_list_frag, container, false);
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

    public static CowManageYListFragment newInstances(String yid,String yname,String tName){
        CowManageYListFragment  fragment = new CowManageYListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("yid",yid);
        bundle.putString("yname",yname);
        bundle.putString("tName",tName);
        fragment.setArguments(bundle);
        return  fragment;
    }

    private void initView(View view) {

        lv_y_list = (SwipeMenuListView) view.findViewById(R.id.lv_y_list);
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

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        lv_y_list.setMenuCreator(creator);
        initFragView(view);
        initActivityView(view);
    }

    private void initActivityView(View view) {
        if(Yidd!=null&&Yname!=null){
            new TitleBuilder(view).setTitleText("健在牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            Log.e(TAG,"activity "+Yidd+Yname);
            initYListView(Yidd,Yname,"");
        }


    }

    private void initFragView(View view) {
        if(getArguments()!=null){
            final String yid = getArguments().getString("yid");
            final String yname = getArguments().getString("yname");
            final String tName = getArguments().getString("tName");
            new TitleBuilder(view).setTitleText("健在牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CowManageExitsFragment fragment = CowManageExitsFragment.newInstance(yid,yname,tName);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }
            }).build();

            initYListView(yid,yname,tName);

        }

    }

    private void initYListView(final String yid, final String yname, final String tname) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                    .addParams("farmid", yid)
                    .addParams("area", yname)
                    .addParams("exist", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                            }else{
                                Type type = new TypeToken<List<Cow>>() {
                                }.getType();
                                Gson gson = new Gson();
                                list =(List<Cow>) gson.fromJson(response, type);
                                adapter = new ListViewCowInfoAdapter(getActivity(),R.layout.item_cowinfo_layout,list);
                                lv_y_list.setAdapter(adapter);
                                lv_y_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.e(TAG,"数据："+list.get(position));
                                        initListData(list.get(position).getID(),list.get(position),yid);
                                    }
                                });
                                lv_y_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                        switch (index){
                                            case 0:
                                                if(isTwoPan){
                                                    if(!"".equals(tname)){
                                                        EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(list.get(position),yid,tname);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                                    }
                                                }else{
                                                    Intent intent = new Intent(getActivity(), EditCowInfoActivty.class);
                                                    intent.putExtra("eeidd",yid);
                                                    intent.putExtra("eename",yname);
                                                    intent.putExtra("eetname",tname);
                                                    intent.putExtra("eeCow",list.get(position));
                                                    Log.e(TAG,"eeidd"+yid+" eename"+yname+" eetname"+tname+" cow"+list.get(position));
                                                    startActivity(intent);

                                                }
                                                break;
                                            case 1:
                                                Showdelete(list.get(position).getID(),yid,yname);
                                                break;
                                        }

                                        return false;
                                    }
                                });
                            }

                        }

                    });
        }
    }

    /**
     * 展示删除弹框
     * @param id
     */
    private void Showdelete(final String id, final String yidd, final String yname) {
        AlertDialog myDialog = new AlertDialog(getActivity()).builder();
        myDialog.setGone().setTitle("提示").setMsg("确认删除").setNegativeButton("取消", null).setPositiveButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCow(id);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initCrashAreaList(yidd,yname);
                    }
                },1000);
            }
        }).show();

    }

    /**
     * 刷新界面
     * @param yidd
     * @param yname
     */
    private void initCrashAreaList(String yidd, String yname) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                    .addParams("farmid", yidd)
                    .addParams("area", yname)
                    .addParams("exist", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                            } else {
                                Type type = new TypeToken<List<Cow>>() {
                                }.getType();
                                Gson gson = new Gson();
                                if (!list.isEmpty()) {
                                    list.clear();
                                }
                                list = (List<Cow>) gson.fromJson(response, type);
                                adapter = new ListViewCowInfoAdapter(getActivity(), R.layout.item_cowinfo_layout, list);
                                adapter.notifyDataSetChanged();
                                lv_y_list.setAdapter(adapter);
                            }
                        }
                    });
        }


    }

    /**
     * 删除
     * @param id
     */
    private void DeleteCow(String id) {
        if(getActivity()!=null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.DELETECOW)
                    .addParams("cattleid", id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)){
                                Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    /**
     * 加载char数据
     * @param id
     */
    private void initListData(String id, final Cow cow, final String yid) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKWEIGHRECORD)
                .addParams("cattleid",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response)){
                            Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                        }else{
                            Type type = new TypeToken<List<WeighData>>(){}.getType();
                            Gson gson = new Gson();
                            List<WeighData> list = (List<WeighData>)gson.fromJson(response, type);
                            ArrayList<String> xValues = null;
                            ArrayList<Float> yValues = null;
                            if(!list.isEmpty()) {
                                xValues = new ArrayList<>();   //x轴数据集合
                                for (WeighData data : list) {
                                    xValues.add(data.getWtime().substring(6));
                                }
                                yValues = new ArrayList<>();  //y轴数据集合
                                for (WeighData data : list) {
                                    yValues.add((float) data.getWeight());
                                }
                            }else{
                                xValues = new ArrayList<>();   //x轴数据集合
                                yValues = new ArrayList<>();  //y轴数据集合
                                for(int i=1;i<=5;i++){

                                    xValues.add("无数据");
                                }
                                for(int i=1;i<=5;i++){
                                    yValues.add(0f);
                                }

                            }
                            if(isTwoPan) {
                                ShowCowInfoFragment fragment = ShowCowInfoFragment.newInstances(cow, yid, xValues, yValues,"1");
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                            }else{
                                Intent intent = new Intent(getActivity(),ShowCowInfoActivity.class);
                                intent.putExtra("cowsInfo",cow);
                                intent.putExtra("xData",xValues);
                                intent.putExtra("yData",yValues);
                                startActivity(intent);

                            }
                        }
                    }
                });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if(context instanceof CowManageYListActivity) {
            Yidd = ((CowManageYListActivity) context).setIdData();
            Yname = ((CowManageYListActivity) context).setNameData();

        }
    }

}
