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

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageExitsActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageNListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageYListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowMangerAreaActivity;
import com.example.chen.intelligentweigh.util.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/16.   16:05
 * description：
 */
public class CowManageExitsFragment extends BaseFragment {

    /*private ListView lv_cow_manage_exits;
    private boolean isTwoPan;
    private String tmanageId;
    private String tmanageName;
    private String tmanageName2;
    private String tidd;
    private String tname;
    private String tname2;
    private String TAG = "CowManageExitsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_exits_frag, container, false);
        initView(view);
        return view;
    }

    public static CowManageExitsFragment newInstance(String id,String name,String name2){
        CowManageExitsFragment fragment = new CowManageExitsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tmanageId",id);
        bundle.putString("tmanageName",name);
        bundle.putString("tmanageName2",name2);
        fragment.setArguments(bundle);
        return fragment;
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

        lv_cow_manage_exits = (ListView) view.findViewById(R.id.lv_cow_manage_exits);
        initActivityView(view);
        initFragView(view);
    }

    private void initFragView(View view) {
        if(getArguments()!=null){
            tmanageId = getArguments().getString("tmanageId");
            tmanageName = getArguments().getString("tmanageName");
            tmanageName2 = getArguments().getString("tmanageName2");
            if(tmanageName!=null) {
                new TitleBuilder(view).setTitleText(tmanageName).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CowManagerAreaFragment fragment = CowManagerAreaFragment.newInstance(tmanageId,tmanageName2);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    }
                });
                initListView(tmanageId,tmanageName);
            }


        }
    }



    private void initActivityView(View view) {
        if(tidd!=null&&tname!=null) {
            new TitleBuilder(view).setTitleText(tname).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            initListView(tidd, tname);
        }

    }

    private void initListView(final String ids, final String names) {
        List<String> list = new ArrayList<>();
        list.add("健在牛");
        list.add("淘汰牛");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        lv_cow_manage_exits.setAdapter(adapter);
        lv_cow_manage_exits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if(isTwoPan){
                        Log.e(TAG,"framid"+ids+" name"+names);
                        //position 0 健在  1 淘汰
                        if(position==0){
                            CowManageYListFragment fragment = CowManageYListFragment.newInstances(ids,names,tmanageName2);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                        }else{
                            CowManageNListFragment fragment = CowManageNListFragment.newInstances(ids,names,tmanageName2);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();

                        }


                    }else{
                        if(position==0){
                            Intent intent = new Intent(getActivity(), CowManageYListActivity.class);
                            intent.putExtra("Yidd",ids);
                            intent.putExtra("Yname",names);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getActivity(), CowManageNListActivity.class);
                            intent.putExtra("Nidd",ids);
                            intent.putExtra("Nname",names);
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

    *//*
     * Deprecated on API 23
     * Use onAttachToContext instead
     *//*
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    *//*
     * Called when the fragment attaches to the context
     *//*
    protected void onAttachToContext(Context context) {
        if(context instanceof CowManageExitsActivity) {
            tidd = ((CowManageExitsActivity) context).setIdData();
            tname = ((CowManageExitsActivity) context).setNameData();
            tname2 = ((CowManageExitsActivity) context).setName2Data();
        }
    }*/
}
