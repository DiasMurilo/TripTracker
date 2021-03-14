package com.example.triptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class selectdate extends AppCompatActivity {

    Button mCancelSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdate);


        mCancelSelectDate = findViewById(R.id.cancelSelectDate);

        mCancelSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                Intent selectDateToHomeScreen = new Intent(selectdate.this, home.class);
                startActivity(selectDateToHomeScreen);
                // Call 3_HOME
            }
        });
    }
}
