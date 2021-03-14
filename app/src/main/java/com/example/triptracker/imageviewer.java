package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class imageviewer extends AppCompatActivity {

    Button mButtonCancelImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);

        mButtonCancelImageViewer = findViewById(R.id.cancelImageViewer);

        mButtonCancelImageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent imageViewerToHome = new Intent(imageviewer.this, home.class);
                startActivity(imageViewerToHome);
                // Call 3_HOME
            }
        });
    }
}
