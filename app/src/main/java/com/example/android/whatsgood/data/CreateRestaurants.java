package com.example.android.whatsgood.data;

import com.example.android.whatsgood.R;
import com.example.android.whatsgood.Restaurant;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/14/2017.
 */

public class CreateRestaurants
{
    //Create an ArrayList of restaurants
    private ArrayList<Restaurant> restaurants = new ArrayList<>();

    public CreateRestaurants()
    {
        Restaurant slate = new Restaurant(
                "The Slate Pub",
                "http://slatepub.com",
                "509 E Main St. Pen Argyl, PA",
                40.871555,
                -75.248991,
                R.drawable.the_slate_logo);

        slate.mSpecialsHashMap.put("Monday", "$0.50 wings");
        slate.mSpecialsHashMap.put("Tuesday", "");
        slate.mSpecialsHashMap.put("Wednesday", "");
        slate.mSpecialsHashMap.put("Thursday", "$0.50 boneless wings");
        slate.mSpecialsHashMap.put("Friday", "live music or DJ 10pm - 2am");
        slate.mSpecialsHashMap.put("Saturday", "live music or DJ 10pm - 2am");
        slate.mSpecialsHashMap.put("Sunday", "$0.50 wings");
        restaurants.add(slate);

        Restaurant detzis = new Restaurant(
                "Detzi's Tavern",
                "http://www.detzistavern.com",
                "570 N Lehigh Ave. Wind Gap, PA",
                40.854098,
                -75.2823377,
                R.drawable.detzis_logo);

        detzis.mSpecialsHashMap.put("Monday", "");
        detzis.mSpecialsHashMap.put("Tuesday", "");
        detzis.mSpecialsHashMap.put("Wednesday", "$0.55 wings, $2 Michelob Ultra pints");
        detzis.mSpecialsHashMap.put("Thursday", "Beer Feature: Miller Lite");
        detzis.mSpecialsHashMap.put("Friday", "");
        detzis.mSpecialsHashMap.put("Saturday", "");
        detzis.mSpecialsHashMap.put("Sunday", "");
        restaurants.add(detzis);

        Restaurant scorecard = new Restaurant(
                "Scorecard Sports Bar & Grill",
                "http://www.scorecardbar.com",
                "130 N Broadway, Wind Gap, PA",
                40.8506398,
                -75.2920164,
                R.drawable.scorecard_logo);

        scorecard.mSpecialsHashMap.put("Monday", "$0.55 wings, $2 Bud Light drafts");
        scorecard.mSpecialsHashMap.put("Tuesday", "$1.59 tacos, $3 Landshark drafts");
        scorecard.mSpecialsHashMap.put("Wednesday", "$1.75 sloppy joes, $6 Coors Light pitchers, Team Trivia at 8pm");
        scorecard.mSpecialsHashMap.put("Thursday", "$0.45 boneless wings, $2 Shock Top drafts, $6 mixed drink pitchers (10pm - midnight), Karaoke at 9:30pm");
        scorecard.mSpecialsHashMap.put("Friday", "$3 off large pizzas, $2 small pizzas, $1 off personal pizzas, $5 domestic non-premium pitchers (8 - 10pm), Free Juke Box at 10pm");
        scorecard.mSpecialsHashMap.put("Saturday", "$3 Coronas, check Facebook for updates");
        scorecard.mSpecialsHashMap.put("Sunday", "$2 Bud Light drafts");
        restaurants.add(scorecard);
    }

    public ArrayList<Restaurant> getArrayList()
    {
        return restaurants;
    }
}
