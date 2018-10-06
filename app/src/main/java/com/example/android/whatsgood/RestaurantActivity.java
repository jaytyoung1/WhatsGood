package com.example.android.whatsgood;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whatsgood.data.CommonDataAccess;
import com.example.android.whatsgood.data.WhatsGoodContract.RestaurantEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by jyoun on 11/10/2017.
 * <p>
 * The RestaurantActivity shows main summary for a restaurant.
 * <p>
 * Entry:
 * 1) Clicking a list item in the MainActivity
 * 2) TODO: Clicking the marker in the MapFragment
 */

public class RestaurantActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    /**
     * Identifier for the restaurant data loader
     */
    private static final int EXISTING_RESTAURANT_LOADER = 0;

    /**
     * Content URI for the existing restaurant (null if it's a new restaurant)
     */
    private Uri mCurrentRestaurantUri;

    /**
     * Google API location variables
     */
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    /**
     * Restaurant variables
     */
    Restaurant currentRestaurant = null;
    TextView restaurantNameTextView;
    TextView addressTextView;
    ImageView websiteLinkImageView;
    ImageView restaurantImageView;
    TextView mondaySpecialsTextView;
    TextView tuesdaySpecialsTextView;
    TextView wednesdaySpecialsTextView;
    TextView thursdaySpecialsTextView;
    TextView fridaySpecialsTextView;
    TextView saturdaySpecialsTextView;
    TextView sundaySpecialsTextView;
    TextView milesAwayText;
    View miles_away_view;
    FloatingActionButton editRestaurantFab;

    /**
     * Common data access class
     */
    CommonDataAccess commonDataAccess = new CommonDataAccess(this);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Get the intent that was used to launch this activity
        mCurrentRestaurantUri = getIntent().getData();

        // Initialize a loader to read the restaurant data from the database
        getLoaderManager().initLoader(EXISTING_RESTAURANT_LOADER, null, this);

        // Find all relevant views for the restaurant
        restaurantNameTextView = findViewById(R.id.restaurant_name);
        addressTextView = findViewById(R.id.address_text_view);
        restaurantImageView = findViewById(R.id.restaurant_image);
        websiteLinkImageView = findViewById(R.id.link);
        mondaySpecialsTextView = findViewById(R.id.monday_specials_text_view);
        tuesdaySpecialsTextView = findViewById(R.id.tuesday_specials_text_view);
        wednesdaySpecialsTextView = findViewById(R.id.wednesday_specials_text_view);
        thursdaySpecialsTextView = findViewById(R.id.thursday_specials_text_view);
        fridaySpecialsTextView = findViewById(R.id.friday_specials_text_view);
        saturdaySpecialsTextView = findViewById(R.id.saturday_specials_text_view);
        sundaySpecialsTextView = findViewById(R.id.sunday_specials_text_view);
        milesAwayText = findViewById(R.id.restaurant_miles_text_view);
        miles_away_view = findViewById(R.id.restaurant_miles_away_container);
        editRestaurantFab = findViewById(R.id.restaurant_activity_fab);

        // Build the Google API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        // Define a projection that contains the columns we need
        String[] projection = {
                RestaurantEntry._ID,
                RestaurantEntry.COLUMN_RESTAURANT_NAME,
                RestaurantEntry.COLUMN_RESTAURANT_ADDRESS,
                RestaurantEntry.COLUMN_RESTAURANT_LATITUDE,
                RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE,
                RestaurantEntry.COLUMN_RESTAURANT_LINK,
                RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID,
                RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS,
                RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,       // Parent activity context
                mCurrentRestaurantUri,      // Query the content URI for the current restaurant
                projection,                 // Columns to include in the resulting Cursor
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1)
            return;

        // Proceed with moving to the first row of the cursor and reading data from it
        // (this should be the only row in the cursor)
        if (cursor.moveToFirst())
        {
            // Map the cursor to a new Restaurant object
            currentRestaurant = commonDataAccess.mapCursorToRestaurant(cursor);

            // Update the views on the screen with the values from the database
            restaurantNameTextView.setText(currentRestaurant.getName());
            addressTextView.setText(currentRestaurant.getAddress());
            restaurantImageView.setImageResource(currentRestaurant.getImageResourceId());

            // Set the specials text views for each day of the week
            // Monday
            if (currentRestaurant.getSpecial("Monday").isEmpty())
                mondaySpecialsTextView.setText(R.string.no_specials_added);
            else
                mondaySpecialsTextView.setText(currentRestaurant.getSpecial("Monday"));

            // Tuesday
            if (currentRestaurant.getSpecial("Tuesday").isEmpty())
                tuesdaySpecialsTextView.setText(R.string.no_specials_added);
            else
                tuesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Tuesday"));

            // Wednesday
            if (currentRestaurant.getSpecial("Wednesday").isEmpty())
                wednesdaySpecialsTextView.setText(R.string.no_specials_added);
            else
                wednesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Wednesday"));

            // Thursday
            if (currentRestaurant.getSpecial("Thursday").isEmpty())
                thursdaySpecialsTextView.setText(R.string.no_specials_added);
            else
                thursdaySpecialsTextView.setText(currentRestaurant.getSpecial("Thursday"));

            // Friday
            if (currentRestaurant.getSpecial("Friday").isEmpty())
                fridaySpecialsTextView.setText(R.string.no_specials_added);
            else
                fridaySpecialsTextView.setText(currentRestaurant.getSpecial("Friday"));

            // Saturday
            if (currentRestaurant.getSpecial("Saturday").isEmpty())
                saturdaySpecialsTextView.setText(R.string.no_specials_added);
            else
                saturdaySpecialsTextView.setText(currentRestaurant.getSpecial("Saturday"));

            // Sunday
            if (currentRestaurant.getSpecial("Sunday").isEmpty())
                sundaySpecialsTextView.setText(R.string.no_specials_added);
            else
                sundaySpecialsTextView.setText(currentRestaurant.getSpecial("Sunday"));

            // Set the on click listener for the link image
            websiteLinkImageView.setOnClickListener(new View.OnClickListener()
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

            // Set the on click listener for the edit floating action button
            editRestaurantFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(RestaurantActivity.this, EditRestaurantActivity.class);
                    intent.putExtra("currentRestaurant", currentRestaurant);
                    startActivity(intent);
                }
            });

            // Set the on click listener to the miles away container to go to the MapFragment
            miles_away_view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Start the MainActivity by intent
                    Intent intent = new Intent(RestaurantActivity.this, MainActivity.class);

                    // Pass the current restaurant in order to fly to it
                    intent.putExtra("restaurantClicked", currentRestaurant);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        // If the loader is invalidated, remove any references it has to the Loader's data
        currentRestaurant = null;
        restaurantNameTextView.setText("");
        addressTextView.setText("");
        websiteLinkImageView.setImageDrawable(null);
        restaurantImageView.setImageDrawable(null);
        mondaySpecialsTextView.setText("");
        tuesdaySpecialsTextView.setText("");
        wednesdaySpecialsTextView.setText("");
        thursdaySpecialsTextView.setText("");
        fridaySpecialsTextView.setText("");
        saturdaySpecialsTextView.setText("");
        sundaySpecialsTextView.setText("");
        milesAwayText.setText("");
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(10); // 10 meters
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
    }
}
