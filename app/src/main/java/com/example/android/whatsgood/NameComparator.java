package com.example.android.whatsgood;

import java.util.Comparator;

/**
 * Created by jyoun on 12/13/2017.
 */


/**
 * This comparator is used to sort the restaurant name alphabetically
 */
public class NameComparator implements Comparator<Restaurant>
{
    @Override
    public int compare(Restaurant r1, Restaurant r2)
    {
        return r1.getName().compareTo(r2.getName());
    }
}

