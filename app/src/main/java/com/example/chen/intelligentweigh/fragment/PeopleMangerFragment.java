package com.example.chen.intelligentweigh.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
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
import com.example.chen.intelligentweigh.activity.SetTouXiangActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.PeopleInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewPeopleAdapter;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.fragment.kidFragment.PeopleInfoFragment;
import com.example.chen.intelligentweigh.util.Event.MessageEvent;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/5.   15:11
 * description： 人员管理
 */
public class PeopleMangerFragment extends BaseFragment {

    private ListView lv_people;
    private boolean isTwoPan;
    private String TAG = "PeopleMangerFragment";
    private List<People> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_manger_frag, container, false);
        initView(view);
        EventBus.getDefault().register(this);
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
        new TitleBuilder(view).setTitleText("人员管理");
        lv_people = (ListView) view.findViewById(R.id.lv_people);
        initListView();
        lv_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!list.isEmpty()){
                    People people = list.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("peopleInfo",people);
                    Log.e(TAG,"传递用户"+people);
                    if(isTwoPan){
                        PeopleInfoFragment fragment = PeopleInfoFragment.newInstance(people);
                        getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();

                    }else{
                        Intent intent = new Intent(getActivity(), PeopleInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }
            }
        });

    }

    private void initListView() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_USER_URL)
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
                            if ("error".equals(response.toString())) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Type type = new TypeToken<List<People>>() {
                                }.getType();
                                list = new Gson().fromJson(response.toString(), type);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, list.toString());
                                        ListViewPeopleAdapter adapter = new ListViewPeopleAdapter(getActivity(), R.layout.item_people, list);
                                        lv_people.setAdapter(adapter);

                                    }
                                });
                            }
                        }

                    }
                });
    }

    public void updateListView(){
            OkHttpUtils.get()
                    .url(HttpUrlUtils.ALL_USER_URL)
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Type type = new TypeToken<List<People>>() {
                                }.getType();
                                list = new Gson().fromJson(response.toString(), type);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, list.toString());
                                        ListViewPeopleAdapter adapter = new ListViewPeopleAdapter(getActivity(), R.layout.item_people, list);
                                        lv_people.setAdapter(adapter);

                                    }
                                });
                            }
                            }

                        }
                    });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if("delete_user".equals(messageEvent.getMessage())){
            Log.e(TAG,"更新了");
            list.clear();
            updateListView();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
