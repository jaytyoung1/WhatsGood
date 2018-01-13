package com.example.android.whatsgood;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by jyoun on 1/12/2018.
 */

public class ProfileFragment extends Fragment
{
    /**
     * Boolean set when ProfileFragment is active or not active (see onPause() and onResume())
     */
    public static boolean isActive;

    /**
     * Empty Constructor
     */
    public ProfileFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Hide the FAB by first getting the context of the MainActivity
        Context mainContext = inflater.getContext();
        FloatingActionButton fab = ((Activity) mainContext).findViewById(R.id.fab);
        fab.hide();

        ListView listView = ((Activity) mainContext).findViewById(R.id.list);
        listView.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isActive = false;
    }
}
