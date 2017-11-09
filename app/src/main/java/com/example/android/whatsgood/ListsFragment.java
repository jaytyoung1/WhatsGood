package com.example.android.whatsgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

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

        //Create an ArrayList of restaurants
        final ArrayList<Restaurant> restaurants = new ArrayList<>();

        Restaurant slate = new Restaurant("The Slate Pub", "http://slatepub.com", R.drawable.the_slate_logo);
        slate.mSpecialsHashMap.put("Monday", "$0.50 wings");
        slate.mSpecialsHashMap.put("Tuesday", "");
        slate.mSpecialsHashMap.put("Wednesday", "");
        slate.mSpecialsHashMap.put("Thursday", "$0.50 boneless wings");
        slate.mSpecialsHashMap.put("Friday", "live music or DJ 10pm - 2am");
        slate.mSpecialsHashMap.put("Saturday", "live music or DJ 10pm - 2am");
        slate.mSpecialsHashMap.put("Sunday", "$0.50 wings");
        restaurants.add(slate);

        Restaurant detzis = new Restaurant("Detzi's Tavern", "http://www.detzistavern.com", R.drawable.detzis_logo);
        detzis.mSpecialsHashMap.put("Monday", "");
        detzis.mSpecialsHashMap.put("Tuesday", "");
        detzis.mSpecialsHashMap.put("Wednesday", "$0.55 wings, $2 Michelob Ultra pints");
        detzis.mSpecialsHashMap.put("Thursday", "");
        detzis.mSpecialsHashMap.put("Friday", "");
        detzis.mSpecialsHashMap.put("Saturday", "");
        detzis.mSpecialsHashMap.put("Sunday", "");
        restaurants.add(detzis);

        Restaurant scorecard = new Restaurant("Scorecard Sports Bar & Grill", "http://www.scorecardbar.com", R.drawable.scorecard_logo);
        scorecard.mSpecialsHashMap.put("Monday", "$0.55 wings, $2 Bud Light drafts");
        scorecard.mSpecialsHashMap.put("Tuesday", "$1.59 tacos, $3 Landshark drafts");
        scorecard.mSpecialsHashMap.put("Wednesday", "$1.75 sloppy joes, $6 Coors Light pitchers, Team Trivia at 8pm");
        scorecard.mSpecialsHashMap.put("Thursday", "$0.45 boneless wings, $2 Shock Top drafts, $6 mixed drink pitchers (10pm - midnight), Karaoke at 9:30pm");
        scorecard.mSpecialsHashMap.put("Friday", "$3 off large pizzas, $2 small pizzas, $1 off personal pizzas, $5 domestic non-premium pitchers (8 - 10pm), Free Juke Box at 10pm");
        scorecard.mSpecialsHashMap.put("Saturday", "$3 Coronas, check Facebook for updates");
        scorecard.mSpecialsHashMap.put("Sunday", "$2 Bud Light drafts");
        restaurants.add(scorecard);

        // Create an {@link RestaurantAdapter}, whose data source is a list of {@link Restaurant}s.
        // The adapter knows how to create list items for each item in the list.
        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurants, R.color.colorBackground);

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
