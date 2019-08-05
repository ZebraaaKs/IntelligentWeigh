package com.example.chen.intelligentweigh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.Main2Activity;
import com.example.chen.intelligentweigh.MainActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.StatusBarUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;
import org.xml.sax.helpers.LocatorImpl;

import java.lang.reflect.Type;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_password;
    private RadioGroup rg_loginType;
    private Button btn_login;
    private TextView tv_lossPwd;
    private TextView tv_register;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isFirstRun){
            LitePal.getDatabase();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
        StatusBarUtils.setStatusBarFull(this);
        CommonAction.getInstance().addActivity(this);
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setInputType( InputType.TYPE_CLASS_NUMBER);
        et_password = (EditText) findViewById(R.id.et_password);
        initPhonePwd();
        rg_loginType = (RadioGroup) findViewById(R.id.rg_loginType);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_lossPwd = (TextView) findViewById(R.id.tv_lossPwd);
        tv_register = (TextView) findViewById(R.id.tv_register);

        btn_login.setOnClickListener(this);

        tv_lossPwd.setOnClickListener(this);

        tv_register.setOnClickListener(this);
    }

    private void initPhonePwd() {
        String phone = SharedUtils.getPhone(this);
        if(phone!=null){
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if(!users.isEmpty()){
                User user = users.get(0);
                et_phone.setText(user.getPhone());
                et_phone.setFocusable(false);
                et_phone.setFocusable(true);
                et_phone.setFocusableInTouchMode(true);
                et_phone.requestFocus();
                et_password.setText(user.getPassword());
                et_password.setFocusable(false);
                et_password.setFocusable(true);
                et_password.setFocusableInTouchMode(true);
                et_password.requestFocus();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                int id = rg_loginType.getCheckedRadioButtonId();
                RadioButton rb_choice = (RadioButton)findViewById(id);
                String loginType = rb_choice.getText().toString();
                String phone = et_phone.getText().toString().trim();
                String password = et_password.getText().toString();
                LoginApp(phone,password,loginType);
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_lossPwd:
                Intent intent1 = new Intent(this, LossPwdActivity.class);
                startActivity(intent1);
                break;
                default: break;
        }
    }

    /**
     *  登录app
     * @param phone  手机号
     * @param password 密码
     * @param loginType 登陆方式 （在线，离线）
     */
    private void LoginApp(String phone, String password, String loginType) {
        Log.e(TAG,"登录方式: "+loginType);
        if("在线".equals(loginType)){
            LoginAppByNet(phone,password);
        }else if("离线".equals(loginType)){
            LoginAppNoNet(phone,password);
        }
    }

    /**
     * 离线登录
     * @param phone
     * @param password
     */
    private void LoginAppNoNet(String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        List<User> users = LitePal.where("phone = ? and password = ?", phone, password).find(User.class);
        if(users!=null&&!users.isEmpty()){
            SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("phone",phone);
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }else{
            showToast("账号密码错误");
        }
    }

    /**
     *  在线登录
     * @param phone
     * @param password
     */
    private void LoginAppByNet(final String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.get()
                .url(HttpUrlUtils.LOGIN_URL)
                .addParams("user_phone",phone)
                .addParams("user_password",password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        uitoast("检查网络是否连接");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,response);
                        Gson gson = new Gson();
                        Type type = new TypeToken<User>(){}.getType();
                        User user = gson.fromJson(response, type);
                        if("1".equals(user.getIsSuccess())){
                            List<User> users = LitePal.where("phone = ?", user.getPhone()).find(User.class);
                            if(users == null || users.isEmpty()){
                                User user_db = new User();
                                user_db.setPhone(user.getPhone());
                                user_db.setPassword(user.getPassword());
                                user_db.setName(user.getName());
                                user_db.save();
                            }
                            SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("phone",user.getPhone());
                            editor.commit();
                            //更新数据库
                            user.updateAll("phone = ?",phone);
                            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            uitoast("账户密码错误");
                        }
                    }
                });
    }





}
