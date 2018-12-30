package com.example.chen.intelligentweigh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.StatusBarUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.mob.MobSDK;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @author chen
 * @date 2018/12/25.   23:12
 * description：
 */
public class LossPwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout rl_titlebar;
    private EditText et_phone;
    private LinearLayout ll_first_phone;
    private EditText ed_ensurecode;
    private Button bt_codesend;
    private EditText et_loss_password;
    private LinearLayout ll_thridpassword;
    private Button bt_register;
    private int i = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private static final int CODE_REPEAT = 1; //重新发送
    private String TAG = "RegisterActivity";
    private ProgressBar mProBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loss_pwd_activity);
        initView();
        CommonAction.getInstance().addActivity(this);
        StatusBarUtils.setColor(LossPwdActivity.this, getResources().getColor(R.color.primary));
        MobSDK.init(this);
    }

    private void initView() {
        rl_titlebar = (LinearLayout) findViewById(R.id.rl_titlebar);
        et_phone = (EditText) findViewById(R.id.et_phone);
        ll_first_phone = (LinearLayout) findViewById(R.id.ll_first_phone);
        ed_ensurecode = (EditText) findViewById(R.id.ed_ensurecode);
        bt_codesend = (Button) findViewById(R.id.bt_codesend);
        et_loss_password = (EditText) findViewById(R.id.et_loss_password);
        ll_thridpassword = (LinearLayout) findViewById(R.id.ll_thridpassword);
        bt_register = (Button) findViewById(R.id.bt_register);
        new TitleBuilder(this).setLeftText("返回登录").setTitleText("忘记密码")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).build();

        // 启动短信验证sdk
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        bt_codesend.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = et_phone.getText().toString();
        switch (v.getId()) {
            case R.id.bt_codesend:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode(country, phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                bt_codesend.setClickable(false);
                bt_codesend.setText("" + i + "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();

                break;
            case R.id.bt_register:
                // validate
                String phone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ensurecode = ed_ensurecode.getText().toString().trim();
                if (TextUtils.isEmpty(ensurecode)) {
                    Toast.makeText(this, "4位数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = et_loss_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "登陆密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode(country, phoneNums, ed_ensurecode
                        .getText().toString());
                createProgressBar();

                break;
            default:break;
        }
    }

    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                bt_codesend.setText("" + i + "");
            } else if (msg.what == -8) {
                bt_codesend.setText("获取验证码");
                bt_codesend.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        String phone = et_phone.getText().toString().trim();
                        String pwd = et_loss_password.getText().toString();
                   /*     //获取新绑定的手机号，存入本地后上传
                        String phoneNums = inputPhoneEt.getText().toString();
                        User user = DataSupport.findFirst(User.class);*/
                        //上传数据
                        sendRequestPhone(phone,pwd);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //错误码官网
                        uitoast("验证码错误");
                        mProBar.setVisibility(View.GONE);
                        ((Throwable) data).printStackTrace();

                    }
                }
            }
        }
    };

    private void sendRequestPhone(String phone, String pwd) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CHANGEINFO_NOTOUXINANG_URL)
                .addParams("ziduan","password")
                .addParams("value",pwd)
                .addParams("phone",phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(LossPwdActivity.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if("ok".equals(response)){
                            Toast.makeText(LossPwdActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(LossPwdActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        //Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
