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
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
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
    private TextView tv_cunlannum;
    private TextView tv_chulannum;
    private TextView tv_taotai;
    private TextView tv_cunlanweight;
    private TextView tv_cunlanmoney;
    private TextView tv_cunlanstartvalue;
    private TextView tv_cunlanallvalue;
    private TextView tv_cunlanprofits;
    private TextView tv_valueday;
    private TextView tv_kpi;
    private TextView tv_dayweight;


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
        tv_cunlannum = (TextView) view.findViewById(R.id.tv_cunlannum);
        tv_chulannum = (TextView) view.findViewById(R.id.tv_chulannum);
        tv_taotai = (TextView) view.findViewById(R.id.tv_taotai);
        tv_cunlanweight = (TextView) view.findViewById(R.id.tv_cunlanweight);
        tv_cunlanmoney = (TextView) view.findViewById(R.id.tv_cunlanmoney);
        tv_cunlanstartvalue = (TextView) view.findViewById(R.id.tv_cunlanstartvalue);
        tv_cunlanallvalue = (TextView) view.findViewById(R.id.tv_cunlanallvalue);
        tv_cunlanprofits = (TextView) view.findViewById(R.id.tv_cunlanprofits);
        tv_valueday = (TextView) view.findViewById(R.id.tv_valueday);
        tv_kpi = (TextView) view.findViewById(R.id.tv_kpi);
        tv_dayweight = (TextView) view.findViewById(R.id.tv_dayweight);
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
        if (SharedUtils.getMyInfo(getContext()) != null) {

            OkHttpUtils.get()
                    .addParams("chengben", feedcost)
                    .addParams("nowprice", currentprice)
                    .addParams("pastid",SharedUtils.getMyInfo(getContext()).getFarmid())
                    .url(HttpUrlUtils.CLOUDMANAGE)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponse(String response) {
                            if (getActivity() != null) {
                                if ("error".equals(response)) {
                                    Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
                                } else {
                                    Type type = new TypeToken<CloudManage>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    final CloudManage cloudManage = gson.fromJson(response, type);
                                    if (cloudManage != null) {

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_chulannum.setText(cloudManage.getChul());
                                                tv_cunlannum.setText(cloudManage.getCunl());
                                                tv_taotai.setText(cloudManage.getTaotai());
                                                tv_cunlanweight.setText(cloudManage.getCl_total_weigh());
                                                tv_cunlanmoney.setText(cloudManage.getCl_total_price());
                                                tv_cunlanstartvalue.setText(cloudManage.getCl_org_price());
                                                tv_cunlanallvalue.setText(cloudManage.getCl_total_cb());
                                                tv_cunlanprofits.setText(cloudManage.getCl_profit());
                                                tv_valueday.setText(cloudManage.getKP());
                                                tv_kpi.setText(cloudManage.getKPI());
                                                tv_dayweight.setText(cloudManage.getCl_pj_zz());

                                            }
                                        });
                                    }


                                }
                            }
                        }
                    });


        }
    }
}
