package com.example.chen.intelligentweigh.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.util.CommonAction;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.StatusBarUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetTouXiangActivity extends BaseActivity {

    private ImageView iconIv;
    private static final int REQUEST_LIST_CODE = 0;
    private String TAG =  "SetTouXiangActivity";
    public static final String action = "jason.broadcast.action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_tou_xiang);
        initView();
        StatusBarUtils.setStatusBarFull(this);
        //设置图片宽高
        int width = iconIv.getContext().getResources().getDisplayMetrics().widthPixels;
        int height = width;
        iconIv.setLayoutParams(new LinearLayout.LayoutParams(width , height));
        refreshIcon();
        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        CommonAction.getInstance().addActivity(this);
    }

    private void refreshIcon() {
        String phone = SharedUtils.getPhone(this);
        Log.e(TAG,"手机号码"+phone);
        if(!TextUtils.isEmpty(phone)){
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if(users!=null||!users.isEmpty()){
                Log.e(TAG,"头像"+users.get(0).getTouxiang());
               if(users.get(0).getTouxiang()!=null){
                   Log.e("TAG","有值了");
                   String touxiang = users.get(0).getTouxiang();
                   if(!touxiang.contains("/JDGJ/TOUX/")) {
                       Log.e(TAG,"本地获取main");
                       Glide.with(this).load(touxiang)
                               .error(R.drawable.iconn)
                               .into(iconIv);
                   }else{
                       Log.e(TAG,"网络获取main");
                       Glide.with(this).load(HttpUrlUtils.TOUXIANG_URL+touxiang)
                               .error(R.drawable.iconn)
                               .signature(new StringSignature(SharedUtils.getTime(this)))
                               .into(iconIv);
                   }
               }else{
                   Log.e("TAG","无值了");
                   Glide.with(this).load(R.drawable.iconn)
                           .error(R.drawable.loadtouxiang)
                           .signature(new StringSignature(SharedUtils.getTime(this)))
                           .into(iconIv);
               }
            }
        }
    }

    private void initView() {
        iconIv = (ImageView) findViewById(R.id.iv_myicon);
        new TitleBuilder(this).setTitleText("头像").setRightText("设置").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iconselect(view);
            }
        }).build();
    }

    public void Iconselect(View view) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(false)
                .titleBgColor(Color.parseColor("#0F0F0F"))
                .cropSize(1, 1, 1000, 1000)
                .needCrop(true)
                .build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String path = pathList.get(0);
            Log.i("ImagePathList", path);
            sendIconRequest(SharedUtils.getPhone(this),path);
        }

    }

    private void sendIconRequest(final String id, final String path){
                    OkHttpClient mOkHttpClent = new OkHttpClient();
                    File file = new File(path);
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_phone",id)
                            .addFormDataPart("photo", "icon.jpg",
                                    RequestBody.create(MediaType.parse("image/jpg"), file));

                    RequestBody requestBody = builder.build();

                    Request request = new Request.Builder()
                            .url(HttpUrlUtils.CHANGEINFO_TOUXIANG_URL)
                            .post(requestBody)
                            .build();
                    Call call = mOkHttpClent.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "头像上传失败"+e );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SetTouXiangActivity.this, "头像上传失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            if("ok".equals(response.body().string().toString())){
                                Log.e(TAG,"设置头像成功");
                                //保存头像地址到本地
                                User user = new User();
                                user.setTouxiang(path);
                                user.updateAll("phone = ?",id);

                                //保存更改时间作为签名
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String time = format.format(calendar.getTime());
                                SharedPreferences.Editor editor = getSharedPreferences(SharedUtils.getPhone(SetTouXiangActivity.this),
                                        MODE_PRIVATE).edit();
                                editor.putString("icontime",time);
                                editor.apply();

                                //通知主页面刷新头像
                                Intent intent = new Intent(action);
                                sendBroadcast(intent);
                                finish();
                            }else{
                                uitoast("设置头像失败");
                            }
                        }
                    });

                }


}
