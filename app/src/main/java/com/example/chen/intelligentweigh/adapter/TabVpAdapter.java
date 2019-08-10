package com.example.chen.intelligentweigh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author chen
 * @date 2019/8/9.   11:36
 * description：
 */
public class TabVpAdapter  extends FragmentPagerAdapter {

    private List<Fragment> list;

    private String mtitle[] ={"称重记录","存栏牛","出栏牛","淘汰牛"};

    public TabVpAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mtitle[position];
    }

}
