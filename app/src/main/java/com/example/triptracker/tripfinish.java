package com.example.triptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class tripfinish extends MainActivity {

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
}
