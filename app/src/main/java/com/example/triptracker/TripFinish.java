package com.example.triptracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

import androidx.annotation.NonNull;

/** <h1>FinishTrip: Class to let user check data before upload to the Realtime Database Firebase</h1>
 * <p>This class displays trip information to user and ask to save or cancel the upload of the trip<p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class TripFinish extends MainActivity {
    /**View element Button*/
    Button mCancelFinish, mSaveFinish;
    /**View element TextView*/
    TextView mFinishedTrip;
    /**Database reference*/
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_finish);

        /**link between view elements and code*/
        mCancelFinish = findViewById(R.id.cancelFinish);
        mSaveFinish = findViewById(R.id.saveFinish);
        mFinishedTrip = findViewById(R.id.finishedTrip);

        /**Get Firebase Database reference*/
        dbRef = FirebaseDatabase.getInstance().getReference();

        /**Initiate Shared preferences*/
        pref = TripFinish.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        /**get the String values from Shared Preferences*/
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        final String pReason = pref.getString("tripReason", "");
        final String pDestiny = pref.getString("tripDestiny", "");
        final String pDistance = getIntent().getExtras().get("distance").toString();

        /**Creates String and put trip info & Settings values in it*/
        String newData =
                "Name:" + pName + "\n" +
                "Reason: " + pReason + "\n" +
                "Destination: " + pDestiny + "\n" +
                "Company: " + pCompany + "\n" +
                "Car Reference: " + pCarRef + "\n" +
                "Autonomy: " + pkml + " km/l" + "\n" +
                "Fuel: " + pFuel + "\n" +
                "Distance: " + pDistance + " km";

        /**Display trip info & Settings values to the user*/
        mFinishedTrip.setText(newData);

        /**Set On CLick Listener in the CANCEL button*/
        mCancelFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                /**Call intent to send user to HOME screen*/
                intentBackToHome();
            }
        });

        /**Set On CLick listener to SAVE button*/
        mSaveFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Create String and add UID value*/
                final String uid = pref.getString("uid", "");
                /**Gets Database reference for the specific user*/
                String key = dbRef.child("trips").push().getKey();

                /**Instantiate calendar and get current data*/
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = sdf.format(calendar.getTime());

                /**Instantiate and send trip data to the TripData.class to be structured*/
                TripData newTrip = new TripData( pName, currentDate, pCompany, pCarRef, pkml, pFuel, pReason, pDestiny, pDistance);
               /**Map the database link to save data inside*/
                Map<String, Object> trip = newTrip.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                /**Saves data in the database*/
                childUpdates.put("/trips/" + uid + "/" + key, trip);

                /**Check if data uploaded with success*/
                dbRef.updateChildren(childUpdates)
                        /**On Success display to user*/
                        .addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText( getBaseContext(), "Trip Register successfully added!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        /**On Failure display to user*/
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getBaseContext(), "Trip Register Failed, Check you connection", Toast.LENGTH_SHORT).show();
                                Log.w("onFailure", "Failed to read value.");
                            }
                        });
                /**Call intent and send user to HOME screen*/
                intentBackToHome();
            }
        });
    }

}



