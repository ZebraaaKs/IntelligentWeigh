package com.example.chen.intelligentweigh.fragment2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.adapter.TabVpAdapter;
import com.example.chen.intelligentweigh.fragment.kidFragment.CowManageNListFragment;
import com.example.chen.intelligentweigh.fragment.kidFragment.CowManageYListFragment;
import com.example.chen.intelligentweigh.fragment2.kidFragment.vpFragment.CzLogFragment;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.example.chen.intelligentweigh.viewpagerutil.BaseFragment;
import com.example.chen.intelligentweigh.viewpagerutil.NoSrcollViewPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2019/8/3.   14:30
 * description：
 */
public class AnimalFragment extends BaseFragment {

    private View rootView;
    private boolean isTwoPan;
    private TabLayout tb_tab;
    private NoSrcollViewPage vp_content;
    private LinearLayout ll_showfrag;
    private ImageView iv_cowweightlog;
    private ImageView iv_cowinfo;
    private RelativeLayout ll_showactivity;
    private LinearLayout ll_weight;
    private LinearLayout ll_cunlan;
    private LinearLayout ll_chulan;
    private LinearLayout ll_taotai;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.animal_frag, container, false);

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
        ll_showfrag = (LinearLayout) rootView.findViewById(R.id.ll_showfrag);
        ll_showactivity = (RelativeLayout) rootView.findViewById(R.id.ll_showactivity);
        new TitleBuilder(rootView).setTitleText("动物档案").build();


    }

    private void initDiffentView(View rootView) {
        if (isTwoPan) {
            ll_showfrag.setVisibility(View.GONE);
            ll_showactivity.setVisibility(View.VISIBLE);

            ll_weight = (LinearLayout) rootView.findViewById(R.id.ll_weight);

            ll_cunlan = (LinearLayout) rootView.findViewById(R.id.ll_cunlan);

            ll_chulan = (LinearLayout) rootView.findViewById(R.id.ll_chulan);

            ll_taotai = (LinearLayout) rootView.findViewById(R.id.ll_taotai);
            ll_weight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CzLogFragment fragment = new CzLogFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                }
            });
            ll_cunlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CowManageYListFragment cowManageYListFragment = CowManageYListFragment.newInstances(SharedUtils.getUserFarmId(getActivity()), "", "");
                        getFragmentManager().beginTransaction().replace(R.id.other_content_frag, cowManageYListFragment).commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            ll_chulan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CowManageNListFragment chulfragment = CowManageNListFragment.newInstances(SharedUtils.getUserFarmId(getActivity()), "出栏牛");
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, chulfragment).commit();

                }
            });
            ll_taotai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CowManageNListFragment taotfragment = CowManageNListFragment.newInstances(SharedUtils.getUserFarmId(getActivity()), "淘汰牛");
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, taotfragment).commit();
                }
            });
        } else {
            ll_showactivity.setVisibility(View.GONE);
            ll_showfrag.setVisibility(View.VISIBLE);
            tb_tab = (TabLayout) rootView.findViewById(R.id.tb_tab);
            vp_content = (NoSrcollViewPage) rootView.findViewById(R.id.vp_content);
            CzLogFragment czLogFragment = new CzLogFragment();
            if (getActivity() != null) {
                String userFarmId = SharedUtils.getUserFarmId(getActivity());
                String userFarms = SharedUtils.getUserFarms(getActivity());
                CowManageYListFragment cowManageYListFragment = null;
                if (!TextUtils.isEmpty(userFarmId)) {
                    CowManageNListFragment chulfragment = CowManageNListFragment.newInstances(userFarmId, "出栏牛");
                    CowManageNListFragment taotfragment = CowManageNListFragment.newInstances(userFarmId, "淘汰牛");
                    cowManageYListFragment = CowManageYListFragment.newInstances(userFarmId, "", "");
                    List<Fragment> list = new ArrayList<>();
                    list.add(czLogFragment);
                    list.add(cowManageYListFragment);
                    list.add(chulfragment);
                    list.add(taotfragment);
                    TabVpAdapter adapter = new TabVpAdapter(getChildFragmentManager(), list);
                    vp_content.setAdapter(adapter);
                    tb_tab.setupWithViewPager(vp_content);
                }
            }


        }
    }
}
