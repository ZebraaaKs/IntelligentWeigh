package com.example.chen.intelligentweigh.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.LoginActivity;
import com.example.chen.intelligentweigh.activity.SetTouXiangActivity;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * author : chen
 * date   : 2018/11/27  15:59
 * desc   : 用户信息的展示
 */
public class MeInfoFragment extends Fragment {


    private RelativeLayout rl_touxiang;
    private TextView tv_item_username;
    private RelativeLayout rl_username;
    private TextView tv_item_age;
    private RelativeLayout rl_age;
    private TextView tv_item_sex;
    private RelativeLayout rl_sex;
    private TextView tv_item_phone;
    private ImageView iv_item_touxiang;
    private String TAG = "MeInfoFragment";
    private TextView tv_house;
    public static final String action = "jason.broadcast.name.action";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_content_frag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rl_touxiang = (RelativeLayout) view.findViewById(R.id.rl_touxiang);
        tv_item_username = (TextView) view.findViewById(R.id.tv_item_username);
        rl_username = (RelativeLayout) view.findViewById(R.id.rl_username);
        tv_item_age = (TextView) view.findViewById(R.id.tv_item_age);
        rl_age = (RelativeLayout) view.findViewById(R.id.rl_age);
        tv_item_sex = (TextView) view.findViewById(R.id.tv_item_sex);
        rl_sex = (RelativeLayout) view.findViewById(R.id.rl_sex);
        tv_house = (TextView) view.findViewById(R.id.tv_house);
        tv_item_phone = (TextView) view.findViewById(R.id.tv_item_phone);
        new TitleBuilder(view).setTitleText("我的信息").setRightText("退出")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                        myDialog.setGone().setTitle("提示").setMsg("确认退出").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }
                        }).show();


                    }
                }).build();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        if (!"".equals(phone) || phone != null) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                tv_item_phone.setText(user.getPhone().substring(0, 3) + "****" + user.getPhone().substring(7, 11));
                if (user.getAge() == null) {
                    tv_item_age.setText("0");
                } else {
                    tv_item_age.setText(user.getAge());
                }
                if (user.getSex() == null) {
                    tv_item_sex.setText("未知");
                } else {
                    tv_item_sex.setText(user.getSex());
                }
                tv_item_username.setText(user.getName());
                if(user.getFarmids() ==null){
                    tv_house.setText("暂无牧场");
                }else{
                    tv_house.setText(user.getFarmids());
                }

            }
        }
        iv_item_touxiang = (ImageView) view.findViewById(R.id.iv_item_touxiang);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        if (!"".equals(phone) || phone != null) {
            initTouxiang();
            initName(phone);
            initAge(phone);
            initSex(phone);
        }
        IntentFilter filter = new IntentFilter(SetTouXiangActivity.action);
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    private void initSex(final String phone) {
        rl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).rgBuilder();
                myDialog.setRgGone().setTitle("性别").setChooseMsg("3").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String msg = myDialog.getRgChoose();
                        OkHttpUtils.get()
                                .url(HttpUrlUtils.CHANGEINFO_NOTOUXINANG_URL)
                                .addParams("ziduan", "sex")
                                .addParams("value", msg)
                                .addParams("phone", phone)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        if (getActivity() != null) {
                                            if ("ok".equals(response.toString())) {
                                                //先更新数据库
                                                User user = new User();
                                                user.setSex(msg);
                                                user.updateAll("phone = ?", phone);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        tv_item_sex.setText(msg);
                                                    }
                                                });
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "修改性别失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }

                                    }
                                });
                    }
                }).show();
            }
        });

    }

    /**
     * 设置用户年龄
     */
    private void initAge(final String phone) {
        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("年龄").setEtMsg("2").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String msg = myDialog.getEtMsg();
                        if (isInteger(msg)) {
                            OkHttpUtils.get()
                                    .url(HttpUrlUtils.CHANGEINFO_NOTOUXINANG_URL)
                                    .addParams("ziduan", "age")
                                    .addParams("value", msg)
                                    .addParams("phone", phone)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            if (getActivity() != null) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if (getActivity() != null) {
                                                if ("ok".equals(response.toString())) {
                                                    //先更新数据库
                                                    User user = new User();
                                                    user.setAge(msg);
                                                    user.updateAll("phone = ?", phone);
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            tv_item_age.setText(msg);
                                                        }
                                                    });

                                                    Intent intent = new Intent(action);
                                                    getActivity().sendBroadcast(intent);
                                                    getActivity().finish();

                                                } else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), "修改年龄失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            }

                                        }
                                    });


                        } else {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "年龄为数字整数", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).show();

            }
        });

    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 设置用户姓名
     */
    private void initName(final String phone) {
        rl_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("姓名").setEtMsg("1").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String msg = myDialog.getEtMsg();
                        OkHttpUtils.get()
                                .url(HttpUrlUtils.CHANGEINFO_NOTOUXINANG_URL)
                                .addParams("ziduan", "name")
                                .addParams("value", msg)
                                .addParams("phone", phone)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        if (getActivity() != null) {
                                            if ("ok".equals(response.toString())) {
                                                //先更新数据库
                                                User user = new User();
                                                user.setName(msg);
                                                user.updateAll("phone = ?", phone);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        tv_item_username.setText(msg);
                                                    }
                                                });
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "修改姓名失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }

                                    }
                                });

                    }
                }).show();

            }
        });
    }

    /**
     * 设置用户头像
     */
    private void initTouxiang() {
        refreshIcon();
        rl_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetTouXiangActivity.class);
                startActivity(intent);
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIcon();
        }
    };

    private void refreshIcon() {
        Log.e(TAG, "me 广播");
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
                                .into(iv_item_touxiang);
                    } else {
                        Log.e(TAG, "网络获取me");
                        Glide.with(getActivity()).load(HttpUrlUtils.TOUXIANG_URL + touxiang)
                                .error(R.drawable.iconn)
                                .signature(new StringSignature(SharedUtils.getTime(getActivity())))
                                .into(iv_item_touxiang);
                    }
                } else {
                    iv_item_touxiang.setImageResource(R.drawable.iconn);
                }
            }
        } else {
            iv_item_touxiang.setImageResource(R.drawable.iconn);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
