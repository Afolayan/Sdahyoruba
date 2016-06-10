package com.jcedar.sdahyoruba.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Afolayan Oluwaseyi on 27/05/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 700;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
