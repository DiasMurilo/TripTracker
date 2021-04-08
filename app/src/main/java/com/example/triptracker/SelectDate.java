package com.example.triptracker;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SelectDate extends MainActivity {
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    // declaring width and height for our PDF file.
    private static final int PAGE_HEIGHT = 1120;
    private static final int PAGE_WIDTH = 792;
    private static final int NUMBER_TRIPS_PER_PAGE = 6;

    private int LINE_HEIGHT_TITLE = 25;
    private int LINE_HEIGHT_TEXT = 18;

    private Button mCancelSelectDate, mReportSelectDate;
    private DatabaseReference dbRef;
    private String uid;
    private boolean isButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdate);

        mCancelSelectDate = findViewById(R.id.cancelSelectDate);
        mReportSelectDate = findViewById(R.id.reportSelectDate);

        //Retrieve User Preferences
        pref = SelectDate.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        uid = pref.getString("uid", "");

        //Get firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference();
        mCancelSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled", Toast.LENGTH_SHORT).show();
                intentBackToHome();
            }
        });

        mReportSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isButtonClicked = true;
                getTripAndGenerateReport();
            }
        });
    }

    private void getTripAndGenerateReport()
    {
        // below code is used for checking our permissions.
        if (checkPermission()) {
            getTrips();
        } else {
            requestPermission();
        }
    }

    private void getTrips() {
        final String startDate = "2021-03-01";
        final String endDate = "2021-05-01";

        // Read data from the database and get reserved days for the selected book
        dbRef.child("trips").child(uid).orderByChild("date").startAt(startDate).endAt(endDate).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    List<TripData> tripDataList = new ArrayList<>();
                    for (DataSnapshot trip : snapshot.getChildren()) {
                        TripData tripData = trip.getValue(TripData.class);
                        tripDataList.add(tripData);
                    }

                    if (tripDataList.size() > 0 && isButtonClicked) {
                        generatePDF(startDate, endDate, tripDataList);
                    } else if (isButtonClicked) {
                        Toast.makeText(getBaseContext(), "No trip found for selected dates.", Toast.LENGTH_LONG).show();
                    }
                    isButtonClicked = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w("DataChange", "Failed to read value.", error.toException());
                }
            });
    }

    private void generatePDF(String startDate, String endDate, List<TripData> tripDataList)
    {
        int intialCurrentLineY = 20;

        int pageRemainder = tripDataList.size() % NUMBER_TRIPS_PER_PAGE;
        int totalPages = (tripDataList.size() / NUMBER_TRIPS_PER_PAGE) + (pageRemainder > 0 ? 1 : 0);
        int tripListIndexStart = 0;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
        String dateTimeNow = sdf.format(calendar.getTime());

        // Creating an object variable for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // Adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, totalPages).create();

        for (int i = 0; i < totalPages; i++)
        {
            int tripListIndexEnd = tripListIndexStart + ((tripListIndexStart + NUMBER_TRIPS_PER_PAGE) > tripDataList.size() ? pageRemainder : NUMBER_TRIPS_PER_PAGE) ;
            List<TripData> tripDataListToPrint = tripDataList.subList(tripListIndexStart,  tripListIndexEnd);

            // below line is used for setting start page for our PDF file.
            PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

            // creating a variable for canvas from our page of PDF.
            Canvas canvas = myPage.getCanvas();

            int currentLineY = intialCurrentLineY;
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "TRIP REPORT", getTitleFontBold(), LINE_HEIGHT_TITLE);
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "From: " + startDate + " To: " + endDate, getTitleFontItalic(), LINE_HEIGHT_TITLE);
            currentLineY = writeLine(canvas, currentLineY);

            for (TripData tripData : tripDataListToPrint)
            {
                currentLineY = writeTrip(tripData, canvas, currentLineY);
                currentLineY = writeLineBetweenTrips(canvas, currentLineY);
            }

            // after adding all attributes to our PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);

            tripListIndexStart += NUMBER_TRIPS_PER_PAGE;
        }

        createPdfDocument(dateTimeNow, pdfDocument);

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private void writePage()
    {

    }

    private int writeLine(Canvas canvas, int y)
    {
        y += 10;
        canvas.drawLine(0, y, PAGE_WIDTH, y, new Paint());
        return y + 10;
    }

    private int writeLineBetweenTrips(Canvas canvas, int y)
    {
        y += 15;
        Paint dashPaint = new Paint();
        dashPaint.setARGB(255, 0, 0, 0);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5}, 0));
        canvas.drawLine(25, y, PAGE_WIDTH - 50, y, dashPaint);
        return y + 10;
    }


    private int writeTextNextLine(Canvas canvas, int x, int y, String text, Paint fontInfo, int lineHeigth)
    {
        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        y += lineHeigth;
        canvas.drawText(text, x, y, fontInfo);
        return y;
    }

    private int writeTrip(TripData tripData, Canvas canvas, int currentLineY)
    {
        // Left margin
        int startX = 25;
        int middleX = PAGE_WIDTH/2;

        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Date: " + tripData.date , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Destination: " + tripData.destination , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Driver: " + tripData.name , getTripFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Company: " + tripData.company , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Car reference: " + tripData.carRef , getTripFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Distance: " + tripData.distance , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "KM/L: " + tripData.kml , getTripFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Fuel: " + tripData.fuel , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Consumed fuel: " + tripData.getConsumedFuel() , getTripFont(), LINE_HEIGHT_TEXT);


        writeTextNextLine(canvas, startX, currentLineY, "Expenses count: " + tripData.getExpensesCount() , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Expenses total: " + tripData.getExpensesSum() , getTripFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Expenses receipt ref.: " + tripData.getExpensesReference(), getTripFont(), LINE_HEIGHT_TEXT);

        return currentLineY;
    }

    private Paint getTitleFontBold()
    {
        Paint title = getTitleBaseFont();

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        return title;
    }

    private Paint getTitleFontItalic()
    {
        Paint title = getTitleBaseFont();

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

        return title;
    }

    private Paint getTitleBaseFont()
    {
        Paint font = getBaseFont();

        // below line is used for setting text size which we will be displaying in our PDF file.
        font.setTextSize(22);

        // below line is used for setting our text to center of PDF.
        font.setTextAlign(Paint.Align.CENTER);

        return font;
    }

    private Paint getBaseFont()
    {
        Paint font = new Paint();

        // below line is sued for setting color of our text inside our PDF file.
        font.setColor(ContextCompat.getColor(this, R.color.black));

        return font;
    }

    private Paint getTripFont()
    {
        Paint font = getBaseFont();

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        font.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size which we will be displaying in our PDF file.
        font.setTextSize(15);

        return font;
    }

    private void createPdfDocument(String dateTIme, PdfDocument pdfDocument) {
        String fileName = dateTIme + ".pdf";

        File myDir = new File(Environment.getExternalStorageDirectory() + "/TripTrackerReports");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // below line is used to set the name of our PDF file and its path.
        File file = new File(myDir, fileName);

        try {
            // after creating a file name we will write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message on completion of PDF generation.
            Toast.makeText(this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used to handle error
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    getTripAndGenerateReport();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
