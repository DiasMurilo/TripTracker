package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class reportviewer extends AppCompatActivity {

    Button mCancelReportViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportviewer);


        mCancelReportViewer = findViewById(R.id.cancelReportViewer);

        mCancelReportViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent reportViewerToHome = new Intent(reportviewer.this, home.class);
                startActivity(reportViewerToHome);
                // Call 3_HOME
            }
        });
    }
}
