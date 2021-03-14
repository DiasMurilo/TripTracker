package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

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
                Intent starttrip = new Intent(home.this, starttrip.class);
                startActivity(starttrip);
                // Call 4_START_TRIP
            }
        });

        mButtonExpense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Select a Trip to add",
                        Toast.LENGTH_SHORT).show();
                Intent expensescreen = new Intent(home.this, selecttrip.class);
                startActivity(expensescreen);
                // Call 7_SELECT_TRIP
            }
        });

       mButtonSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Settings",
                        Toast.LENGTH_SHORT).show();
                Intent settingsscreen = new Intent(home.this, settings.class);
                startActivity(settingsscreen);
                // Call 7_SELECT_TRIP
            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Select Dates",
                        Toast.LENGTH_LONG).show();
                Intent reportscreen = new Intent(home.this, selectdate.class);
                startActivity(reportscreen);
                // Call 9_SELECT_DATE
            }
        });

        mButtonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logout",
                        Toast.LENGTH_SHORT).show();
                Intent homescreen = new Intent(home.this, MainActivity.class);
                startActivity(homescreen);
                // close connection and any life cicle need
            }
        });

    }
}
