package com.example.android.whatsgood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whatsgood.data.CreateRestaurants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/10/2017.
 */

public class RestaurantActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView milesAwayText;
    Restaurant currentRestaurant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Get the currentRestaurant object
        Intent intent = getIntent();
        currentRestaurant = (Restaurant) intent.getSerializableExtra("currentRestaurant");

        // Set the restaurant name text view
        TextView restaurantTextView = (TextView) findViewById(R.id.restaurant_name);
        restaurantTextView.setText(currentRestaurant.getName());

        // Set the restaurant image
        ImageView restaurantImageView = (ImageView) findViewById(R.id.restaurant_photo);
        restaurantImageView.setImageResource(currentRestaurant.getImageResourceId());

        // Set the on click listener for the link image
        ImageView linkImage = (ImageView) findViewById(R.id.link);
        linkImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(currentRestaurant.getLink()));
                startActivity(intent);
            }
        });

        // Set the specials text views for each day of the week
        TextView mondaySpecialsTextView = (TextView) findViewById(R.id.monday_specials_text);
        mondaySpecialsTextView.setText(currentRestaurant.getSpecial("Monday"));
        TextView tuesdaySpecialsTextView = (TextView) findViewById(R.id.tuesday_specials_text);
        tuesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Tuesday"));
        TextView wednesdaySpecialsTextView = (TextView) findViewById(R.id.wednesday_specials_text);
        wednesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Wednesday"));
        TextView thursdaySpecialsTextView = (TextView) findViewById(R.id.thursday_specials_text);
        thursdaySpecialsTextView.setText(currentRestaurant.getSpecial("Thursday"));
        TextView fridaySpecialsTextView = (TextView) findViewById(R.id.friday_specials_text);
        fridaySpecialsTextView.setText(currentRestaurant.getSpecial("Friday"));
        TextView saturdaySpecialsTextView = (TextView) findViewById(R.id.saturday_specials_text);
        saturdaySpecialsTextView.setText(currentRestaurant.getSpecial("Saturday"));
        TextView sundaySpecialsTextView = (TextView) findViewById(R.id.sunday_specials_text);
        sundaySpecialsTextView.setText(currentRestaurant.getSpecial("Sunday"));

        milesAwayText = (TextView) findViewById(R.id.restaurant_miles_text_view);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); // Update location every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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

        Location restLocation = new Location("");
        restLocation.setLatitude(currentRestaurant.getLatitude());
        restLocation.setLongitude(currentRestaurant.getLongitude());

        float distance = mLastLocation.distanceTo(restLocation);
        distance = distance * 0.00062137f; // in mi

        String txt = String.format(java.util.Locale.US, "%.1f mi", distance);

        milesAwayText.setText(txt);
        /*
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
        */
    }
}
