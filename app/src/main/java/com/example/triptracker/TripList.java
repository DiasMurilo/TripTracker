package com.example.triptracker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class TripList extends FirebaseRecyclerAdapter<ModelTripData,TripList.myviewholder> {
    public TripList(@NonNull FirebaseRecyclerOptions<ModelTripData> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ModelTripData modelTripData) {
        holder.tripRef.setText(String.valueOf(position  + 1));
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
