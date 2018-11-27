package com.example.chen.intelligentweigh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.LoginActivity;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.TitleBuilder;

/**
 * author : chen
 * date   : 2018/11/27  15:59
 * desc   : 用户信息的展示
 */
public class MeInfoFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_content_frag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("我的信息").setRightText("退出")
                .setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                myDialog.setGone().setTitle("提示").setMsg("确认退出").setNegativeButton("取消",null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    }
                }).show();


            }
        }).build();
    }
}
