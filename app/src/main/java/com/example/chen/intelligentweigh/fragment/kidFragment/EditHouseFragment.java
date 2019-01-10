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

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditHouseActivity;
import com.example.chen.intelligentweigh.adapter.ListViewHouse2Adapter;
import com.example.chen.intelligentweigh.adapter.ListViewHouseAdapter;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.fragment.NewCowFragment;
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
 * @date 2018/12/19.   22:37
 * description：
 */
public class EditHouseFragment extends BaseFragment {

    private ListView lv_choose_house;
    private boolean isTwoPan;
    private String TAG = "EditHouseFragment";
    private String framdata;
    private Cow newCow;
    private Cow cow;
    private String bindstr;
    private String editid;
    private String etname;
    private List<House> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_house_frag, container, false);
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

    public static EditHouseFragment newInstances(Cow cow,String bindStr,String editid,String etname){
        EditHouseFragment fragment = new EditHouseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bindstr",bindStr);
        bundle.putSerializable("editcow",cow);
        bundle.putString("editid",editid);
        bundle.putString("etname",etname);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        lv_choose_house = (ListView) view.findViewById(R.id.lv_choose_house);
        initFragView(view);
        initActivityView(view);
    }

    private void initActivityView(View view) {
        if(getActivity()!=null&&framdata!=null&&newCow!=null){
            new TitleBuilder(view).setTitleText("选择牧场").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            initListView(framdata);

            
        }
    }

    private void initListView(String framids) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.HOUSE_CHOOSE_URL)
                .addParams("bindstr",framids)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(final String response) {
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Type type = new TypeToken<List<House>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    list = (List<House>) gson.fromJson(response.toString(), type);
                                    ListViewHouse2Adapter adapter = new ListViewHouse2Adapter(getActivity(),R.layout.item_house,list);
                                    lv_choose_house.setAdapter(adapter);
                                    lv_choose_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            House house = list.get(position);
                                            if(isTwoPan){
                                                cow.setPast_id(Integer.parseInt(house.getID()));
                                                cow.setFarmname(house.getName());
                                                Log.e(TAG,cow.toString());
                                                EditAreaFragment fragment = EditAreaFragment.newInstances(cow,bindstr,editid,house.getName())
                                                        ;
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                            }else{
                                                newCow.setPast_id(Integer.parseInt(house.getID()));
                                                newCow.setFarmname(house.getName());
                                                Intent intent = new Intent(getActivity(), EditAreaActivity.class);
                                                intent.putExtra("aeditCow",newCow);
                                                startActivity(intent);
                                            }
                                        }
                                    });


                                }
                            });

                        }
                    }
                });
    }

    private void initFragView(View view) {
        if(getArguments()!=null&&getActivity()!=null){
            cow = (Cow)getArguments().getSerializable("editcow");
            bindstr = getArguments().getString("bindstr");
            editid = getArguments().getString("editid");
            etname = getArguments().getString("etname");
            new TitleBuilder(view).setTitleText("选择牧场").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cow != null) {
                        EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(cow,editid,etname);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    }

                }
            });
            initListView(bindstr);
        }

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
        if(context instanceof EditHouseActivity) {
            framdata = ((EditHouseActivity) context).setData();
            newCow = ((EditHouseActivity) context).setcowData();
            Log.e(TAG, "activity传递来的数据" + framdata+newCow);
        }
    }
}
