package com.example.chen.intelligentweigh.fragment2.kidFragment.vpFragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.adapter.CowWightLogAdapter;
import com.example.chen.intelligentweigh.bean.WeightLogData;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chen
 * @date 2019/8/9.   12:56
 * description：
 */
public class CzLogFragment extends BaseFragment {

    private View rootView;
    private LinearLayout rl_titlebar;
    private RecyclerView lv_czlog;
    private SmartRefreshLayout srl_czlog;
    private boolean isTwoPan;
    private List<WeightLogData> list;
    private CowWightLogAdapter adapter;

    private final static String TAG = "CzLogFragment";
    private RelativeLayout rl_nodata_show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.czlog_frag, container, false);

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTwoPan = true;
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTwoPan = false;
        }
        initDiffentView(rootView);
    }

    private void initView(View rootView) {
        rl_titlebar = (LinearLayout) rootView.findViewById(R.id.rl_titlebar);
        lv_czlog = (RecyclerView) rootView.findViewById(R.id.lv_czlog);
        if (getActivity() != null) {
            lv_czlog.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        srl_czlog = (SmartRefreshLayout) rootView.findViewById(R.id.srl_czlog);


        rl_nodata_show = (RelativeLayout) rootView.findViewById(R.id.rl_nodata_show);

    }

    private void initDiffentView(View rootView) {
        if (isTwoPan) {
            new TitleBuilder(rootView).setTitleText("称重记录").build();
        } else {
            rl_titlebar.setVisibility(View.GONE);
        }
        if (getActivity() != null) {
            srl_czlog.setRefreshHeader(new ClassicsHeader(getActivity()));
            srl_czlog.setRefreshFooter(new ClassicsFooter(getActivity()));
            final String farmId = SharedUtils.getUserFarmId(getActivity());
            if (!TextUtils.isEmpty(farmId)) {
                getWightLogData(farmId);
            }else{
                srl_czlog.setVisibility(View.GONE);
                lv_czlog.setVisibility(View.GONE);
                rl_nodata_show.setVisibility(View.VISIBLE);
            }

            srl_czlog.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if (!TextUtils.isEmpty(farmId)) {
                        getMoreWightLogData(farmId, "1");
                    }
                    srl_czlog.finishRefresh(2000);
                }
            });

            srl_czlog.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    if (list.size() > 0 && list != null) {
                        Log.e(TAG, "长度" + list.size());
                        getMoreWightLogData(farmId, String.valueOf((list.size() + 1)));
                    }
                    srl_czlog.finishLoadMore(2000);
                }
            });

        }

    }

    private void getWightLogData(String farmId) {
        OkHttpUtils.get()
                .addParams("farmid", farmId)
                .addParams("index", "1")
                .url(HttpUrlUtils.CATTLEBACKDAYWEIGHRECORD)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Log.e(TAG, response);
                            Type type = new TypeToken<List<WeightLogData>>() {
                            }.getType();
                            list = new Gson().fromJson(response, type);
                            if(list!=null&&list.size()>0) {
                                adapter = new CowWightLogAdapter(list);
                                lv_czlog.setAdapter(adapter);
                            }else{
                                srl_czlog.setVisibility(View.GONE);
                                lv_czlog.setVisibility(View.GONE);
                                rl_nodata_show.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
    }

    private void getMoreWightLogData(String farmId, final String index) {
        OkHttpUtils.get()
                .addParams("farmid", farmId)
                .addParams("index", index)
                .url(HttpUrlUtils.CATTLEBACKDAYWEIGHRECORD)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Type type = new TypeToken<List<WeightLogData>>() {
                            }.getType();
                            List<WeightLogData> mList = new Gson().fromJson(response, type);
                            if ("1".equals(index)) {
                                adapter.refresh(mList);
                            } else {
                                adapter.add(mList);
                            }
                        }
                    }
                });

    }
}
