package com.example.android.whatsgood;

import android.Manifest;
import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

/*
* {@link RestaurantAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link Restaurant} objects.
* */
public class RestaurantAdapter extends ArrayAdapter<Restaurant>
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    // Resource ID for the background color for this list of restaurants
    private int mColorResourceId;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Restaurant currentRestaurant = null;
    TextView milesAwayText;
    ArrayList<TextView> milesAwayTextViews = new ArrayList<>();

    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

    Context mContext;

    /**
     * Create a new {@link RestaurantAdapter} object
     *
     * @param context     The current context. Used to inflate the layout file.
     * @param restaurants A List of AndroidFlavor objects to display in a list
     */
    public RestaurantAdapter(Activity context, ArrayList<Restaurant> restaurants, int colorResourceId)
    {
        super(context, 0, restaurants);
        mContext = context;

        mColorResourceId = colorResourceId;

        restaurantsArrayList = restaurants;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        currentRestaurant = getItem(position);
        final Restaurant rest = currentRestaurant;

        // Find the TextView in the list_item.xml layout with the ID restaurant_name_text_view
        TextView restaurantNameTextView = (TextView) listItemView.findViewById(R.id.restaurant_name_text_view);
        // Get the Restaurant name from the currentRestaurant object and set this text on the text view
        restaurantNameTextView.setText(currentRestaurant.getName());

        // Find the TextView in the list_item.xml layout with the specials
        TextView specialsTextView = (TextView) listItemView.findViewById(R.id.specials_text_view);

        // Get the current day to know which specials to display
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        //Date date = new Date();
        //String dayOfTheWeek = sdf.format(date);

        specialsTextView.setText(currentRestaurant.getSpecial(MainActivity.dayString));

        milesAwayText = (TextView) listItemView.findViewById(R.id.miles_text_view);
        milesAwayTextViews.add(milesAwayText);

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
                milesAwayText.setText(txt);
            } catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }

        // Set the text of the milesAwayTextView
        //milesAwayText.setText();

        // Find the ImageView in the list_item.xml layout with the ID image
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        // Add a click listener to the image to go to the Restaurant activity
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), RestaurantActivity.class);
                intent.putExtra("currentRestaurant", rest);
                getContext().startActivity(intent);
            }
        });

        // Add on click listener to the miles away container to go to the MapFragment
        //TODO: this listener should take you to the MapFragment, not the MapActivity
        View miles_away_view = listItemView.findViewById(R.id.miles_away_container);

        miles_away_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment mapFragment = new MapFragment();
                FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, mapFragment);
                transaction.commit();
            }
        });

        if (currentRestaurant.hasImage())
        {
            // Set the ImageView to the image resource specified in the current Word
            imageView.setImageResource(currentRestaurant.getImageResourceId());

            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else
        {
            // Otherwise hide the ImageView
            imageView.setVisibility(View.GONE);
        }

        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in the ListView
        return listItemView;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(25); // 10 meters
        mLocationRequest.setInterval(1000); // Update location every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
        mLastLocation = location;

        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.action_map) == null && MainActivity.isActive)
        {


            for (int i = 0; i < restaurantsArrayList.size(); i++)
            {
                Location restLocation = new Location("");
                restLocation.setLatitude(restaurantsArrayList.get(i).getLatitude());
                restLocation.setLongitude(restaurantsArrayList.get(i).getLongitude());

                float distance = mLastLocation.distanceTo(restLocation);
                distance = distance * 0.00062137f; // in mi

                String txt = String.format(java.util.Locale.US, "%.1f mi", distance);

                try
                {
                    milesAwayTextViews.get(i).setText(txt);
                } catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }
}