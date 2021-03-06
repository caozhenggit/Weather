package com.caozheng.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.caozheng.weather.module.fragment.WeatherFragment;

import java.util.List;

/**
 * @author caozheng
 * Created time on 2017/10/11.
 * Description:
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;
    private List<WeatherFragment> listFragment;

    public MyFragmentPagerAdapter(FragmentManager mFragmentManager, List<WeatherFragment> list) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
        this.listFragment = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return listFragment.get(arg0);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
