package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

/**
 * @author chen
 * @date 2018/12/7.   12:48
 * description：绑定用户农场
 */
public class BindHouseActivity extends BaseActivity {
    private String TAG = "BindHouseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bind_house_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }


    public People setData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            People people = (People) data.getSerializable("BindPeopleInfo");
            Log.e(TAG, "BindHouseActivity" + people);
            return people;
        }else{
            return null;
        }
    }
}
