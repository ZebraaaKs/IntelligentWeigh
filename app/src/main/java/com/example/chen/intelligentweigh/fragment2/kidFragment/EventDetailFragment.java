package com.example.chen.intelligentweigh.fragment2.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity2.EventDetailActivity;
import com.example.chen.intelligentweigh.adapter.EventDetailAdapter;
import com.example.chen.intelligentweigh.bean.EventDetail;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment2.BlankFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.DataCheckUtils;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chen
 * @date 2019/8/6.   11:05
 * description：
 */
public class EventDetailFragment extends BaseFragment {


    private ListView lv_eventdetail;
    private boolean isTwoPan;
    private String date;
    private TextView tv_allnum;
    private final String TAG = "EventDetailFragment";
    private List<EventDetail> list;
    public  static final  String REFUSE_EVENT = "broadcast.refuse_event";
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.eventdetail_frag, container, false);

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
            isTwoPan = true;
        } else {
            isTwoPan = false;
        }
    }


    private void initView(View view) {
        lv_eventdetail = (ListView) view.findViewById(R.id.lv_eventdetail);
        tv_allnum = (TextView) view.findViewById(R.id.tv_allnum);
        initFrag(view);
        initActivity(view);



    }

    private void initActivity(View view) {
        if (date != null) {
            new TitleBuilder(view).setTitleText(date).setRightText("编辑").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(date);
                }
            });
            initListViewData(date);
        }
    }

    private void initFrag(View view) {
        if (getArguments() != null) {
            final String titleData = (String) getArguments().getString("eventDate");
            new TitleBuilder(view).setTitleText(titleData).setRightText("编辑").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       showDialog(titleData);
                }
            });
            initListViewData(titleData);


        }
    }


    public static EventDetailFragment newInstance(String date) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventDate", date);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void showDialog(final String date){
        if(getActivity()!=null) {
            final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
            myDialog.setEtGone().setTitle("肉价(单位:元/公斤)").setEtMsg("11").setNegativeButton("取消", null)
                    .setPositiveButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String etMsg = myDialog.getEtMsg();
                            if(".".equals(etMsg)){
                                Toast.makeText(getActivity(),"数据格式不正确,上传数据失败",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            boolean flag = DataCheckUtils.isDouble(etMsg);
                            if(flag){
                               String data =  DataCheckUtils.double2String(DataCheckUtils.string2Double(etMsg));
                                User myInfo = SharedUtils.getMyInfo(getActivity());
                                if(myInfo!=null){
                                    upLoadTodayPrice(data,date,myInfo.getFarmid());
                                }
                            }else{
                                Toast.makeText(getActivity(),"数据格式不正确,上传数据失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).show();
        }
    }

    private void upLoadTodayPrice(String data, String date, String farmid) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.EXCUTEWEIHPRICEEVENT)
                .addParams("farmid",farmid)
                .addParams("date",date)
                .addParams("price",data)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG,e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            if("ok".equals(response)){
                                Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(REFUSE_EVENT);
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                if(isTwoPan){
                                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, new BlankFragment()).commit();
                                }else{
                                    getActivity().finish();
                                }
                            }else{
                                Toast.makeText(getActivity(),"上传失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }




    private void initListViewData(String date) {
        if (getActivity() != null) {
            User myInfo = SharedUtils.getMyInfo(getActivity());
            if(myInfo!=null){
                OkHttpUtils.get()
                        .url(HttpUrlUtils.BACKNOEXCUTEDETAILS)
                        .addParams("farmid",myInfo.getFarmid())
                        .addParams("date",date)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.e(TAG,e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                if(!TextUtils.isEmpty(response)){
                                    Type type = new TypeToken<List<EventDetail>>(){}.getType();
                                    list = new Gson().fromJson(response,type);
                                    if(list.size()>0&&list!=null) {
                                        EventDetailAdapter adapter = new EventDetailAdapter(getActivity(), R.layout.item_eventdetail, list);
                                        lv_eventdetail.setAdapter(adapter);
                                        tv_allnum.setText("称重总数量：" + list.size());
                                    }else{
                                        tv_allnum.setText("称重总数量：" + 0);
                                    }
                                }

                            }
                        });
            }
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
        if (context instanceof EventDetailActivity) {
            date = ((EventDetailActivity) context).setDate();
        }
    }


}
