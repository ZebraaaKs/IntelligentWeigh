package com.example.chen.intelligentweigh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * author : chen
 * date   : 2018/11/27  12:09
 * desc   : 基础Fragment类
 */
public class BaseFragment extends Fragment implements CustomAdapt {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
