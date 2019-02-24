package com.example.chen.intelligentweigh;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.activity.CloudManageActivity;
import com.example.chen.intelligentweigh.activity.CowManageActivity;
import com.example.chen.intelligentweigh.activity.HouseMangerActivity;
import com.example.chen.intelligentweigh.activity.LossPwdActivity;
import com.example.chen.intelligentweigh.activity.MeInfoActivity;
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.PeopleMangerActivity;
import com.example.chen.intelligentweigh.activity.RealTimeWeightActivity;
import com.example.chen.intelligentweigh.activity.SetTouXiangActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CattleFramKindActivity;
import com.example.chen.intelligentweigh.adapter.GridViewAdapter;
import com.example.chen.intelligentweigh.bean.CardData;
import com.example.chen.intelligentweigh.bean.Items;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.CloudManageFragment;
import com.example.chen.intelligentweigh.fragment.CowManageFragment;
import com.example.chen.intelligentweigh.fragment.HouseMangerFragment;
import com.example.chen.intelligentweigh.fragment.MeInfoFragment;
import com.example.chen.intelligentweigh.fragment.NewCowFragment;
import com.example.chen.intelligentweigh.fragment.PeopleMangerFragment;
import com.example.chen.intelligentweigh.fragment.RealTimeWeightFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.CattleFramKindFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.NetWorkUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * author : chen
 * date   : 2018/11/27  15:30
 * desc   :
 */
public class MainFragment extends BaseFragment {

