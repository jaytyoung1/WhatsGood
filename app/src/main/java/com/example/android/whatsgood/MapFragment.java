package com.example.android.whatsgood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jyoun on 11/9/2017.
 * A simple {@link Fragment} subclass
 */

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    GoogleMap mMap;
    boolean mapReady = false;

    ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();

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
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        Button buttonSatellite = (Button) rootView.findViewById(R.id.button_satellite);
        buttonSatellite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        Button buttonHybrid = (Button) rootView.findViewById(R.id.button_hybrid);
        buttonHybrid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        for (Restaurant r : restaurants)
//        {
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(new LatLng(r.getLatitude(), r.getLongitude()))
//                    .snippet(r.getAddress())
//                    .title(r.getName());
//
//            markerOptionsArrayList.add(markerOptions);
//        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mapReady = true;
        mMap = map;

        for (MarkerOptions m : markerOptionsArrayList)
            mMap.addMarker(m);

        LatLng penArgyl = new LatLng(40.8687, -75.2549);
        CameraPosition target = CameraPosition.builder().target(penArgyl).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null);
    }
}
