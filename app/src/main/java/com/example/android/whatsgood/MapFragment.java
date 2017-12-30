package com.example.android.whatsgood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    GoogleMap mGoogleMap;
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
     * ArrayList of marker options
     */
    ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();

    /**
     * ArrayList for the restaurants
     */
    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();


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
        View rootView = inflater.inflate(R.layout.activity_map, container, false);

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

        // Create a marker for each restaurant, with the special of today's day
        for (Restaurant r : restaurantsArrayList)
        {
            // Get the day of the week as a string
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime());

            // Create a marker
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(r.getLatitude(), r.getLongitude()))
                    .snippet(r.getSpecial(weekDay))
                    .title(r.getName());

            // Add the marker to the ArrayList
            markerOptionsArrayList.add(markerOptions);
        }
        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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

        // Add all markers in the array list
        for (MarkerOptions m : markerOptionsArrayList)
            mGoogleMap.addMarker(m);

//        LatLng penArgyl = new LatLng(40.8687, -75.2549);
//        CameraPosition target = CameraPosition.builder().target(penArgyl).zoom(12).build();
//        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null);
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

        // Get the current location when the map connects and fly to it
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null)
        {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraPosition target = CameraPosition.builder().target(latLng).zoom(12).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null);
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
    public void onStop()
    {
        // Disconnect the client
        mGoogleApiClient.disconnect();

        super.onStop();
    }

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
}
