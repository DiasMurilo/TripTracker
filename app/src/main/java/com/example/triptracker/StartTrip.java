package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** <h1>StartTrip: Class to let user check data before track trip</h1>
 * <p>This class displays mandatory information to user and ask for trip references, also allows user to change data before start
 * <p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

public class StartTrip extends MainActivity{
    /**View element Button*/
    Button mCancelBefore, mSettingsBefore, mStartBefore;
    /**View element TextView*/
    TextView sName, sCompany, sCarRef, skml, sFuel;
    /**View element Edit Text*/
    EditText mFieldReason, mFieldDestiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_trip);

        /**link between view elements and code*/
        mCancelBefore = findViewById(R.id.cancelBefore);
        mSettingsBefore = findViewById(R.id.settingsBefore);
        mStartBefore = findViewById(R.id.saveImageViewer);
        sName = findViewById(R.id.textName);
        sCompany = findViewById(R.id.textCompany);
        sCarRef = findViewById(R.id.textCarRef);
        skml = findViewById(R.id.textkml);
        sFuel = findViewById(R.id.textFuel);
        mFieldReason = findViewById(R.id.field_Reason);
        mFieldDestiny = findViewById(R.id.field_Destiny);

        /** Updates fields with previous trip information*/
        updateFieldsStartTrip();

        /**On CLick send user to Settings*/
        mSettingsBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /**call intent to send user to settings*/
                intentBackToSettings();
            }
        });

        /**On CLick send user to Home*/
        mCancelBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /**call intent to send user to Home and cancel trip*/
                intentBackToHome();
            }
        });


        /**On click send user to Tracking trip screen*/
        mStartBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Starting Trip",
                        Toast.LENGTH_SHORT).show();
                /**Create intent*/
                Intent startTripToTrip = new Intent(StartTrip.this, TrackingTrip.class);
                /**Call intent*/
                startActivity(startTripToTrip);
                /**save Reason and Destination to outofill fields next time*/
                saveReasonDestination();
            }
        });


    }

    /**On resume update the fields*/
    @Override
    protected void onResume() {
        super.onResume();
        updateFieldsStartTrip();
    }

    /**Method to save Trip Reason and Destination*/
    private void saveReasonDestination(){
        /**Initiate shared preferences Editor*/
        editor = StartTrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        /**Get fields values to Strings*/
        String myTripReason = mFieldReason.getText().toString();
        String myDestiny = mFieldDestiny.getText().toString();
        /**Save Strings values in the Shared Preferences*/
        editor.putString("tripReason", myTripReason);
        editor.putString("tripDestiny", myDestiny);
        /**Do the commit*/
        editor.commit();
    }

    /**Method to update fields and TextView from Shared Preferences*/
    private void updateFieldsStartTrip() {
        /**Initiate Shared preferences*/
        pref = StartTrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**get the String values from Shared Preferences*/
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        final String pReason = pref.getString("tripReason", "");
        final String pDestiny = pref.getString("tripDestiny", "");
        /**Set values to Text Fields and textEditor*/
        sName.setText(pName);
        sCompany.setText(pCompany);
        sCarRef.setText(pCarRef);
        skml.setText(pkml);
        sFuel.setText(pFuel);
        mFieldReason.setText(pReason);
        mFieldDestiny.setText(pDestiny);
    }
}
