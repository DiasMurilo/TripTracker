package com.example.triptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/** <h1>ReportViewer: Class that displays recycler view list to the user</h1>
 * <p>This class displays the recycler view to the user after the elements being created<p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class ReportViewer extends MainActivity {

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
                intentBackToHome();
                // Call 3_HOME
            }
        });
    }
}