package com.example.android.whatsgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jyoun on 11/9/2017.
 *
 * A simple {@link Fragment} subclass
 */

public class ListsFragment extends Fragment
{
    public ListsFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.restaurant_list, container, false);

        return rootView;
    }
}
