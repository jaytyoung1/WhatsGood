package com.example.android.whatsgood.data;

/**
 * Created by jyoun on 11/22/2017.
 */

import android.net.Uri;
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
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.whatsgood";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's) (PATH_table_name)
     * For instance, content://com.example.android.whatsgood/restaurants/ is a valid path for
     * looking at whatsgood data. content://com.example.android.whatsgood/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_RESTAURANTS = "restaurants";

    /**
     * Inner class that defines constant values for the restaurants database table.
     * Each entry in the table represents a single restaurant.
     */
    public static final class RestaurantEntry implements BaseColumns
    {
        /**
         * The content URI to access the restaurant data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RESTAURANTS);

        /**
         * Name of database table for restaurants
         */
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
