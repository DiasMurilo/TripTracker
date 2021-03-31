package com.example.triptracker;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class TripList extends FirebaseRecyclerAdapter<ModelTripData,TripList.myviewholder> {

    public TripList(@NonNull FirebaseRecyclerOptions<ModelTripData> options) {
        super(options);
    }
    //private Context mTripList;



    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, final int position, @NonNull final ModelTripData modelTripData) {
        holder.tripRef.setText(String.valueOf(position  + 1));
        holder.date.setText(modelTripData.getDate());
        holder.reason.setText(modelTripData.getReason());
        holder.destination.setText(modelTripData.getDestination());
        holder.selectTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context tripList = view.getContext();
                Intent tripListIntent = new Intent(tripList, ImageViewer.class);
                tripListIntent.putExtra("tripRef",String.valueOf(position  + 1));
                tripListIntent.putExtra("date",modelTripData.getDate());
                tripListIntent.putExtra("reason",modelTripData.getReason());
                tripListIntent.putExtra("destination",modelTripData.getDestination());
                tripList.startActivity(tripListIntent);

                //tripList.startActivity(new Intent(tripList, ImageViewer.class));
            }
        });
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
        Button selectTrip;
        //RelativeLayout mTripList;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            //img=(CircleImageView)itemView.findViewById(R.id.img1);
            tripRef = (TextView) itemView.findViewById(R.id.select_tripref);
            date = (TextView) itemView.findViewById(R.id.select_date);
            reason = (TextView) itemView.findViewById(R.id.select_reason);
            destination = (TextView) itemView.findViewById(R.id.select_destination);
            selectTrip = (Button) itemView.findViewById(R.id.buttonCamera);
            //mTripList = itemView.findViewById(R.id.listItemLayout);
        }
    }


}