    private boolean isTwoPan;
    private GridView gv_menu;
    // private CircleImageView iv_touxiang;
    private String TAG = "MainFragment";
    private TextView tv_date;
    private TextView tv_weekday;
    private TextView tv_yearmonth;
    private TextView cunlannum;
    private TextView taotainum;
    private TextView chulannum;
    private TextView tv_dayaddnum;
    private CardView cv_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_frag, container, false);
        initView(view);
        return view;
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
        IntentFilter filter = new IntentFilter(SetTouXiangActivity.action);
        //getActivity().registerReceiver(broadcastReceiver1, filter);
    }

    private void initView(View view) {
        gv_menu = (GridView) view.findViewById(R.id.gv_menu);
        GridViewAdapter adapter = new GridViewAdapter(getActivity(), R.layout.item, initGvData());
        gv_menu.setAdapter(adapter);
        //iv_touxiang = (CircleImageView) view.findViewById(R.id.iv_touxiang);
        //refreshIcon();
        //进行item的事件点击监听
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //要是双屏 （平板Pad端） 就是进行fragment的替换；要是单屏（手机端phone） 进行activity的跳转
                        if (isTwoPan) {
                            NewCowFragment fragment = new NewCowFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {

                            Intent intent = new Intent(getActivity(), NewCowActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if (isTwoPan) {
                            CattleFramKindFragment fragment = new CattleFramKindFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                          /*  CowManageFragment fragment = new CowManageFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();*/
                        } else {
                            /*Intent intent = new Intent(getActivity(), CowManageActivity.class);
                            startActivity(intent);*/
                            Intent intent = new Intent(getActivity(), CattleFramKindActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (isTwoPan) {
                            RealTimeWeightFragment fragment = new RealTimeWeightFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), RealTimeWeightActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (isTwoPan) {
                            CloudManageFragment fragment = new CloudManageFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), CloudManageActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        if (isTwoPan) {
                            MeInfoFragment fragment = new MeInfoFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), MeInfoActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 5:
                        if (isTwoPan) {
                            PeopleMangerFragment fragment = new PeopleMangerFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), PeopleMangerActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 6:
                        if (isTwoPan) {
                            HouseMangerFragment fragment = new HouseMangerFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        } else {
                            Intent intent = new Intent(getActivity(), HouseMangerActivity.class);
                            startActivity(intent);
                        }
                        break;

                    default:
                        break;
                }
            }
        });

       /* iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTwoPan) {
                    MeInfoFragment fragment = new MeInfoFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), MeInfoActivity.class);
                    startActivity(intent);
                }
            }
        });*/
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = "星期";
        switch (i) {
            case 1:
                weekDay = weekDay + "日";
                break;
            case 2:
                weekDay = weekDay + "一";
                break;
            case 3:
                weekDay = weekDay + "二";
                break;
            case 4:
                weekDay = weekDay + "三";
                break;
            case 5:
                weekDay = weekDay + "四";
                break;
            case 6:
                weekDay = weekDay + "五";
                break;
            case 7:
                weekDay = weekDay + "六";
                break;
        }
        Log.e(TAG, "星期" + i);
        String tyearmonth = year + "-" + (month + 1);
        String tdate = "" + date;
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_date.setText(tdate);
        tv_weekday = (TextView) view.findViewById(R.id.tv_weekday);
        tv_weekday.setText(weekDay);
        tv_yearmonth = (TextView) view.findViewById(R.id.tv_yearmonth);
        tv_yearmonth.setText(tyearmonth);
        cunlannum = (TextView) view.findViewById(R.id.cunlannum);
        taotainum = (TextView) view.findViewById(R.id.taotainum);
        chulannum = (TextView) view.findViewById(R.id.chulannum);
        tv_dayaddnum = (TextView) view.findViewById(R.id.tv_dayaddnum);
        initCradView();
        cv_view = (CardView) view.findViewById(R.id.cv_view);
        cv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCradView();
            }
        });

    }

    private void initCradView() {
        if(getActivity()!=null) {
            boolean networkConnected = NetWorkUtils.isNetworkConnected(getActivity());
            if(networkConnected) {
                final String phone = SharedUtils.getPhone(getActivity());
                Log.e(TAG,"phone"+phone);
                List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                Log.e(TAG,users.toString());
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
                                        Log.e(TAG,response);
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
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


  /*  BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIcon();
        }
    };*/

   /* private void refreshIcon() {
        Log.e(TAG, "main 广播");
        String phone = SharedUtils.getPhone(getActivity());
        if (!TextUtils.isEmpty(phone)) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null || !users.isEmpty()) {
                String touxiang = users.get(0).getTouxiang();
                if (touxiang != null) {
                    Log.e(TAG, "头像路径" + touxiang);
                    if (!touxiang.contains("/JDGJ/TOUX/")) {
                        Log.e(TAG, "本地获取main");
                        Glide.with(getActivity()).load(touxiang)
                                .error(R.drawable.iconn)
                                .signature(new StringSignature(SharedUtils.getTime(getActivity())))
                                .into(iv_touxiang);
                    } else {
                        Log.e(TAG, "网络获取main");
                        Glide.with(getActivity()).load(HttpUrlUtils.TOUXIANG_URL + touxiang)
                                .error(R.drawable.iconn)
                                .signature(new StringSignature(SharedUtils.getTime(getActivity())))
                                .into(iv_touxiang);
                    }
                } else {
                    iv_touxiang.setImageResource(R.drawable.iconn);
                }
            }
        } else {
            iv_touxiang.setImageResource(R.drawable.iconn);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        // getActivity().unregisterReceiver(broadcastReceiver1);
    }

    /**
     * 初始化界面GridView  的数据
     *
     * @return
     */
    private List<Items> initGvData() {
        List<Items> list = new ArrayList<>();
        Items item_1 = new Items();
        item_1.setImageId(R.drawable.cow);
        item_1.setText("新牛录入");
        list.add(item_1);
        Items item_2 = new Items();
        item_2.setImageId(R.drawable.manage);
        item_2.setText("肉牛管理");
        list.add(item_2);
        Items item_3 = new Items();
        item_3.setImageId(R.drawable.chengzhong);
        item_3.setText("实时称重");
        list.add(item_3);
        Items item_4 = new Items();
        item_4.setImageId(R.drawable.tongji);
        item_4.setText("牧场统计");
        list.add(item_4);
        Items item_7 = new Items();
        item_7.setImageId(R.drawable.accountsetting);
        item_7.setText("账户设置");
        list.add(item_7);
        String phone = SharedUtils.getPhone(getActivity());
        List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        if (users != null && users.size() > 0) {
            User user = users.get(0);
            Log.e(TAG, "当前用户信息：" + user.toString());
            if ("1".equals(user.getJurisdiction())) {
                Items item_5 = new Items();
                item_5.setImageId(R.drawable.shenpi);
                item_5.setText("人员管理");
                list.add(item_5);
                Items item_6 = new Items();
                item_6.setImageId(R.drawable.gongdan);
                item_6.setText("牧场管理");
                list.add(item_6);
            }
        }
        return list;
    }
}
