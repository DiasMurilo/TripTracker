package com.example.triptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class tripfinish extends MainActivity {

    Button mCancelFinish, mSaveFinish;
    TextView mFinishedTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripfinish);


        mCancelFinish = findViewById(R.id.cancelFinish);
        mSaveFinish = findViewById(R.id.saveFinish);
        mFinishedTrip = findViewById(R.id.finishedTrip);

        printUserTrip();

        mCancelFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });

        mSaveFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Trip Saved",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });

    }


    public void printUserTrip(){


        //check if there are already data and set it to Fields
        pref = tripfinish.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        final String pReason = pref.getString("tripReason", "");
        final String pDestiny = pref.getString("tripDestiny", "");

        String newData = "Name: " + pName + "\n" + "Reason: " + pReason + "\n" + "Destiny: " + pDestiny + "\n" + "Company: " + pCompany + "\n" + "Car Reference: " + pCarRef + "\n" + "km/l: " + pkml+ "\n" + "Fuel: " + pFuel  + "\n" + "Distance: " + "13.5 km" + "\n" + "Comsumed: " + "1.125L";
        mFinishedTrip.setText(newData);
    }




}
