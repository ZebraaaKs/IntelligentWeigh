package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.LoginActivity;
import com.example.chen.intelligentweigh.activity.PeopleMangerActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.BindHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.PeopleInfoActivity;
import com.example.chen.intelligentweigh.bean.People;
import com.example.chen.intelligentweigh.fragment.PeopleMangerFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.Event.MessageEvent;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author chen
 * @date 2018/12/5.   16:50
 * description：
 */
public class PeopleInfoFragment extends BaseFragment {

    private CircleImageView iv_people_touxiang;
    private TextView tv_people_name;
    private TextView tv_people_name2;
    private TextView tv_people_sex;
    private TextView tv_people_age;
    private TextView tv_people_phone;
    private TextView tv_people_house;
    private Button btn_bindhouse;
    private Button btn_deletepeople;
    private People people;
    private boolean isTwoPan;
    private String TAG = "PeopleInfoFragment";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_info_frag, container, false);
        initView(view);
        return view;
    }

    /**
     *  提前加载布局
     * @param people
     * @return
     */
    public static PeopleInfoFragment newInstance(People people){
        PeopleInfoFragment peopleInfoFragment = new PeopleInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("peopleInfo",people);
        peopleInfoFragment.setArguments(bundle);
        return peopleInfoFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            isTwoPan = true;
        } else {
            isTwoPan = false;
        }

    }



    private void initView(View view) {
        iv_people_touxiang = (CircleImageView) view.findViewById(R.id.iv_people_touxiang);
        tv_people_name = (TextView) view.findViewById(R.id.tv_people_name);
        tv_people_name2 = (TextView) view.findViewById(R.id.tv_people_name2);
        tv_people_sex = (TextView) view.findViewById(R.id.tv_people_sex);
        tv_people_age = (TextView) view.findViewById(R.id.tv_people_age);
        tv_people_phone = (TextView) view.findViewById(R.id.tv_people_phone);
        tv_people_house = (TextView) view.findViewById(R.id.tv_people_house);
        btn_bindhouse = (Button) view.findViewById(R.id.btn_bindhouse);
        btn_deletepeople = (Button) view.findViewById(R.id.btn_deletepeople);
        onEvent(view);
        onEventByActivity(view);

    }







    /**
     * fragment赋值控件
     * @param view
     */
    public void onEvent(View view) {
        if (getArguments() != null) {
            final People people = (People) getArguments().getSerializable("peopleInfo");
            new TitleBuilder(view).setTitleText(people.getName()).setLeftText("返回").setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PeopleMangerFragment fragment = new PeopleMangerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                }
            });
            Glide.with(getActivity()).load(HttpUrlUtils.TOUXIANG_URL + people.getTouxiang())
                    .error(R.drawable.moren)
                    .into(iv_people_touxiang);
            tv_people_name.setText(people.getName());
            tv_people_name2.setText(people.getName());
            tv_people_age.setText(people.getAge());
            if (people.getFarmids() != null) {
                tv_people_house.setText(people.getFarmids());
            } else {
                tv_people_house.setText("暂无绑定");
            }
            tv_people_phone.setText(people.getPhone());
            tv_people_sex.setText(people.getSex());
            //绑定农场
            btn_bindhouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity()!=null){
                        BindHouseFragment fragment = BindHouseFragment.newInstance(people);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    }

                }
            });
            //删除用户
            btn_deletepeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                    myDialog.setGone().setTitle("提示").setMsg("删除用户").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doDeletePeople(people.getPhone());

                        }
                    }).show();
                }
            });
        }
    }

    /**
     * 删除用户
     * @param phone 手机号
     */
    private void doDeletePeople(String phone) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.DELETEUSER_URL)
                .addParams("user_phone",phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (getActivity() != null) {
                            if ("ok".equals(response.toString())) {
                                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();
                                EventBus.getDefault().post(new MessageEvent("delete_user"));
                                if (isTwoPan) {
                                    PeopleMangerFragment fragment = new PeopleMangerFragment();
                                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                                } else {
                                    getActivity().finish();
                                }

                            } else {
                                Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }


    /**
     * 通过activity来拿取值赋值孔径啊
     */
    private void onEventByActivity(View view) {
        if(people!=null){
            new TitleBuilder(view).setTitleText(people.getName()).setLeftText("返回").setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            Glide.with(getActivity()).load(HttpUrlUtils.TOUXIANG_URL + people.getTouxiang())
                    .error(R.drawable.moren)
                    .into(iv_people_touxiang);
            tv_people_name.setText(people.getName());
            tv_people_name2.setText(people.getName());
            tv_people_age.setText(people.getAge());
            if (people.getFarmids() != null) {
                tv_people_house.setText(people.getFarmids());
            } else {
                tv_people_house.setText("暂无绑定");
            }
            tv_people_phone.setText(people.getPhone());
            tv_people_sex.setText(people.getSex());
            //绑定农场
            btn_bindhouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity()!=null){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("BindPeopleInfo",people);
                        Intent intent = new Intent(getActivity(), BindHouseActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }
            });
            //删除用户
            btn_deletepeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                    myDialog.setGone().setTitle("提示").setMsg("删除用户").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doDeletePeople(people.getPhone());

                        }
                    }).show();
                }
            });
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
        if(context instanceof PeopleInfoActivity) {
            people = ((PeopleInfoActivity) context).setData();
            Log.e(TAG, "activity传递来的数据" + people);
        }
    }
}
