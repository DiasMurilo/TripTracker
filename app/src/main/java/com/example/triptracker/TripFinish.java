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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

import androidx.annotation.NonNull;

public class TripFinish extends MainActivity {

    Button mCancelFinish, mSaveFinish;
    TextView mFinishedTrip;

    DatabaseReference dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripfinish);

        mCancelFinish = findViewById(R.id.cancelFinish);
        mSaveFinish = findViewById(R.id.saveFinish);
        mFinishedTrip = findViewById(R.id.finishedTrip);

        dbRef = FirebaseDatabase.getInstance().getReference();

        //Retrieve User Preferences
        pref = TripFinish.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        //final String pEmail = pref.getString("email", "");
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        final String pReason = pref.getString("tripReason", "");
        final String pDestiny = pref.getString("tripDestiny", "");
        final String pDistance = pref.getString("distance", "");
        //final String pConsumed = pref.getString("consumed", "");

        // Print trip data to user
        String newData = "Name: " + pName + "\n" + "Reason: " + pReason + "\n" + "Destiny: " + pDestiny + "\n" + "Company: " + pCompany + "\n" + "Car Reference: " + pCarRef + "\n" + "km/l: " + pkml + "\n" + "Fuel: " + pFuel + "\n" + "Distance: " + pDistance + "\n";
        mFinishedTrip.setText(newData);

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
                //Toast.makeText(getApplicationContext(), "Trip Saved",Toast.LENGTH_SHORT).show();

                //public tripData(String email, String name, Date date, String company, String carRef, float kml, String fuel, String reason, String destiny, float distance, float fuelCons) {
                //___________________________________________________________

                final String uid = pref.getString("uid", "");
                String key = dbRef.child("trips").push().getKey();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                String currentDate = sdf.format(calendar.getTime());


                TripData newTrip = new TripData( pName, currentDate, pCompany, pCarRef, pkml, pFuel, pReason, pDestiny, pDistance);
                Map<String, Object> trip = newTrip.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/trips/" + uid + "/" + key, trip);


                dbRef.updateChildren(childUpdates)
                        .addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText( getBaseContext(), "Trip Register successfully added!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getBaseContext(), "Trip Register Failed, Check you connection", Toast.LENGTH_SHORT).show();
                                Log.w("onFailure", "Failed to read value.");
                            }
                        });

                //____________________________________________________________________________________


                // Write a message to the database


                intentBackToHome();
                // Call 3_HOME
            }
        });
    }

}



