package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.chen.intelligentweigh.activity.kidActivity.PeopleInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewChooseHouseAdapter;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/12.   20:24
 * description：
 */
public class ChooseHouseFragment extends BaseFragment {

    private ListView lv_choose_house;
    private boolean isTwoPan;
    private String TAG = "ChooseHouseFragment";
    private String framdata;
    private NewCow newCow;
    private List<House> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_house_frag, container, false);
        initView(view);
        return view;
    }

    /**
     *  提前加载布局
     * @return
     */
    public static ChooseHouseFragment newInstance(String house,NewCow newCow){
        ChooseHouseFragment chooseHouseFragment = new ChooseHouseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("house",house);
        bundle.putSerializable("choosecow",newCow);
        chooseHouseFragment.setArguments(bundle);
        return chooseHouseFragment;
    }

    public static ChooseHouseFragment newInstanceEdit(String ehouse,Cow ecow){
        ChooseHouseFragment chooseHouseFragment = new ChooseHouseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ehouse",ehouse);
        bundle.putSerializable("editCow",ecow);
        chooseHouseFragment.setArguments(bundle);
        return chooseHouseFragment;
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
        lv_choose_house = (ListView) view.findViewById(R.id.lv_choose_house);
        onFragmentUI(view);
        onActivityUI(view);
    }

    /**
     * 在Activity下
     * @param view
     */
    private void onActivityUI(View view) {

        if(framdata!=null){
            new TitleBuilder(view).setTitleText("选择牧场").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            ChooseHouse(framdata);
        }
    }

    /**
     * fragment下UI
     * @param view
     */
    private void onFragmentUI(View view) {
        if(getArguments()!=null){
            final NewCow cow = (NewCow) getArguments().getSerializable("choosecow");
            final Cow cow1 = (Cow) getArguments().getSerializable("editCow");
            new TitleBuilder(view).setTitleText("选择牧场").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cow != null) {
                        NewCowFragment fragment = NewCowFragment.newInstances(cow);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    }else if(cow1!=null){
                        EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(cow1,cow1.getFather_id(),cow1.getArea(),cow1.getName());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    }

                }
            });
            String house = getArguments().getString("house");
            if(house!=null) {
                ChooseHouse(house);
            }
        }
    }

    /**
     * 请求得到牧场信息
     * @param farmids
     */
    private void ChooseHouse(String farmids) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.HOUSE_CHOOSE_URL)
                .addParams("bindstr",farmids)
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
                                    ListViewHouseAdapter adapter = new ListViewHouseAdapter(getActivity(),R.layout.item_house,list);
                                    lv_choose_house.setAdapter(adapter);
                                    lv_choose_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            House house = list.get(position);
                                            if(isTwoPan){
                                                NewCow cow = (NewCow)getArguments().getSerializable("choosecow");
                                                cow.setHouseId(house.getID());
                                                ChooseAreaFragment fragment = ChooseAreaFragment.newInstances(house.getID(),house.getName(),cow);
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment,"1").commit();
                                            }else{
                                                Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
                                                intent.putExtra("name",house.getName());
                                                intent.putExtra("id",house.getID());
                                                newCow.setHouseName(house.getName());
                                                newCow.setHouseId(house.getID());
                                                intent.putExtra("cowInfo",newCow);
                                                Log.e(TAG,"name"+house.getName()+"id"+house.getID()+"cowInfo"+newCow);
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

    /**
     * fragment展示area
     * @param id
     */
    private void initHouseAreaFrag(String id) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .addParams("farmid",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {


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
        if(context instanceof ChooseHouseActivity) {
            framdata = ((ChooseHouseActivity) context).setData();
            newCow = ((ChooseHouseActivity) context).setNewcowData();
            Log.e(TAG, "activity传递来的数据" + framdata+newCow);
        }
    }



}
