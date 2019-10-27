package com.example.chen.intelligentweigh.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.chen.intelligentweigh.Main2Activity;
import com.example.chen.intelligentweigh.R;

/**
 * @author chen
 * @date 2019/10/27.   18:36
 * description：
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("autoLogin",MODE_PRIVATE);
                String autoFlag = sharedPreferences.getString("autoFlag", "NO");
                if("NO".equals(autoFlag)){
                    Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);
    }
}
