package com.example.triptracker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/** <h1>SelectTrip: Class that displays recycler view list to the user</h1>
 * <p>This class displays the recycler view to the user after the elements being created<p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */

public class SelectTrip extends AppCompatActivity{
    /**Initiate shared preferences Editor*/
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    /**Declare shared pref var itself*/
    SharedPreferences pref;
    /**Declare recycler view from library*/
    RecyclerView recview;
    /**Declare TripList.class for recycler view*/
    TripList adapter;
    /**View element Button*/
    Button sCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_trip);

        /**link between view elements and code*/
        recview=(RecyclerView)findViewById(R.id.tripListView);
        recview.setLayoutManager(new LinearLayoutManager(this));
        sCancel = findViewById(R.id.cancelSelectTrip);

        /**Create string to hold UID reference*/
        String mUID = getUID();
        /**Point to the specific user data in the Firebase database*/
        FirebaseRecyclerOptions<ModelTripData> options =
                new FirebaseRecyclerOptions.Builder<ModelTripData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("trips/" + mUID + "/"), ModelTripData.class)
                        .build();
        /**Send to the TripList.class to structure data and create the view holder*/
        adapter=new TripList(options);
        recview.setAdapter(adapter);

        /**Se On Click Listener to CANCEL button*/
        sCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                /**Create intent to send user to HOME screen*/
                Intent backToHomeFromSelect = new Intent(getApplicationContext(), Home.class);
                /**Call intent*/
                startActivity(backToHomeFromSelect);
            }
        });
    }

    /**OnStart check the database to create view holders*/
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**OnStop stop check data in the database*/
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**Method to get UID reference*/
    private String getUID() {
        /**Initiate Shared preferences*/
        pref = SelectTrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**Get user reference from user preferences */
        final String uid = pref.getString("uid", "");
        return uid;
    }

}