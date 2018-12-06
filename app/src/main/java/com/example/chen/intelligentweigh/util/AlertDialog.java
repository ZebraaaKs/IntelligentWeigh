package com.example.chen.intelligentweigh.util;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbRequest;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.bean.User;

import org.litepal.LitePal;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/***
 * 弹框提示
 */
public class AlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private EditText et_msg;
    private Button btn_neg;
    private Button btn_pos;
    private RadioGroup rg_choose;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showEtMsg = false;
    private boolean showRg = false;
    private User user = null;


    public AlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    /**
     * 提示消息弹框
     *
     * @return
     */
    public AlertDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alert_dialog, null);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        setGone();
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        return this;
    }

    /**
     * 编辑信息弹框
     *
     * @return
     */
    public AlertDialog etBuilder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alert_etdialog, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        et_msg = (EditText) view.findViewById(R.id.et_msg);

        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        setEtGone();
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return this;
    }


    /**
     *  选择弹框
     * @return
     */
    public AlertDialog rgBuilder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alert_choosedialog, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        rg_choose = (RadioGroup) view.findViewById(R.id.rg_choose);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        setRgGone();
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return this;
    }

    public AlertDialog setRgGone() {
        if (lLayout_bg != null) {
            txt_title.setVisibility(View.GONE);
            rg_choose.setVisibility(View.GONE);
            btn_neg.setVisibility(View.GONE);
            btn_pos.setVisibility(View.GONE);
            img_line.setVisibility(View.GONE);

        }
        showTitle = false;
        showRg = false;
        showPosBtn = false;
        showNegBtn = false;
        return this;
    }

    /**
     * 恢复初始
     *
     * @return
     */
    public AlertDialog setGone() {
        if (lLayout_bg != null) {
            txt_title.setVisibility(View.GONE);
            txt_msg.setVisibility(View.GONE);
            btn_neg.setVisibility(View.GONE);
            btn_pos.setVisibility(View.GONE);
            img_line.setVisibility(View.GONE);

        }
        showTitle = false;
        showMsg = false;
        showPosBtn = false;
        showNegBtn = false;
        return this;
    }

    public AlertDialog setEtGone() {
        if (lLayout_bg != null) {
            txt_title.setVisibility(View.GONE);
            et_msg.setVisibility(View.GONE);
            btn_neg.setVisibility(View.GONE);
            btn_pos.setVisibility(View.GONE);
            img_line.setVisibility(View.GONE);

        }
        showTitle = false;
        showEtMsg = false;
        showPosBtn = false;
        showNegBtn = false;
        return this;
    }

    public String getRgChoose(){
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alert_choosedialog, null);
        int id = rg_choose.getCheckedRadioButtonId();
        RadioButton rb_choice = (RadioButton) view.findViewById(id);
        return rb_choice.getText().toString();
    }

    /**
     * 设置title
     *
     * @param title
     * @return
     */
    public AlertDialog setTitle(String title) {
        showTitle = true;
        if (TextUtils.isEmpty(title)) {
            txt_title.setText("提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    /**
     * 设置输入Message
     * 1 姓名  2 年龄   3牧场  4牧场分区
     * @param type
     * @return
     */
    public AlertDialog setEtMsg(String type) {
        showEtMsg = true;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        if (!"".equals(phone) && phone != null) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null || !users.isEmpty()) {
                user = users.get(0);
            }
        }
        if (user != null) {
            if ("1".equals(type)) {
                if (TextUtils.isEmpty(user.getName())) {
                    et_msg.setText("培肥用户");
                    et_msg.setSelection("培肥用户".length());//将光标移至文字末尾
                } else {
                    et_msg.setText(user.getName());
                    et_msg.setSelection(user.getName().length());
                }
            } else if ("2".equals(type)) {
                et_msg.setInputType( InputType.TYPE_CLASS_NUMBER);
                if (TextUtils.isEmpty(user.getAge())) {
                    et_msg.setText("0");
                    et_msg.setSelection("0".length());
                } else {
                    et_msg.setText(user.getAge());
                    et_msg.setSelection(user.getAge().length());
                }
            }else if("3".equals(type)){
                et_msg.setHint("牧场名字");
            }else if("4".equals(type)){
                et_msg.setHint("分区名字");
            }

        }
        return this;
    }

    /**
     *  choose选择框
     * @param type 3 性别
     * @return
     */
    public AlertDialog setChooseMsg(String type) {
        showRg = true;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        if (!"".equals(phone) && phone != null) {
            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
            if (users != null || !users.isEmpty()) {
                user = users.get(0);
            }
        }
        if (user != null) {
            if ("3".equals(type)) {
                if (!TextUtils.isEmpty(user.getSex())) {
                    if("男".equals(user.getSex())){
                        rg_choose.check(R.id.rb_man);
                    }else{
                        rg_choose.check(R.id.rb_woman);
                    }

                }
            }

        }
        return this;
    }

    /**
     * 设置Message
     *
     * @param msg
     * @return
     */
    public AlertDialog setMsg(String msg) {
        showMsg = true;
        if (TextUtils.isEmpty(msg)) {
            txt_msg.setText("");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    /**
     * 设置点击外部是否消失
     *
     * @param cancel
     * @return
     */
    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 右侧按钮
     *
     * @param text
     * @param listener
     * @return
     */
    public AlertDialog setPositiveButton(String text,
                                         final OnClickListener listener) {
        return setPositiveButton(text, -1, listener);
    }

    public AlertDialog setPositiveButton(String text, int color,
                                         final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("");
        } else {
            btn_pos.setText(text);
        }
        if (color == -1) {
            color = R.color.action_sheet_blue;
        }
        btn_pos.setTextColor(ContextCompat.getColor(context, color));
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


    public String getEtMsg(){
        return et_msg.getText().toString();
    }

    /**
     * 左侧按钮
     *
     * @param text
     * @param listener
     * @return
     */

    public AlertDialog setNegativeButton(String text,
                                         final OnClickListener listener) {

        return setNegativeButton(text, -1, listener);
    }

    public AlertDialog setNegativeButton(String text, int color,
                                         final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("");
        } else {
            btn_neg.setText(text);
        }
        if (color == -1) {
            color = R.color.action_sheet_blue;
        }
        btn_neg.setTextColor(ContextCompat.getColor(context, color));

        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置显示
     */
    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if(showEtMsg){
            et_msg.setVisibility(View.VISIBLE);
        }
        if(showRg){
            rg_choose.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_dialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_dialog_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public boolean isShowing() {
        if (dialog != null) {
            if (dialog.isShowing())
                return true;
            else
                return false;
        }
        return false;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }
}
