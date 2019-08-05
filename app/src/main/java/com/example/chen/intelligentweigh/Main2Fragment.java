package com.example.chen.intelligentweigh;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.chen.intelligentweigh.adapter.MyFragmentPagerAdapter;
import com.example.chen.intelligentweigh.fragment2.AnimalFragment;
import com.example.chen.intelligentweigh.fragment2.DataFragment;
import com.example.chen.intelligentweigh.fragment2.EventFragment;
import com.example.chen.intelligentweigh.fragment2.MeFragment;
import com.example.chen.intelligentweigh.fragment2.WeightFragment;
import com.example.chen.intelligentweigh.viewpagerutil.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2019/8/3.   13:54
 * description：
 */
public class Main2Fragment extends BaseFragment {

    private boolean isTwoPan;
    private RadioButton rb_event;
    private RadioButton rb_weight;
    private RadioButton rb_animal;
    private RadioButton rb_data;
    private RadioButton rb_me;
    private RadioGroup radioGroup;
    private CustomViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main2_menu_frag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        rb_event = (RadioButton) view.findViewById(R.id.rb_event);

        rb_weight = (RadioButton) view.findViewById(R.id.rb_weight);

        rb_animal = (RadioButton) view.findViewById(R.id.rb_animal);

        rb_data = (RadioButton) view.findViewById(R.id.rb_data);

        rb_me = (RadioButton) view.findViewById(R.id.rb_me);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);

        viewPager.setScanScroll(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_event:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_weight:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_animal:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_data:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_me:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        EventFragment eventFragment = new EventFragment();
        WeightFragment weightFragment = new WeightFragment();
        AnimalFragment animalFragment = new AnimalFragment();
        DataFragment dataFragment = new DataFragment();
        MeFragment meFragment = new MeFragment();
        List<Fragment> list = new ArrayList<>();
        list.add(eventFragment);
        list.add(weightFragment);
        list.add(animalFragment);
        list.add(dataFragment);
        list.add(meFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(this.getFragmentManager(),list));
        viewPager.setCurrentItem(0);

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

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
