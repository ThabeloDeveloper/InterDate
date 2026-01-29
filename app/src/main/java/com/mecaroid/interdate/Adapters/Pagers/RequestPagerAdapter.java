package com.mecaroid.interdate.Adapters.Pagers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mecaroid.interdate.Fragments.Requests.Received;
import com.mecaroid.interdate.Fragments.Requests.Sent;

public class RequestPagerAdapter extends FragmentPagerAdapter {


    public RequestPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Received();
            case 1: return new Sent();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Received";
            case 1: return "Sent";
        }
        return null;
    }
}
