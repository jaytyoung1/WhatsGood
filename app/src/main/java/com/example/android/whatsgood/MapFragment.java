package com.example.android.whatsgood;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/9/2017.
 * A simple {@link Fragment} subclass
 */

public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    /**
     * Google Map variables
     */
    GoogleApiClient mGoogleApiClient;
    public static GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    boolean mapReady = false;

    /**
     * Location variables
     */
    LocationRequest mLocationRequest;
    Location mLastLocation;


    /**
     * Marker for user's current location
     */
    Marker mCurrLocationMarker;

    /**
     * ArrayList of marker options (Accessed in MainActivity if user changes day spinner in MapFragment
     */
    public static ArrayList<MarkerOptions> markerOptionsArrayList;

    /**
     * ArrayList of Markers. Markers have a method to remove (Used in MainActivity when user changes day in spinner)
     */
    public static ArrayList<Marker> markerArrayList;

    /**
     * ArrayList for the restaurants
     */
    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

    /**
     * Restaurant object for the restaurant that was clicked
     */
    Restaurant restaurantClicked = null;

    /**
     * Boolean set when MapFragment is active or not active (see onPause() and onResume())
     */
    public static boolean isActive;

    /**
     * Boolean used to determine if map should fly to a restaurant or current location
     */
    private boolean isFlyingToRestaurant;

    /**
     * Empty Constructor
     */
    public MapFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Hide the FAB by first getting the context of the MainActivity
        Context mainContext = inflater.getContext();
        FloatingActionButton fab = ((Activity) mainContext).findViewById(R.id.fab);
        fab.hide();

        // Show the toolbar
        Toolbar toolbar = ((Activity) mainContext).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

        // Make the listView invisible
        ListView listView = ((Activity) mainContext).findViewById(R.id.list);
        listView.setVisibility(View.INVISIBLE);

        // If a restaurant was clicked, get it in order to fly to it
        try
        {
            restaurantClicked = (Restaurant) getArguments().getSerializable("restaurantClicked");
            isFlyingToRestaurant = true;
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        // Map button sets map type to normal
        Button buttonMap = (Button) rootView.findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        // Satellite button sets map type to satellite
        Button buttonSatellite = (Button) rootView.findViewById(R.id.button_satellite);
        buttonSatellite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        // Hybrid button sets map type to hybrid
        Button buttonHybrid = (Button) rootView.findViewById(R.id.button_hybrid);
        buttonHybrid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        // Get the map fragment
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Get the restaurants
        try
        {
            restaurantsArrayList = new GetRestaurantsAsyncTask(getContext()).execute().get();
        } catch (java.lang.InterruptedException e)
        {

        } catch (java.util.concurrent.ExecutionException e)
        {

        }
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isActive = true;
        //mGoogleApiClient.connect();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isActive = false;

        //stop location updates when Activity is no longer active
//        if (mGoogleApiClient != null)
//        {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mapReady = true;
        mGoogleMap = map;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else
            {
                //Request Location Permission
                checkLocationPermission();
            }
        } else
        {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        addMarkersForRestaurants(restaurantsArrayList);
    }

    /**
     * Adds MarkerOptions for each restaurant. Method is public static because it is called in the
     * MainActivity if the user changes the day spinner while in the MapFragment
     *
     * @param arrayList ArrayList of restaurants
     */
    public static void addMarkersForRestaurants(ArrayList<Restaurant> arrayList)
    {
        markerOptionsArrayList = new ArrayList<>();
        markerArrayList = new ArrayList<>();

        // Create a marker for each restaurant, with the special of today's day
        for (Restaurant r : arrayList)
        {
            // Create a marker
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(r.getLatitude(), r.getLongitude()))
                    .snippet(r.getSpecial(MainActivity.dayString))
                    .title(r.getName());

            // Add the marker to the ArrayList
            markerOptionsArrayList.add(markerOptions);
        }

        // Add all markers in the array list
        for (MarkerOptions m : markerOptionsArrayList)
        {
            Marker marker = mGoogleMap.addMarker(m);
            markerArrayList.add(marker);
        }
    }

    /**
     * Build and connect a Google API client
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        // If the user clicked a miles away textView, either on the ListView item or in the RestaurantActivity, fly to the restaurant
        if (isFlyingToRestaurant)
        {
            // Get the latitude and longitude of the restaurant that was clicked
            LatLng restaurantLatLng = new LatLng(restaurantClicked.getLatitude(), restaurantClicked.getLongitude());

            // Get the camera position of the restaurant that was clicked
            CameraPosition restaurantCameraPosition = getCameraPosition(restaurantLatLng, 17);

            // Fly to the restaurant that was clicked
            flyTo(restaurantCameraPosition);

            // Reset the boolean
            isFlyingToRestaurant = false;
        }
        // Fly to the user's current location
        else
        {
            // Get the current location when the map connects and fly to it
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (currentLocation != null)
            {
                // Get the latitude and longitude of the user's current location
                LatLng currentPositionLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                // Get the camera position of the user's current location
                CameraPosition currentLocationCameraPosition = getCameraPosition(currentPositionLatLng, 12);

                // Fly to current location
                flyTo(currentLocationCameraPosition);
                isFlyingToRestaurant = false;
            }
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

//    @Override
//    public void onStop()
//    {
//        // Disconnect the client
//        mGoogleApiClient.disconnect();
//
//        super.onStop();
//    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null)
            mCurrLocationMarker.remove();

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.visible(false);
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Get a CameraPosition based on latitude and longitude
     */
    private CameraPosition getCameraPosition(LatLng latLng, int zoom)
    {
        return CameraPosition.builder().target(latLng).zoom(zoom).build();
    }

    /**
     * Fly to the cameraPosition by animating the camera
     */
    private void flyTo(CameraPosition target)
    {
        // Use moveCamera, not animateCamera to jump right to target
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
