package com.example.chen.intelligentweigh;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.chen.intelligentweigh.activity.CloudManageActivity;
import com.example.chen.intelligentweigh.activity.CowManageActivity;
import com.example.chen.intelligentweigh.activity.MeInfoActivity;
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.RealTimeWeightActivity;
import com.example.chen.intelligentweigh.adapter.GridViewAdapter;
import com.example.chen.intelligentweigh.bean.Items;
import com.example.chen.intelligentweigh.fragment.CloudManageFragment;
import com.example.chen.intelligentweigh.fragment.CowManageFragment;
import com.example.chen.intelligentweigh.fragment.MeInfoFragment;
import com.example.chen.intelligentweigh.fragment.NewCowFragment;
import com.example.chen.intelligentweigh.fragment.RealTimeWeightFragment;
import com.example.chen.intelligentweigh.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : chen
 * date   : 2018/11/27  15:30
 * desc   :
 */
public class MainFragment extends BaseFragment {

    private boolean isTwoPan;
    private GridView gv_menu;
    private CircleImageView iv_touxiang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_frag, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.other_content_frag)!=null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTwoPan = true;
        }else{
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTwoPan = false;
        }

    }

    private void initView(View view) {
        gv_menu = (GridView) view.findViewById(R.id.gv_menu);
        GridViewAdapter adapter = new GridViewAdapter(getActivity(), R.layout.item, initGvData());
        gv_menu.setAdapter(adapter);
        iv_touxiang = (CircleImageView) view.findViewById(R.id.iv_touxiang);
        //进行item的事件点击监听
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //要是双屏 （平板Pad端） 就是进行fragment的替换；要是单屏（手机端phone） 进行activity的跳转
                        if(isTwoPan){
                            NewCowFragment fragment = new NewCowFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        }else{

                            Intent intent = new Intent(getActivity(), NewCowActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if(isTwoPan){
                            CowManageFragment fragment = new CowManageFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        }else{
                            Intent intent = new Intent(getActivity(), CowManageActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(isTwoPan){
                            RealTimeWeightFragment fragment = new RealTimeWeightFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        }else{
                            Intent intent = new Intent(getActivity(), RealTimeWeightActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if(isTwoPan){
                            CloudManageFragment fragment = new CloudManageFragment();
                            getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                        }else{
                            Intent intent = new Intent(getActivity(), CloudManageActivity.class);
                            startActivity(intent);
                        }
                        break;
                        default:break;
                }
            }
        });
        iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTwoPan){
                    MeInfoFragment fragment = new MeInfoFragment();
                    getFragmentManager().beginTransaction().replace(R.id.other_content_frag, fragment).commit();
                }else{
                    Intent intent = new Intent(getActivity(), MeInfoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化界面GridView  的数据
     * @return
     */
    private List<Items> initGvData() {
        List<Items> list = new ArrayList<>();
        Items item_1 = new Items();
        item_1.setImageId(R.drawable.cow);
        item_1.setText("新牛录入");
        list.add(item_1);
        Items item_2 = new Items();
        item_2.setImageId(R.drawable.manage);
        item_2.setText("肉牛管理");
        list.add(item_2);
        Items item_3 = new Items();
        item_3.setImageId(R.drawable.chengzhong);
        item_3.setText("实时称重");
        list.add(item_3);
        Items item_4 = new Items();
        item_4.setImageId(R.drawable.tongji);
        item_4.setText("牧场云统计");
        list.add(item_4);
        return list;
    }
}
