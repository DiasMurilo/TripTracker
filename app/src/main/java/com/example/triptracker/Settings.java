package com.example.triptracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/** <h1>Settings: Class automatic called at first time that user login and any time that wish to change mandatory information</h1>
 * <p>This class displays and allow user to change mandatory data, this data is saved at shared preferences to be accessed by other classes<p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class Settings extends Login {
    /**Initiate shared preferences Editor*/
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    /**Declare Editor to edit shared pref data*/
    SharedPreferences.Editor editor;
    /**Declare shared pref var itself*/
    SharedPreferences pref;

    /**View element Button*/
    Button mCancelSettings, mSaveSettings;
    /**View element EditText*/
    EditText mName, mCompany, mCarRef, mkml, mFuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        /**link between view elements and code*/
        mName = findViewById(R.id.name);
        mCompany = findViewById(R.id.company);
        mCarRef = findViewById(R.id.carref);
        mkml = findViewById(R.id.kml);
        mFuel = findViewById(R.id.fuel);
        mCancelSettings = findViewById(R.id.cancelSettings);
        mSaveSettings = findViewById(R.id.saveSettings);

        /**Check for previous information in Shared Prefs to set in the fields*/
        updateFields();

        /**Set On Click Listener to SAVE data on user click*/
        mSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /**Check if all mandatory fields filled*/
                if (checkFields() == true){
                    /**Saves the data in Shared Pref*/
                    savePreferences();
                    /**Display action at Screen*/
                    Toast.makeText(getApplicationContext(), "Settings Saved",
                            Toast.LENGTH_SHORT).show();
                    /**Finish screen sending user to previous screen*/
                    Settings.this.finish();
                }
            }
        });
        /**Set On Click Listener to CANCEL on user click*/
        mCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /**Check if all mandatory fields filled*/
                if(checkFields() == true) {
                    /**Display action at Screen*/
                    Toast.makeText(getApplicationContext(), "Canceled",
                            Toast.LENGTH_SHORT).show();
                    /**Finish screen sending user to previous screen*/
                    Settings.this.finish();
                }
            }
        });
    }

    /**Method to check if all mandatory fields are filled
     * note: all fields in the screen are mandatory*/
    private boolean checkFields(){
        /**Initiate Shared preferences*/
        pref = Settings.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**Create string and get from String value from textview*/
        String iName = mName.getText().toString();
        String iCompany = mCompany.getText().toString();
        String iCarref = mCarRef.getText().toString();
        String ikml = mkml.getText().toString();
        String iFuel = mFuel.getText().toString();
        /**Check if  field Name is empty*/
        if (iName.equals("")) {
            Toast.makeText(Settings.this, "Name is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**Check if  field Company is empty*/
        else if (iCompany.equals("")) {
            Toast.makeText(Settings.this, "Company is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**Check if  field Car Reference is empty*/
        else if (iCarref.equals("")) {
            Toast.makeText(Settings.this, "Car Reference is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**Check if  field Autonomy is empty*/
        else if (ikml.equals("")) {
            Toast.makeText(Settings.this, "km/l is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**Check if  field Fuel is empty*/
        else if (iFuel.equals("")) {
            Toast.makeText(Settings.this, "Car Reference is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        /**If all fields filled so return true*/
        else {
            Toast.makeText(Settings.this, "Settings saved", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    /**Method to save Shared Preferences*/
    private void savePreferences(){
        /**Initiate shared preferences Editor*/
        editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        /**Get fields values to Strings*/
        String oName = mName.getText().toString();
        String oCompany = mCompany.getText().toString();
        String oCarRef = mCarRef.getText().toString();
        String okml = mkml.getText().toString();
        String oFuel = mFuel.getText().toString();
        /**Save Strings values in the Shared Preferences*/
        editor.putString("name", oName);
        editor.putString("company", oCompany);
        editor.putString("carref", oCarRef);
        editor.putString("kml", okml);
        editor.putString("fuel", oFuel);
        /**Do the commit*/
        editor.commit();
    }
    /**Method to update fields and TextView from Shared Preferences*/
    private void updateFields() {
        /**Initiate Shared preferences*/
        pref = Settings.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**Get fields values to Strings*/
        final String pName = pref.getString("name", "");
        final String pCompany = pref.getString("company", "");
        final String pCarRef = pref.getString("carref", "");
        final String pkml = pref.getString("kml", "");
        final String pFuel = pref.getString("fuel", "");
        /**Set values to Text Fields and textEditor*/
        mName.setText(pName);
        mCompany.setText(pCompany);
        mCarRef.setText(pCarRef);
        mkml.setText(pkml);
        mFuel.setText(pFuel);
    }

    /**Method that block user from exiting Settings without fill the fields and display toast message*/
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Setting fields mandatory", Toast.LENGTH_SHORT).show();
    }
}
