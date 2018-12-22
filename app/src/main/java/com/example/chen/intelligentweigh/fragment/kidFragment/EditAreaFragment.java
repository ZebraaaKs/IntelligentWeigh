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
import com.example.chen.intelligentweigh.activity.kidActivity.EditAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditCowInfoActivty;
import com.example.chen.intelligentweigh.activity.kidActivity.EditHouseActivity;
import com.example.chen.intelligentweigh.bean.Area;
import com.example.chen.intelligentweigh.bean.Cow;
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
 * @date 2018/12/22.   21:02
 * description：
 */
public class EditAreaFragment extends BaseFragment {

    private ListView lv_edit_area;
    private Cow editcow;
    private String bindStr;
    private String yid;
    private boolean isTwoPan;
    private String tname;
    private List<String> dataList =  new ArrayList<>();
    private String TAG = "EditAreaFragment";
    private Cow aeditcow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_area_frag, container, false);
        initView(view);
        return view;
    }

    public static EditAreaFragment newInstances(Cow cow,String bindStr,String yid,String tname){
        EditAreaFragment fragment = new EditAreaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("editcow",cow);
        bundle.putString("bindStr",bindStr);
        bundle.putString("yid",yid);
        bundle.putString("tname",tname);
        fragment.setArguments(bundle);
        return  fragment;
    }

    private void initView(View view) {
        lv_edit_area = (ListView) view.findViewById(R.id.lv_edit_area);
        initFragView(view);
        initActivtyView(view);
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

    private void initActivtyView(View view) {
        if(getActivity()!=null&&aeditcow!=null){
            new TitleBuilder(view).setTitleText("修改分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            initListView(String.valueOf(aeditcow.getPast_id()));
        }


    }


    private void initFragView(View view) {
        if(getArguments()!=null&&getActivity()!=null){
             editcow = (Cow)getArguments().getSerializable("editcow");
             bindStr = getArguments().getString("bindStr");
             yid = getArguments().getString("yid");
             tname = getArguments().getString("tname");
            new TitleBuilder(view).setTitleText("修改分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditHouseFragment fragment = EditHouseFragment.newInstances(editcow,bindStr,yid,tname);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }
            });
            Log.e(TAG,editcow.toString());
            initListView(String.valueOf(editcow.getPast_id()));
        }
    }


    private void initListView(String past_id) {
        OkHttpUtils.get()
                .addParams("farmid",past_id)
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response)){
                            Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                        }else{
                            Type type = new TypeToken<List<Area>>(){}.getType();
                            Gson gson = new Gson();
                            final List<Area> list = gson.fromJson(response, type);

                            for(Area area:list){
                                dataList.add(area.getArea());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
                            lv_edit_area.setAdapter(adapter);
                            lv_edit_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if(isTwoPan){
                                        Log.e(TAG,tname);
                                        editcow.setArea(dataList.get(position));
                                       EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(editcow,yid,tname);
                                       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                    }else{
                                        aeditcow.setArea(dataList.get(position));
                                        Intent intent = new Intent(getActivity(), EditCowInfoActivty.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("eeCow",aeditcow);
                                        startActivity(intent);
                                        getActivity().finish();
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
        if(context instanceof EditAreaActivity) {
            aeditcow = ((EditAreaActivity) context).setCowData();
        }
    }
}
