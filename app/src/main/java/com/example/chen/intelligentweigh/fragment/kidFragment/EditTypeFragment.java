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
import com.example.chen.intelligentweigh.activity.kidActivity.EditCowInfoActivty;
import com.example.chen.intelligentweigh.activity.kidActivity.EditTypeActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.HouseAreaActivity;
import com.example.chen.intelligentweigh.adapter.ListViewChooseTypeAdapter;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.CowType;
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
 * @date 2018/12/22.   22:34
 * description：
 */
public class EditTypeFragment extends BaseFragment {

    private ListView lv_edit_type;
    private boolean isTwoPan;
    private List<CowType> list = new ArrayList<>();
    private Cow fcow;
    private String fid;
    private String fetname;
    private Cow acow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_type_frag, container, false);
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

    private void initView(View view) {
        lv_edit_type = (ListView) view.findViewById(R.id.lv_edit_type);
        initFragView(view);
        initActivityView(view);
    }

    private void initActivityView(View view) {
        if(getActivity()!=null&&acow!=null){
            new TitleBuilder(view).setTitleText("修改种类").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            onInitUI();
        }
    }

    private void initFragView(View view) {
        if(getArguments()!=null&&getActivity()!=null){
             fcow = (Cow)getArguments().getSerializable("cow");
             fid = getArguments().getString("id");
             fetname = getArguments().getString("etname");
             new TitleBuilder(view).setTitleText("修改种类").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(fcow,fid,fetname);
                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                 }
             });

             onInitUI();

        }
    }


    /**
     * ListUI
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
                                lv_edit_type.setAdapter(adapter);
                                lv_edit_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        CowType cowType = list.get(position);
                                        if(isTwoPan){
                                            fcow.setKind(cowType.getName());
                                            EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(fcow,fid,fetname);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                                        }else{
                                            acow.setKind(cowType.getName());
                                            Intent intent = new Intent(getActivity(),EditCowInfoActivty.class);
                                            intent.putExtra("eeCow",acow);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public static EditTypeFragment newInstances(Cow eCow, String eyid, String etname) {
        EditTypeFragment fragment = new EditTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cow",eCow);
        bundle.putString("id",eyid);
        bundle.putString("etname",etname);
        fragment.setArguments(bundle);
        return  fragment;
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
        if(context instanceof EditTypeActivity) {
            acow = ((EditTypeActivity) context).setcowData();
        }
    }
}
