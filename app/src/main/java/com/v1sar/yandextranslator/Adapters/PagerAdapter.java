package com.v1sar.yandextranslator.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.v1sar.yandextranslator.Views.FavouriteFragment;
import com.v1sar.yandextranslator.Views.HistoryFragment;
import com.v1sar.yandextranslator.Views.TranslatorFragment;

/**
 * Created by qwerty on 28.03.17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabsCount;

    public PagerAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.tabsCount = tabsCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TranslatorFragment tab1 = new TranslatorFragment();
                return tab1;
            case 1:
                HistoryFragment tab2 = new HistoryFragment();
                return tab2;
            case 2:
                FavouriteFragment tab3 = new FavouriteFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}
