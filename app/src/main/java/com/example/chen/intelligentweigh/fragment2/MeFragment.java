package com.example.chen.intelligentweigh.fragment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.HouseMangerActivity;
import com.example.chen.intelligentweigh.activity.LoginActivity;
import com.example.chen.intelligentweigh.activity.MeInfoActivity;
import com.example.chen.intelligentweigh.activity.PeopleMangerActivity;
import com.example.chen.intelligentweigh.activity.SetTouXiangActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.HouseAreaActivity;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.HouseMangerFragment;
import com.example.chen.intelligentweigh.fragment.MeInfoFragment;
import com.example.chen.intelligentweigh.fragment.PeopleMangerFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.HouseAreaFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author chen
 * @date 2019/8/3.   14:30
 * description：
 */
public class MeFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private CircleImageView iv_touxiang;
    private TextView tv_username;
    private TextView tv_userphone;
    private LinearLayout ll_account;
    private LinearLayout ll_team;
    private LinearLayout ll_aboutus;
    private LinearLayout ll_usermanage;
    private LinearLayout ll_areamanage;
    private boolean isTwoPan;
    private LinearLayout ll_admin;
    private LinearLayout ll_meinfo;

    private final String TAG = "MeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.me_frag, container, false);

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

        IntentFilter filter = new IntentFilter(SetTouXiangActivity.action);
        getActivity().registerReceiver(broadcastReceiver, filter);


        IntentFilter filter1 = new IntentFilter(MeInfoFragment.action);
        getActivity().registerReceiver(broadcastReceiver1, filter1);


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIcon();
        }
    };

    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            String phone = sharedPreferences.getString("phone", "");
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if(users.size()>0&&users!=null) {
                tv_username.setText(users.get(0).getName());
            }
        }
    };

    private void refreshIcon() {
        String phone = SharedUtils.getPhone(getActivity());
        if (!TextUtils.isEmpty(phone)) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null || !users.isEmpty()) {
                String touxiang = users.get(0).getTouxiang();
                if (touxiang != null) {
                    if (!touxiang.contains("/JDGJ/TOUX/")) {
                        Glide.with(getActivity()).load(touxiang)
                                .error(R.drawable.iconn)
                                .signature(new StringSignature(SharedUtils.getTime(getActivity())))
                                .into(iv_touxiang);
                    } else {

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
    }

    private void initView(View rootView) {
        Log.e("加载meFragment", "加载了");
        iv_touxiang = (CircleImageView) rootView.findViewById(R.id.iv_touxiang);
        tv_username = (TextView) rootView.findViewById(R.id.tv_username);
        tv_userphone = (TextView) rootView.findViewById(R.id.tv_userphone);
        ll_account = (LinearLayout) rootView.findViewById(R.id.ll_account);
        ll_account.setOnClickListener(this);
        ll_team = (LinearLayout) rootView.findViewById(R.id.ll_team);
        ll_team.setOnClickListener(this);
        ll_aboutus = (LinearLayout) rootView.findViewById(R.id.ll_aboutus);
        ll_aboutus.setOnClickListener(this);
        ll_usermanage = (LinearLayout) rootView.findViewById(R.id.ll_usermanage);
        ll_usermanage.setOnClickListener(this);
        ll_areamanage = (LinearLayout) rootView.findViewById(R.id.ll_areamanage);
        ll_areamanage.setOnClickListener(this);
        ll_admin = (LinearLayout) rootView.findViewById(R.id.ll_admin);
        ll_meinfo = (LinearLayout) rootView.findViewById(R.id.ll_meinfo);
        ll_meinfo.setOnClickListener(this);
        new TitleBuilder(rootView).setTitleText("我").build();




    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
      if(isVisible){
                Log.e(TAG,"发生改变");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                String phone = sharedPreferences.getString("phone", "");
                List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                if(users.size()>0&&users!=null) {
                    tv_username.setText(users.get(0).getName());
                }
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        if (!"".equals(phone) || phone != null) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                if ("1".equals(user.getJurisdiction())) {
                    ll_admin.setVisibility(View.VISIBLE);
                }
                tv_username.setText(user.getName());
                tv_userphone.setText("手机号:" + user.getPhone());
                if (getContext() != null) {
                    if (user.getTouxiang() != null && !"".equals(user.getTouxiang())) {
                        Log.e(TAG,"加载头像");
                        Glide.with(getContext()).load(HttpUrlUtils.TOUXIANG_URL + user.getTouxiang()).signature(new StringSignature(SharedUtils.getTime(getActivity()))).into(iv_touxiang);
                    } else {
                        iv_touxiang.setImageResource(R.drawable.moren);
                    }
                } else {
                    iv_touxiang.setImageResource(R.drawable.moren);
                }


            } else {
                tv_userphone.setText("手机号:未知");
                tv_username.setText("未知");
                iv_touxiang.setImageResource(R.drawable.moren);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_meinfo:
                    if (isTwoPan) {
                        MeInfoFragment fragment = new MeInfoFragment();
                        getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                    } else {
                        Intent intent = new Intent(getActivity(), MeInfoActivity.class);
                        startActivity(intent);
                    }
                    break;
            case R.id.ll_account:
                if (isTwoPan) {
                    MeInfoFragment fragment = new MeInfoFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), MeInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_team:
                loadFramInfo();
                break;
            case R.id.ll_aboutus:
                try{
                    AlertDialog myDialog = new AlertDialog(getActivity()).onebuilder();
                    myDialog.setOneGone().setTitle("关于我们").setMsg("欢迎来电合作:xxxxxxxxxxx").setNegativeButton("好的", null).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.ll_usermanage:
                if (isTwoPan) {
                    PeopleMangerFragment fragment = new PeopleMangerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), PeopleMangerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_areamanage:
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

    private  void loadFramInfo() {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG,e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        if(!TextUtils.isEmpty(response)){
                            Type type = new TypeToken<List<House>>(){}.getType();
                            List<House> list = new Gson().fromJson(response,type);
                            if(list!=null&&list.size()>0){
                                String userFarmId = SharedUtils.getUserFarmId(getActivity());
                                for(House house:list){
                                    if(!TextUtils.isEmpty(userFarmId)){
                                        if(userFarmId.equals(house.getID())){
                                            Log.e(TAG,"服务端数据:"+house.toString());
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("houseInfo", house);
                                                Log.e(TAG, "传递农场" +house );
                                                if(isTwoPan){
                                                    HouseAreaFragment fragment = HouseAreaFragment.newInstance(house);
                                                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                                                }else{
                                                    Intent intent = new Intent(getActivity(), HouseAreaActivity.class);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            }
                                        }else{
                                        Toast.makeText(getActivity(),"未绑定牧场,稍后再试",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    }

                                }
                            }
                        }


                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(broadcastReceiver1);
    }
}
