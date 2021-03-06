package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.ShowCowInfoActivity;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.util.LineChartsUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;

import java.util.ArrayList;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author chen
 * @date 2018/12/16.   19:01
 * description：
 */
public class ShowCowInfoFragment extends BaseFragment {
    private ImageView iv_cow_photo;
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
    private String TAG = "TAG";
    private Cow cows;
    private ArrayList<String> xListData, xListData2;
    private ArrayList<Float> yListData, yListData2;
    private LineChartView lcv_chart;
    private LineChartView lcv_chart_b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_cow_info_frag, container, false);
        initView(view);
        return view;
    }

    public static ShowCowInfoFragment newInstances(Cow cow, String framid, ArrayList<String> xList, ArrayList<Float> yList, String YN, String type1, ArrayList<String> xList2, ArrayList<Float> yList2) {
        ShowCowInfoFragment fragment = new ShowCowInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cowInfo", cow);
        bundle.putSerializable("framid", framid);
        bundle.putSerializable("xlist", xList);
        bundle.putSerializable("ylist", yList);
        bundle.putSerializable("xlist2", xList2);
        bundle.putSerializable("ylist2", yList2);
        bundle.putSerializable("YN", YN);
        bundle.putString("type1", type1);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        iv_cow_photo = (ImageView) view.findViewById(R.id.iv_cow_photo);
        tv_cow_id = (TextView) view.findViewById(R.id.tv_cow_id);
        tv_cow_name = (TextView) view.findViewById(R.id.tv_cow_name);
        tv_house_name = (TextView) view.findViewById(R.id.tv_house_name);
        tv_house_id = (TextView) view.findViewById(R.id.tv_house_id);
        tv_cow_type = (TextView) view.findViewById(R.id.tv_cow_type);
        tv_cow_birth = (TextView) view.findViewById(R.id.tv_cow_birth);
        tv_register_date = (TextView) view.findViewById(R.id.tv_register_date);
        tv_father_id = (TextView) view.findViewById(R.id.tv_father_id);
        tv_cow_sex = (TextView) view.findViewById(R.id.tv_cow_sex);
        tv_access_date = (TextView) view.findViewById(R.id.tv_access_date);
        tv_access_price = (TextView) view.findViewById(R.id.tv_access_price);
        tv_mother_id = (TextView) view.findViewById(R.id.tv_mother_id);
        lcv_chart = (LineChartView) view.findViewById(R.id.lcv_chart);
        lcv_chart_b = (LineChartView) view.findViewById(R.id.lcv_chart_b);
        initFragView(view);
        initActivityView(view);



    }

    private void initActivityView(View view) {
        if (cows != null && getActivity() != null && xListData != null && yListData != null && xListData2 != null && yListData2 != null) {
            new TitleBuilder(view).setTitleText("详情").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            Glide.with(getActivity()).load(R.drawable.cowphoto).into(iv_cow_photo);
            tv_cow_id.setText("" + cows.getID());
            tv_access_date.setText(cows.getEntranceDay());
            tv_access_price.setText("" + cows.getEnterancePrice() + " (元/公斤)");
            tv_cow_birth.setText(cows.getBirthday());
            tv_cow_name.setText(cows.getName());
            tv_cow_sex.setText(cows.getSex());
            tv_cow_type.setText(cows.getKind());
            tv_father_id.setText(cows.getFather_id());
            tv_house_id.setText(cows.getFather_id());
            tv_house_name.setText(cows.getFarmname() + "·" + cows.getArea());
            tv_mother_id.setText(cows.getMother_id());
            tv_register_date.setText(cows.getRegisterDay());
            String[] date = (String[]) xListData.toArray(new String[xListData.size()]);
            Float[] czdata = (Float[]) yListData.toArray(new Float[yListData.size()]);
            LineChartsUtils.initLineCharts(date,czdata,lcv_chart,"肉牛称重(重量/公斤--日期)");
            String[] date1 = (String[]) xListData2.toArray(new String[xListData.size()]);
            Float[] czdata1 = (Float[]) yListData2.toArray(new Float[yListData.size()]);
            LineChartsUtils.initLineCharts(date1,czdata1,lcv_chart_b,"肉牛日增(日增重/公斤--日期)");



        }
    }

    private void initFragView(final View view) {

        if (getArguments() != null && getActivity() != null) {
            final Cow newInfo = (Cow) getArguments().getSerializable("cowInfo");
            final String framid = getArguments().getString("framid");
            ArrayList<String> xlist = (ArrayList<String>) getArguments().getSerializable("xlist");
            ArrayList<Float> ylist = (ArrayList<Float>) getArguments().getSerializable("ylist");
            ArrayList<String> xlist2 = (ArrayList<String>) getArguments().getSerializable("xlist2");
            ArrayList<Float> ylist2 = (ArrayList<Float>) getArguments().getSerializable("ylist2");
            final String yn = getArguments().getString("YN");
            final String type1 = getArguments().getString("type1");
            new TitleBuilder(view).setTitleText("详情").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(yn)) {
                        CowManageYListFragment fragment = CowManageYListFragment.newInstances(framid, newInfo.getArea(), newInfo.getFarmname());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    } else {

                        CowManageNListFragment fragment = CowManageNListFragment.newInstances(framid, type1);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();

                    }
                }
            });

            Glide.with(getActivity()).load(R.drawable.cow).into(iv_cow_photo);
            tv_cow_id.setText("" + newInfo.getID());
            tv_access_date.setText(newInfo.getEntranceDay());
            tv_access_price.setText("" + newInfo.getEnterancePrice() + " (元/公斤)");
            tv_cow_birth.setText(newInfo.getBirthday());
            tv_cow_name.setText(newInfo.getName());
            tv_cow_sex.setText(newInfo.getSex());
            tv_cow_type.setText(newInfo.getKind());
            tv_father_id.setText(newInfo.getFather_id());
            tv_house_id.setText(newInfo.getFather_id());
            tv_house_name.setText(newInfo.getFarmname() + "·" + newInfo.getArea());
            tv_mother_id.setText(newInfo.getMother_id());
            tv_register_date.setText(newInfo.getRegisterDay());
            Log.e(TAG, ylist.toString());
            String[] date = (String[]) xlist.toArray(new String[xlist.size()]);
            Float[] czdata = (Float[]) ylist.toArray(new Float[ylist.size()]);
            LineChartsUtils.initLineCharts(date,czdata,lcv_chart,"肉牛称重(重量/公斤--日期)");
            String[] date1 = (String[]) xlist2.toArray(new String[xlist2.size()]);
            Float[] czdata1 = (Float[]) ylist2.toArray(new Float[ylist2.size()]);
            LineChartsUtils.initLineCharts(date1,czdata1,lcv_chart_b,"肉牛日增(日增重/公斤--日期)");



        }
    }


    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (context instanceof ShowCowInfoActivity) {
            cows = ((ShowCowInfoActivity) context).setData();
            xListData = ((ShowCowInfoActivity) context).setXData();
            yListData = ((ShowCowInfoActivity) context).setYData();
            xListData2 = ((ShowCowInfoActivity) context).setXData2();
            yListData2 = ((ShowCowInfoActivity) context).setYData2();
            Log.e(TAG, "activity传递来的数据" + cows);
        }
    }
}
