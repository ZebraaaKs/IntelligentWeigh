package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

/**
 * @author chen
 * @date 2018/12/16.   16:07
 * description：
 */
public class CowManageExitsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cow_manage_exits_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    public String setIdData(){
        Intent intent = getIntent();
        if(intent!=null){
            String id = intent.getStringExtra("tidd");
            return id;
        }else{
            return null;
        }
    }

    public String setNameData(){
        Intent intent = getIntent();
        if(intent!=null){
            String name = intent.getStringExtra("tname");
            return name;
        }else{
            return null;
        }
    }

    public String setName2Data(){
        Intent intent = getIntent();
        if(intent!=null){
            String name2 = intent.getStringExtra("tname2");
            return name2;
        }else{
            return null;
        }
    }
}
