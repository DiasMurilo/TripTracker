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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class TripList extends FirebaseRecyclerAdapter<ModelTripData,TripList.myviewholder> {
    public TripList(@NonNull FirebaseRecyclerOptions<ModelTripData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ModelTripData modelTripData) {
        holder.tripRef.setText(modelTripData.getTripRef());
        holder.date.setText(modelTripData.getDate());
        holder.reason.setText(modelTripData.getReason());
        holder.destination.setText(modelTripData.getDestination());
        //holder.carRef.setText(model.getCarRef());
        //Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_trip, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        //CircleImageView img;
        TextView tripRef, date, reason, destination;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            //img=(CircleImageView)itemView.findViewById(R.id.img1);
            tripRef = (TextView) itemView.findViewById(R.id.select_tripref);
            date = (TextView) itemView.findViewById(R.id.select_date);
            reason = (TextView) itemView.findViewById(R.id.select_reason);
            destination = (TextView) itemView.findViewById(R.id.select_destination);
        }
    }
}




















/*public class TripList extends RecyclerView.Adapter<TripList.ViewHolder> {

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
    }*/
