package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class selecttrip extends AppCompatActivity {

    Button mCancelSelectTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttrip);



        mCancelSelectTrip = findViewById(R.id.cancelSelectTrip);

        mCancelSelectTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent selectTripToHomeScreen = new Intent(selecttrip.this, home.class);
                startActivity(selectTripToHomeScreen);
                // Call 3_HOME
            }
        });
    }
}