package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

import java.util.ArrayList;

/**
 * @author chen
 * @date 2018/12/17.   20:57
 * description：
 */
public class ShowCowInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.show_cow_info_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    public Cow setData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            Cow cow = (Cow) data.getSerializable("cowsInfo");
            return cow;
        }else{
            return null;
        }
    }

    public ArrayList<String> setXData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            ArrayList<String> xdata = (ArrayList<String>) data.getSerializable("xData");
            return xdata;
        }else{
            return null;
        }
    }

    public ArrayList<Float> setYData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            ArrayList<Float> ydata = (ArrayList<Float>) data.getSerializable("yData");
            return ydata;
        }else{
            return null;
        }
    }
}
