package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.CowManageNListActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ShowCowInfoActivity;
import com.example.chen.intelligentweigh.adapter.ListViewCowInfoAdapter;
import com.example.chen.intelligentweigh.bean.ChenZhong;
import com.example.chen.intelligentweigh.bean.Cow;
import com.example.chen.intelligentweigh.bean.RiZeng;
import com.example.chen.intelligentweigh.bean.WeighData;
import com.example.chen.intelligentweigh.fragment2.BlankFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/18.   20:11
 * description：
 */
public class CowManageNListFragment extends BaseFragment {

    private RecyclerView lv_n_list;
    private Boolean isTwoPan;
    private String Nidd;
    private String Tname;
    private List<Cow> list;
    private ListViewCowInfoAdapter adapter;
    private String TAG = "CowManageNListFragment";
    String type1 = "";
    private LinearLayout rl_titlebar;
    private SmartRefreshLayout srl_nlist;
    private RelativeLayout rl_nodata_show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_manage_n_list_frag, container, false);
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

    public static CowManageNListFragment newInstances(String yid, String tName) {
        CowManageNListFragment fragment = new CowManageNListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nid", yid);
        bundle.putString("type", tName);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        lv_n_list = (RecyclerView) view.findViewById(R.id.lv_n_list);
        srl_nlist = (SmartRefreshLayout) view.findViewById(R.id.srl_nlist);
        if (getActivity() != null) {
            srl_nlist.setRefreshHeader(new ClassicsHeader(getActivity()));
            srl_nlist.setRefreshFooter(new ClassicsFooter(getActivity()));
        }
        rl_titlebar = (LinearLayout) view.findViewById(R.id.rl_titlebar);
        initFragView(view);
        initActivityView(view);


        rl_nodata_show = (RelativeLayout) view.findViewById(R.id.rl_nodata_show);

    }

    private void initActivityView(View view) {
        if (Nidd != null && Tname != null) {
            new TitleBuilder(view).setTitleText(Tname).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            initListView(Nidd, Tname);

            try {
                srl_nlist.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        loadMoreDataListView(Nidd, Tname, "0");
                        srl_nlist.finishRefresh(2000);
                    }
                });

                srl_nlist.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        Log.e(TAG, "长度" + (list.size()));
                        if (list.size() > 0 && list != null) {
                            loadMoreDataListView(Nidd, Tname, String.valueOf((list.size())));
                        }
                        srl_nlist.finishLoadMore(2000);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void initFragView(View view) {
        if (getArguments() != null && getActivity() != null) {
            final String nid = getArguments().getString("nid");
            final String tName = getArguments().getString("type");
            new TitleBuilder(view).setTitleText(tName).setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlankFragment framKindFragment = new BlankFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag, framKindFragment).commit();
                }
            });
            initListView(nid, tName);
            try {
                srl_nlist.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        loadMoreDataListView(nid, tName, "0");
                        srl_nlist.finishRefresh(2000);
                    }
                });

                srl_nlist.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        Log.e(TAG, "长度" + (list.size()));
                        if (list.size() > 0 && list != null) {
                            loadMoreDataListView(nid, tName, String.valueOf((list.size() + 1)));
                        }
                        srl_nlist.finishLoadMore(2000);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void initListView(final String nnnid, final String name) {

        if ("出栏牛".equals(name)) {
            type1 = "2";
        } else {
            type1 = "0";
        }
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                .addParams("farmid", nnnid)
                .addParams("exist", type1)
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
                            Log.e(TAG, "数据 " + response);
                            Type type = new TypeToken<List<Cow>>() {
                            }.getType();
                            Gson gson = new Gson();
                            list = (List<Cow>) gson.fromJson(response, type);
                            if(list!=null&&list.size()>0) {
                                adapter = new ListViewCowInfoAdapter(list, getActivity());
                                lv_n_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                                lv_n_list.setAdapter(adapter);

                                adapter.setOnItemClickListener(new ListViewCowInfoAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, Cow cow, int position) {
                                        initListData(list.get(position).getID(), list.get(position), nnnid, name);

                                    }
                                });
                            }else{
                                srl_nlist.setVisibility(View.GONE);
                                lv_n_list.setVisibility(View.GONE);
                                rl_nodata_show.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
    }


    private void loadMoreDataListView(String farmid, String name, final String index) {
        if ("出栏牛".equals(name)) {
            type1 = "2";
        } else {
            type1 = "0";
        }
        OkHttpUtils.get()
                .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                .addParams("farmid", farmid)
                .addParams("exist", type1)
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
                            Type type = new TypeToken<List<Cow>>() {
                            }.getType();
                            List<Cow> mlist = new Gson().fromJson(response, type);
                            if ("0".equals(index)) {
                                adapter.refresh(mlist);
                            } else {
                                adapter.add(mlist);
                            }
                        }

                    }
                });


    }

    private void initListData(final String id, final Cow cow, final String nnnid, final String name) {
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
                                ShowCowInfoFragment fragment = ShowCowInfoFragment.newInstances(cow, nnnid, xValues, yValues, "0", name, xValues2, yValues2);
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
        if (context instanceof CowManageNListActivity) {
            Nidd = ((CowManageNListActivity) context).setIdData();
            Tname = ((CowManageNListActivity) context).setNameData();

        }
    }
}
