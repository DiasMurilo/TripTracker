package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class starttrip extends MainActivity{

    Button mCancelBefore, mSettingsBefore, mStartBefore;
    TextView sName, sCompany, sCarRef, skml, sFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starttrip);


        mCancelBefore = findViewById(R.id.cancelBefore);
        mSettingsBefore = findViewById(R.id.settingsBefore);
        mStartBefore = findViewById(R.id.startBefore);
        sName = findViewById(R.id.textName);
        sCompany = findViewById(R.id.textCompany);
        sCarRef = findViewById(R.id.textCarRef);
        skml = findViewById(R.id.textkml);
        sFuel = findViewById(R.id.textFuel);

        updateFieldsStartTrip();

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

    @Override
    protected void onResume() {
        super.onResume();
        updateFieldsStartTrip();
    }

    private void updateFieldsStartTrip() {
        //check if there are already data and set it to Fields
        pref = starttrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        sName.setText(pName);
        sCompany.setText(pCompany);
        sCarRef.setText(pCarRef);
        skml.setText(pkml);
        sFuel.setText(pFuel);
    }
}
