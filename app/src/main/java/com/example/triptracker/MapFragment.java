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


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private SupportMapFragment supportMapFragment;

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Polyline polyline;
    private ArrayList<Location> locations;
    private ArrayList<LatLng> polylinePoints;

    private final int MIN_TIME = 1000; // 1 sec
    private final int MIN_DISTANCE = 1; // 1 meter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Start view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Start View
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frame);

        //Async map
        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locations = new ArrayList<>();
        polylinePoints = new ArrayList<>();

        getLocationUpdate();
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdate();
            }
        }
    }

    private void getLocationUpdate()
    {
        if (locationManager != null)
        {
            //Check permission
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    Toast.makeText(getContext(), "No provider enabled.", Toast.LENGTH_SHORT);
                }
            } else {
                // if not granted ask again
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            saveLocation(location);
        } else {
            Toast.makeText(getContext(), "No location", Toast.LENGTH_SHORT);
        }
    }

    private void saveLocation(Location location)
    {
        locations.add(location);
        polylinePoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
        updateUi();
    }

    private void updateUi()
    {
        if (mGoogleMap != null)
        {
            if (polyline != null) {
                polyline.setPoints(polylinePoints);
            } else {
                polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
            }

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(polylinePoints.get(polylinePoints.size() - 1), 17));
        }
    }

    public ArrayList<Location> getLocations()
    {
        return locations;
    }
}