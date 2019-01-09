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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.PeopleMangerActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.BindHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.HouseAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.PeopleInfoActivity;
import com.example.chen.intelligentweigh.bean.Area;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.PeopleMangerFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/7.   12:43
 * description：用户绑定牧场
 */
public class BindHouseFragment extends BaseFragment {

    private ListView lv_bind_house;
    private boolean isTwoPan;
    private People people;
    private String TAG = "BindHouseFragment";
    private List<House> list ;
    private List<String> housenameList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bind_house_frag, container, false);
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

    /**
     *  提前加载
     * @param people
     * @return
     */
    public static BindHouseFragment newInstance(People people){
        BindHouseFragment fragment = new BindHouseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BindHouseInfo",people);
        fragment.setArguments(bundle);
        return  fragment;
    }

    private void initView(View view) {
        lv_bind_house = (ListView) view.findViewById(R.id.lv_bind_house);
        lv_bind_house.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        initFrag(view);
        initActivity(view);
        initListView();
    }

    /**
     * view视图
     */
    private void initListView() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if(getActivity()!=null){
                            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if(getActivity()!=null){
                            if("error".equals(response)){
                                Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                            }else{
                                Type type = new TypeToken<List<House>>(){}.getType();
                                list = new Gson().fromJson(response, type);
                                for(House house:list){
                                    housenameList.add(house.getName());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice,housenameList);
                                lv_bind_house.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    /**
     * activity中进行UI
     * @param view
     */
    private void initActivity(View view) {
        if(people!=null){
            new TitleBuilder(view).setLeftImage(R.drawable.arrowleft)
                    .setTitleText(people.getName()+"绑定牧场")
                    .setRightText("提交")
                    .setLeftOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getActivity()!=null){
                                getActivity().finish();
                            }
                        }
                    })
                    .setRightOnClickListener(new View.OnClickListener() {
                        String allHouseId = "";
                        String house = "";
                        @Override
                        public void onClick(View v) {
                            SparseBooleanArray checkedItemPositions = lv_bind_house.getCheckedItemPositions();
                            //循环遍历集合中所有的数据，获取每个item是否在SparseBooleanArray存储，以及对应的值；
                            for (int i = 0; i < housenameList.size(); i++) {
                                //根据key获取对应的boolean值，没有则返回false
                                if(checkedItemPositions.get(i)==true){
                                    house = housenameList.get(i);
                                    for(House h:list){
                                        if(house.equals(h.getName())){
                                            allHouseId = h.getID();
                                        }
                                    }
                                    break;
                                }

                            }
                            Log.e(TAG,allHouseId);
                            BindHouseByActivity(people.getPhone(),allHouseId,house);

                        }
                    });

        }
    }

    /**
     * activity提交绑定
     * @param phone
     * @param allHouse
     */
    private void BindHouseByActivity(final String phone, final String allHouse, final String house) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.BING_USER_HOUSE_URL)
                .addParams("framids",allHouse)
                .addParams("user_phone",phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if(getActivity()!=null){
                            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if(getActivity()!=null){
                            if("ok".equals(response)){
                                List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                                if(!users.isEmpty()){
                                    User user = users.get(0);
                                    user.setFarmids(house);
                                    user.setFarmid(allHouse);
                                    user.updateAll("phone = ?",phone);
                                }
                                Intent intent = new Intent(getActivity(), PeopleMangerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(),"绑定失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /**
     * fragment中进行U
     * @param view
     */
    private void initFrag(View view) {
        if(getArguments()!=null){
            final People people = (People)getArguments().getSerializable("BindHouseInfo");


            new TitleBuilder(view).setLeftImage(R.drawable.arrowleft)
                    .setTitleText(people.getName()+"绑定牧场")
                    .setRightText("提交")
                    .setLeftOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getActivity()!=null){
                                PeopleInfoFragment fragment = PeopleInfoFragment.newInstance(people);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                            }

                        }
                    })
                    .setRightOnClickListener(new View.OnClickListener() {
                        String allHouseId = "";
                        String house = "";
                        @Override
                        public void onClick(View v) {

                            SparseBooleanArray checkedItemPositions = lv_bind_house.getCheckedItemPositions();
                            //循环遍历集合中所有的数据，获取每个item是否在SparseBooleanArray存储，以及对应的值；
                            for (int i = 0; i < housenameList.size(); i++) {
                                //根据key获取对应的boolean值，没有则返回false
                                if(checkedItemPositions.get(i)==true){
                                    house = housenameList.get(i);
                                    for(House h:list){
                                        if(house.equals(h.getName())){
                                            allHouseId = h.getID();
                                        }
                                    }
                                    break;
                                }

                            }
                            BindHouseByFrag(people.getPhone(),allHouseId,house);

                        }
                    });

        }
    }

    /**
     *  fragment提交绑定
     * @param phone
     * @param allHouse
     */
    private void BindHouseByFrag(final String phone, final String allHouse, final String house) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.BING_USER_HOUSE_URL)
                .addParams("framids",allHouse)
                .addParams("user_phone",phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if(getActivity()!=null){
                          Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if(getActivity()!=null){
                            if("ok".equals(response)){
                                List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                                if(!users.isEmpty()){
                                    User user = users.get(0);
                                    user.setFarmids(house);
                                    user.setFarmid(allHouse);
                                    user.updateAll("phone = ?",phone);
                                }
                                PeopleMangerFragment fragment = new PeopleMangerFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                            }else{
                                Toast.makeText(getActivity(),"绑定失败",Toast.LENGTH_SHORT).show();
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
        if(context instanceof BindHouseActivity) {
            people = ((BindHouseActivity) context).setData();
            Log.e(TAG, "HouseAreaActivity传递来的数据" + people);
        }
    }
}
