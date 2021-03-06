package com.example.chen.intelligentweigh.util;

/**
 * author : chen
 * date   : 2018/11/27  12:17
 * desc   :
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.intelligentweigh.R;
import com.google.gson.Gson;

/**
 * 标题栏构造器,使用方法 new TitleBuilder().setMethod().setMethod()......
 * <p/>
 * 统一格式为标题文字,左右各自是文字/图片按钮
 * 按钮都默认不显示,只有在你调用setLeftText时才会显示左侧按钮文字,图片同理
 * 图片或文字的点击事件都用Left/RightOnClickListener
 */
public class TitleBuilder {

    private View viewTitle;
    private TextView tvTitle;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivCall;
    private SearchView searchView;

    /**
     * Activity中使用这个构造方法
     */
    public TitleBuilder(Activity context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
        ivCall = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_call);
        searchView = (SearchView) viewTitle.findViewById(R.id.sv_search);

    }

    /**
     * Fragment中使用这个构造方法
     */
    public TitleBuilder(View context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
        ivCall = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_call);
        searchView = (SearchView) viewTitle.findViewById(R.id.sv_search);
    }

    // title

    public TitleBuilder setTitleBgRes(int resid) {
        viewTitle.setBackgroundResource(resid);
        return this;
    }

    public TitleBuilder setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    public TitleBuilder setSv(Context context,String text,boolean showIcon){
        searchView.setVisibility(TextUtils.isEmpty(text)? View.GONE:View.VISIBLE);
        searchView.setQueryHint(text);
        searchView.setIconifiedByDefault(showIcon);
        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
        searchView.setBackground(context.getResources().getDrawable(R.drawable.searview_shape));
        return this;
    }


    // left
    public TitleBuilder setLeftImage(int resId) {
        ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if (resId > 0)
            ivLeft.setImageResource(resId);
        return this;
    }

    public TitleBuilder setLeftText(String text) {
        tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvLeft.setText(text);
        return this;
    }

    public TitleBuilder setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    // right

    public TitleBuilder setRightImage(int resId) {
        ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if (resId > 0)
            ivRight.setImageResource(resId);
        return this;
    }

    public TitleBuilder setCallImage(int resId) {
        ivCall.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if (resId > 0)
            ivCall.setImageResource(resId);
        return this;
    }

    public TitleBuilder setRightText(String text) {
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    public TitleBuilder setRightTextColor(Context context, int resId) {
        tvRight.setTextColor(context.getResources().getColor(resId));
        return this;
    }

    public TitleBuilder setTitleTextColor(Context context, int resId) {
        tvTitle.setTextColor(context.getResources().getColor(resId));
        return this;
    }


    public TitleBuilder setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }

    public TitleBuilder setCallOnClickListener(View.OnClickListener listener) {
        ivCall.setOnClickListener(listener);
        return this;
    }


    public View build() {
        return viewTitle;
    }

}
