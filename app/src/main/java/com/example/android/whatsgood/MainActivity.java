package com.example.android.whatsgood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    /**
     * Database variables
     */
    //private static final int WHATSGOOD_LOADER = 0;
    //WhatsGoodCursorAdapter mCursorAdapter;

    /**
     * Location variables
     */
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;

    /**
     * ArrayList of restaurants
     */
    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

    /**
     * RestaurantAdapter extends ArrayAdapter and provides the layout for each list
     */
    RestaurantAdapter restaurantAdapter;

    /**
     * Restaurant ListView used to set the Adapter
     */
    ListView listView;

    /**
     * Spinner drop down arrow to enter day of the week
     */
    private Spinner mDaySpinner;

    /**
     * String set when day is selected from spinner
     * public because the Restaurant Adapter uses it to set the correct specials
     */
    public static String dayString = "";

    /**
     * Boolean set when MainActivity is active or not active (see onPause() and onResume())
     */
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // This code is executed when restarting the MainFragment
        // If the drop down spinner has previously been set, get the day of the week to pre set the spinner
        Bundle extras = getIntent().getExtras();
        if (getIntent().getStringExtra("dayString") != null)
            dayString = extras.getString("dayString");

        // This code is executed when going from the RestaurantActivity to the MapFragment in the MainActivity
        if (getIntent().getSerializableExtra("restaurantClicked") != null)
        {
            // Get the Restaurant object that was clicked
            Restaurant restaurantClicked = (Restaurant) getIntent().getSerializableExtra("restaurantClicked");

            // Create a new MapFragment
            Fragment mapFragment = new MapFragment();

            // Bundle the restaurant that was clicked in order to fly to it
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurantClicked", restaurantClicked);
            mapFragment.setArguments(bundle);

            // Start the MapFragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, mapFragment);
            transaction.commit();
        }

        //Initialize Google Play Services, ask user for permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                //Location Permission already granted
                buildGoogleApiClient();
                //mGoogleMap.setMyLocationEnabled(true);
            } else
            {
                //Request Location Permission
                checkLocationPermission();
            }
        } else
        {
            buildGoogleApiClient();
            //mGoogleMap.setMyLocationEnabled(true);
        }

        // Database statements
        //WhatsGoodDbHelper mDbHelper = new WhatsGoodDbHelper(this);
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Set up the action bar, but get rid of the title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        // Set up the spinner to select the day of the week
        mDaySpinner = (Spinner) findViewById(R.id.spinner_day_of_the_week);
        setupSpinner();

//        // Find the view pager that will allow the user to swipe between fragments
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        // Create an adapter that knows which fragment should be shown on each page
//        TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//
//        // Find the tab layout that shows the tabs
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        // Connect the tab layout with the view pager. This will
//        //   1. Update the tab layout when the view pager is swiped
//        //   2. Update the view pager when a tab is selected
//        //   3. Set the tab layout's tab names with the view pager's adapter's titles
//        //      by calling onPageTitle()
//        tabLayout.setupWithViewPager(viewPager);
//
//        // Use icons for the tabs
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_white);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_location_on_white);

        // Add a page change listener on the viewPager to show/hide fab depending on which tab is selected
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Get the floating action button by ID and set the on click listener
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AddNewRestaurantActivity.class);
                startActivity(intent);
            }
        });

        // Get the restaurants ArrayList using an AsyncTask
        try
        {
            restaurantsArrayList = new GetRestaurantsAsyncTask(this).execute().get();
        } catch (java.lang.InterruptedException e)
        {

        } catch (java.util.concurrent.ExecutionException e)
        {

        }

        // Find the ListView layout for the restaurants
        listView = (ListView) findViewById(R.id.list);

        // Set up Bottom Navigation Bar to create new instances of the fragments when clicked
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                // For navigating to the MapFragment
                Fragment selectedFragment;

                // For navigating to the MainFragment and ProfileFragment (Main)
                Intent intent;

                switch (item.getItemId())
                {
                    // Restart the MainActivity
                    case R.id.action_list:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        // If the spinner has been set to a day of the week, add it as an extra
                        if (!dayString.isEmpty())
                            intent.putExtra("dayString", dayString);
                        startActivity(intent);
                        break;

                    // Start the MapFragment
                    case R.id.action_map:
                        selectedFragment = new MapFragment();
                        FragmentTransaction mapTransaction = getSupportFragmentManager().beginTransaction();
                        mapTransaction.replace(R.id.frame_layout, selectedFragment);
                        mapTransaction.commit();
                        break;

                    // Start the ProfileFragment
                    case R.id.action_profile:
                        selectedFragment = new ProfileFragment();
                        FragmentTransaction profileTransaction = getSupportFragmentManager().beginTransaction();
                        profileTransaction.replace(R.id.frame_layout, selectedFragment);
                        profileTransaction.commit();
                        break;
                }
                return true;
            }
        });

