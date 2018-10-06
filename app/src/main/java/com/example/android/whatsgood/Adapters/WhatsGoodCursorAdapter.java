package com.example.android.whatsgood.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whatsgood.MainActivity;
import com.example.android.whatsgood.MapFragment;
import com.example.android.whatsgood.R;
import com.example.android.whatsgood.Restaurant;
import com.example.android.whatsgood.RestaurantActivity;
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
    private Context mContext;

    /**
     * Google API location variables
     */
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    /**
     * Restaurant variables
     */
    private Restaurant currentRestaurant = null;
    private TextView milesAwayTextView;
    ArrayList<TextView> milesAwayTextViews = new ArrayList<>();
    // Resource ID for the background color for this list of restaurants
    private int mColorResourceId;

    /**
     * Constructs a new {@link WhatsGoodCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    // TODO: pass colorResourceId in Constructor? I did this in RestaurantAdapter. Why?
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
        TextView restaurantNameTextView = view.findViewById(R.id.restaurant_name_text_view);
        TextView specialsTextView = view.findViewById(R.id.specials_text_view);
        View textContainer = view.findViewById(R.id.text_container);
        ImageView restaurantImageView = view.findViewById(R.id.image);
        View miles_away_view = view.findViewById(R.id.miles_away_container);
        milesAwayTextView = view.findViewById(R.id.miles_text_view);
        milesAwayTextViews.add(milesAwayTextView);

        // Find the columns of the Restaurant attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(WhatsGoodContract.RestaurantEntry.COLUMN_RESTAURANT_NAME);
        int specialsColumnIndex = cursor.getColumnIndex(MainActivity.dayString.toLowerCase() + " specials");
        int imageColumnIndex = cursor.getColumnIndex(WhatsGoodContract.RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID);

        // Read the attributes from the cursor for the current Restaurant
        String restaurantName = cursor.getString(nameColumnIndex);
        String restaurantSpecials = cursor.getString(specialsColumnIndex);
        String imageID = cursor.getString(imageColumnIndex);
        int restaurantImageResourceID = context.getResources().getIdentifier(imageID, "drawable", context.getPackageName());

        // Update the views with the attributes for the current Restaurant
        restaurantNameTextView.setText(restaurantName);
        restaurantNameTextView.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        specialsTextView.setText(restaurantSpecials);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(mContext, mColorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        // Set the image if the Restaurant has one
        if (restaurantImageResourceID != -1)
        {
            // Set the ImageView to the image resource specified in the current Restaurant
            restaurantImageView.setImageResource(restaurantImageResourceID);

            // Make sure the view is visible
            restaurantImageView.setVisibility(View.VISIBLE);
        } else
            // If no image exists for the Restaurant, hide the ImageView
            restaurantImageView.setVisibility(View.GONE);

        // Set the milesAwayText when the list item comes into view
        if (mLastLocation != null && MainActivity.isActive)
        {
            Location restLocation = new Location("");
            restLocation.setLatitude(currentRestaurant.getLatitude());
            restLocation.setLongitude(currentRestaurant.getLongitude());

            float distance = mLastLocation.distanceTo(restLocation);
            distance = distance * 0.00062137f; // in mi

            String txt = String.format(java.util.Locale.US, "%.1f mi", distance);

            try
            {
                milesAwayTextView.setText(txt);
            } catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }

        // Add an onClickListener to the miles away container to go to the MapFragment
        miles_away_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!MapFragment.isActive)
                {
                    // Go to the MapFragment
                    Fragment mapFragment = new MapFragment();

                    // Bundle the restaurant that was clicked in order to fly to it
                    Bundle bundle = new Bundle();
                    // TODO: Need to figure out how to pass Cursor in putSerializable()
                    //bundle.putSerializable("restaurantClicked", rest);
                    mapFragment.setArguments(bundle);

                    FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, mapFragment);
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(25); // 25 meters
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
        // TODO: Need to implement onLocationChanged() to loop through Cursor of all Restaurants.
        // TODO: Need to apply change to implementations of this method in other files

//        mLastLocation = location;
//
//        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
//
//        if (fragmentManager.findFragmentById(R.id.action_map) == null && MainActivity.isActive)
//        {
//
//
//            for (int i = 0; i < restaurantsArrayList.size(); i++)
//            {
//                Location restLocation = new Location("");
//                restLocation.setLatitude(restaurantsArrayList.get(i).getLatitude());
//                restLocation.setLongitude(restaurantsArrayList.get(i).getLongitude());
//
//                float distance = mLastLocation.distanceTo(restLocation);
//                distance = distance * 0.00062137f; // in mi
//
//                String txt = String.format(java.util.Locale.US, "%.1f mi", distance);
//
//                try
//                {
//                    milesAwayTextViews.get(i).setText(txt);
//                } catch (IndexOutOfBoundsException e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//        }
    }
}
