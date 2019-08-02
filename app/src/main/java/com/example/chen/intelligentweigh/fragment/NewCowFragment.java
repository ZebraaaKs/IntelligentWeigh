package com.example.chen.intelligentweigh.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowTypeActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.DeviceList2Activity;
import com.example.chen.intelligentweigh.activity.kidActivity.DeviceListActivity;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.kidFragment.ChooseAreaFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.ChooseHouseFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.CowTypeFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.DeviceListFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.NetWorkUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * author : chen
 * date   : 2018/11/27  17:10
 * desc   : 新牛的录入
 */
public class NewCowFragment extends BaseFragment {

    private TextView tv_choose_housename;
    private Context mcontext;
    private RelativeLayout rl_house_name;
    private TextView tv_cow_name;
    private RelativeLayout rl_cow_name;
    private TextView tv_cow_type;
    private RelativeLayout rl_cow_type;
    private TextView tv_cow_sex;
    private RelativeLayout rl_cow_sex;
    private TextView tv_cow_birth;
    private RelativeLayout rl_cow_birth;
    private TextView tv_cow_access;
    private RelativeLayout rl_cow_access;
    private TextView tv_cow_register;
    private RelativeLayout rl_cow_register;
    private TextView tv_access_peice;
    private RelativeLayout rl_access_price;
    private TextView tv_cow_id;
    private RelativeLayout rl_cow_id;
    private TextView tv_father_id;
    private RelativeLayout rl_father_id;
    private TextView tv_mather_id;
    private RelativeLayout rl_mather_id;
    private TextView tv_choose_id;
    private TextView tv_cow_typeid;
    private User user;
    private boolean isTwoPan;
    private String TAG = "NewCowFragment";
    private NewCow newCow = new NewCow();
    private int mYear;
    private int mMonth;
    private int mDay;
    private NewCow cow;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private List<Integer> mBuffer;
    private static final int MSG_NEW_DATA = 3;
    private static final int MSG_TV = 4;
    private final int HEX = 0;
    private final int ASCII = 2;
    private int mCodeType = HEX;
    private ConnectThread mConnectThread;
    public ConnectedThread mConnectedThread;
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private TextView tv_cowweight;
    private View view;
    private String cid = "";
    private String cweight = "";
    private boolean flag = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.new_cow_frag, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            isTwoPan = true;
        } else {
            isTwoPan = false;
        }
        if (getArguments() != null) {
            NewCow cow1 = (NewCow) getArguments().getSerializable("ToCow");
            if (cow1 != null) {
                Log.e(TAG, "传递的值" + cow1);
                initViewByToOther(cow1);
            }
        }
        IntentFilter filter = new IntentFilter(ChooseAreaFragment.BROADCAST_ACTION);
        getActivity().registerReceiver(broadcastReceiver, filter);


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cow = (NewCow)intent.getSerializableExtra("cowInfos");
            if (cow != null) {
                Log.e(TAG,"activity 中开始了"+cow);
                initViewByToOther(cow);
            }
        }
    };

    /**
     * 初始化UI 从其他fragment跳转得到的新值
     */
    private void initViewByToOther(NewCow cow1) {
        if(cow1.getCowWeight()!=null||!"".equals(cow1.getCowWeight())){
            tv_cowweight.setText(cow1.getCowWeight());
        }
        if (cow1.getHouseName() != null || !"".equals(cow1.getHouseName())) {
            tv_choose_housename.setText(cow1.getHouseName());
        }
        if (cow1.getCowName() != null || !"".equals(cow1.getCowName())) {
            tv_cow_name.setText(cow1.getCowName());
        }
        if (cow1.getCowBrith() != null || !"".equals(cow1.getCowBrith())) {
            tv_cow_birth.setText(cow1.getCowBrith());
        }
        if (cow1.getCowFatherId() != null || !"".equals(cow1.getCowFatherId())) {
            tv_father_id.setText(cow1.getCowFatherId());
        }
        if (cow1.getCowMontherId() != null || !"".equals(cow1.getCowMontherId())) {
            tv_mather_id.setText(cow1.getCowMontherId());
        }
        if (cow1.getCowHouseDate() != null || !"".equals(cow1.getCowHouseDate())) {
            tv_cow_access.setText(cow1.getCowHouseDate());
        }
        if (cow1.getCowId() != null || !"".equals(cow1.getCowId())) {
            tv_cow_id.setText(cow1.getCowId());
        }
        if (cow1.getCowPrice() != null || !"".equals(cow1.getCowPrice())) {
            tv_access_peice.setText(cow1.getCowPrice());
        }
        if (cow1.getCowRegisterDate() != null || !"".equals(cow1.getCowRegisterDate())) {
            tv_cow_register.setText(cow1.getCowRegisterDate());
        }
        if (cow1.getCowSex() != null || !"".equals(cow1.getCowSex())) {
            tv_cow_sex.setText(cow1.getCowSex());
        }
        if (cow1.getCowType() != null || !"".equals(cow1.getCowType())) {
            tv_cow_type.setText(cow1.getCowType());
        }
        if (cow1.getHouseId() != null || !"".equals(cow1.getHouseId())) {
            tv_choose_id.setText(cow1.getHouseId());
        }
        if (cow1.getCowTypeId() != null || !"".equals(cow1.getCowTypeId())) {
            tv_cow_typeid.setText(cow1.getCowTypeId());
        }
    }

    private void initView(View view) {
        mBuffer = new ArrayList<Integer>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "请打开蓝牙",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;
        }
        tv_choose_housename = (TextView) view.findViewById(R.id.tv_choose_housename);
        rl_house_name = (RelativeLayout) view.findViewById(R.id.rl_house_name);
        tv_cow_name = (TextView) view.findViewById(R.id.tv_cow_name);
        rl_cow_name = (RelativeLayout) view.findViewById(R.id.rl_cow_name);
        tv_cow_type = (TextView) view.findViewById(R.id.tv_cow_type);
        rl_cow_type = (RelativeLayout) view.findViewById(R.id.rl_cow_type);
        tv_cow_sex = (TextView) view.findViewById(R.id.tv_cow_sex);
        rl_cow_sex = (RelativeLayout) view.findViewById(R.id.rl_cow_sex);
        tv_cow_birth = (TextView) view.findViewById(R.id.tv_cow_birth);
        rl_cow_birth = (RelativeLayout) view.findViewById(R.id.rl_cow_birth);
        tv_cow_access = (TextView) view.findViewById(R.id.tv_cow_access);
        rl_cow_access = (RelativeLayout) view.findViewById(R.id.rl_cow_access);
        tv_cow_register = (TextView) view.findViewById(R.id.tv_cow_register);
        rl_cow_register = (RelativeLayout) view.findViewById(R.id.rl_cow_register);
        tv_access_peice = (TextView) view.findViewById(R.id.tv_access_peice);
        rl_access_price = (RelativeLayout) view.findViewById(R.id.rl_access_price);
        tv_cow_id = (TextView) view.findViewById(R.id.tv_cow_id);
        rl_cow_id = (RelativeLayout) view.findViewById(R.id.rl_cow_id);
        tv_father_id = (TextView) view.findViewById(R.id.tv_father_id);
        rl_father_id = (RelativeLayout) view.findViewById(R.id.rl_father_id);
        tv_mather_id = (TextView) view.findViewById(R.id.tv_mather_id);
        rl_mather_id = (RelativeLayout) view.findViewById(R.id.rl_mather_id);
        tv_choose_id = (TextView) view.findViewById(R.id.tv_choose_id);
        tv_cow_typeid = (TextView) view.findViewById(R.id.tv_cow_typeid);
        tv_cowweight = (TextView) view.findViewById(R.id.tv_cowweight);
        tv_father_id.setText("0000");
        tv_mather_id.setText("0000");
        tv_cow_name.setText("默认");
        tv_cow_sex.setText("公");
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        tv_cow_birth.setText(formateDate());
        tv_cow_access.setText(formateDate());
        tv_cow_register.setText(formateDate());
        new TitleBuilder(view).setTitleText("新牛录入").setRightText("提交").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitCowInfo();

            }
        }).setLeftText("选择蓝牙设备").setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeType = HEX;
                mHandler.sendEmptyMessage(MSG_NEW_DATA);
                if (mConnectThread != null) {
                    mConnectThread.cancel();
                    mConnectThread = null;
                }
                if(isTwoPan){
                    DeviceList2Fragment fragment = new DeviceList2Fragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }else{
                    Intent serverIntent = new Intent(getActivity(), DeviceList2Activity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }

            }
        }).build();
        ChooseHouseArea();
        ChooseType();
        InputCowName();
        InputCowPrice();
        InputCowId();
        InputFatherId();
        InputMatherId();
        ChooseBrithDate();
        ChooseAccessDate();
        ChooseRegisterDate();
        ChooseCowSex();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case MSG_NEW_DATA:
                        StringBuffer buf = new StringBuffer();
                        String str = "";
                        synchronized (mBuffer) {
                            if (mCodeType == ASCII) {
                                for (int i : mBuffer) {
                                    buf.append((char) i);
                                }

                            } else if (mCodeType == HEX) {
                                for (int i : mBuffer) {
                                    if (i > 9) {
                                        buf.append(Integer.toHexString(i));
                                    } else {
                                        buf.append(0);
                                        buf.append(i);
                                    }
                                }

                            } else {
                                for (int i : mBuffer) {
                                    buf.append(i);
                                }
                            }
                            Log.e(TAG, "元数据" + buf.toString());
                            String data = buf.toString();
                            int strlength = data.length() - 1;
                            char c[] = data.toCharArray();
                            if (strlength > 61) {
                                if (c[strlength] == 'e' && c[strlength - 1] == 'f' && c[strlength - 60] == 'f' && c[strlength - 61] == 'f') {
                                    String data1 = data.substring(strlength - 61, strlength + 1);
                                    Log.e(TAG, "新数据rrr" + data1);
                                    String date = formatDate(data1.substring(10, 18));
                                    Log.e(TAG, "日期rrr" + date);
                                     cid = formatId(data1.substring(18, 48));
                                    Log.e(TAG, "编号rrr" + cid);
                                     cweight = hex2decimal(data1.substring(48, 56), data1.substring(58, 60));
                                    Log.e(TAG, "重量rrr" + cweight);

                                    tv_cowweight.setText(cweight);
                                    Log.e(TAG,tv_cowweight.getText().toString());
                                    tv_cow_id.setText(cid);
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("weight",cweight);
                                    bundle.putString("id",cid);
                                    message.setData(bundle);
                                    message.what = MSG_TV;
                                    handler.sendMessage(message);
                                }
                                break;
                            }


                        }


                        break;

                    default:
                        break;
                }
            }

        };







    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG,"赋值赋值"+mcontext);
            switch (msg.what){
                case MSG_TV:
                    tv_cowweight.setText(msg.getData().getString("weight"));
            }
        }
    };

    private String formateDate() {
        String days;
        if (mMonth + 1 < 10) {
            if (mDay < 10) {
                days = new StringBuffer().append(mYear).append("-").append("0").
                        append(mMonth + 1).append("-").append("0").append(mDay).toString();
            } else {
                days = new StringBuffer().append(mYear).append("-").append("0").
                        append(mMonth + 1).append("-").append(mDay).toString();
            }

        } else {
            if (mDay < 10) {
                days = new StringBuffer().append(mYear).append("-").
                        append(mMonth + 1).append("-").append("0").append(mDay).toString();
            } else {
                days = new StringBuffer().append(mYear).append("-").
                        append(mMonth + 1).append("-").append(mDay).toString();
            }

        }
        return days;

    }

    /**
     * 提交录入新牛的信息
     */
    private void SubmitCowInfo() {

        if (tv_choose_housename.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请选择牧场分区", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_name.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_id.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛编号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_father_id.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入父牛编号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_mather_id.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入母牛编号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_type.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请选择肉牛种类", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_sex.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛公母", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_birth.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛出生日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_access.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛进场日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_cow_register.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛注册日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_access_peice.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入肉牛价格", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e(TAG, "cowweight" + tv_cowweight.getText().toString());
        Log.e(TAG, "past_id" + tv_choose_id.getText().toString() + "area" + tv_choose_housename.getText().toString());
        Log.e(TAG, "name" + tv_cow_name.getText().toString());
        Log.e(TAG, "id" + tv_cow_id.getText().toString());
        Log.e(TAG, "father_id" + tv_father_id.getText().toString());
        Log.e(TAG, "mother_id" + tv_mather_id.getText().toString());
        Log.e(TAG, "kind" + tv_cow_type.getText().toString());
        Log.e(TAG, "sex" + tv_cow_sex.getText().toString());
        Log.e(TAG, "birthday" + tv_cow_birth.getText().toString());
        Log.e(TAG, "entranceDay" + tv_cow_access.getText().toString());
        Log.e(TAG, "registerDay" + tv_cow_register.getText().toString());
        Log.e(TAG, "enterancePrice" + tv_access_peice.getText().toString());
        Log.e(TAG, "typeId" + tv_cow_typeid.getText().toString());
        String phone = SharedUtils.getPhone(getActivity());
        OkHttpUtils.get()
                .addParams("cattleid",tv_cow_id.getText().toString())
                .addParams("phone",phone)
                .addParams("weigh",tv_cowweight.getText().toString())
                .addParams("datetime",tv_cow_access.getText().toString())
                .addParams("kind","1")
                .url(HttpUrlUtils.ADDONEWEIGHT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });


        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEADDSERVER)
                .addParams("past_id", tv_choose_id.getText().toString())
                .addParams("area", tv_choose_housename.getText().toString())
                .addParams("name", tv_cow_name.getText().toString())
                .addParams("id", tv_cow_id.getText().toString())
                .addParams("father_id", tv_father_id.getText().toString())
                .addParams("mother_id", tv_mather_id.getText().toString())
                .addParams("kind", tv_cow_type.getText().toString())
                .addParams("sex", tv_cow_sex.getText().toString())
                .addParams("birthday", tv_cow_birth.getText().toString())
                .addParams("entranceDay", tv_cow_access.getText().toString())
                .addParams("registerDay", tv_cow_register.getText().toString())
                .addParams("enterancePrice", tv_access_peice.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if (getActivity() != null) {
                            if ("ok".equals(response)) {
                                Toast.makeText(getActivity(), "录入成功", Toast.LENGTH_SHORT).show();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_access_peice.setText("");
                                        tv_cow_type.setText("");
                                        tv_choose_housename.setText("");
                                        tv_cow_id.setText("");
                                        tv_cowweight.setText("");
                                    }
                                });
                            } else {
                                Log.e(TAG,response);
                                Toast.makeText(mcontext, "录入失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

    /**
     * 性别点击事件
     */
    private void ChooseCowSex() {
        rl_cow_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).rgCowBuilder();
                myDialog.setRgGone().setTitle("公母").setChooseMsg("4").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_cow_sex.setText(myDialog.getRgCowChoose());
                    }
                }).show();
            }
        });
    }

    /**
     * 进场日期点击事件
     */
    private void ChooseAccessDate() {
        rl_cow_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用时间选择器
                new DatePickerDialog(getActivity(), onAccessDateSetListener, mYear, mMonth, mDay).show();
            }
        });
    }

    /**
     * 登记日期点击事件
     */
    private void ChooseRegisterDate() {
        rl_cow_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用时间选择器
                new DatePickerDialog(getActivity(), onRegisterDateSetListener, mYear, mMonth, mDay).show();
            }
        });
    }

    /**
     * 出生日期点击事件
     */
    private void ChooseBrithDate() {
        rl_cow_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用时间选择器
                new DatePickerDialog(getActivity(), onBirthDateSetListener, mYear, mMonth, mDay).show();
            }
        });
    }

    /**
     * 母牛ID点击事件
     */
    private void InputMatherId() {
        rl_mather_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("母牛编号").setEtMsg("9").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_mather_id.setText(myDialog.getEtMsg());
                    }
                }).show();

            }
        });

    }

    /**
     * 父牛Id点击事件
     */
    private void InputFatherId() {

        rl_father_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("父牛编号").setEtMsg("8").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_father_id.setText(myDialog.getEtMsg());
                    }
                }).show();

            }
        });
    }

    /**
     * 肉牛Id点击事件
     */
    private void InputCowId() {
        rl_cow_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("肉牛编号").setEtMsg("7").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_cow_id.setText(myDialog.getEtMsg());
                    }
                }).show();

            }
        });
    }

    /**
     * 肉牛价格点击事件
     */
    private void InputCowPrice() {
        rl_access_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("价格").setEtMsg("6").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_access_peice.setText(myDialog.getEtMsg());
                    }
                }).show();

            }
        });
    }

    /**
     * 肉牛名称点击事件
     */
    private void InputCowName() {
        rl_cow_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                myDialog.setEtGone().setTitle("名称").setEtMsg("5").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_cow_name.setText(myDialog.getEtMsg());
                    }
                }).show();

            }
        });
    }


    /**
     * 肉牛类型点击事件
     */
    private void ChooseType() {
        rl_cow_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTwoPan) {
                    CowTypeFragment fragment = CowTypeFragment.newInstance(SaveStausData());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), CowTypeActivity.class);
                    intent.putExtra("cowsInfo", SaveStausData());
                    startActivityForResult(intent,10);
                    //startActivity(intent);
                }
            }
        });
    }



    /**
     * 肉牛牧区点击事件
     */
    private void ChooseHouseArea() {
        rl_house_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    String phone = SharedUtils.getPhone(getActivity());
                    List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                    if (!users.isEmpty()) {
                        user = users.get(0);
                    }
                    if (NetWorkUtils.isNetworkConnected(getActivity())) {
                        UpdateUser(user.getPhone(), user.getPassword());
                    }
                    List<User> users2 = LitePal.where("phone = ?", phone).find(User.class);
                    if (!users2.isEmpty()) {
                        user = users2.get(0);
                    }
                    Log.e(TAG, "牧场：" + user.getFarmids());
                    if (isTwoPan) {
                        ChooseHouseFragment fragment = ChooseHouseFragment.newInstance(user.getFarmids(), SaveStausData());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment, "0").addToBackStack(null).commit();
                    } else {
                        if (getActivity() != null) {
                            Intent intent = new Intent(getActivity(), ChooseHouseActivity.class);
                            intent.putExtra("framid", user.getFarmids());
                            intent.putExtra("newcow", SaveStausData());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }


    /**
     * 更新本地数据库
     *
     * @param phone
     * @param pwd
     */
    private void UpdateUser(final String phone, String pwd) {
        OkHttpUtils.get()
                .addParams("user_phone", phone)
                .addParams("user_password", pwd)
                .url(HttpUrlUtils.LOGIN_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        User newUser = gson.fromJson(response.toString(), User.class);
                        newUser.updateAll("phone = ?", phone);
                    }
                });
    }


    public static NewCowFragment newInstances(NewCow cow) {
        NewCowFragment fragment = new NewCowFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ToCow", cow);
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 保存数据
     *
     * @return
     */
    private NewCow SaveStausData() {
        if(!tv_cowweight.getText().toString().isEmpty()){
            newCow.setCowWeight(tv_cowweight.getText().toString());
        }
        if (!tv_cow_name.getText().toString().isEmpty()) {
            newCow.setCowName(tv_cow_name.getText().toString());
        }
        if (!tv_choose_housename.getText().toString().isEmpty()) {
            newCow.setHouseName(tv_choose_housename.getText().toString());
        }
        if (!tv_cow_sex.getText().toString().isEmpty()) {
            newCow.setCowSex(tv_cow_sex.getText().toString());
        }
        if (!tv_cow_type.getText().toString().isEmpty()) {
            newCow.setCowType(tv_cow_type.getText().toString());
        }
        if (!tv_cow_register.getText().toString().isEmpty()) {
            newCow.setCowRegisterDate(tv_cow_register.getText().toString());
        }
        if (!tv_access_peice.getText().toString().isEmpty()) {
            newCow.setCowPrice(tv_access_peice.getText().toString());
        }
        if (!tv_cow_id.getText().toString().isEmpty()) {
            newCow.setCowId(tv_cow_id.getText().toString());
        }
        if (!tv_mather_id.getText().toString().isEmpty()) {
            newCow.setCowMontherId(tv_mather_id.getText().toString());
        }
        if (!tv_father_id.getText().toString().isEmpty()) {
            newCow.setCowFatherId(tv_father_id.getText().toString());
        }
        if (!tv_cow_access.getText().toString().isEmpty()) {
            newCow.setCowHouseDate(tv_cow_access.getText().toString());
        }
        if (!tv_cow_birth.getText().toString().isEmpty()) {
            newCow.setCowBrith(tv_cow_birth.getText().toString());
        }
        if (!tv_choose_id.getText().toString().isEmpty()) {
            newCow.setHouseId(tv_choose_id.getText().toString());
        }
        if (!tv_cow_typeid.getText().toString().isEmpty()) {
            newCow.setCowTypeId(tv_cow_typeid.getText().toString());
        }

        return newCow;
    }


    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onBirthDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            tv_cow_birth.setText(days);
        }
    };

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onRegisterDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            tv_cow_register.setText(days);
        }
    };

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onAccessDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            tv_cow_access.setText(days);
        }
    };


    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
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
        mcontext = activity;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (context instanceof NewCowActivity) {
            cow = ((NewCowActivity) context).setData();
            Log.e(TAG, "cow " + cow);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }

    }


    public String formatDate(String data) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String m1 = data.substring(0, 2);
        int i = Integer.parseInt(m1);
        String m2 = data.substring(2, 4);
        int i1 = Integer.parseInt(m2);
        int month = i + i1;
        String d1 = data.substring(4, 6);
        int i2 = Integer.parseInt(d1);
        String d2 = data.substring(6, 8);
        int i3 = Integer.parseInt(d2);
        int day = i2 + i3;
        return year + "-" + month + "-" + day;
    }

    public String formatId(String data) {
        String id = "";
        for (int i = 0; i < data.length(); i++) {
            if (i == 0 || i % 2 == 0) {
                String d = data.substring(i, i + 2);
                int i1 = Integer.parseInt(d);
                Log.e(TAG, "id数据" + i1);
                id = id + i1;
            }
        }
        return id;
    }


    public String hex2decimal(String hex, String xiaoshu) {
        int k = Integer.parseInt(xiaoshu);
        String j = "";
        for (int i = 0; i < hex.length(); i++) {
            if (i == 0 || i % 2 == 0) {
                String substring = hex.substring(i, i + 2);
                int i1 = Integer.parseInt(substring);
                j = j + i1;
            }
        }
        Log.e(TAG, "j=====" + j);

        return String.valueOf(Integer.parseInt(j) + "." + k);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled Launch the DeviceListActivity to see
                    // devices and do scan
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), "BT not enabled", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                } else {
                    String address = data.getExtras().getString(
                            DeviceListFragment.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    connect(device);
                }
                break;
            case 10:
                NewCow cow = (NewCow)data.getSerializableExtra("cowInfo");
                initViewByToOther(cow);
                break;
        }

    }

    public void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID
                        .fromString(SPP_UUID));
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {

                Log.e(TAG, "unable to connect() socket", e);
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG,
                            "unable to close() socket during connection failure",
                            e2);
                }
                return;
            }

            mConnectThread = null;

            // Start the connected thread
            // Start the thread to manage the connection and perform
            // transmissions
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[256];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    synchronized (mBuffer) {
                        for (int i = 0; i < bytes; i++) {
                            mBuffer.add(buffer[i] & 0xFF);
                        }
                    }
                    mHandler.sendEmptyMessage(MSG_NEW_DATA);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }


}
