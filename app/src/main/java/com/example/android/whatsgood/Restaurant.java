package com.example.android.whatsgood;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jyoun on 11/9/2017.
 */

public class Restaurant
{
    // Name of the restaurant
    private String mName;

    // Link to the restaurant's website
    private String mLink;

    // Image resource ID for the restaurant
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    // LinkedHashMap for the weekly specials
    LinkedHashMap<String,String> mSpecialsHashMap = new LinkedHashMap<>();

    /**
     * Create a Restaurant object
     */
    public Restaurant(String name, String link)
    {
        mName = name;
        mLink = link;
    }

    /**
     * Create a Restaurant object with an image
     */
    public Restaurant(String name, String link, int imageResourceId)
    {
        mName = name;
        mLink = link;
        mImageResourceId = imageResourceId;
    }

    /**
     * Get the name of the Restaurant
     */
    public String getName() { return mName; }

    /**
     * Get the image
     */
    public int getImageResourceId()
    {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this restaurant
     */
    public boolean hasImage()
    {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    /**
     * Returns the string representation of the {@link Restaurant} object
     */
    @Override
    public String toString()
    {
        String str = "Restaurant: " + mName + "\n";
        str += "link: " + mLink + "\n";
        for (Map.Entry m : mSpecialsHashMap.entrySet())
        {
            str += String.format("%-10s %s\n", m.getKey(), m.getValue());
        }
        return str;
    }
}
