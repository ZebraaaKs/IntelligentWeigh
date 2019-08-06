package com.example.chen.intelligentweigh.fragment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity2.EventDetailActivity;
import com.example.chen.intelligentweigh.adapter.EventDateAdapter;
import com.example.chen.intelligentweigh.bean.EventDate;
import com.example.chen.intelligentweigh.bean.EventDetail;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment2.kidFragment.EventDetailFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.example.chen.intelligentweigh.viewpagerutil.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 * @date 2019/8/3.   14:30
 * description：
 */
public class EventFragment extends BaseFragment {

    private View rootView;
    private ListView lv_noexcute;
    private boolean isTwoPan;
    private List<EventDate> list;
    private final String TAG = "EventFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.event_frag, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTwoPan = true;
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTwoPan = false;
        }
    }

    private void initView(View rootView) {
        lv_noexcute = (ListView) rootView.findViewById(R.id.lv_noexcute);
        new TitleBuilder(rootView).setTitleText("事件");
        receiveRefuseEvent();


    }

    @Override
    protected void onFragmentFirstVisible() {
        Log.e(TAG,"未处理数据初始化");
        doGetData();

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(isVisible){
            doGetData();
        }
    }

    LocalBroadcastManager broadcastManager;

    /**
     * 注册广播接收器
     */
    private void receiveRefuseEvent() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventDetailFragment.REFUSE_EVENT);
        broadcastManager.registerReceiver(mAdDownLoadReceiver, intentFilter);
    }

    BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"接收到了");
            doGetData();
        }
    };


    private void doGetData(){
        if(getActivity()!=null) {
            User user = SharedUtils.getMyInfo(getActivity());
            if(user!=null){
                OkHttpUtils.get()
                        .addParams("farmid",user.getFarmid())
                        .url(HttpUrlUtils.NOEXECUTEEVENT)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.e(TAG,e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                if(!TextUtils.isEmpty(response)){
                                    Map map = new Gson().fromJson(response, Map.class);
                                    final String wtime = (String)map.get("wtime");
                                    if(!TextUtils.isEmpty(wtime)) {
                                        Log.e(TAG, response + wtime);
                                        list = new ArrayList<EventDate>();
                                        list.add(new EventDate(wtime));
                                        EventDateAdapter adapter = new EventDateAdapter(getActivity(), R.layout.item_events, list);
                                        lv_noexcute.setAdapter(adapter);
                                        lv_noexcute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                EventDate eventDate = list.get(position);
                                                Log.e(TAG,"点击的事件是 "+eventDate.getWtime());
                                                if(isTwoPan){
                                                    EventDetailFragment fragment =  EventDetailFragment.newInstance(wtime);
                                                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                                                }else{
                                                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("AeventDate",wtime);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }
}
