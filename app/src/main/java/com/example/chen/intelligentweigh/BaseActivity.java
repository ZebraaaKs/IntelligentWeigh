package com.example.chen.intelligentweigh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * author : chen
 * date   : 2018/11/27  12:00
 * desc   : 基础Activity类
 */
public class BaseActivity extends AppCompatActivity implements CustomAdapt {

    public String text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showToast(String mtext){
        this.text = mtext;
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    public void uitoast(String str) {
        this.text = str;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            }
        });
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
