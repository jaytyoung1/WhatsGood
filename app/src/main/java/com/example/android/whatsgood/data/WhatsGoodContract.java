package com.example.android.whatsgood.data;

/**
 * Created by jyoun on 11/22/2017.
 */

import android.provider.BaseColumns;

/**
 * API Contract for the WhatsGood app
 */
public final class WhatsGoodContract
{
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private WhatsGoodContract()
    {
    }

    /**
     * Inner class that defines constant values for the restaurants database table.
     * Each entry in the table represents a single restaurant.
     */
    public static final class RestaurantEntry implements BaseColumns
    {
        public final static String TABLE_NAME = "restaurants";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RESTAURANT_NAME = "name";
        public final static String COLUMN_RESTAURANT_LINK = "link";
        public final static String COLUMN_RESTAURANT_ADDRESS = "address";
        public final static String COLUMN_RESTAURANT_LATITUDE = "latitude";
        public final static String COLUMN_RESTAURANT_LONGITUDE = "longitude";
        public final static String COLUMN_RESTAURANT_IMAGE_RESOURCE_ID = "imageID";
        public final static String COLUMN_RESTAURANT_MONDAY_SPECIALS = "monday specials";
        public final static String COLUMN_RESTAURANT_TUESDAY_SPECIALS = "tuesday specials";
        public final static String COLUMN_RESTAURANT_WEDNESDAY_SPECIALS = "wednesday specials";
        public final static String COLUMN_RESTAURANT_THURSDAY_SPECIALS = "thursday specials";
        public final static String COLUMN_RESTAURANT_FRIDAY_SPECIALS = "friday specials";
        public final static String COLUMN_RESTAURANT_SATURDAY_SPECIALS = "saturday specials";
        public final static String COLUMN_RESTAURANT_SUNDAY_SPECIALS = "sunday specials";
    }
}
