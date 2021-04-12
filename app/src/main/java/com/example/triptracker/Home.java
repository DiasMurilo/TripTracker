package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/** <h1>Home: Class that directs user to multiple actions</h1>
 * <p>This activity is the link between different actions that user can do:
 * - TRACK A TRIP
 * - ADD TRIP EXPENSE
 * - SETTINGS
 * - SEND REPORT
 * - LOGOUT
 * <p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

public class Home extends MainActivity {

    /**View elements Buttons*/
    Button mButtonTrack, mButtonExpense, mButtonSettings, mButtonReport, mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        /**link between view elements and code*/
        mButtonTrack = findViewById(R.id.buttonTrack);
        mButtonExpense = findViewById(R.id.buttonExpense);
        mButtonSettings = findViewById(R.id.buttonSettings);
        mButtonReport = findViewById(R.id.buttonReport);
        mButtonLogout = findViewById(R.id.buttonLogout);

        /**On CLick send user to Start trip*/
        mButtonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /**Prepare and call intent to Start Trip Screen*/
                Intent homeToStartTrip = new Intent(Home.this, StartTrip.class);
                startActivity(homeToStartTrip);
            }
        });

        /**On CLick send user to Select trip Screen*/
        mButtonExpense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /**Prepare and call intent to Start Trip Screen*/
                Intent homeToSelectTrip = new Intent(Home.this, SelectTrip.class);
                startActivity(homeToSelectTrip);
                // Call 7_SELECT_TRIP
            }
        });

        /**On CLick send user to Settings*/
       mButtonSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent homeToSettings = new Intent(Home.this, Settings.class);
                startActivity(homeToSettings);
                // Call 7_SELECT_TRIP
            }
        });

        /**On CLick send user to Send Report Screen*/
        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Prepare and call intent to Send Report Screen*/
                Intent homeToSelectDate = new Intent(Home.this, SendReport.class);
                startActivity(homeToSelectDate);
                // Call 9_SELECT_DATE
            }
        });

        /**On CLick send user to login screen*/
        mButtonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /** close connection and any life circle need*/
                signOut();
                Intent homeLogout = new Intent(Home.this, MainActivity.class);
                /**Call intent to send user back to login screen*/
                startActivity(homeLogout);
            }
        });

        /**In the first login user is directed to Settings to fill mandatory fields*/
        if (checkIfSettingsSaved()==false){
            intentBackToSettings();
        }

    }

}
