package com.example.android.whatsgood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whatsgood.data.CreateRestaurants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jyoun on 11/15/2017.
 */

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    ArrayList<MarkerOptions> mMarkerOptionsArrayList = new ArrayList<>();
    ArrayList<Restaurant> mRestaurants;
    boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button buttonMap = (Button) findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        Button buttonSatellite = (Button) findViewById(R.id.button_satellite);
        buttonSatellite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        Button buttonHybrid = (Button) findViewById(R.id.button_hybrid);
        buttonHybrid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Create an instance of the object that creates restaurants
        CreateRestaurants createRestaurantsObject = new CreateRestaurants();

        // Get it's ArrayList of restaurants
        mRestaurants = createRestaurantsObject.getArrayList();

        for (Restaurant r : mRestaurants)
        {
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(r.getLatitude(), r.getLongitude()))
                    .snippet(r.getSpecial(weekDay))
                    .title(r.getName());

            mMarkerOptionsArrayList.add(markerOptions);
        }
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
    public void onMapReady(GoogleMap googleMap)
    {
        mapReady = true;
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,
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

        for (MarkerOptions m : mMarkerOptionsArrayList)
            mGoogleMap.addMarker(m);
    }

    protected synchronized void buildGoogleApiClient()
    {
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
                                ActivityCompat.requestPermissions(MapActivity.this,
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
                    if (ContextCompat.checkSelfPermission(this,
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
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
