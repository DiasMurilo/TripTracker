package com.example.triptracker;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/** <h1>TrackingTrip: Class to display Google Maps while tracking the trip</h1>
 * <p>This class displays the map while tracking the trip, here user can watch in realtime the route being created<p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

public class TrackingTrip extends Login {

    /**View element Button*/
    Button mCancelTracking, mFinishTrack;
    /**View element MapFragment*/
    MapFragment mMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_trip);

        /**Replace the initial fragment view with the fragment from MapFragment.Class*/
        mMapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, mMapFragment)
                .commit();

        /**link between view elements and code*/
        mCancelTracking = findViewById(R.id.cancelTracking);
        mFinishTrack = findViewById(R.id.finishTracking);

        /**On CLick cancel tracking trip and send user to home*/
        mCancelTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Trip Canceled",
                        Toast.LENGTH_SHORT).show();
                /**Call intent to Home*/
                intentBackToHome();
                // Call 3_HOME
            }
        });

        /**On CLick finish tracking trip and send user to TripFinish*/
        mFinishTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Track Finished",
                        Toast.LENGTH_SHORT).show();

                /**Create a float and get value from MapFragment from the Method*/
                float distance = calculateDistance(mMapFragment.getLocations());
                /**change meters to kilometers*/
                distance = distance/1000;
                /**Create intent to TripFinish screen*/
                Intent trackingTripToTripFinish = new Intent(getApplicationContext(), TripFinish.class);
                /**Get distance to pass vvalue in the intent*/
                trackingTripToTripFinish.putExtra("distance", distance);
                /**Call intent*/
                startActivity(trackingTripToTripFinish);
                // Call 6_TRIP_FINISH
            }
        });

    }

    /**Method to calculate distance from a array of locations
     * @param locations  Array of GPS points*/
    private float calculateDistance(ArrayList<Location> locations)
    {
        /**Create a float var to get distance*/
        float distance = 0;
        /**Loop to sum distance between all points*/
        for(int i = 1; i < locations.size(); i++)
        {
            /**Sum distance between two points*/
            distance += locations.get(i - 1).distanceTo(locations.get(i));
        }
        return distance;
    }
}
