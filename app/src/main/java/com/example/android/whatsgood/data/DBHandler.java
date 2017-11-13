package com.example.android.whatsgood.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jyoun on 11/13/2017.
 */

/**
 * Database helper for WhatsGood app. Manages database creation and version management.
 */
public class DBHandler extends SQLiteOpenHelper
{
    // Name of the database file
    private static final String DATABASE_NAME = "whatsgood.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_RESTAURANTS = "restaurants";

    // Restaurants Table columns
    private static final String KEY_ID = "id";
    private static final String KEY_RESTAURANT_OBJECT = "object";
    private static final String KEY_RESTAURANT_NAME = "name";
    private static final String KEY_RESTAURANT_ADDRESS = "address";
    private static final String KEY_RESTAURANT_LINK = "link";
    private static final String KEY_RESTAURANT_IMAGE = "image";
    private static final String KEY_RESTAURANT_MONDAY = "monday_specials";
    private static final String KEY_RESTAURANT_TUESDAY = "tuesday_specials";
    private static final String KEY_RESTAURANT_WEDNESDAY = "wednesday_specials";
    private static final String KEY_RESTAURANT_THURSDAY = "thursday_specials";
    private static final String KEY_RESTAURANT_FRIDAY = "friday_specials";
    private static final String KEY_RESTAURANT_SATURDAY = "saturday_specials";
    private static final String KEY_RESTAURANT_SUNDAY = "sunday_specials";

    /**
     * Constructs a new instance of {@link DBHandler}.
     *
     * @param context of the app
     */
    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create a String that contains the SQL statement to create the pets table
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_RESTAURANT_OBJECT + "BLOB NOT NULL,"
                + KEY_RESTAURANT_NAME + "TEXT NOT NULL,"
                + KEY_RESTAURANT_ADDRESS + "TEXT NOT NULL,"
                + KEY_RESTAURANT_LINK + "TEXT NOT NULL,"
                + KEY_RESTAURANT_IMAGE + "TEXT NOT NULL,"
                + KEY_RESTAURANT_MONDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_TUESDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_WEDNESDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_THURSDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_FRIDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_SATURDAY + "TEXT NOT NULL,"
                + KEY_RESTAURANT_SUNDAY + "TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(CREATE_RESTAURANTS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop the table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);

        // Create table again
        onCreate(db);
    }
}
