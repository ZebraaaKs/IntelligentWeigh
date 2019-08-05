package com.example.chen.intelligentweigh.fragment2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.viewpagerutil.BaseFragment;

/**
 * @author chen
 * @date 2019/8/3.   14:30
 * description：
 */
public class WeightFragment extends BaseFragment{

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.weight_frag, container, false);

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }
}
