package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class starttrip extends AppCompatActivity {

    Button mCancelBefore, mSettingsBefore, mStartBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starttrip);


        mCancelBefore = findViewById(R.id.cancelBefore);
        mSettingsBefore = findViewById(R.id.bostaPreta);
        mStartBefore = findViewById(R.id.startBefore);

        mSettingsBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Settings",
                        Toast.LENGTH_SHORT).show();
                Intent starttriptosettings = new Intent(starttrip.this, settings.class);
                startActivity(starttriptosettings);
                // Call 2_SETTINGS
            }
        });

        mCancelBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent starttriptohome = new Intent(starttrip.this, home.class);
                startActivity(starttriptohome);
                // Call 3_HOME
            }
        });

        mStartBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Starting Trip",
                        Toast.LENGTH_SHORT).show();
                Intent starttriptotrip = new Intent(starttrip.this, trackingtrip.class);
                startActivity(starttriptotrip);
                // Call 5_TRACKING_TRIP
            }
        });


    }
}
