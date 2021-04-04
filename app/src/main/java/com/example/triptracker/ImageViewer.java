package com.example.triptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ImageViewer extends MainActivity {





    String iTripRef, iDate, iReason, iDestination;
    TextView mtripRef, mDate, mReason, mDestination;
    ImageView mImageViewer;
    Button mButtonCancelImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);



        mButtonCancelImageViewer = findViewById(R.id.cancelImageViewer);
        mtripRef = findViewById(R.id.camera_tripRef);
        mDate = findViewById(R.id.camera_date);
        mReason = findViewById(R.id.camera__reason);
        mDestination = findViewById(R.id.camera_destination);
        mImageViewer = findViewById(R.id.imageViewerField);



        iTripRef = getIntent().getExtras().get("tripRef").toString();
        iDate = getIntent().getExtras().get("date").toString();
        iReason = getIntent().getExtras().get("reason").toString();
        iDestination = getIntent().getExtras().get("destination").toString();


        mtripRef.setText(iTripRef);
        mDate.setText(iDate);
        mReason.setText(iReason);
        mDestination.setText(iDestination);


        mButtonCancelImageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });
    }
}
