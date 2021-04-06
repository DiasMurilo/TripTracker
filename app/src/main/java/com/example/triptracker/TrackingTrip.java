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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class TrackingTrip extends MainActivity {

    //
    Button mCancelTracking, mFinishTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackingtrip);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
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
                //Saving data as a test
                saveDistanceConsume();

                Intent trackingTripToTripFinish = new Intent(getApplicationContext(), TripFinish.class);
                startActivity(trackingTripToTripFinish);
                // Call 6_TRIP_FINISH
            }
        });

    }

    private void saveDistanceConsume(){
        //save trip values
        editor = TrackingTrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String myDistance = "13.5";
        String myConsumed = "16.875";
        editor.putString("distance", myDistance);
        editor.putString("consumed", myConsumed);
        editor.commit();
    }
}
