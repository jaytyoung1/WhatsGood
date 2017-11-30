package com.example.android.whatsgood.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.whatsgood.data.WhatsGoodContract.RestaurantEntry;

/**
 * {@link ContentProvider} for Whats Good app.
 */
public class WhatsGoodProvider extends ContentProvider
{
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = WhatsGoodProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the restaurants table
     */
    private static final int RESTAURANTS = 100;

    /**
     * URI matcher code for the content URI for a single restaurant in the restaurants table
     */
    private static final int RESTAURANT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static
    {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(WhatsGoodContract.CONTENT_AUTHORITY, WhatsGoodContract.PATH_RESTAURANTS, RESTAURANTS);

        sUriMatcher.addURI(WhatsGoodContract.CONTENT_AUTHORITY, WhatsGoodContract.PATH_RESTAURANTS + "/#", RESTAURANT_ID);
    }

    /**
     * Database helper object
     */
    private WhatsGoodDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate()
    {
        mDbHelper = new WhatsGoodDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder)
    {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case RESTAURANTS:
                // For the RESTAURANTS code, query the restaurants table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the restaurants table.
                cursor = database.query(RestaurantEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RESTAURANT_ID:
                // For the RESTAURANT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.whatsgood/restaurants/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = RestaurantEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the restaurants table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(RestaurantEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case RESTAURANTS:
                return insertRestaurant(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a restaurant into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertRestaurant(Uri uri, ContentValues values)
    {
        // Check that the restaurant name is not null
        String name = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_NAME);
        if (name == null)
            throw new IllegalArgumentException("Restaurant requires a name");

        // Check that the restaurant link is not null
        String link = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_LINK);
        if (link == null)
            throw new IllegalArgumentException("Restaurant requires a link");

        // Check that the restaurant address is not null
        String address = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_ADDRESS);
        if (address == null)
            throw new IllegalArgumentException("Restaurant requires an address");

        // Check that the restaurant latitude is not null
        double latitude = values.getAsDouble(RestaurantEntry.COLUMN_RESTAURANT_LATITUDE);
        if (latitude == 0.0)
            throw new IllegalArgumentException("Restaurant requires a latitude");

        // Check that the restaurant longitude is not null
        double longitude = values.getAsDouble(RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE);
        if (longitude == 0.0)
            throw new IllegalArgumentException("Restaurant requires a longitude");

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new restaurant with the given values
        long id = database.insert(RestaurantEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1)
        {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Once we know the ID of the new row in the table, return the new URI with the ID appended
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case RESTAURANTS:
                return updateRestaurant(uri, contentValues, selection, selectionArgs);
            case RESTAURANT_ID:
                // For the RESTAURANT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = RestaurantEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRestaurant(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update restaurants in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more restaurants).
     * Return the number of rows that were successfully updated.
     */
    private int updateRestaurant(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        // If the {@link RestaurantEntry#COLUMN_RESTAURANT_NAME} key is present, check that it's not null
        if (values.containsKey(RestaurantEntry.COLUMN_RESTAURANT_NAME))
        {
            String name = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_NAME);
            if (name == null)
                throw new IllegalArgumentException("Restaurant requires a name");
        }

        // If the {@link RestaurantEntry#COLUMN_RESTAURANT_LINK} key is present, check that it's not null
        if (values.containsKey(RestaurantEntry.COLUMN_RESTAURANT_LINK))
        {
            String link = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_LINK);
            if (link == null)
                throw new IllegalArgumentException("Restaurant requires a link");
        }

        // If the {@link RestaurantEntry#COLUMN_RESTAURANT_ADDRESS} key is present, check that it's not null
        if (values.containsKey(RestaurantEntry.COLUMN_RESTAURANT_ADDRESS))
        {
            String address = values.getAsString(RestaurantEntry.COLUMN_RESTAURANT_ADDRESS);
            if (address == null)
                throw new IllegalArgumentException("Restaurant requires an address");
        }

        // If the {@link RestaurantEntry#COLUMN_RESTAURANT_LATITUDE} key is present, check that it's not null
        if (values.containsKey(RestaurantEntry.COLUMN_RESTAURANT_LATITUDE))
        {
            double latitude = values.getAsDouble(RestaurantEntry.COLUMN_RESTAURANT_LATITUDE);
            if (latitude == 0.0)
                throw new IllegalArgumentException("Restaurant requires a latitude");
        }

        // If the {@link RestaurantEntry#COLUMN_RESTAURANT_LONGITUDE} key is present, check that it's not null
        if (values.containsKey(RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE))
        {
            double longitude = values.getAsDouble(RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE);
            if (longitude == 0.0)
                throw new IllegalArgumentException("Restaurant requires a longitude");
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0)
            return 0;

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(RestaurantEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case RESTAURANTS:
                // Delete all rows that match the selection and selection args
                return database.delete(RestaurantEntry.TABLE_NAME, selection, selectionArgs);
            case RESTAURANT_ID:
                // Delete a single row given by the ID in the URI
                selection = RestaurantEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(RestaurantEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case RESTAURANTS:
                return RestaurantEntry.CONTENT_LIST_TYPE;
            case RESTAURANT_ID:
                return RestaurantEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}