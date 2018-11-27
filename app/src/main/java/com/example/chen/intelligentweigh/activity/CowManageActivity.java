package com.example.chen.intelligentweigh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

/**
 * author : chen
 * date   : 2018/11/27  17:20
 * desc   :
 */
public class CowManageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cow_manage_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }
}
