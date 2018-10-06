package com.example.android.whatsgood.data;

import android.content.Context;
import android.database.Cursor;

import com.example.android.whatsgood.Restaurant;
import com.example.android.whatsgood.data.WhatsGoodContract.RestaurantEntry;
import com.facebook.common.Common;

import java.util.ArrayList;

/**
 * Common database methods
 * <p>
 * 1) examineDataBaseInfo()
 * 2) getRestaurantDBCount()
 * 3) getRestaurantGoogleSheetCount()
 */
public class CommonDataAccess
{
    // Variable to hold context
    private Context mContext;

    // Data provider class to make query, insert, etc. calls
    private WhatsGoodProvider whatsGoodProvider = new WhatsGoodProvider();

    /**
     * Constructor
     *
     * @param context
     */
    public CommonDataAccess(Context context)
    {
        mContext = context;
    }

    /**
     * Returns Cursor containing rows of ID and restaurant name
     *
     * @return cursor
     */
    public Cursor examineDataBaseInfo()
    {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = {
                RestaurantEntry._ID,
                RestaurantEntry.COLUMN_RESTAURANT_NAME
        };

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = mContext.getContentResolver().query(
                RestaurantEntry.CONTENT_URI,  // The content URI of the restaurants table
                projection,                   // The columns to return for each row
                null,                 // Selection criteria
                null,              // Selection criteria
                null                  // The sort order for the returned rows
        );

        return cursor;
    }

    /**
     * Returns total number of restaurants in sqlite database
     *
     * @return cursor.getCount()
     */
    public int getRestaurantDBCount()
    {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = {
                RestaurantEntry._ID,
                RestaurantEntry.COLUMN_RESTAURANT_NAME
        };

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = mContext.getContentResolver().query(
                RestaurantEntry.CONTENT_URI,  // The content URI of the restaurants table
                projection,                   // The columns to return for each row
                null,                 // Selection criteria
                null,              // Selection criteria
                null                  // The sort order for the returned rows
        );

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public int getRestaurantGoogleSheetCount()
    {
        ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

        try
        {
            restaurantsArrayList = new GetRestaurantsAsyncTask(mContext).execute().get();
        } catch (java.lang.InterruptedException e)
        {

        } catch (java.util.concurrent.ExecutionException e)
        {

        }

        return restaurantsArrayList.size();
    }

    /**
     * Maps input cursor to a Restaurant object.
     *
     * @param cursor - must contain all fields necessary to instantiate new Restaurant
     * @return restaurant - new instance of Restaurant class
     */
    public Restaurant mapCursorToRestaurant(Cursor cursor)
    {
        // Find the columns of the restaurant attributes that we're interested in
        int restaurantNameColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_NAME);
        int addressColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_ADDRESS);
        int latitudeColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE);
        int websiteLinkColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_LINK);
        int restaurantImageColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID);
        int mondayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS);
        int tuesdayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS);
        int wednesdayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS);
        int thursdayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS);
        int fridayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS);
        int saturdayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS);
        int sundayColumnIndex = cursor.getColumnIndex(RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS);

        String imageID = cursor.getString(restaurantImageColumnIndex);
        int resID = mContext.getResources().getIdentifier(imageID, "drawable", mContext.getPackageName());

        Restaurant restaurant = new Restaurant(
                cursor.getString(restaurantNameColumnIndex),
                cursor.getString(websiteLinkColumnIndex),
                cursor.getString(addressColumnIndex),
                cursor.getDouble(latitudeColumnIndex),
                cursor.getDouble(longitudeColumnIndex),
                resID);

        restaurant.mSpecialsHashMap.put("Monday", cursor.getString(mondayColumnIndex));
        restaurant.mSpecialsHashMap.put("Tuesday", cursor.getString(tuesdayColumnIndex));
        restaurant.mSpecialsHashMap.put("Wednesday", cursor.getString(wednesdayColumnIndex));
        restaurant.mSpecialsHashMap.put("Thursday", cursor.getString(thursdayColumnIndex));
        restaurant.mSpecialsHashMap.put("Friday", cursor.getString(fridayColumnIndex));
        restaurant.mSpecialsHashMap.put("Saturday", cursor.getString(saturdayColumnIndex));
        restaurant.mSpecialsHashMap.put("Sunday", cursor.getString(sundayColumnIndex));

        return restaurant;
    }

    // TODO:
//    public ArrayList<Restaurant> getArrayListOfRestaurantsFromDB()
//    {
//        // For comparing the google sheet array list to the array list from the db
//        ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();
//        try
//        {
//            restaurantsArrayList = new GetRestaurantsAsyncTask(mContext).execute().get();
//        } catch (java.lang.InterruptedException e)
//        {
//
//        } catch (java.util.concurrent.ExecutionException e)
//        {
//
//        }
//
//
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query
//        String[] projection = {
//                RestaurantEntry._ID,
//                RestaurantEntry.COLUMN_RESTAURANT_NAME,
//                RestaurantEntry.COLUMN_RESTAURANT_LINK,
//                RestaurantEntry.COLUMN_RESTAURANT_ADDRESS,
//                RestaurantEntry.COLUMN_RESTAURANT_LATITUDE,
//                RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE,
//                RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID,
//                RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS
//        };
//
//        // Perform a query on the provider using the ContentResolver.
//        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
//        Cursor cursor = whatsGoodProvider.query(
//                RestaurantEntry.CONTENT_URI,  // The content URI of the restaurants table
//                projection,                   // The columns to return for each row
//                null,                 // Selection criteria
//                null,              // Selection criteria
//                null                  // The sort order for the returned rows
//        );
//
//        return restaurantsArrayList;
//    }
}
