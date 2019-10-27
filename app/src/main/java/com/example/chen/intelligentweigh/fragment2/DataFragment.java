package com.example.chen.intelligentweigh.fragment2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.CardData;
import com.example.chen.intelligentweigh.bean.CloudManage;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.util.DataCheckUtils;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.example.chen.intelligentweigh.viewpagerutil.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chen
 * @date 2019/8/3.   14:30
 * description：
 */
public class DataFragment extends BaseFragment {

    private View rootView;
    private boolean isTwoPan;
    private TextView cunlannum;
    private TextView taotainum;
    private TextView chulannum;
    private TextView tv_dayaddnum;
    private EditText et_feedcost;
    private EditText et_currentprice;
    private TextView tv_cunlannum;
    private TextView tv_chulannum;
    private TextView tv_taotai;
    private TextView tv_cunlanweight;
    private TextView tv_cunlanmoney;
    private TextView tv_cunlanstartvalue;
    private TextView tv_cunlanprofits;
    private TextView tv_kpi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.data_frag, container, false);

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        cunlannum = (TextView) rootView.findViewById(R.id.cunlannum);
        taotainum = (TextView) rootView.findViewById(R.id.taotainum);
        chulannum = (TextView) rootView.findViewById(R.id.chulannum);
        tv_dayaddnum = (TextView) rootView.findViewById(R.id.tv_dayaddnum);
        et_feedcost = (EditText) rootView.findViewById(R.id.et_feedcost);
        et_feedcost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_currentprice = (EditText) rootView.findViewById(R.id.et_currentprice);
        et_currentprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_cunlannum = (TextView) rootView.findViewById(R.id.tv_cunlannum);
        tv_chulannum = (TextView) rootView.findViewById(R.id.tv_chulannum);
        tv_taotai = (TextView) rootView.findViewById(R.id.tv_taotai);
        tv_cunlanweight = (TextView) rootView.findViewById(R.id.tv_cunlanweight);
        tv_cunlanmoney = (TextView) rootView.findViewById(R.id.tv_cunlanmoney);
        tv_cunlanstartvalue = (TextView) rootView.findViewById(R.id.tv_cunlanstartvalue);
        tv_cunlanprofits = (TextView) rootView.findViewById(R.id.tv_cunlanprofits);
        tv_kpi = (TextView) rootView.findViewById(R.id.tv_kpi);
        new TitleBuilder(rootView).setTitleText("数据").setRightText("统计").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPrice = et_currentprice.getText().toString();
                String feedCost = et_feedcost.getText().toString();
                StatData(currentPrice,feedCost);

            }
        });
        initStarGridView();


    }

    private void initStarGridView() {
        final String phone = SharedUtils.getPhone(getActivity());
        List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        if (!users.isEmpty()&&users.get(0).getFarmid()!=null) {
            OkHttpUtils.get()
                    .addParams("pastid", users.get(0).getFarmid())
                    .url(HttpUrlUtils.FRAMINFO)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Type type = new TypeToken<CardData>(){}.getType();
                                Gson gson = new Gson();
                                CardData data = (CardData)gson.fromJson(response, type);
                                cunlannum.setText(data.getCunl());
                                chulannum.setText(data.getChul());
                                taotainum.setText(data.getTaotai());
                                if(data.getRizengzhong().contains("-")){
                                    tv_dayaddnum.setTextColor(getResources().getColor(R.color.red));
                                }else{
                                    tv_dayaddnum.setTextColor(getResources().getColor(R.color.greenofnum));
                                }
                                tv_dayaddnum.setText(data.getRizengzhong());
                            }

                        }
                    });
        }
    }

    private void StatData(String currentPrice, String feedCost) {
        if(getActivity()!=null) {
            if(feedCost.isEmpty()){
                Toast.makeText(getActivity(),"日成本不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentPrice.isEmpty()) {
                Toast.makeText(getActivity(),"当前肉价不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!DataCheckUtils.isDouble(feedCost)||!DataCheckUtils.isDouble(currentPrice)||".".equals(feedCost)||".".equals(currentPrice)){
                Toast.makeText(getActivity(),"数据格式不正确",Toast.LENGTH_SHORT).show();
                return;
            }

            String str_currentPrice = DataCheckUtils.double2String(Double.parseDouble(currentPrice));
            String str_feedCost = DataCheckUtils.double2String(Double.parseDouble(feedCost));

            if (SharedUtils.getMyInfo(getContext()) != null) {

                OkHttpUtils.get()
                        .addParams("chengben", str_feedCost)
                        .addParams("nowprice", str_currentPrice)
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
                                                    tv_cunlanprofits.setText(cloudManage.getCl_profit());
                                                    tv_kpi.setText(cloudManage.getKPI());

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTwoPan = true;
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTwoPan = false;
        }
    }


}
