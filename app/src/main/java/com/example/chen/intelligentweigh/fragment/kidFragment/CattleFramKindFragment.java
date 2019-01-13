package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageNListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowMangerAreaActivity;
import com.example.chen.intelligentweigh.bean.CattleFramKind;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chen
 * @date 2019/1/13.   12:10
 * description：
 */
public class CattleFramKindFragment extends BaseFragment {

    private TextView tv_cunlan;
    private LinearLayout ll_cunlan;
    private TextView tv_chulan;
    private LinearLayout ll_chulan;
    private TextView tv_taotai;
    private LinearLayout ll_taotai;
    private boolean isTwoPan;
    private String TAG = "CattleFramKindFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cattleframkind_frag, container, false);
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
        tv_cunlan = (TextView) view.findViewById(R.id.tv_cunlan);
        ll_cunlan = (LinearLayout) view.findViewById(R.id.ll_cunlan);
        tv_chulan = (TextView) view.findViewById(R.id.tv_chulan);
        ll_chulan = (LinearLayout) view.findViewById(R.id.ll_chulan);
        tv_taotai = (TextView) view.findViewById(R.id.tv_taotai);
        ll_taotai = (LinearLayout) view.findViewById(R.id.ll_taotai);
        new TitleBuilder(view).setTitleText("分类").build();
        if(getActivity()!=null) {
            initUI();
        }

    }

    /**
     * 初始化界面
     */
    private void initUI() {
        String phone = SharedUtils.getPhone(getActivity());
        final List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        //Log.e(TAG,users.get(0).toString());
        if(!users.isEmpty()){
            String farmid = users.get(0).getFarmid();
            if(farmid!=null||!"".equals(farmid)){
                OkHttpUtils.get()
                        .addParams("pastid",farmid)
                        .url(HttpUrlUtils.CATTLEFRAMKIND)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response)){
                                    Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                                }else{
                                    Type type = new TypeToken<CattleFramKind>(){}.getType();
                                    Gson gson = new Gson();
                                    CattleFramKind cattleFramKind = (CattleFramKind)gson.fromJson(response, type);
                                    tv_chulan.setText("("+cattleFramKind.getChul()+"头)");
                                    tv_cunlan.setText("("+cattleFramKind.getCunl()+"头)");
                                    tv_taotai.setText("("+cattleFramKind.getTaotai()+"头)");
                                    doEventLister(cattleFramKind,users.get(0));
                                }

                            }
                        });

            }
        }
    }

    /**
     * 点击事件
     * @param cattleFramKind
     */
    private void doEventLister(CattleFramKind cattleFramKind, final User user) {
        if(!"0".equals(cattleFramKind.getChul())) {
            ll_chulan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwoPan) {
                        CowManageNListFragment fragment = CowManageNListFragment.newInstances(user.getFarmid(),"出栏牛");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    } else {
                        Intent intent = new Intent(getActivity(), CowManageNListActivity.class);
                        intent.putExtra("Nidd",user.getFarmid());
                        intent.putExtra("Nname","出栏牛");
                        startActivity(intent);
                    }

                }
            });
        }

        if(!"0".equals(cattleFramKind.getCunl())) {

            ll_cunlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwoPan) {
                        CowManagerAreaFragment fragment = CowManagerAreaFragment.newInstance(user.getFarmid(),user.getFarmids());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    } else {
                        Intent intent = new Intent(getActivity(), CowMangerAreaActivity.class);
                        intent.putExtra("mangerId",user.getFarmid());
                        intent.putExtra("mangerName",user.getFarmids());
                        startActivity(intent);
                    }
                }
            });
        }

        if(!"0".equals(cattleFramKind.getTaotai())) {
            ll_taotai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwoPan) {
                        CowManageNListFragment fragment = CowManageNListFragment.newInstances(user.getFarmid(),"淘汰牛");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    } else {
                        Intent intent = new Intent(getActivity(), CowManageNListActivity.class);
                        intent.putExtra("Nidd",user.getFarmid());
                        intent.putExtra("Nname","淘汰牛");
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
