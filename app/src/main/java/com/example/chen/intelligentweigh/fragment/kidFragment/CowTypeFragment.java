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
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowTypeActivity;
import com.example.chen.intelligentweigh.adapter.ListViewChooseTypeAdapter;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.CowType;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.fragment.NewCowFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
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
 * @date 2018/12/15.   19:09
 * description：
 */
public class CowTypeFragment extends BaseFragment {

    private ListView lv_cow_type;
    private boolean isTwoPan;
    private List<CowType> list = new ArrayList<>();
    private String TAG = "CowTypeFragment";
    private NewCow cows;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_type_frag, container, false);
        initView(view);
        return view;
    }


    public static CowTypeFragment newInstance(NewCow cow){
        CowTypeFragment fragment = new CowTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cowInfo",cow);
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
        lv_cow_type = (ListView) view.findViewById(R.id.lv_cow_type);
        onFragUI(view);
        onActivityUI(view);
        onInitUI();

    }

    private void onActivityUI(View view) {
        if(cows!=null){
            new TitleBuilder(view).setTitleText("选择种类").setRightText("添加").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddType();

                }
            }).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            })
            ;

        }
    }

    private void onFragUI(View view) {
        if (getArguments() != null) {
            new TitleBuilder(view).setTitleText("选择种类").setRightText("添加").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddType();

                }
            }).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCow cow = (NewCow) getArguments().getSerializable("cowInfo");
                    if (cow != null) {
                        NewCowFragment fragment = NewCowFragment.newInstances(cow);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    }


                }
            })
            ;
        }
    }

    /**
     * 添加牛的品种
     */
    private void AddType() {
        final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
        myDialog.setEtGone().setTitle("种类").setEtMsg("10").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myDialog.getEtMsg().isEmpty()) {
                    OkHttpUtils.get()
                            .url(HttpUrlUtils.CATTLEKINDADD)
                            .addParams("kind", myDialog.getEtMsg())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {


                                }

                                @Override
                                public void onResponse(String response) {
                                    if ("ok".equals(response)) {
                                        refrshUI();
                                    } else {
                                        if (getActivity() != null) {
                                            Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        }).show();


    }


    /**
     * 刷新展示
     */
    private void refrshUI() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKALLKIND)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response)){
                            if(getActivity()!=null){
                                Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(getActivity()!=null) {
                                Type type = new TypeToken<List<CowType>>() {
                                }.getType();
                                list.clear();
                                list = new Gson().fromJson(response, type);
                                ListViewChooseTypeAdapter adapter = new ListViewChooseTypeAdapter(getActivity(), R.layout.item_cow_type, list);
                                adapter.notifyDataSetChanged();
                                lv_cow_type.setAdapter(adapter);
                            }
                        }

                    }
                });
    }


    /**
     * UI
     */
    private void onInitUI() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKALLKIND)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response)){
                            if(getActivity()!=null){
                                Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(getActivity()!=null) {

                                Type type = new TypeToken<List<CowType>>() {
                                }.getType();
                                list = new Gson().fromJson(response, type);
                                ListViewChooseTypeAdapter adapter = new ListViewChooseTypeAdapter(getActivity(), R.layout.item_cow_type, list);
                                lv_cow_type.setAdapter(adapter);
                                lv_cow_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        CowType cowType = list.get(position);
                                        if(isTwoPan){
                                            NewCow cow = (NewCow)getArguments().getSerializable("cowInfo");
                                            cow.setCowType(cowType.getName());
                                            cow.setCowTypeId(cowType.getID());
                                            NewCowFragment fragment = NewCowFragment.newInstances(cow);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                        }else{
                                            cows.setCowType(cowType.getName());
                                            cows.setCowTypeId(cowType.getID());
                                            Intent intent = new Intent(getActivity(), NewCowActivity.class);
                                            intent.putExtra("cowInfo",cows);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }
                                });
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
        if(context instanceof CowTypeActivity) {
            cows = ((CowTypeActivity)context).setData();
        }
    }
}
