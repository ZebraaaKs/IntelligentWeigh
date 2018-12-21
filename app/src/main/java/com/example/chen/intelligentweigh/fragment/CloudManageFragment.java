package com.example.chen.intelligentweigh.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.CloudManage;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

/**
 * author : chen
 * date   : 2018/11/27  17:17
 * desc   : 数据统计  展示
 */
public class CloudManageFragment extends BaseFragment {

    private EditText et_feedcost;
    private EditText et_currentprice;
    private TextView tv_cownum;
    private TextView tv_dayadd;
    private TextView tv_reducecost;
    private TextView tv_earnings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cloud_manage_frag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_feedcost = (EditText) view.findViewById(R.id.et_feedcost);
        et_feedcost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_currentprice = (EditText) view.findViewById(R.id.et_currentprice);
        et_currentprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_cownum = (TextView) view.findViewById(R.id.tv_cownum);
        tv_dayadd = (TextView) view.findViewById(R.id.tv_dayadd);
        tv_reducecost = (TextView) view.findViewById(R.id.tv_reducecost);
        tv_earnings = (TextView) view.findViewById(R.id.tv_earnings);
        new TitleBuilder(view).setTitleText("牧场云统计").setRightText("统计").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        }).build();
    }


    private void submit() {
        // validate
        String feedcost = et_feedcost.getText().toString().trim();
        if (TextUtils.isEmpty(feedcost)) {
            Toast.makeText(getContext(), "请输入日成本", Toast.LENGTH_SHORT).show();
            return;
        }

        final String currentprice = et_currentprice.getText().toString().trim();
        if (TextUtils.isEmpty(currentprice)) {
            Toast.makeText(getContext(), "请输入当前肉价", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpUtils.get()
                .addParams("chengben",feedcost)
                .addParams("nowprice",currentprice)
                .url(HttpUrlUtils.CLOUDMANAGE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if(getActivity()!=null){
                            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if(getActivity()!=null) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                            } else {
                                Type type = new TypeToken<CloudManage>(){}.getType();
                                Gson gson = new Gson();
                                final CloudManage cloudManage = gson.fromJson(response, type);
                                if(cloudManage!=null){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_cownum.setText(cloudManage.getCattlenum());
                                            tv_dayadd.setText(cloudManage.getRizengzhong());
                                            tv_reducecost.setText(cloudManage.getZhesuanchengben());
                                            tv_earnings.setText(cloudManage.getProfit());
                                        }
                                    });
                                }


                            }
                        }
                    }
                });



    }
}
