package com.t3hh4xx0r.leash;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.wearable.view.CardFragment;

public class CardFragmentPagerAdapter extends FragmentPagerAdapter {
    public Activity act;

    public CardFragmentPagerAdapter(Activity act) {
        super(act.getFragmentManager());
        this.act = act;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new TextDisplayFragment();
            case 1:
                return new FindPhoneFragment();
            case 2:
                return new ShowNotificationFragment();
            case 3:
                return new OpenOnPhoneFragment();

            default:
                return new CardFragment();
        }

    }

}