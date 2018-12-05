package com.example.chen.intelligentweigh.activity.kidActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/5.   16:47
 * description：查看个人信息的详情
 */
public class PeopleInfoActivity  extends BaseActivity{
    private String TAG = "PeopleInfoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.people_info_activity);
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    public People setData(){
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle data = intent.getExtras();
            People peopleInfo = (People) data.getSerializable("peopleInfo");
            Log.e(TAG, "PeopleInfoActivity" + peopleInfo);
            return peopleInfo;
        }else{
            return null;
        }
    }
}
