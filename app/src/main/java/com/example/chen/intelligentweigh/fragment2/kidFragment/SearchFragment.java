package com.example.chen.intelligentweigh.fragment2.kidFragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.ChenZhong;
import com.example.chen.intelligentweigh.bean.RiZeng;
import com.example.chen.intelligentweigh.bean.SearchCowData;
import com.example.chen.intelligentweigh.bean.SearchCowInfo;
import com.example.chen.intelligentweigh.fragment2.AnimalFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.LineChartsUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author chen
 * @date 2019/8/18.   22:28
 * description：
 */
public class SearchFragment extends BaseFragment {

    private View rootView;
    private LinearLayout rl_titlebar;
    private TextView tv_devicename;
    private LinearLayout ll_btnsearch;
    private TextView tv_cow_id;
    private TextView tv_cow_name;
    private TextView tv_house_name;
    private TextView tv_house_id;
    private TextView tv_cow_type;
    private TextView tv_cow_birth;
    private TextView tv_register_date;
    private TextView tv_father_id;
    private TextView tv_cow_sex;
    private TextView tv_access_date;
    private TextView tv_access_price;
    private TextView tv_mother_id;

    private boolean isTwoPan;
    private SearchView sv_search;
    private final static String TAG = "SearchFragment";

    private RelativeLayout rl_nodata_show;
    private LinearLayout ll_hasdata;
    private LineChartView lcv_chart;
    private LineChartView lcv_chart_b;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.search_frag, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        return rootView;
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

