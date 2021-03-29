package com.example.triptracker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TripList extends RecyclerView.Adapter<TripList.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context mNewContext;


    //add arrays
    private ArrayList<String> mTripNs;
    private ArrayList<String> mDates;
    private ArrayList<String> mReasons;
    private ArrayList<String> mDestinations;


    TripList(Context mNewContext, ArrayList<String> mTripN, ArrayList<String> mDate, ArrayList<String> mReason, ArrayList<String> mDestination) {
        this.mNewContext = mNewContext;
        this.mTripNs = mTripN;
        this.mDates = mDate;
        this.mReasons = mReason;
        this.mDestinations = mDestination;
    }

    //declare methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_trip, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");

        // Get information about book's Author and Title from arrays and set in the viewHolder
        viewHolder.tripText.setText(mTripNs.get(position));
        viewHolder.dateText.setText(mDates.get(position));
        viewHolder.reasonText.setText(mReasons.get(position));
        viewHolder.destinationText.setText(mDestinations.get(position));

        Log.d(TAG, "onBindViewHolder: was called at " + mTripNs.get(position));
    }

    @Override
    public int getItemCount() {
        return mTripNs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripText, dateText, reasonText, destinationText;
        RelativeLayout itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //grab the image, the text and the layout id's
            tripText = itemView.findViewById(R.id.select_tripref);
            dateText = itemView.findViewById(R.id.select_date);
            reasonText = itemView.findViewById(R.id.select_reason);
            destinationText = itemView.findViewById(R.id.select_destination);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }


}


        //view holder class for recycler_list_item.xml
       /* class ViewHolder extends RecyclerView.ViewHolder {
            TextView tripText, dateText, reasonText, destinationText;
            RelativeLayout itemParentLayout;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                //grab the image, the text and the layout id's
                tripText = itemView.findViewById(R.id.select_tripref);
                dateText = itemView.findViewById(R.id.select_date);
                reasonText = itemView.findViewById(R.id.select_reason);
                destinationText = itemView.findViewById(R.id.select_destination);
                itemParentLayout = itemView.findViewById(R.id.listItemLayout);
            }
        }
    }*/



 /*   //add array for each item
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
        SelectTrip myTripAdapter = new SelectTrip(this, mTripN, mDate, mReason, mDestination);
        recyclerView.setAdapter(myTripAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }*/