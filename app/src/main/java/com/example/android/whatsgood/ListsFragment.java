package com.example.android.whatsgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/9/2017.
 *
 * A simple {@link Fragment} subclass
 */

public class ListsFragment extends Fragment
{
    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

    public ListsFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.restaurant_list, container, false);

        try
        {
            restaurantsArrayList = new GetRestaurantsAsyncTask(getContext()).execute().get();
        } catch (java.lang.InterruptedException e)
        {

        } catch (java.util.concurrent.ExecutionException e)
        {

        }

        // Create an {@link RestaurantAdapter}, whose data source is a list of {@link Restaurant}s.
        // The adapter knows how to create list items for each item in the list.
        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurantsArrayList, R.color.colorBackground);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        return rootView;
    }
}