    private void initView(View rootView) {
        rl_titlebar = (LinearLayout) rootView.findViewById(R.id.rl_titlebar);
        tv_devicename = (TextView) rootView.findViewById(R.id.tv_devicename);
        ll_btnsearch = (LinearLayout) rootView.findViewById(R.id.ll_btnsearch);
        tv_cow_id = (TextView) rootView.findViewById(R.id.tv_cow_id);
        tv_cow_name = (TextView) rootView.findViewById(R.id.tv_cow_name);
        tv_house_name = (TextView) rootView.findViewById(R.id.tv_house_name);
        tv_house_id = (TextView) rootView.findViewById(R.id.tv_house_id);
        tv_cow_type = (TextView) rootView.findViewById(R.id.tv_cow_type);
        tv_cow_birth = (TextView) rootView.findViewById(R.id.tv_cow_birth);
        tv_register_date = (TextView) rootView.findViewById(R.id.tv_register_date);
        tv_father_id = (TextView) rootView.findViewById(R.id.tv_father_id);
        tv_cow_sex = (TextView) rootView.findViewById(R.id.tv_cow_sex);
        tv_access_date = (TextView) rootView.findViewById(R.id.tv_access_date);
        tv_access_price = (TextView) rootView.findViewById(R.id.tv_access_price);
        tv_mother_id = (TextView) rootView.findViewById(R.id.tv_mother_id);


        sv_search = (SearchView) rootView.findViewById(R.id.sv_search);
        if (getActivity() != null) {
            new TitleBuilder(rootView).setLeftImage(R.drawable.arrowleft).setSv(getActivity(), "耳标搜索", false).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwoPan) {
                        AnimalFragment animalFragment = new AnimalFragment();
                        getFragmentManager().beginTransaction().replace(R.id.other_content_frag, animalFragment).commit();
                    } else {
                        getActivity().finish();

                    }
                }
            }).build();

            sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String text) {

                    if (!TextUtils.isEmpty(text)) {
                        final String userFarmId = SharedUtils.getUserFarmId(getActivity());
                        loadViewData(String.valueOf(text), userFarmId);
                    }


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    return false;
                }
            });


        }


        rl_nodata_show = (RelativeLayout) rootView.findViewById(R.id.rl_nodata_show);

        ll_hasdata = (LinearLayout) rootView.findViewById(R.id.ll_hasdata);

        lcv_chart = (LineChartView) rootView.findViewById(R.id.lcv_chart);

        lcv_chart_b = (LineChartView) rootView.findViewById(R.id.lcv_chart_b);

    }

    private void loadViewData(String cowId, String userFarmId) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKSEARCHINFO)
                .addParams("cattleid", cowId)
                .addParams("pastid", userFarmId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Type type = new TypeToken<SearchCowInfo>() {
                            }.getType();
                            SearchCowInfo cowInfo = new Gson().fromJson(response, type);
                            if (cowInfo != null) {
                                Type cowDataType = new TypeToken<SearchCowData>() {
                                }.getType();
                                SearchCowData cowData = new Gson().fromJson(cowInfo.getCattle(), cowDataType);
                                if (cowData != null) {
                                    ll_hasdata.setVisibility(View.VISIBLE);
                                    rl_nodata_show.setVisibility(View.GONE);
                                    tv_cow_id.setText(cowData.getID());
                                    tv_access_date.setText(cowData.getEntranceDay());
                                    tv_access_price.setText(cowData.getEnteranceprice() + " (元/公斤)");
                                    tv_cow_birth.setText(cowData.getBirthday());
                                    tv_cow_name.setText(cowData.getName());
                                    tv_cow_sex.setText(cowData.getSex());
                                    tv_cow_type.setText(cowData.getKind());
                                    tv_father_id.setText(cowData.getFather_id());
                                    tv_house_id.setText(cowData.getFather_id());
                                    tv_house_name.setText(cowData.getFarmname());
                                    tv_mother_id.setText(cowData.getMother_id());
                                    tv_register_date.setText(cowData.getRegisterDay());
                                } else {
                                    Log.e(TAG, "无数据");
                                    ll_hasdata.setVisibility(View.GONE);
                                    rl_nodata_show.setVisibility(View.VISIBLE);
                                }
                                try {
                                    ArrayList<String> xlist = new ArrayList<String>();
                                    ArrayList<Float> ylist = new ArrayList<Float>();
                                    ArrayList<String> xlist2 = new ArrayList<String>();
                                    ArrayList<Float> ylist2 = new ArrayList<Float>();
                                    Type cztype = new TypeToken<List<ChenZhong>>() {
                                    }.getType();
                                    List<ChenZhong> list = (List<ChenZhong>) new Gson().fromJson(cowInfo.getCz(), cztype);
                                    Type rztype = new TypeToken<List<RiZeng>>() {
                                    }.getType();
                                    List<RiZeng> list2 = (List<RiZeng>) new Gson().fromJson(cowInfo.getZw(), rztype);
                                    if (list != null && list.size() > 0) {
                                        for (ChenZhong data : list) {
                                            xlist.add(data.getWtime().substring(5, 6).equals("0") ? data.getWtime().substring(6) : data.getWtime().substring(5));
                                        }
                                        for (ChenZhong data : list) {
                                            ylist.add((float) data.getWeight());
                                        }
                                        String[] date = (String[]) xlist.toArray(new String[xlist.size()]);
                                        Float[] czdata = (Float[]) ylist.toArray(new Float[ylist.size()]);
                                        LineChartsUtils.initLineCharts(date, czdata, lcv_chart,"肉牛称重(重量/公斤--日期)");

                                    }else{
                                        String[] date = {"无数据","无数据","无数据","无数据","无数据"};
                                        Float[] czdata ={0f,0f,0f,0f,0f};
                                        LineChartsUtils.initLineCharts(date, czdata, lcv_chart,"肉牛称重(重量/公斤--日期)");


                                    }


                                    if (list2 != null && list2.size() > 0) {

                                        for (RiZeng data : list2) {
                                            xlist2.add(data.getWtime().substring(5, 6).equals("0") ? data.getWtime().substring(6) : data.getWtime().substring(5));
                                        }

                                        for (RiZeng data : list2) {
                                            ylist2.add((float) data.getDzengzhong());
                                        }
                                        String[] date = (String[]) xlist2.toArray(new String[xlist.size()]);
                                        Float[] czdata = (Float[]) ylist2.toArray(new Float[ylist.size()]);
                                        LineChartsUtils.initLineCharts(date, czdata, lcv_chart_b,"肉牛日增(日增重/公斤--日期)");


                                    }else{
                                        String[] date = {"无数据","无数据","无数据","无数据","无数据"};
                                        Float[] czdata ={0f,0f,0f,0f,0f};
                                        LineChartsUtils.initLineCharts(date, czdata, lcv_chart_b,"肉牛日增(日增重/公斤--日期)");

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        }


                    }


                });


    }


}
