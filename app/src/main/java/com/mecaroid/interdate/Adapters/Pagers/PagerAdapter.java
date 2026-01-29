package com.mecaroid.interdate.Adapters.Pagers;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mecaroid.interdate.Fragments.Calls;
import com.mecaroid.interdate.Fragments.Chats;
import com.mecaroid.interdate.Fragments.Global;
import com.mecaroid.interdate.Fragments.Mingle;
import com.mecaroid.interdate.R;

public class PagerAdapter  extends FragmentPagerAdapter {



    public PagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Global();
            case 1: return new Chats();
            case 2: return new Mingle();
            case 3: return new Calls();


        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return null;
            case 1: return "Chats";
            case 2: return "Mingle";
            case 3: return "Calls";
        }


        return null;
    }

}
