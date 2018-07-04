package com.example.android.whatsgood.data;

/**
 * Created by jyoun on 11/22/2017.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the WhatsGood app
 *
 * What is a Contract Class?
 *
 * 1) Define schema and have a convention for where to find database constants
 * 2) When generating SQL commands, remove possibility of introducing spelling errors
 * 3)Ease of updating database schema
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
         * The MIME type of the {@link #CONTENT_URI} for a list of restaurants
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single restaurant
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANTS;

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
        public final static String COLUMN_RESTAURANT_MONDAY_SPECIALS = "mondaySpecials";
        public final static String COLUMN_RESTAURANT_TUESDAY_SPECIALS = "tuesdaySpecials";
        public final static String COLUMN_RESTAURANT_WEDNESDAY_SPECIALS = "wednesdaySpecials";
        public final static String COLUMN_RESTAURANT_THURSDAY_SPECIALS = "thursdaySpecials";
        public final static String COLUMN_RESTAURANT_FRIDAY_SPECIALS = "fridaySpecials";
        public final static String COLUMN_RESTAURANT_SATURDAY_SPECIALS = "saturdaySpecials";
        public final static String COLUMN_RESTAURANT_SUNDAY_SPECIALS = "sundaySpecials";
    }
}
