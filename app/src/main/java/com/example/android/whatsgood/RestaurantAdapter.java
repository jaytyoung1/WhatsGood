package com.example.android.whatsgood;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whatsgood.data.CreateRestaurants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    /**
     * Create a new {@link RestaurantAdapter} object
     *
     * @param context     The current context. Used to inflate the layout file.
     * @param restaurants A List of AndroidFlavor objects to display in a list
     */
    public RestaurantAdapter(Activity context, ArrayList<Restaurant> restaurants, int colorResourceId)
    {
        super(context, 0, restaurants);
        mColorResourceId = colorResourceId;

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
        restaurantNameTextView.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));

        // Find the TextView in the list_item.xml layout with the specials
        TextView specialsTextView = (TextView) listItemView.findViewById(R.id.specials_text_view);

        // Get the current day to know which specials to display
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        //Date date = new Date();
        //String dayOfTheWeek = sdf.format(date);

        specialsTextView.setText(currentRestaurant.getSpecial(MainActivity.dayString));

        milesAwayText = (TextView) listItemView.findViewById(R.id.miles_text_view);
        milesAwayTextViews.add(milesAwayText);

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

        // Add on click listener to the miles away container to go to the Map activity
        View miles_away_view = listItemView.findViewById(R.id.miles_away_container);
        miles_away_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), MapActivity.class);
                getContext().startActivity(intent);
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

        // Create an instance of the object that creates restaurants
        CreateRestaurants createRestaurantsObject = new CreateRestaurants();

        // Get it's ArrayList of restaurants

        ArrayList<Restaurant> mRestaurants = createRestaurantsObject.getArrayList();

        int i = 0;
        for (Restaurant r : mRestaurants)
        {
            Location restLocation = new Location("");
            restLocation.setLatitude(r.getLatitude());
            restLocation.setLongitude(r.getLongitude());

            float distance = mLastLocation.distanceTo(restLocation);
            distance = distance * 0.00062137f; // in mi

            String txt = String.format(java.util.Locale.US, "%.1f mi", distance);

            milesAwayTextViews.get(i).setText(txt);

            i++;
        }
    }
}