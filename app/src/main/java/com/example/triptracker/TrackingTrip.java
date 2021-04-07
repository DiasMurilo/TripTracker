package com.example.triptracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class TrackingTrip extends MainActivity {

    //
    Button mCancelTracking, mFinishTrack;
    MapFragment mMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackingtrip);

        mMapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mMapFragment)
                .commit();

        mCancelTracking = findViewById(R.id.cancelTracking);
        mFinishTrack = findViewById(R.id.finishTracking);

        mCancelTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });

        mFinishTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Track Finished",
                        Toast.LENGTH_SHORT).show();


                float distance = calculateDistance(mMapFragment.getLocations());
                distance = distance/1000; // change meters to kilometers

                Intent trackingTripToTripFinish = new Intent(getApplicationContext(), TripFinish.class);
                trackingTripToTripFinish.putExtra("distance", distance);
                startActivity(trackingTripToTripFinish);
                // Call 6_TRIP_FINISH
            }
        });

    }

    private float calculateDistance(ArrayList<Location> locations)
    {
        float distance = 0;
        for(int i = 1; i < locations.size(); i++)
        {
            distance += locations.get(i - 1).distanceTo(locations.get(i));
        }

        return distance;
    }
}
