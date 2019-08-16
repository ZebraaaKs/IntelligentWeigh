package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.HouseAreaActivity;
import com.example.chen.intelligentweigh.bean.Area;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.util.AlertDialog;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/6.   19:49
 * description：牧场分区
 */
public class HouseAreaFragment extends BaseFragment {

    private ListView lv_house_area;
    private House house;
    private String TAG = "HouseAreaFragment";
    private boolean isTwoPan;
    private List<Area> list;
    private List<String> listArea = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RelativeLayout rl_nodata_show;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_area_frag, container, false);
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

    /**
     * 提前加载
     *
     * @param house
     * @return
     */
    public static HouseAreaFragment newInstance(House house) {
        HouseAreaFragment fragment = new HouseAreaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("HouseInfo", house);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        lv_house_area = (ListView) view.findViewById(R.id.lv_house_area);
        onFragmentUI(view);
        onActivityUI(view);
        rl_nodata_show = (RelativeLayout) view.findViewById(R.id.rl_nodata_show);

    }

    /**
     * 在双屏模式下，fragment下
     *
     * @param view
     */
    private void onFragmentUI(View view) {
        if (getArguments() != null) {
            final House house = (House) getArguments().getSerializable("HouseInfo");
            initAreaList(house);
            new TitleBuilder(view).setTitleText(house.getName() + "分区").setRightText("添加分区").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();

                        myDialog.setEtGone().setTitle("添加").setEtMsg("4").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String etMsg = myDialog.getEtMsg();
                                if (!etMsg.isEmpty()) {
                                    AddHouseArea(house, etMsg);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            initCrashAreaList(house.getID());
                                        }
                                    }, 1000);
                                }
                            }
                        }).show();

                    }
                }
            }).build();

            //长按删除
            lv_house_area.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                    myDialog.setGone().setTitle("提示").setMsg("删除分区" + listArea.get(position)).setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String s = listArea.get(position);
                            OkHttpUtils.get()
                                    .addParams("farmid", house.getID())
                                    .addParams("area", listArea.get(position))
                                    .addParams("exist", "1")
                                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {

                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if (response.contains("past_id")) {
                                                Toast.makeText(getContext(), "分区有牛，需选择新的分区", Toast.LENGTH_SHORT).show();
                                                lv_house_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                                                        if (position1 == position) {
                                                            Toast.makeText(getContext(), "请选择正确分区", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            String s1 = listArea.get(position1);
                                                            MoveCows(house.getID(), s, s1);
                                                        }
                                                    }
                                                });

                                            } else {
                                                DeleteHouseArea(house.getID(), listArea.get(position));
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        initCrashAreaList(house.getID());
                                                    }
                                                }, 1000);
                                            }

                                        }
                                    });

                        }
                    }).show();
                    return false;
                }

            });

        }


    }

    /**
     * 删除牧场分区
     *
     * @param id 牧场ID
     * @param s  分区名字
     */
    private void DeleteHouseArea(String id, String s) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.DELETE_HOUSE_AREA_URL)
                .addParams("farmid", id)
                .addParams("area", s)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (getActivity() != null) {
                            if ("ok".equals(response.toString())) {
                                Toast.makeText(getActivity(), "删除分区成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "删除分区失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    /**
     * 添加牧场分区
     *
     * @param house
     */
    private void AddHouseArea(House house, String msg) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ADD_HOUSE_AREA_URL)
                .addParams("farmid", house.getID())
                .addParams("area", msg)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if ("ok".equals(response.toString())) {
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    /**
     * 展示分区listview
     *
     * @param house
     */
    private void initAreaList(House house) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .addParams("farmid", house.getID())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if ("error".equals(response.toString())) {
                            Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                        } else {
                            Type type = new TypeToken<List<Area>>() {
                            }.getType();
                            list = new Gson().fromJson(response, type);
                            if (list != null && list.size() > 0) {
                                for (Area area : list) {
                                    listArea.add(area.getArea());
                                }
                                Log.e(TAG, "更新前的list" + listArea.size());
                                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listArea);
                                lv_house_area.setAdapter(adapter);
                            }else{
                                lv_house_area.setVisibility(View.GONE);
                                rl_nodata_show.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }


    /**
     * 刷新展示
     */
    private void initCrashAreaList(String id) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .addParams("farmid", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if ("error".equals(response.toString())) {
                            Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT).show();
                        } else {
                            Type type = new TypeToken<List<Area>>() {
                            }.getType();
                            list = new Gson().fromJson(response, type);
                            Log.e(TAG, "原始数据" + list.toString());
                            listArea.clear();
                            for (Area area : list) {
                                listArea.add(area.getArea());
                            }
                            Log.e(TAG, "更新后的list" + listArea.size() + listArea.toString());
                            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listArea);
                            adapter.notifyDataSetChanged();
                            lv_house_area.setAdapter(adapter);

                        }
                    }
                });
    }

    /**
     * 在Activity中，单屏模式下
     *
     * @param view
     */
    private void onActivityUI(View view) {
        if (house != null) {
            initAreaList(house);
            new TitleBuilder(view).setTitleText(house.getName() + "分区").setLeftText("返回").setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            }).setRightText("添加分区").setRightOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog myDialog = new AlertDialog(getActivity()).etBuilder();
                    myDialog.setEtGone().setTitle("添加").setEtMsg("4").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String etMsg = myDialog.getEtMsg();
                            if (!etMsg.isEmpty()) {
                                AddHouseArea(house, etMsg);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        initCrashAreaList(house.getID());
                                    }
                                }, 1000);
                            }
                        }
                    }).show();

                }
            });

            lv_house_area.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog myDialog = new AlertDialog(getActivity()).builder();
                    myDialog.setGone().setTitle("提示").setMsg("删除分区" + listArea.get(position)).setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String s = listArea.get(position);
                            OkHttpUtils.get()
                                    .addParams("farmid", house.getID())
                                    .addParams("area", listArea.get(position))
                                    .addParams("exist", "1")
                                    .url(HttpUrlUtils.CATTLEBACKBYFRAMAREA)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {

                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if (response.contains("past_id")) {
                                                Toast.makeText(getContext(), "分区有牛，需选择新的分区", Toast.LENGTH_SHORT).show();
                                                lv_house_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                                                        if (position1 == position) {
                                                            Toast.makeText(getContext(), "请选择正确分区", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            String s1 = listArea.get(position1);
                                                            MoveCows(house.getID(), s, s1);
                                                        }
                                                    }
                                                });

                                            } else {
                                                DeleteHouseArea(house.getID(), listArea.get(position));
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        initCrashAreaList(house.getID());
                                                    }
                                                }, 1000);
                                            }
                                        }
                                    });

                        }
                    }).show();

                    return false;
                }
            });
        }

    }

    private void MoveCows(final String id, final String s, String s1) {
        OkHttpUtils.get()
                .addParams("farmid", id)
                .addParams("oldarea", s)
                .addParams("newarea", s1)
                .url(HttpUrlUtils.CATTLESMOVEAREABYOLDAREA)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        if ("ok".equals(response)) {
                            Toast.makeText(getContext(), "重新分配分区成功", Toast.LENGTH_SHORT).show();
                            DeleteHouseArea(id, s);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    initCrashAreaList(id);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(getContext(), "重新分配分区失败", Toast.LENGTH_SHORT).show();

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
        if (context instanceof HouseAreaActivity) {
            house = ((HouseAreaActivity) context).setData();
            Log.e(TAG, "HouseAreaActivity传递来的数据" + house);
        }
    }
}
