package com.mecaroid.interdate.Adapters.Pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mecaroid.interdate.InformationPages.EducationInformation;
import com.mecaroid.interdate.InformationPages.LocationInformation;
import com.mecaroid.interdate.InformationPages.PartnerPreferenceInformation;
import com.mecaroid.interdate.InformationPages.PersonalInformation;

public class PersonalInformationAdapter extends FragmentPagerAdapter {


    public PersonalInformationAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new PersonalInformation();
            case 1: return new EducationInformation();
            case 2: return new LocationInformation();
            case 3: return new PartnerPreferenceInformation();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
