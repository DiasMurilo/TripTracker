package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class starttrip extends MainActivity{

    Button mCancelBefore, mSettingsBefore, mStartBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starttrip);


        mCancelBefore = findViewById(R.id.cancelBefore);
        mSettingsBefore = findViewById(R.id.settingsBefore);
        mStartBefore = findViewById(R.id.startBefore);

        mSettingsBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Settings",
                        Toast.LENGTH_SHORT).show();
                intentBackToSettings();
                // Call 2_SETTINGS
            }
        });

        mCancelBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });

        mStartBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Starting Trip",
                        Toast.LENGTH_SHORT).show();
                Intent startTripToTrip = new Intent(starttrip.this, trackingtrip.class);
                startActivity(startTripToTrip);
                // Call 5_TRACKING_TRIP
            }
        });


    }
}
