package com.example.chen.intelligentweigh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_phone;
    private EditText ed_ensurecode;
    private Button bt_codesend;
    private EditText et_register_password;
    private EditText et_register_username;
    private Button bt_register;
    private int i = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private static final int CODE_REPEAT = 1; //重新发送
    private String TAG = "RegisterActivity";
    private ProgressBar mProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CommonAction.getInstance().addActivity(this);
        StatusBarUtils.setColor(RegisterActivity.this,getResources().getColor(R.color.primary));
        MobSDK.init(this);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        ed_ensurecode = (EditText) findViewById(R.id.ed_ensurecode);
        bt_codesend = (Button) findViewById(R.id.bt_codesend);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_username = (EditText) findViewById(R.id.et_register_username);
        bt_register = (Button) findViewById(R.id.bt_register);

        new TitleBuilder(this).setLeftText("已有账号").setTitleText("注册账号")
                    .setLeftOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).build();

        bt_codesend.setOnClickListener(this);
        bt_register.setOnClickListener(this);

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

                if(TextUtils.isEmpty(et_phone.getText().toString())){
                    showToast("手机号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(ed_ensurecode.getText().toString())){
                    showToast("验证码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_register_password.getText().toString())){
                    showToast("密码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_register_username.getText().toString())){
                    showToast("用户名不能为空");
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
                        String pwd = et_register_password.getText().toString();
                        String username = et_register_username.getText().toString();
                   /*     //获取新绑定的手机号，存入本地后上传
                        String phoneNums = inputPhoneEt.getText().toString();
                        User user = DataSupport.findFirst(User.class);*/
                        //上传数据

                        sendRequestPhone(phone,pwd,username);


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

    private void sendRequestPhone(String phone, String pwd, String username) {
        OkHttpUtils.get()
                .addParams("user_phone",phone)
                .addParams("user_password",pwd)
                .addParams("user_name",username)
                .url(HttpUrlUtils.REGISTER_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        uitoast("请检查网络连接");
                    }

                    @Override
                    public void onResponse(String response) {
                        if("ok".equals(response.toString())){
                            uitoast("注册成功");
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            uitoast("注册失败");
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
