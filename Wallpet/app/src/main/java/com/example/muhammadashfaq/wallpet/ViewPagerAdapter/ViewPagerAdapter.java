package com.example.muhammadashfaq.wallpet.ViewPagerAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.muhammadashfaq.wallpet.TabFragments.LatestFragment;
import com.example.muhammadashfaq.wallpet.TabFragments.TrendingFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case (0):
                LatestFragment latestFragment=new LatestFragment();
                return  latestFragment;
            case (1):
                TrendingFragment trendingFragment=new TrendingFragment();
                return  trendingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case (0):
                return "Latest";
            case (1):
                return "Trending";
                default:
                    return null;
        }
    }
}
