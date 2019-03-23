package com.example.muhammadashfaq.snippet.ViewPagerAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.muhammadashfaq.snippet.TabFragments.ChatFragment;
import com.example.muhammadashfaq.snippet.TabFragments.FriendFragment;
import com.example.muhammadashfaq.snippet.TabFragments.CallsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case (0):
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            case (1):
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case (2):
                CallsFragment callsFragment = new CallsFragment();
                return callsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case (0):
                return "Friends";
            case (1):
                return "Chats";
            case (2):
                return "Calls";
                default:
                    return null;
        }
    }
}
