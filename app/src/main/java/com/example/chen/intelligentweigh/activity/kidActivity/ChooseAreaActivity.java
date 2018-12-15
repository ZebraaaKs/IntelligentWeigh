package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

/**
 * @author chen
 * @date 2018/12/13.   20:40
 * description：
 */
public class ChooseAreaActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choose_area_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    public String setIdData(){
        Intent intent = getIntent();
        if(intent!=null){
            String id = intent.getStringExtra("id");
            return id;
        }else{
            return null;
        }
    }

    public String setNameData(){
        Intent intent = getIntent();
        if(intent!=null){
            String name = intent.getStringExtra("name");
            return name;
        }else{
            return null;
        }
    }

    public NewCow setCowData(){
        Intent intent = getIntent();
        if(intent!=null){
            NewCow cow = (NewCow)intent.getSerializableExtra("cowInfo");
            return cow;
        }else{
            return null;
        }
    }
}
