package com.example.triptracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/** <h1>Mapfragment: Class that creates map view fragment and updates during the trip</h1>
 * <p>This class creates a fragment view with google maps, tracks the position and hold values in a string to calculate distance<p>
 * Citation:
 * Class contains code adapted from
 * URL: https://firebase.google.com/docs/database
 * Permission:  Creative Commons Attribution 4.0 License under the Apache 2.0 License
 * Retrieved on: 28 Feb 2021
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    /**Declare library method to support fragment*/
    private SupportMapFragment supportMapFragment;
    /**Declare library method to display map*/
    private GoogleMap mGoogleMap;
    /**Declare library method to track location*/
    private LocationManager locationManager;
    /**Declare library method to draw a polyline*/
    private Polyline polyline;
    /**Declare variable to hold GPS points*/
    private ArrayList<Location> locations;
    /**Declare variable to hold GPS points to draw the lines*/
    private ArrayList<LatLng> polylinePoints;

    /**Declare variable update GPS each 1000 miliseconds*/
    private final int MIN_TIME = 1000; // 1 sec
    /**Declare variable of minimum distance to update GPS location*/
    private final int MIN_DISTANCE = 1; // 1 meter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**Reference container layout to Start view*/
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        /**Reference the element view to display map*/
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frame);

        /**Starts the Async map in the fragment*/
        supportMapFragment.getMapAsync(this);

        /**References location manager and give context*/
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        /**Array list holding the GPS points*/
        locations = new ArrayList<>();
        /***Array list of GPS points of the polyline*/
        polylinePoints = new ArrayList<>();
        /**Get updated locations */
        getLocationUpdate();
        /**Returns the view*/
        return view;
    }

    /**Check if user gave the permissions*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdate();
            }
        }
    }

    /**Method that check permission and get locations update*/
    private void getLocationUpdate()
    {
        if (locationManager != null)
        {
            /**Check permissions need*/
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                /**Check permission for GPS_PROVIDER*/
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                }
                /**Check permission for NETWORK_PROVIDER*/
                else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                }
                /**Display to user*/
                else {
                    Toast.makeText(getContext(), "No provider enabled.", Toast.LENGTH_SHORT);
                }
            } else {
                /** if not granted ask again*/
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    /**Method to get google map On Map Ready and display*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    /**method On Location Change and calls saves location method*/
    @Override
    public void onLocationChanged(@NonNull Location location) {
        /** Get last known location. In some rare situations this can be null.*/
        if (location != null) {
            saveLocation(location);
        }  else {
            Toast.makeText(getContext(), "No location", Toast.LENGTH_SHORT);
        }
    }

    /**Method to save locations in the array and polyline*/
    private void saveLocation(Location location)
    {
        /**Add location to array*/
        locations.add(location);
        /**add reference points to polyline*/
        polylinePoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
        /**Calls method to update Ui*/
        updateUi();
    }

    /**Method to update User Interface "Ui"*/
    private void updateUi()
    {
        if (mGoogleMap != null)
        {
            /**Draw polyline*/
            if (polyline != null) {
                polyline.setPoints(polylinePoints);
            } else {
                polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
            }
            /**Map fragment zoom in the current location*/
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(polylinePoints.get(polylinePoints.size() - 1), 17));
        }
    }
    /**Method to return array of locations*/
    public ArrayList<Location> getLocations()
    {
        return locations;
    }
}