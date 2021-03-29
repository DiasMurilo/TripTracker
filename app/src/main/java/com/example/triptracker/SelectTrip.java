package com.example.triptracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class SelectTrip extends AppCompatActivity{

    //private static final String TAG = "SelectTrip";
    //private Context mNewContext;



    //add array for each item
    private ArrayList<String> mTripN = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    private ArrayList<String> mReason = new ArrayList<>();
    private ArrayList<String> mDestination = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttrip);


        // add link to pictures inside of Firebase bucket
        mTripN.add("10"); mDate.add("01/10/2015"); mReason.add("Class SDA"); mDestination.add("DCU");
        mTripN.add("20"); mDate.add("02/08/2009"); mReason.add("Class CA"); mDestination.add("DCU");
        mTripN.add("30"); mDate.add("03/05/2006"); mReason.add("Grab a coffee"); mDestination.add("DCU");
        mTripN.add("40"); mDate.add("04/07/2005"); mReason.add("Study group"); mDestination.add("DCU");
        mTripN.add("50"); mDate.add("05/10/2000"); mReason.add("SDA Class"); mDestination.add("DCU");
        mTripN.add("60"); mDate.add("06/12/2020"); mReason.add("CA Class"); mDestination.add("DCU");
        mTripN.add("70"); mDate.add("07/09/2021"); mReason.add("Career event"); mDestination.add("DCU");
        mTripN.add("80"); mDate.add("08/10/2023"); mReason.add("SDA Class"); mDestination.add("DCU");
        mTripN.add("100"); mDate.add("09/6/2022"); mReason.add("Play rugby"); mDestination.add("DCU");
        mTripN.add("1001"); mDate.add("10/3/2021"); mReason.add("CA Class"); mDestination.add("DCU");


        //SelectTrip recyclerViewAdapter = new SelectTrip(getContext(), mTripN, mDate, mReason, mDestination, SelectTrip.class);

        RecyclerView recyclerView = findViewById(R.id.tripListView);
        TripList myAdapter = new TripList(this, mTripN, mDate, mReason, mDestination);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }














}