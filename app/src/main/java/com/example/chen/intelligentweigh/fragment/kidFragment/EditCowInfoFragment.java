package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.CowManageActivity;
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.CowTypeActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditCowInfoActivty;
import com.example.chen.intelligentweigh.activity.kidActivity.EditHouseActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditTypeActivity;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.CowManageFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.NetWorkUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/18.   21:21
 * description：
 */
public class EditCowInfoFragment extends BaseFragment {
    private TextView tv_housename;
    private TextView tv_choose_housename;
    private TextView tv_choose_id;
    private RelativeLayout rl_house_name;
    private TextView tv_cowname;
    private TextView tv_cow_name;
    private RelativeLayout rl_cow_name;
    private TextView tv_cowtype;
    private TextView tv_cow_type;
    private TextView tv_cow_typeid;
    private RelativeLayout rl_cow_type;
    private TextView tv_cowsex;
    private TextView tv_cow_sex;
    private RelativeLayout rl_cow_sex;
    private TextView tv_cowbirth;
    private TextView tv_cow_birth;
    private RelativeLayout rl_cow_birth;
    private TextView tv_cowaccess;
    private TextView tv_cow_access;
    private RelativeLayout rl_cow_access;
    private TextView tv_cowregister;
    private TextView tv_cow_register;
    private RelativeLayout rl_cow_register;
    private TextView tv_accessprice;
    private TextView tv_access_peice;
    private RelativeLayout rl_access_price;
    private TextView tv_cowid;
    private TextView tv_cow_id;
    private RelativeLayout rl_cow_id;
    private TextView tv_fatherid;
    private TextView tv_father_id;
    private RelativeLayout rl_father_id;
    private TextView tv_matherid;
    private TextView tv_mather_id;
    private RelativeLayout rl_mather_id;
    private User user;
    private boolean isTwoPan;
    private String TAG = "NewCowFragment";
    private int mYear;
    private int mMonth;
    private int mDay;
    private Cow eCow;
    private Cow eeCow;
    private String eeid;
    private String eename;
    private String etname;
    private String eyid;
    private String eetname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_cow_frag1, container, false);
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
    }

    public static EditCowInfoFragment newInsatnces(Cow cow,String yid,String etname){
        EditCowInfoFragment fragment = new EditCowInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("editCow",cow);
        bundle.putString("eyid",yid);
        bundle.putString("etname",etname);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        tv_housename = (TextView) view.findViewById(R.id.tv_housename);
        tv_choose_housename = (TextView) view.findViewById(R.id.tv_choose_housename);
        tv_choose_id = (TextView) view.findViewById(R.id.tv_choose_id);
        rl_house_name = (RelativeLayout) view.findViewById(R.id.rl_house_name);
        tv_cowname = (TextView) view.findViewById(R.id.tv_cowname);
        tv_cow_name = (TextView) view.findViewById(R.id.tv_cow_name);
        rl_cow_name = (RelativeLayout) view.findViewById(R.id.rl_cow_name);
        tv_cowtype = (TextView) view.findViewById(R.id.tv_cowtype);
        tv_cow_type = (TextView) view.findViewById(R.id.tv_cow_type);
        tv_cow_typeid = (TextView) view.findViewById(R.id.tv_cow_typeid);
        rl_cow_type = (RelativeLayout) view.findViewById(R.id.rl_cow_type);
        tv_cowsex = (TextView) view.findViewById(R.id.tv_cowsex);
        tv_cow_sex = (TextView) view.findViewById(R.id.tv_cow_sex);
        rl_cow_sex = (RelativeLayout) view.findViewById(R.id.rl_cow_sex);
        tv_cowbirth = (TextView) view.findViewById(R.id.tv_cowbirth);
        tv_cow_birth = (TextView) view.findViewById(R.id.tv_cow_birth);
        rl_cow_birth = (RelativeLayout) view.findViewById(R.id.rl_cow_birth);
        tv_cowaccess = (TextView) view.findViewById(R.id.tv_cowaccess);
        tv_cow_access = (TextView) view.findViewById(R.id.tv_cow_access);
        rl_cow_access = (RelativeLayout) view.findViewById(R.id.rl_cow_access);
        tv_cowregister = (TextView) view.findViewById(R.id.tv_cowregister);
        tv_cow_register = (TextView) view.findViewById(R.id.tv_cow_register);
        rl_cow_register = (RelativeLayout) view.findViewById(R.id.rl_cow_register);
        tv_accessprice = (TextView) view.findViewById(R.id.tv_accessprice);
        tv_access_peice = (TextView) view.findViewById(R.id.tv_access_peice);
        rl_access_price = (RelativeLayout) view.findViewById(R.id.rl_access_price);
        tv_cowid = (TextView) view.findViewById(R.id.tv_cowid);
        tv_cow_id = (TextView) view.findViewById(R.id.tv_cow_id);
        rl_cow_id = (RelativeLayout) view.findViewById(R.id.rl_cow_id);
        tv_fatherid = (TextView) view.findViewById(R.id.tv_fatherid);
        tv_father_id = (TextView) view.findViewById(R.id.tv_father_id);
        rl_father_id = (RelativeLayout) view.findViewById(R.id.rl_father_id);
        tv_matherid = (TextView) view.findViewById(R.id.tv_matherid);
        tv_mather_id = (TextView) view.findViewById(R.id.tv_mather_id);
        rl_mather_id = (RelativeLayout) view.findViewById(R.id.rl_mather_id);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        initFragView(view);
        initActivityView(view);
    }


    private void initActivityView(View view) {
        if(getActivity()!=null&&eeCow!=null){
            new TitleBuilder(view).setTitleText("信息编辑").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            }).setRightText("提交").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Submit(eeCow);

                }
            }).build();
            tv_cow_name.setText(eeCow.getName());
            tv_access_peice.setText(""+eeCow.getEnterancePrice());
            tv_choose_housename.setText(eeCow.getFarmname()+"·"+eeCow.getArea());
            tv_choose_id.setText(eeid);
            tv_cow_access.setText(eeCow.getEntranceDay());
            tv_cow_birth.setText(eeCow.getBirthday());
            tv_cow_id.setText(eeCow.getID());
            tv_cow_register.setText(eeCow.getRegisterDay());
            tv_cow_sex.setText(eeCow.getSex());
            tv_cow_type.setText(eeCow.getKind());
            tv_father_id.setText(eeCow.getFather_id());
            tv_mather_id.setText(eeCow.getMother_id());
            ChooseHouseArea();
            ChooseType();
            InputCowName();
            InputCowPrice();
            ChooseBrithDate();
            ChooseAccessDate();
            ChooseRegisterDate();
            ChooseCowSex();
        }

    }

    /**
     * 修改信息提交
     */
    private void Submit(Cow cow) {
        if(tv_choose_housename.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请选择牧场分区",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_name.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛名称",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_type.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请选择肉牛种类",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_sex.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛公母",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_birth.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛出生日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_access.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛进场日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_cow_register.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛注册日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if( tv_access_peice.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请输入肉牛价格",Toast.LENGTH_SHORT).show();
            return;
        }


        OkHttpUtils.get()
                .url(HttpUrlUtils.UPDATECOWINFO)
                .addParams("past_id",String.valueOf(cow.getPast_id()))
                .addParams("area",cow.getArea())
                .addParams("name",tv_cow_name.getText().toString())
                .addParams("cattleid",tv_cow_id.getText().toString())
                .addParams("father_id",tv_father_id.getText().toString())
                .addParams("mother_id",tv_mather_id.getText().toString())
                .addParams("kind",tv_cow_type.getText().toString())
                .addParams("sex",tv_cow_sex.getText().toString())
                .addParams("birthday",tv_cow_birth.getText().toString())
                .addParams("entranceDay",tv_cow_access.getText().toString())
                .addParams("registerDay",tv_cow_register.getText().toString())
                .addParams("enterancePrice",tv_access_peice.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response) {
                        if("ok".equals(response)){
                            Toast.makeText(getActivity(),"修改信息成功",Toast.LENGTH_SHORT).show();
                            if(isTwoPan){
                                CowManageFragment fragment = new CowManageFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                            }else{
                                Intent intent = new Intent(getActivity(), CowManageActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initFragView(View view) {
        if(getArguments()!=null&&getActivity()!=null){
            eCow = (Cow)getArguments().getSerializable("editCow");
            eyid = getArguments().getString("eyid");
            etname = getArguments().getString("etname");
            new TitleBuilder(view).setTitleText("信息编辑").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e(TAG,eyid+eyname+etname);
                    CowManageYListFragment fragment = CowManageYListFragment.newInstances(eyid,eCow.getArea(),etname);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }
            }).setRightText("提交").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Submit(eCow);

                }
            }).build();
            tv_cow_name.setText(eCow.getName());
            tv_access_peice.setText(""+eCow.getEnterancePrice());
            tv_choose_housename.setText(eCow.getFarmname()+"·"+eCow.getArea());
            tv_choose_id.setText(eyid);
            tv_cow_access.setText(eCow.getEntranceDay());
            tv_cow_birth.setText(eCow.getBirthday());
            tv_cow_id.setText(eCow.getID());
            tv_cow_register.setText(eCow.getRegisterDay());
            tv_cow_sex.setText(eCow.getSex());
            tv_cow_type.setText(eCow.getKind());
            tv_father_id.setText(eCow.getFather_id());
            tv_mather_id.setText(eCow.getMother_id());
            ChooseHouseArea();
            ChooseType();
            InputCowName();
            InputCowPrice();
            ChooseBrithDate();
            ChooseAccessDate();
            ChooseRegisterDate();
            ChooseCowSex();

        }
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
                    EditTypeFragment fragment = EditTypeFragment.newInstances(SaveStausData(),eyid,etname);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                } else {
                    Intent intent = new Intent(getActivity(), EditTypeActivity.class);
                    intent.putExtra("acow",SaveStauseData());
                    startActivity(intent);

                }
            }
        });
    }

    /**
     * 保存数据
     *
     * @return
     */
    private Cow SaveStausData() {
        if (!tv_cow_name.getText().toString().isEmpty()) {
            eCow.setName(tv_cow_name.getText().toString());
        }
        if (!tv_choose_housename.getText().toString().isEmpty()) {
            if (tv_choose_housename.getText().toString().contains("·")) {
                String[] split = tv_choose_housename.getText().toString().split("·");
                eCow.setFarmname(split[0]);
            } else {
                eCow.setFarmname(tv_choose_housename.getText().toString());
            }
        }
        if (!tv_cow_sex.getText().toString().isEmpty()) {
            eCow.setSex(tv_cow_sex.getText().toString());
        }
        if (!tv_cow_type.getText().toString().isEmpty()) {
            eCow.setKind(tv_cow_type.getText().toString());
        }
        if (!tv_cow_register.getText().toString().isEmpty()) {
            eCow.setRegisterDay(tv_cow_register.getText().toString());
        }
        if (!tv_access_peice.getText().toString().isEmpty()) {
            eCow.setEnterancePrice(Double.parseDouble(tv_access_peice.getText().toString()));
        }
        if (!tv_cow_id.getText().toString().isEmpty()) {
            eCow.setID(tv_cow_id.getText().toString());
        }
        if (!tv_mather_id.getText().toString().isEmpty()) {
            eCow.setMother_id(tv_mather_id.getText().toString());
        }
        if (!tv_father_id.getText().toString().isEmpty()) {
            eCow.setFather_id(tv_father_id.getText().toString());
        }
        if (!tv_cow_access.getText().toString().isEmpty()) {
            eCow.setEntranceDay(tv_cow_access.getText().toString());
        }
        if (!tv_cow_birth.getText().toString().isEmpty()) {
            eCow.setBirthday(tv_cow_birth.getText().toString());
        }
        return eCow;
    }


    /**
     * 保存数据
     *
     * @return
     */
    private Cow SaveStauseData() {
        if (!tv_cow_name.getText().toString().isEmpty()) {
            eeCow.setName(tv_cow_name.getText().toString());
        }
        if (!tv_choose_housename.getText().toString().isEmpty()) {
            if (tv_choose_housename.getText().toString().contains("·")) {
                String[] split = tv_choose_housename.getText().toString().split("·");
                eeCow.setFarmname(split[0]);
            } else {
                eeCow.setFarmname(tv_choose_housename.getText().toString());
            }
        }
        if (!tv_cow_sex.getText().toString().isEmpty()) {
            eeCow.setSex(tv_cow_sex.getText().toString());
        }
        if (!tv_cow_type.getText().toString().isEmpty()) {
            eeCow.setKind(tv_cow_type.getText().toString());
        }
        if (!tv_cow_register.getText().toString().isEmpty()) {
            eeCow.setRegisterDay(tv_cow_register.getText().toString());
        }
        if (!tv_access_peice.getText().toString().isEmpty()) {
            eeCow.setEnterancePrice(Double.parseDouble(tv_access_peice.getText().toString()));
        }
        if (!tv_cow_id.getText().toString().isEmpty()) {
            eeCow.setID(tv_cow_id.getText().toString());
        }
        if (!tv_mather_id.getText().toString().isEmpty()) {
            eeCow.setMother_id(tv_mather_id.getText().toString());
        }
        if (!tv_father_id.getText().toString().isEmpty()) {
            eeCow.setFather_id(tv_father_id.getText().toString());
        }
        if (!tv_cow_access.getText().toString().isEmpty()) {
            eeCow.setEntranceDay(tv_cow_access.getText().toString());
        }
        if (!tv_cow_birth.getText().toString().isEmpty()) {
            eeCow.setBirthday(tv_cow_birth.getText().toString());
        }
        return eeCow;
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
                    Log.e(TAG, "牧场：" + user.getFarmids());
                    if (isTwoPan) {
                        EditHouseFragment fragment = EditHouseFragment.newInstances(SaveStausData(),user.getFarmids(),eyid,etname);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                    } else {
                        Intent intent = new Intent(getActivity(), EditHouseActivity.class);
                        intent.putExtra("abindstr",user.getFarmids());
                        intent.putExtra("aeditcow",SaveStauseData());
                        startActivity(intent);
                    }
                }
            }
        });
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
        if(context instanceof EditCowInfoActivty) {
            eeid = ((EditCowInfoActivty) context).setIdData();
            eename = ((EditCowInfoActivty) context).setNameData();
            eetname = ((EditCowInfoActivty) context).seteetNameData();
            eeCow = ((EditCowInfoActivty) context).setCowData();
            Log.e(TAG,"eeid "+eeid+" eename"+eename+" eeCow"+eeCow);

        }
    }
}
