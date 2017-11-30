package com.example.android.whatsgood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whatsgood.data.CreateRestaurants;
import com.example.android.whatsgood.data.WhatsGoodContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/27/2017.
 */

/**
 * {@link WhatsGoodCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of restaurant data as its data source. This adapter knows
 * how to create list items for each row of restaurant data in the {@link Cursor}.
 */
public class WhatsGoodCursorAdapter extends CursorAdapter
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    /**
     * Context of the app
     */
    Context mContext;

    TextView milesAwayText;
    ArrayList<TextView> milesAwayTextViews = new ArrayList<>();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    /**
     * Constructs a new {@link WhatsGoodCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public WhatsGoodCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
        mContext = context;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the restaurant data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        // Find the individual views that we want to modify in the list item layout
        TextView restaurantNameTextView = (TextView) view.findViewById(R.id.restaurant_name_text_view);
        TextView specialsTextView = (TextView) view.findViewById(R.id.specials_text_view);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        milesAwayText = (TextView) view.findViewById(R.id.miles_text_view);
        milesAwayTextViews.add(milesAwayText);

        // Find the columns of the restaurant attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(WhatsGoodContract.RestaurantEntry.COLUMN_RESTAURANT_NAME);
        int specialsColumnIndex = cursor.getColumnIndex(MainActivity.dayString.toLowerCase() + " specials");
        int imageColumnIndex = cursor.getColumnIndex(WhatsGoodContract.RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID);

        // Read the restaurant attributes from the cursor for the current restaurant
        String restaurantName = cursor.getString(nameColumnIndex);
        String restaurantSpecials = cursor.getString(specialsColumnIndex);
        String imageID = cursor.getString(imageColumnIndex);
        int restaurantImageResourceID = context.getResources().getIdentifier(imageID, "drawable", context.getPackageName());

        // Update the views with the attributes for the current restaurant
        restaurantNameTextView.setText(restaurantName);
        restaurantNameTextView.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        specialsTextView.setText(restaurantSpecials);
        imageView.setImageResource(restaurantImageResourceID);

//        // Add a click listener to the image to go to the Restaurant activity
//        imageView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(context, RestaurantActivity.class);
//                intent.putExtra("currentRestaurant", rest);
//                context.startActivity(intent);
//            }
//        });
//
//        // Add on click listener to the miles away container to go to the Map activity
//        View miles_away_view = view.findViewById(R.id.miles_away_container);
//        miles_away_view.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(context, MapActivity.class);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); // Update location every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
//        mLastLocation = location;
//
//        // Create an instance of the object that creates restaurants
//        CreateRestaurants createRestaurantsObject = new CreateRestaurants();
//
//        // Get it's ArrayList of restaurants
//
//        ArrayList<Restaurant> mRestaurants = createRestaurantsObject.getArrayList();
//
//        int i = 0;
//        for (Restaurant r : mRestaurants)
//        {
//            Location restLocation = new Location("");
//            restLocation.setLatitude(r.getLatitude());
//            restLocation.setLongitude(r.getLongitude());
//
//            float distance = mLastLocation.distanceTo(restLocation);
//            distance = distance * 0.00062137f; // in mi
//
//            String txt = String.format(java.util.Locale.US, "%.1f mi", distance);
//
//            milesAwayTextViews.get(i).setText(txt);
//
//            i++;
//        }
    }
}
