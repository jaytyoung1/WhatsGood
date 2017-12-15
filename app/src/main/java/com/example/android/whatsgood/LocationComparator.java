package com.example.android.whatsgood;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by jyoun on 12/15/2017.
 */

public class LocationComparator implements Comparator<Restaurant>
{
    Location mCurrentLocation;

    public LocationComparator(Location l)
    {
        mCurrentLocation = l;
    }

    @Override
    public int compare(Restaurant r1, Restaurant r2)
    {
        Location r1Location = new Location("");
        r1Location.setLatitude(r1.getLatitude());
        r1Location.setLongitude(r1.getLongitude());

        Location r2Location = new Location("");
        r2Location.setLatitude(r2.getLatitude());
        r2Location.setLongitude(r2.getLongitude());

        float r1Distance = mCurrentLocation.distanceTo(r1Location);
        r1Distance = r1Distance * 0.00062137f; // in mi

        float r2Distance = mCurrentLocation.distanceTo(r2Location);
        r2Distance = r2Distance * 0.00062137f; // in mi

        if (r1Distance > r2Distance)
            return 1;
        else
            return -1;
    }
}