//        // Setup an Adapter to create a list item for each row of restaurant data in the Cursor.
//        // There is no restaurant data yet (until the loader finishes) so pass in null for the Cursor.
//        mCursorAdapter = new WhatsGoodCursorAdapter(this, null);
//        restaurantsListView.setAdapter(mCursorAdapter);
//
//        // Kick off the loader
//        getLoaderManager().initLoader(WHATSGOOD_LOADER, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isActive = false;
    }

    /**
     * Build and connect a Google API client
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the day of the week
     */
    private void setupSpinner()
    {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_days_of_the_week, R.layout.spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDaySpinner.setAdapter(daySpinnerAdapter);

        // If the day spinner has been set previously, set the spinner to the appropriate selection
        if (!dayString.isEmpty())
        {
            int spinnerPosition = daySpinnerAdapter.getPosition(dayString);
            mDaySpinner.setSelection(spinnerPosition);
        }
        // If the spinner has not yet been set, set it to the current day
        else
        {
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime());
            int position = daySpinnerAdapter.getPosition(weekDay);
            mDaySpinner.setSelection(position);
        }

        // Set the integer mSelected to the constant values
        mDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection))
                {
                    if (selection.equals(getString(R.string.day_monday)))
                        dayString = getString(R.string.day_monday);
                    else if (selection.equals(getString(R.string.day_tuesday)))
                        dayString = getString(R.string.day_tuesday);
                    else if (selection.equals(getString(R.string.day_wednesday)))
                        dayString = getString(R.string.day_wednesday);
                    else if (selection.equals(getString(R.string.day_thursday)))
                        dayString = getString(R.string.day_thursday);
                    else if (selection.equals(getString(R.string.day_friday)))
                        dayString = getString(R.string.day_friday);
                    else if (selection.equals(getString(R.string.day_saturday)))
                        dayString = getString(R.string.day_saturday);
                    else if (selection.equals(getString(R.string.day_sunday)))
                        dayString = getString(R.string.day_sunday);

                    restaurantAdapter = new RestaurantAdapter(MainActivity.this, restaurantsArrayList, R.color.colorBackground);
                    //listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(restaurantAdapter);

                    // If changing the day in the MapFragment, update the restaurant markers
                    if (MapFragment.isActive)
                    {
                        // Remove the current Markers
                        for (Marker m : MapFragment.markerArrayList)
                            m.remove();

                        // Add new MarkerOptions with the updated dayString
                        MapFragment.addMarkersForRestaurants(restaurantsArrayList);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sort_by_name)
        {
            // Sort the ArrayList of restaurants by name
            Collections.sort(restaurantsArrayList, new NameComparator());
            restaurantAdapter = new RestaurantAdapter(MainActivity.this, restaurantsArrayList, R.color.colorBackground);
            //listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(restaurantAdapter);
            return true;
        }
        if (id == R.id.action_sort_by_location)
        {
            // Sort the ArrayList of restaurants by location
            Collections.sort(restaurantsArrayList, new LocationComparator(mCurrentLocation));
            restaurantAdapter = new RestaurantAdapter(MainActivity.this, restaurantsArrayList, R.color.colorBackground);
            //listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(restaurantAdapter);
            return true;
        }
        if (id == R.id.action_reset)
        {
            try
            {
                restaurantsArrayList = new GetRestaurantsAsyncTask(this).execute().get();
            } catch (java.lang.InterruptedException e)
            {

            } catch (java.util.concurrent.ExecutionException e)
            {

            }

            restaurantAdapter = new RestaurantAdapter(MainActivity.this, restaurantsArrayList, R.color.colorBackground);
            //listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(restaurantAdapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(10); // 10 meters
        mLocationRequest.setInterval(1000); // Update location every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
        mCurrentLocation = location;

        Collections.sort(restaurantsArrayList, new LocationComparator(mCurrentLocation));

        // Create an {@link RestaurantAdapter}, whose data source is a list of {@link Restaurant}s.
        // The adapter knows how to create list items for each item in the list.
        restaurantAdapter = new RestaurantAdapter(MainActivity.this, restaurantsArrayList, R.color.colorBackground);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        //listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(restaurantAdapter);
    }

    /**
     * Methods to handle location permission upon first run
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        if (mGoogleApiClient == null)
                            buildGoogleApiClient();
                        //mGoogleMap.setMyLocationEnabled(true);
                    }
                } else
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

//    /**
//     * Database methods
//     */
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args)
//    {
//        // Define a projection that specifies the columns from the table we care about
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
//                RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS};
//
//        // This loader will execute the ContentProvider's query method on a background thread
//        return new CursorLoader(this,     // Parent activity context
//                RestaurantEntry.CONTENT_URI,    // Provider content URI to query
//                projection,
//                null,
//                null,
//                null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
//    {
//        // Update {@link WhatsGoodCursorAdapter} with this new cursor containing updated pet data
//        mCursorAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader)
//    {
//        // Callback called when the data needs to be deleted
//        mCursorAdapter.swapCursor(null);
//    }
}
