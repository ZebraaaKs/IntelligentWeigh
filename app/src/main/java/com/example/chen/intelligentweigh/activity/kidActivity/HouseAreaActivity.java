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
 * @date 2018/12/6.   19:32
 * description：牧场分区
 */
public class HouseAreaActivity extends BaseActivity {

    private String TAG = "HouseAreaActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.house_area_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }


    public House setData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            House houseInfo = (House) data.getSerializable("houseInfo");
            Log.e(TAG, "HouseAreaActivity" + houseInfo);
            return houseInfo;
        }else{
            return null;
        }
    }
}
