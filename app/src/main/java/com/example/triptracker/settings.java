package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    Button mCancelSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mCancelSet = findViewById(R.id.cancelImageViewer);

        mCancelSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent settingsToHomeScreen = new Intent(settings.this, home.class);
                startActivity(settingsToHomeScreen);
                // Call 3_HOME
            }
        });
    }

}
