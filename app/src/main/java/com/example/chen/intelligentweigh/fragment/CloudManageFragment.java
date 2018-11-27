package com.example.chen.intelligentweigh.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.util.TitleBuilder;

/**
 * author : chen
 * date   : 2018/11/27  17:17
 * desc   : 数据统计  展示
 */
public class CloudManageFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cloud_manage_frag,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("牧场云统计").build();
    }


}
