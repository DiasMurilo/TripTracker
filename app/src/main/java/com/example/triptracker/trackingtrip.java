package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class trackingtrip extends AppCompatActivity {

    Button mCancelTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackingtrip);

        mCancelTracking = findViewById(R.id.cancelTracking);

        mCancelTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent trackingtriptohome = new Intent(trackingtrip.this, home.class);
                startActivity(trackingtriptohome);
                // Call 3_HOME
            }
        });
    }
}
