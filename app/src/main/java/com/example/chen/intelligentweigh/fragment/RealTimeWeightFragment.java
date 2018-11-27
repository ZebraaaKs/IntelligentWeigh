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
 * desc   : 对肉牛的实时称重
 */
public class RealTimeWeightFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.real_time_weight_frag,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText("实时称重").build();
    }


}
