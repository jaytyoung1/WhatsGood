package com.example.android.whatsgood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by jyoun on 1/20/2018.
 */

public class NotLoggedInFragment extends Fragment
{
    Button loginButton;

    /**
     * Empty Constructor
     */
    public NotLoggedInFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_not_logged_in, container, false);

        // Hide the FAB by first getting the context of the MainActivity
        Context mainContext = inflater.getContext();
        FloatingActionButton fab = ((Activity) mainContext).findViewById(R.id.fab);
        fab.hide();

        // Hide the toolbar
        Toolbar toolbar = ((Activity) mainContext).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        // Make the listView invisible
        ListView listView = ((Activity) mainContext).findViewById(R.id.list);
        listView.setVisibility(View.INVISIBLE);

        loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchLoginActivity(v);
            }
        });
        return rootView;
    }

    public void launchLoginActivity(View view)
    {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
