package com.example.triptracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SendReport extends MainActivity {

    private static final String TAG = "SendReport";
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    // declaring width and height for our PDF file.
    private static final int PAGE_HEIGHT = 1120;
    private static final int PAGE_WIDTH = 792;
    private static final int EXPENSIVE_HEIGHT = 180;
    private static final int EXPENSIVE_WIDTH = 230;
    private static final int NUMBER_TRIPS_PER_PAGE = 6;
    private static final int NUMBER_EXPENSES_PER_PAGE = 5;

    private int LINE_HEIGHT_TITLE = 25;
    private int LINE_HEIGHT_TEXT = 18;

    private Button mCancelSelectDate, mReportSelectDate, mRepFrom, mRepTo;
    private TextView mTextFrom, mTextTo;
    private ProgressBar mProgress;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private String uid, reportFrom, reportTo;
    private int totalDownloadedAttempts;
    private boolean isButtonClicked, whatDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_report);

        mCancelSelectDate = findViewById(R.id.reportCancel);
        mReportSelectDate = findViewById(R.id.reportSelectDate);
        mProgress = findViewById(R.id.progressBar);
        mRepFrom = findViewById(R.id.repFrom);
        mRepTo = findViewById(R.id.repTo);
        mTextFrom = findViewById(R.id.textFrom);
        mTextTo = findViewById(R.id.textTo);

        mProgress.setVisibility(View.GONE);






        //Retrieve User Preferences
        pref = SendReport.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        uid = pref.getString("uid", "");

        //Get firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef =  FirebaseStorage.getInstance().getReference();

        //Disable button "Send Report"
        mReportSelectDate.setEnabled(false);

        mRepFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatDate = false;
                displayCalendar();
            }
        });

        mRepTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatDate = true;
                displayCalendar();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month ,int day){
                month = month + 1;

                String displayDate = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "MM/dd/uuuu" ) );

                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);
                //String date = year + "-" + month + "-" + day;
                if (whatDate == true)
                {
                    mTextTo.setText(displayDate);
                    reportTo = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "uuuu-MM-dd" ) );
                }
                else{
                    mTextFrom.setText(displayDate);
                    reportFrom = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "uuuu-MM-dd" ) );
                }

                enableReportButton();
            }
        };

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
                mProgress.setVisibility(View.VISIBLE);
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
        final String startDate = reportFrom;
        final String endDate = reportTo;

        // Read data from the database and get reserved days for the selected book
        dbRef.child("trips").child(uid).orderByChild("date").startAt(startDate).endAt(endDate).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    HashMap<String, TripData> tripDataList = new HashMap<>();
                    for (DataSnapshot trip : snapshot.getChildren()) {
                        TripData tripData = trip.getValue(TripData.class);
                        tripDataList.put(trip.getKey(), tripData);
                    }

                    if (tripDataList.size() > 0 && isButtonClicked) {
                        getExpensesImages(startDate, endDate, tripDataList);
                    } else if (isButtonClicked) {
                        mProgress.setVisibility(View.GONE);
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

    private void getExpensesImages(final String startDate, final String endDate, final HashMap<String, TripData> tripDataMap) {
        totalDownloadedAttempts = 0;
        final HashMap<String, String>  expensesTripMap = new HashMap<>();
        final List<Pair<Pair<String, String>, Bitmap>> expenseImagesList = new ArrayList<>();

        for(Map.Entry<String, TripData> tripData : tripDataMap.entrySet())
        {
            for(Expense expense : tripData.getValue().getExpenses())
            {
                expensesTripMap.put(expense.imageRef, tripData.getKey());
            }
        }

        for(final Map.Entry<String, String> expense : expensesTripMap.entrySet())
        {
            try
            {
                final File localFile = File.createTempFile(expense.getKey(), "");
                storageRef.child(uid).child(expense.getKey()).getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        createBitmapFromImage(expenseImagesList, expense, localFile);
                        generatePdfWhenFinished(startDate, endDate, tripDataMap, expensesTripMap, expenseImagesList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        generatePdfWhenFinished(startDate, endDate, tripDataMap, expensesTripMap, expenseImagesList);
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createBitmapFromImage(final List<Pair<Pair<String, String>, Bitmap>> expenseImagesList, final Map.Entry<String, String> expense, File localFile)
    {
        Bitmap  bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        if (bmp != null)
        {
            Bitmap scaledBmp  = Bitmap.createScaledBitmap(bmp, EXPENSIVE_WIDTH, EXPENSIVE_HEIGHT, false);
            expenseImagesList.add(new Pair(new Pair(expense.getKey(), expense.getValue()), scaledBmp));
        }
    }

    private void generatePdfWhenFinished(
            String startDate,
            String endDate,
            HashMap<String, TripData> tripDataMap,
            HashMap<String, String>  expensesTripMap,
            List<Pair<Pair<String, String>, Bitmap>> expenseImagesList)
    {
        totalDownloadedAttempts++;

        if (expensesTripMap.size() == totalDownloadedAttempts)
        {
            generatePDF(startDate, endDate, tripDataMap, expenseImagesList);
        }
    }

    private void generatePDF(String startDate, String endDate, HashMap<String, TripData> tripDataMap, List<Pair<Pair<String, String>, Bitmap>> expenseImagesList)
    {
        int intialCurrentLineY = 10;

        int pageTripRemainder = tripDataMap.size() % NUMBER_TRIPS_PER_PAGE;
        int totalTripPages = (tripDataMap.size() / NUMBER_TRIPS_PER_PAGE) + (pageTripRemainder > 0 ? 1 : 0);

        int pageExpensesRemainder = expenseImagesList.size() % NUMBER_EXPENSES_PER_PAGE;
        int totalExpensesPages = (expenseImagesList.size() / NUMBER_EXPENSES_PER_PAGE) + (pageExpensesRemainder > 0 ? 1 : 0);

        int totalPages = totalTripPages + totalExpensesPages;

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

        for (int pageTripDetails = 0; pageTripDetails < totalTripPages; pageTripDetails++)
        {
            int tripListIndexEnd = tripListIndexStart + ((tripListIndexStart + NUMBER_TRIPS_PER_PAGE) > tripDataMap.size() ? pageTripRemainder : NUMBER_TRIPS_PER_PAGE);
            List<TripData> tripDataListToPrint = getValuesInRange(tripDataMap, tripListIndexStart,  tripListIndexEnd);

            // below line is used for setting start page for our PDF file.
            PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

            // creating a variable for canvas from our page of PDF.
            Canvas canvas = myPage.getCanvas();

            int currentLineY = intialCurrentLineY;
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "TRIP REPORT - DETAILS", getTitleFontBold(), LINE_HEIGHT_TITLE);
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "From: " + startDate + " To: " + endDate, getTitleFontItalic(), LINE_HEIGHT_TITLE);
            currentLineY = writeLine(canvas, currentLineY);

            for (TripData tripData : tripDataListToPrint)
            {
                currentLineY = writeTrip(tripData, canvas, currentLineY);
                currentLineY = writeLineBetweenTrips(canvas, currentLineY);
            }

            writeFooter(canvas, pageTripDetails + 1 , totalPages);

            // after adding all attributes to our PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);

            tripListIndexStart += NUMBER_TRIPS_PER_PAGE;
        }

        int expenseListIndexStart = 0;

        for (int pageExpense = 0; pageExpense < totalExpensesPages; pageExpense++)
        {
            int expenseListIndexEnd = expenseListIndexStart + ((expenseListIndexStart + NUMBER_EXPENSES_PER_PAGE) > expenseImagesList.size() ? pageExpensesRemainder : NUMBER_EXPENSES_PER_PAGE);
            List<Pair<Pair<String, String>, Bitmap>>  expenseListToPrint = expenseImagesList.subList(expenseListIndexStart,  expenseListIndexEnd);

            // below line is used for setting start page for our PDF file.
            PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

            // creating a variable for canvas from our page of PDF.
            Canvas canvas = myPage.getCanvas();

            int currentLineY = intialCurrentLineY;
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "TRIP REPORT - EXPENSES", getTitleFontBold(), LINE_HEIGHT_TITLE);
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "From: " + startDate + " To: " + endDate, getTitleFontItalic(), LINE_HEIGHT_TITLE);
            currentLineY = writeLine(canvas, currentLineY);

            for (Pair<Pair<String, String>, Bitmap> expense : expenseListToPrint)
            {
                String tripKey = expense.first.second;
                String expenseRef = expense.first.first;

                TripData tripData = tripDataMap.get(tripKey);
                Expense expenseData = tripData.expenses.get(expenseRef);

                Bitmap bitmap = expense.second;
                canvas.drawBitmap(bitmap, 25, currentLineY, new Paint());
                writeExpenseData(tripData, expenseData, canvas, currentLineY);

                currentLineY += EXPENSIVE_HEIGHT + 25;
            }

            writeFooter(canvas, pageExpense + totalTripPages + 1 , totalPages);

            // after adding all attributes to our PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);

            expenseListIndexStart += NUMBER_EXPENSES_PER_PAGE;
        }

        File report  = createPdfDocument(dateTimeNow, pdfDocument);

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
        mProgress.setVisibility(View.GONE);

        sendReportByEmail(report);
    }

    private void sendReportByEmail(File report)
    {
        Uri reportURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.triptracker.provider", report);

        // Initiate intent to send e-mail
        Intent iSend = new Intent(Intent.ACTION_SEND);

        iSend.setType("application/pdf");
        iSend.putExtra(Intent.EXTRA_STREAM, reportURI);

        // Try send report and handle exception if the case
        try {
            startActivity(Intent.createChooser(iSend, "Send report"));
        } catch (Exception ex) {
            Toast.makeText(this, "Error in sending PDF, please try again.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

    }

    private void writeFooter(Canvas canvas, int currentPage, int totalPage)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dateTimeNow = sdf.format(calendar.getTime());

        int currentLineY = PAGE_HEIGHT  - 40;
        currentLineY = writeLine(canvas, currentLineY);
        currentLineY += 10;

        canvas.drawText(dateTimeNow, 25, currentLineY, getFooterFontLeft());
        canvas.drawText("Page " + currentPage + " of " + totalPage, PAGE_WIDTH - 50, currentLineY, getFooterFontRight());
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

        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Date: " + tripData.date , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Destination: " + tripData.destination , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Driver: " + tripData.name , getFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Company: " + tripData.company , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Car reference: " + tripData.carRef , getFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Distance: " + tripData.distance , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "KM/L: " + tripData.kml , getFont(), LINE_HEIGHT_TEXT);

        writeTextNextLine(canvas, startX, currentLineY, "Fuel: " + tripData.fuel , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Consumed fuel: " + tripData.getConsumedFuel() , getFont(), LINE_HEIGHT_TEXT);


        writeTextNextLine(canvas, startX, currentLineY, "Expenses count: " + tripData.getExpensesCount() , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Expenses total: " + tripData.getExpensesSum() , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, tripData.getExpenseInfo(), getFontItalic(), LINE_HEIGHT_TEXT);

        return currentLineY;
    }

    private void writeExpenseData(TripData tripData, Expense expenseData, Canvas canvas, int currentLineY)
    {
        // Left margin
        int startX = EXPENSIVE_WIDTH + 40;
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Trip Date: " + tripData.date , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Trip Destination: " + tripData.destination , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Expense value: " + expenseData.value , getFont(), LINE_HEIGHT_TEXT);
        writeTextNextLine(canvas, startX, currentLineY, "Expense description: " + expenseData.description , getFont(), LINE_HEIGHT_TEXT);
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

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        font.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is sued for setting color of our text inside our PDF file.
        font.setColor(ContextCompat.getColor(this, R.color.black));

        return font;
    }

    private Paint getFont()
    {
        Paint font = getBaseFont();

        // below line is used for setting text size which we will be displaying in our PDF file.
        font.setTextSize(15);

        return font;
    }

    private Paint getFontItalic()
    {
        Paint font = getBaseFont();

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        font.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

        // below line is used for setting text size which we will be displaying in our PDF file.
        font.setTextSize(15);

        return font;
    }


    private Paint getFooterFontLeft()
    {
        Paint font = getFooterFont();
        font.setTextAlign(Paint.Align.LEFT);
        return font;
    }

    private Paint getFooterFontRight()
    {
        Paint font = getFooterFont();
        font.setTextAlign(Paint.Align.RIGHT);
        return font;
    }

    private Paint getFooterFont()
    {
        Paint font = getBaseFont();

        // below line is used for setting text size which we will be displaying in our PDF file.
        font.setTextSize(11);

        return font;
    }

    private File createPdfDocument(String fileName, PdfDocument pdfDocument) {
        File file = null;
        try
        {
            file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),  fileName + ".pdf");

            // after creating a file name we will write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message on completion of PDF generation.
            Toast.makeText(this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used to handle error
            e.printStackTrace();
        }

        return  file;
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
                    mProgress.setVisibility(View.GONE);
                }
            } else {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    private void displayCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                SendReport.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void enableReportButton(){
        if(mTextFrom.equals("") || mTextTo.equals(""))
        {
            mReportSelectDate.setEnabled(false);
        }
        else
        {
            mReportSelectDate.setEnabled(true);
        }
    }

    public List<TripData> getValuesInRange(HashMap<String, TripData> map, Integer start, Integer end) {
        ArrayList<TripData> list =  new ArrayList<>(map.values());
        return list.subList(start, end);
    }
}
