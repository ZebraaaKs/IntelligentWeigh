package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageYListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.EditCowInfoActivty;
import com.example.chen.intelligentweigh.activity.kidActivity.ShowCowInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewCowInfoAdapter;
import com.example.chen.intelligentweigh.bean.ChenZhong;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.RiZeng;
import com.example.chen.intelligentweigh.bean.WeighData;
import com.example.chen.intelligentweigh.fragment2.BlankFragment;
import com.example.chen.intelligentweigh.util.AlertDialog;
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
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/16.   16:45
 * description：健在牛list列表展示
 */
public class CowManageYListFragment extends BaseFragment {

    private SwipeRecyclerView lv_y_list;
    private String TAG = "CowManageYListFragment";
    private List<Cow> list;
    private ListViewCowInfoAdapter adapter;
    private String Yidd;
    private String Yname;
    private Boolean isTwoPan;
    private LinearLayout rl_titlebar;
    private SmartRefreshLayout srl_ylist;
    private LocalBroadcastManager broadcastManager;
    private RelativeLayout rl_nodata_show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_y_list_frag, container, false);
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
        if (!isTwoPan) {
            rl_titlebar.setVisibility(View.GONE);
        }

    }

    public static CowManageYListFragment newInstances(String yid, String yname, String tName) {
        CowManageYListFragment fragment = new CowManageYListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("yid", yid);
        bundle.putString("yname", yname);
        bundle.putString("tName", tName);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        receiveAdDownload();

        lv_y_list = (SwipeRecyclerView) view.findViewById(R.id.lv_y_list);
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem addItem = new SwipeMenuItem(getActivity());
                addItem.setBackground(new ColorDrawable(Color.rgb(255,
                        170, 37)));
                addItem.setImage(R.drawable.ic_edit); // 图标。
                addItem.setWidth(dp2px(90)); // 宽度。

                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setImage(R.drawable.ic_delete); // 图标。
                deleteItem.setWidth(dp2px(90)); // 宽度。


                swipeRightMenu.addMenuItem(deleteItem);// 添
            }


        };
        lv_y_list.setSwipeMenuCreator(swipeMenuCreator);

        initFragView(view);
        initActivityView(view);
        rl_titlebar = (LinearLayout) view.findViewById(R.id.rl_titlebar);
        srl_ylist = (SmartRefreshLayout) view.findViewById(R.id.srl_ylist);
        if (getActivity() != null) {
            srl_ylist.setRefreshHeader(new ClassicsHeader(getActivity()));
            srl_ylist.setRefreshFooter(new ClassicsFooter(getActivity()));
        }

        srl_ylist.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadMoreList(SharedUtils.getUserFarmId(getActivity()), "0");
                srl_ylist.finishRefresh(2000);
            }
        });

        srl_ylist.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreList(SharedUtils.getUserFarmId(getActivity()), String.valueOf((list.size() + 1)));
                srl_ylist.finishLoadMore(2000);
            }
        });
        rl_nodata_show = (RelativeLayout) view.findViewById(R.id.rl_nodata_show);

    }

    private void initActivityView(View view) {
        if (Yidd != null && Yname != null) {
            new TitleBuilder(view).setTitleText("存栏牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            Log.e(TAG, "activity " + Yidd + Yname);
            initYListView(Yidd, Yname, "");
        }


    }

    private void initFragView(View view) {
        if (getArguments() != null) {
            final String yid = getArguments().getString("yid");
            final String yname = getArguments().getString("yname");
            final String tName = getArguments().getString("tName");
            new TitleBuilder(view).setTitleText("存栏牛").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlankFragment fragment = new BlankFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                }
            }).build();

            initYListView(yid, yname, tName);

        }

    }

    private void initYListView(final String yid, final String yname, final String tname) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                    .addParams("farmid", yid)
                    .addParams("exist", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                            } else {
                                Type type = new TypeToken<List<Cow>>() {
                                }.getType();
                                Gson gson = new Gson();
                                list = (List<Cow>) gson.fromJson(response, type);
                                if (list != null && list.size() > 0) {
                                    adapter = new ListViewCowInfoAdapter(list, getActivity());
                                    lv_y_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
                                        @Override
                                        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                                            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                                            menuBridge.closeMenu();

                                            // 左侧还是右侧菜单：
                                            int direction = menuBridge.getDirection();
                                            // 菜单在Item中的Position：
                                            int menuPosition = menuBridge.getPosition();
                                            switch (menuPosition) {
                                                case 0:
                                                    if (isTwoPan) {
                                                        EditCowInfoFragment fragment = EditCowInfoFragment.newInsatnces(list.get(position), yid, null);
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                                                    } else {
                                                        Intent intent = new Intent(getActivity(), EditCowInfoActivty.class);
                                                        intent.putExtra("eeidd", yid);
                                                        intent.putExtra("eename", yname);
                                                        intent.putExtra("eetname", tname);
                                                        intent.putExtra("eeCow", list.get(position));
                                                        Log.e(TAG, "eeidd" + yid + " eename" + yname + " eetname" + tname + " cow" + list.get(position));
                                                        startActivity(intent);
                                                    }
                                                    break;
                                                case 1:
                                                    Showdelete(list.get(position).getID(), yid, yname);
                                                    break;
                                            }

                                        }
                                    };
                                    try {
                                        lv_y_list.setOnItemMenuClickListener(mItemMenuClickListener);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    lv_y_list.setAdapter(adapter);
                                    adapter.setOnItemClickListener(new ListViewCowInfoAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, Cow cow, int position) {
                                            initListData(list.get(position).getID(), list.get(position), yid);
                                        }
                                    });

                                } else {
                                    srl_ylist.setVisibility(View.GONE);
                                    lv_y_list.setVisibility(View.GONE);
                                    rl_nodata_show.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                    });
        }
    }

    /**
     * 展示删除弹框
     *
     * @param id
     */
    private void Showdelete(final String id, final String yidd, final String yname) {
        AlertDialog myDialog = new AlertDialog(getActivity()).builder();
        myDialog.setGone().setTitle("提示").setMsg("确认删除").setNegativeButton("取消", null).setPositiveButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCow(id);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initCrashAreaList(yidd, yname);
                    }
                }, 1000);
            }
        }).show();

    }

    /**
     * 刷新界面
     *
     * @param yidd
     * @param yname
     */
    private void initCrashAreaList(String yidd, String yname) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                    .addParams("farmid", yidd)
                    .addParams("exist", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                            } else {
                                Type type = new TypeToken<List<Cow>>() {
                                }.getType();
                                Gson gson = new Gson();
                                if (!list.isEmpty()) {
                                    list.clear();
                                }
                                list = (List<Cow>) gson.fromJson(response, type);
                                adapter = new ListViewCowInfoAdapter(list, getActivity());
                                adapter.notifyDataSetChanged();
                                lv_y_list.setAdapter(adapter);
                            }
                        }
                    });
        }


    }

    private void loadMoreList(String farmid, final String index) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                    .addParams("farmid", farmid)
                    .addParams("exist", "1")
                    .addParams("index", index)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e(TAG, e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            if (!TextUtils.isEmpty(response)) {
                                if ("error".equals(response)) {
                                    Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                                } else {
                                    Type type = new TypeToken<List<Cow>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    List<Cow> mlist = gson.fromJson(response, type);
                                    if ("0".equals(index)) {
                                        adapter.refresh(mlist);
                                    } else {
                                        adapter.add(mlist);
                                    }
                                }

                            }
                        }
                    });
        }

    }

    /**
     * 删除
     *
     * @param id
     */
    private void DeleteCow(String id) {
        if (getActivity() != null) {
            OkHttpUtils.get()
                    .url(HttpUrlUtils.DELETECOW)
                    .addParams("cattleid", id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            if ("error".equals(response)) {
                                Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    /**
     * 加载char数据
     *
     * @param id
     */
    private void initListData(String id, final Cow cow, final String yid) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKWEIGHRECORD)
                .addParams("cattleid", id)
                .addParams("pastid", String.valueOf(cow.getPast_id()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if ("error".equals(response)) {
                            Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                        } else {
                            Type type = new TypeToken<WeighData>() {
                            }.getType();
                            Gson gson = new Gson();
                            WeighData weighData = (WeighData) gson.fromJson(response, type);
                            Type cztype = new TypeToken<List<ChenZhong>>() {
                            }.getType();
                            List<ChenZhong> list = (List<ChenZhong>) gson.fromJson(weighData.getCz(), cztype);
                            Type rztype = new TypeToken<List<RiZeng>>() {
                            }.getType();
                            List<RiZeng> list2 = (List<RiZeng>) gson.fromJson(weighData.getZw(), rztype);


                            ArrayList<String> xValues = null;
                            ArrayList<Float> yValues = null;
                            ArrayList<String> xValues2 = null;
                            ArrayList<Float> yValues2 = null;
                            if (!list.isEmpty()) {
                                xValues = new ArrayList<>();   //x轴数据集合
                                for (ChenZhong data : list) {
                                    xValues.add(data.getWtime().substring(5,6).equals("0")?data.getWtime().substring(6):data.getWtime().substring(5));
                                }
                                yValues = new ArrayList<>();  //y轴数据集合
                                for (ChenZhong data : list) {
                                    yValues.add((float) data.getWeight());
                                }
                            } else {
                                xValues = new ArrayList<>();   //x轴数据集合
                                yValues = new ArrayList<>();  //y轴数据集合
                                for (int i = 1; i <= 5; i++) {

                                    xValues.add("无数据");
                                }
                                for (int i = 1; i <= 5; i++) {
                                    yValues.add(0f);
                                }

                            }

                            if (!list2.isEmpty()) {
                                xValues2 = new ArrayList<>();   //x轴数据集合
                                for (RiZeng data : list2) {
                                    xValues2.add(data.getWtime().substring(5,6).equals("0")?data.getWtime().substring(6):data.getWtime().substring(5));
                                }
                                yValues2 = new ArrayList<>();  //y轴数据集合
                                for (RiZeng data : list2) {
                                    yValues2.add((float) data.getDzengzhong());
                                }
                            } else {
                                xValues2 = new ArrayList<>();   //x轴数据集合
                                yValues2 = new ArrayList<>();  //y轴数据集合
                                for (int i = 1; i <= 5; i++) {

                                    xValues2.add("无数据");
                                }
                                for (int i = 1; i <= 5; i++) {
                                    yValues2.add(0f);
                                }

                            }
                            if (isTwoPan) {
                                ShowCowInfoFragment fragment = ShowCowInfoFragment.newInstances(cow, yid, xValues, yValues, "1", "", xValues2, yValues2);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                            } else {
                                Intent intent = new Intent(getActivity(), ShowCowInfoActivity.class);
                                intent.putExtra("cowsInfo", cow);
                                intent.putExtra("xData", xValues);
                                intent.putExtra("yData", yValues);
                                intent.putExtra("xData2", xValues2);
                                intent.putExtra("yData2", yValues2);
                                startActivity(intent);

                            }
                        }
                    }
                });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

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
        if (context instanceof CowManageYListActivity) {
            Yidd = ((CowManageYListActivity) context).setIdData();
            Yname = ((CowManageYListActivity) context).setNameData();

        }
    }

    /**
     * 注册广播接收器
     */
    private void receiveAdDownload() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EditCowInfoFragment.NOTICE_REFRESH_ACTION);
        broadcastManager.registerReceiver(mrefrshReceiver, intentFilter);
    }

    BroadcastReceiver mrefrshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userFarmId = SharedUtils.getUserFarmId(context);
            if (!TextUtils.isEmpty(userFarmId)) {
                loadMoreList(userFarmId, "0");
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mrefrshReceiver);
    }
}
