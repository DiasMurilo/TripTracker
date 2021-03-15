package com.example.triptracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class settings extends MainActivity {

    // Class wide variables
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    Button mCancelSettings, mSaveSettings;
    EditText mName, mCompany, mCarRef, mkml, mFuel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mName = findViewById(R.id.name);
        mCompany = findViewById(R.id.company);
        mCarRef = findViewById(R.id.carref);
        mkml = findViewById(R.id.kml);
        mFuel = findViewById(R.id.fuel);
        mCancelSettings = findViewById(R.id.cancelSettings);
        mSaveSettings = findViewById(R.id.saveSettings);

        updateFields();
        mSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (checkFields() == true){
                    savePreferences();
                    settings.this.finish();
                }
            }
        });
        mCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(checkFields() == true) {
                    Toast.makeText(getApplicationContext(), "Canceled",
                            Toast.LENGTH_SHORT).show();
                    settings.this.finish();
                }
            }
        });
    }

    private boolean checkFields(){
        String iName = mName.getText().toString();
        String iCompany = mCompany.getText().toString();
        String iCarref = mCarRef.getText().toString();
        String ikml = mkml.getText().toString();
        String iFuel = mFuel.getText().toString();
        if (iName.equals("")) {
            Toast.makeText(settings.this, "Name is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (iCompany.equals("")) {
            Toast.makeText(settings.this, "Company is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (iCarref.equals("")) {
            Toast.makeText(settings.this, "Car Reference is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (ikml.equals("")) {
            Toast.makeText(settings.this, "km/l is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (iFuel.equals("")) {
            Toast.makeText(settings.this, "Car Reference is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Toast.makeText(settings.this, "Settings saved", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void savePreferences(){
        //save the values in the Preferences
        editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String oName = mName.getText().toString();
        String oCompany = mCompany.getText().toString();
        String oCarRef = mCarRef.getText().toString();
        String okml = mkml.getText().toString();
        String oFuel = mFuel.getText().toString();
        editor.putString("name", oName);
        editor.putString("company", oCompany);
        editor.putString("carref", oCarRef);
        editor.putString("kml", okml);
        editor.putString("fuel", oFuel);
        editor.commit();
    }

    private void updateFields() {
        //check if there are already data and set it to Fields
        pref = settings.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        mName.setText(pName);
        mCompany.setText(pCompany);
        mCarRef.setText(pCarRef);
        mkml.setText(pkml);
        mFuel.setText(pFuel);
    }

    @Override
    public void onBackPressed() {
    // Block user from exiting Settings without filling fields
    }
}
