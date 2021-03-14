package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class tripfinish extends AppCompatActivity {

    Button mCancelFinish, mSaveFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripfinish);


        mCancelFinish = findViewById(R.id.cancelFinish);
        mSaveFinish = findViewById(R.id.saveFinish);

        mCancelFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent cancelTrackFinishToHome = new Intent(tripfinish.this, home.class);
                startActivity(cancelTrackFinishToHome);
                // Call 3_HOME
            }
        });

        mSaveFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Trip Saved",
                        Toast.LENGTH_SHORT).show();
                Intent saveTrackFinishToHome = new Intent(tripfinish.this, home.class);
                startActivity(saveTrackFinishToHome);
                // Call 3_HOME
            }
        });

    }
}
