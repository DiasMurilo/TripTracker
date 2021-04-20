package com.example.triptracker;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


/** <h1>TripList: Class associated to recycler view</h1>
 * <p>This class creates elements in view holder to be shown in the recycler view
 * Citation:
 * Class contains code adapted from
 * URL: https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 * Permission:  Creative Commons Attribution 4.0
 * Retrieved on: 24 Feb 2021
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class TripList extends FirebaseRecyclerAdapter<ModelTripData,TripList.myviewholder> {

    /**Method given from Firebase to deal with recycler views
     * @param options is from Firebase library*/
    public TripList(@NonNull FirebaseRecyclerOptions<ModelTripData> options) {
        super(options);
    }

    /**method to fill the view holders with database information
     * @param holder refer to the view holder
     * @param modelTripData refers to database info
     * @param position position of the item in the database*/
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull final ModelTripData modelTripData) {
        /**Adds 1 to position and set in tripRef in the view holder*/
        holder.tripRef.setText(String.valueOf(position  + 1));
        /**Set date of the trip in the view holder*/
        holder.date.setText(modelTripData.getDate());
        /**Set reason of the trip in the view holder*/
        holder.reason.setText(modelTripData.getReason());
        /**Set destination of the trip in the view holder*/
        holder.destination.setText(modelTripData.getDestination());
        /**get the link of a given element*/
        final String urlLink = String.valueOf(getRef(position));
        /**Set a click listener in the view holder*/
        holder.selectTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**create a context to each view holder*/
                Context tripList = view.getContext();

                /**Create a intent for each selectable trip*/
                Intent tripListIntent = new Intent(tripList, ImageViewer.class);
                /**Put information int the intent as extra*/
                tripListIntent.putExtra("tripRef",String.valueOf(position  + 1));
                tripListIntent.putExtra("date",modelTripData.getDate());
                tripListIntent.putExtra("reason",modelTripData.getReason());
                tripListIntent.putExtra("destination",modelTripData.getDestination());
                tripListIntent.putExtra("urlLink", urlLink);
                /**Call the intent*/
                tripList.startActivity(tripListIntent);
            }
        });
    }


    /**Method On create of view holder inflate the Recycler View layout
     * @param parent Context of the parent layout activity
     * @param viewType The view type of the element
     */
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_trip, parent, false);
        return new myviewholder(view);
    }

    /**Class that link variables to view elements*/
    class myviewholder extends RecyclerView.ViewHolder {
        /**View element TextView*/
        TextView tripRef, date, reason, destination;
        /**View element Button*/
        Button selectTrip;

        /**method to link elements to the code variables*/
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            /**link between view elements and code*/
            tripRef = (TextView) itemView.findViewById(R.id.select_tripref);
            date = (TextView) itemView.findViewById(R.id.select_date);
            reason = (TextView) itemView.findViewById(R.id.select_reason);
            destination = (TextView) itemView.findViewById(R.id.select_destination);
            selectTrip = (Button) itemView.findViewById(R.id.buttonCamera);
        }
    }



}
