package com.example.android.whatsgood.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.whatsgood.data.WhatsGoodContract.RestaurantEntry;

/**
 * Created by jyoun on 11/22/2017.
 *
 * What does SQLiteOpenHelper do?
 *
 * 1) Create a SQLite database when it is first accessed
 * 2) Gives you a connection to that database
 * 3) Manages updating the database schema if version changes
 */

public class WhatsGoodDbHelper extends SQLiteOpenHelper
{
    // Name of the database file
    private static final String DATABASE_NAME = "whatsgood.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link WhatsGoodDbHelper}.
     *
     * @param context of the app
     */
    public WhatsGoodDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create a String that contains the SQL statement to create the restaurants table
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + RestaurantEntry.TABLE_NAME + "("
                + RestaurantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RestaurantEntry.COLUMN_RESTAURANT_NAME + " TEXT NOT NULL, "
                + RestaurantEntry.COLUMN_RESTAURANT_LINK + " TEXT NOT NULL, "
                + RestaurantEntry.COLUMN_RESTAURANT_ADDRESS + " TEXT NOT NULL, "
                + RestaurantEntry.COLUMN_RESTAURANT_LATITUDE + " REAL NOT NULL, "
                + RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE + " REAL NOT NULL, "
                + RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS + " TEXT, "
                + RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS + " TEXT);";

        // Execute the SQL statement
        db.execSQL(CREATE_RESTAURANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
