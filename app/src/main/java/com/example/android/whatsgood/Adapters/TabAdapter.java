package com.example.android.whatsgood.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.whatsgood.MainFragment;
import com.example.android.whatsgood.MapFragment;

/**
 * Created by jyoun on 11/9/2017.
 */

public class TabAdapter extends FragmentPagerAdapter
{
    /**
     * Context of the app
     */
    private Context mContext;

    /**
     * Create a new {@link TabAdapter} object
     *
     * @param context   is the context of the app
     * @param fm        is the fragment manager that will keep each fragment's state in the adapter
     *                  across swipes
     */
    public TabAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number
     */
    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
            return new MainFragment();
        else
            return new MapFragment();
    }

    /**
     * Return the total number of pages
     */
    @Override
    public int getCount() { return 2; }

    @Override
    public CharSequence getPageTitle(int position)
    {
       return "";
    }
}
