package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class trackingtrip extends MainActivity {

    Button mCancelTracking, mFinishTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackingtrip);

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
                Intent trackingTripToTripFinish = new Intent(trackingtrip.this, tripfinish.class);
                startActivity(trackingTripToTripFinish);
                // Call 6_TRIP_FINISH
            }
        });

    }
}
