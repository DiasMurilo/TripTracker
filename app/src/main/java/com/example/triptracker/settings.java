package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    Button mCancelSettings, mSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mCancelSettings = findViewById(R.id.cancelSettings);
        mSaveSettings = findViewById(R.id.saveSettings);

        mCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent cancelSettingsToHomeScreen = new Intent(settings.this, home.class);
                startActivity(cancelSettingsToHomeScreen);
                // Call 3_HOME
            }
        });

        mSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Settings Saved",
                        Toast.LENGTH_SHORT).show();
                Intent saveSettingsToHomeScreen = new Intent(settings.this, home.class);
                startActivity(saveSettingsToHomeScreen);
                // Call 3_HOME
            }
        });
    }

}
