package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

/**
 * @author chen
 * @date 2018/12/18.   22:15
 * description：
 */
public class EditCowInfoActivty extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_cow_info_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    public Cow setCowData(){
        Intent intent = getIntent();
        if(intent!=null){
            Cow cow = (Cow)intent.getSerializableExtra("eeCow");
            return cow;
        }else{
            return null;
        }

    }


    public String setIdData(){
        Intent intent = getIntent();
        if(intent!=null){
            String id = intent.getStringExtra("eeidd");
            return id;
        }else{
            return null;
        }
    }

    public String setNameData(){
        Intent intent = getIntent();
        if(intent!=null){
            String name = intent.getStringExtra("eename");
            return name;
        }else{
            return null;
        }
    }

    public String seteetNameData(){
        Intent intent = getIntent();
        if(intent!=null){
            String name = intent.getStringExtra("eetname");
            return name;
        }else{
            return null;
        }
    }
}
