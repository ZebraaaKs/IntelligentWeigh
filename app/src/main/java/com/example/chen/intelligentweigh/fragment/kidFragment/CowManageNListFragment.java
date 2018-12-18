package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageNListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageYListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ShowCowInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewCowInfoAdapter;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.WeighData;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/18.   20:11
 * description：
 */
public class CowManageNListFragment extends BaseFragment {

    private ListView lv_n_list;
    private Boolean isTwoPan;
    private String Nidd;
    private String Nname;
    private List<Cow> list ;
    private ListViewCowInfoAdapter adapter;
    private String TAG = "CowManageNListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_n_list_frag, container, false);
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

    public static CowManageNListFragment newInstances(String yid,String yname,String tName){
        CowManageNListFragment  fragment = new CowManageNListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nid",yid);
        bundle.putString("nname",yname);
        bundle.putString("tName",tName);
        fragment.setArguments(bundle);
        return  fragment;
    }

    private void initView(View view) {
        lv_n_list = (ListView) view.findViewById(R.id.lv_n_list);
        initFragView(view);
        initActivityView(view);
    }

    private void initActivityView(View view) {
        if(Nidd!=null&&Nname!=null&&getActivity()!=null){
            new TitleBuilder(view).setTitleText("淘汰牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                         getActivity().finish();
                }
            });
            initListView(Nidd,Nname);
        }



    }

    private void initFragView(View view) {
        if(getArguments()!=null&&getActivity()!=null){
            final String nid = getArguments().getString("nid");
            final String nname = getArguments().getString("nname");
            final String tName = getArguments().getString("tName");
            new TitleBuilder(view).setTitleText("淘汰牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CowManageExitsFragment fragment = CowManageExitsFragment.newInstance(nid,nname,tName);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }
            });

            initListView(nid,nname);
        }

    }

    private void initListView(final String nnnid, String name) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                .addParams("farmid", nnnid)
                .addParams("area", name)
                .addParams("exist", "0")
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
                            Log.e(TAG,"数据 "+response);
                            Type type = new TypeToken<List<Cow>>() {
                            }.getType();
                            Gson gson = new Gson();
                            list =(List<Cow>) gson.fromJson(response, type);
                            adapter = new ListViewCowInfoAdapter(getActivity(),R.layout.item_cowinfo_layout,list);
                            lv_n_list.setAdapter(adapter);
                            lv_n_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    initListData(list.get(position).getID(),list.get(position),nnnid);
                                }
                            });

                        }
                    }
                });
    }

    private void initListData(final String id, final Cow cow, final String nnnid) {
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
                                ShowCowInfoFragment fragment = ShowCowInfoFragment.newInstances(cow, nnnid, xValues, yValues,"0");
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
        if(context instanceof CowManageNListActivity) {
            Nidd = ((CowManageNListActivity) context).setIdData();
            Nname = ((CowManageNListActivity) context).setNameData();

        }
    }
}
