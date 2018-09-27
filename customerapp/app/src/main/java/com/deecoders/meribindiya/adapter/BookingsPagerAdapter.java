package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deecoders.meribindiya.fragment.BookingFrag;

public class BookingsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public BookingsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "ongoing");
            BookingFrag frag1 = new BookingFrag();
            frag1.setArguments(bundle);
            return frag1;
        }
        else if(position == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "history");
            BookingFrag frag2 = new BookingFrag();
            frag2.setArguments(bundle);
            return frag2;
        }
        return null;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Ongoing";
            case 1:
                return "History";
            default:
                return null;
        }
    }
}
