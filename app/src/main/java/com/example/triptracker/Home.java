package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends MainActivity {

    Button mButtonTrack, mButtonExpense, mButtonSettings, mButtonReport, mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mButtonTrack = findViewById(R.id.buttonTrack);
        mButtonExpense = findViewById(R.id.buttonExpense);
        mButtonSettings = findViewById(R.id.buttonSettings);
        mButtonReport = findViewById(R.id.buttonReport);
        mButtonLogout = findViewById(R.id.buttonLogout);

        mButtonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Confirm your data before start",
                        Toast.LENGTH_SHORT).show();
                Intent homeToStartTrip = new Intent(Home.this, StartTrip.class);
                startActivity(homeToStartTrip);
                // Call 4_START_TRIP
            }
        });

        mButtonExpense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Select a Trip to add",
                        Toast.LENGTH_SHORT).show();
                Intent homeToSelectTrip = new Intent(Home.this, SelectTrip.class);
                startActivity(homeToSelectTrip);
                // Call 7_SELECT_TRIP
            }
        });

       mButtonSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Settings",
                        Toast.LENGTH_SHORT).show();
                Intent homeToSettings = new Intent(Home.this, Settings.class);
                startActivity(homeToSettings);
                // Call 7_SELECT_TRIP
            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Select Dates",
                        Toast.LENGTH_LONG).show();
                Intent homeToSelectDate = new Intent(Home.this, SendReport.class);
                startActivity(homeToSelectDate);
                // Call 9_SELECT_DATE
            }
        });

        mButtonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signOut();
                Intent homeLogout = new Intent(Home.this, MainActivity.class);
                startActivity(homeLogout);
                // close connection and any life cicle need
            }
        });

        if (checkIfSettingsSaved()==false){
            intentBackToSettings();
        }

    }

}
