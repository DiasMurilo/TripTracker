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

public class SelectTrip extends AppCompatActivity{
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    RecyclerView recview;
    TripList adapter;

    Button sCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_trip);

        recview=(RecyclerView)findViewById(R.id.tripListView);
        recview.setLayoutManager(new LinearLayoutManager(this));

        sCancel = findViewById(R.id.cancelSelectTrip);

        String mUID = getUID();
        FirebaseRecyclerOptions<ModelTripData> options =
                new FirebaseRecyclerOptions.Builder<ModelTripData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("trips/" + mUID + "/"), ModelTripData.class)
                        .build();

        adapter=new TripList(options);
        recview.setAdapter(adapter);

        sCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent backToHomeFromSelect = new Intent(getApplicationContext(), Home.class);
                startActivity(backToHomeFromSelect);
                // Call 3_HOME
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private String getUID() {
        //check if there are already dates and set it to Fields, otherwise "Edit Text" fields are set empty
        pref = SelectTrip.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String uid = pref.getString("uid", "");
        return uid;
    }

}