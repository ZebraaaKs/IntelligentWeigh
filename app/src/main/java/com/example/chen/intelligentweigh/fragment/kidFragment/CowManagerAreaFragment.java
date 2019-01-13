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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageExitsActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageYListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowMangerAreaActivity;
import com.example.chen.intelligentweigh.bean.Area;
import com.example.chen.intelligentweigh.fragment.CowManageFragment;
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
 * @date 2018/12/16.   15:29
 * description：
 */
public class CowManagerAreaFragment extends BaseFragment {

    private ListView lv_cow_manger_area;
    private boolean isTwoPan;
    private String cowmId;
    private String cowmName;
    private String idd;
    private String name;
    private List<String> listArea = new ArrayList<>();
    private String TAG = "CowManagerAreaFragment";


    public static CowManagerAreaFragment newInstance(String id,String name){
        CowManagerAreaFragment fragment = new CowManagerAreaFragment();
        Bundle bundle  = new Bundle();
        bundle.putString("cowmId",id);
        bundle.putString("cowmName",name);
        fragment.setArguments(bundle);
        return  fragment;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manager_area_frag, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        lv_cow_manger_area = (ListView) view.findViewById(R.id.lv_cow_manger_area);
        initFragView(view);
        initActivityView(view);
    }

    private void initActivityView(View view) {
        if(idd!=null&&name!=null){
            new TitleBuilder(view).setTitleText(name+"的分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            }).build();
            initAreaList(idd);
        }
    }

    private void initFragView(View view) {
        if (getArguments() != null) {
            cowmId = getArguments().getString("cowmId");
            cowmName = getArguments().getString("cowmName");
            if (!cowmName.isEmpty()) {
                new TitleBuilder(view).setTitleText(cowmName+"的分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       CattleFramKindFragment fragment = new CattleFramKindFragment();
                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    }
                }).build();
            }
            if(!cowmId.isEmpty()){
                initAreaList(cowmId);
            }
        }
    }

    /**
     * 展示分区listview
     * @param id
     */
    private void initAreaList(String id) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .addParams("farmid",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response.toString())){
                            Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                        }else{
                            Type type = new TypeToken<List<Area>>(){}.getType();
                            List<Area> list = new Gson().fromJson(response, type);
                            for(Area area:list){
                                listArea.add(area.getArea()+"("+area.getNum()+"头)");
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listArea);
                            lv_cow_manger_area.setAdapter(adapter);
                            lv_cow_manger_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String[] split = listArea.get(position).split("[(]");
                                    if(isTwoPan){
                                        CowManageYListFragment fragment = CowManageYListFragment.newInstances(cowmId,split[0],cowmName);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                    }else{
                                       Intent intent = new Intent(getActivity(), CowManageYListActivity.class);
                                       intent.putExtra("Yidd",idd);
                                       intent.putExtra("Yname",split[0]);
                                       startActivity(intent);
                                    }
                                }
                            });

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
        if(context instanceof CowMangerAreaActivity) {
            idd = ((CowMangerAreaActivity) context).setIdData();
            name = ((CowMangerAreaActivity) context).setNameData();
            Log.e(TAG,"id"+idd+"name"+name);
        }
    }
}
