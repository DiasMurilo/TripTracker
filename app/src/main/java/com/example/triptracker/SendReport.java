package com.example.triptracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * <h1>SendReport: creates PDF report, save and call intent to share</h1>
 * <p>The Trips Report is based on a range of dates between two
 * the calendar is presented to user to collect the dates
 * values are passed to method that checks the Realtime Database (Firebase)
 * After the PDF document is done and saved in the phone, the intent is called allowing user to send by email, google driver, message or bluetooth.
 * <p>
 * Citation:
 * Class contains code adapted from
 * URL: https://developer.android.com/reference/android/graphics/pdf/PdfDocument
 * Permission:  Creative Commons Attribution 2.5 & Apache 2.0 license
 * Retrieved on: 23 Mar 2021
 *
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class SendReport extends Login {


    /**String used to display info in debug mode*/
    private static final String TAG = "SendReport";

    /** constant used for runtime permissions*/
    private static final int PERMISSION_REQUEST_CODE = 200;
    /** "1120" is the height of A4 page*/
    private static final int PAGE_HEIGHT = 1120;
    /** "792" is the width of A4 page*/
    private static final int PAGE_WIDTH = 792;
    /** "180" is the height of the receipt image*/
    private static final int EXPENSIVE_HEIGHT = 180;
    /** "230" is the width of the receipt image*/
    private static final int EXPENSIVE_WIDTH = 230;
    /** "6" is the maximum number of trips per A4 page*/
    private static final int NUMBER_TRIPS_PER_PAGE = 6;
    /** "5" is the maximum number of expenses per A4 page*/
    private static final int NUMBER_EXPENSES_PER_PAGE = 5;
    /** "25" is the value that title font line space requires*/
    private int LINE_HEIGHT_TITLE = 25;
    /** "18" is the value that body font line space requires*/
    private int LINE_HEIGHT_TEXT = 18;
    /**View elements Buttons*/
    private Button mCancelSelectDate, mReportSelectDate, mRepFrom, mRepTo;
    /**View elements TextView*/
    private TextView mTextFrom, mTextTo;
    /**Progress round bar*/
    private ProgressBar mProgress;
    /**Firebase Realtime Database Reference*/
    private DatabaseReference dbRef;
    /**Firebase Bucket Storage reference*/
    private StorageReference storageRef;
    /**Datepicker visual element*/
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    /** String*/
    private String uid, reportFrom, reportTo;
    /**Total of reports in the selected range*/
    private int totalDownloadedAttempts;
    /**boolean variable*/
    private boolean isButtonClicked, whatDate;
    /**Initiate decimal format*/
    DecimalFormat df = new DecimalFormat("0.00");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_report);

        /**link between view elements and code*/
        mCancelSelectDate = findViewById(R.id.reportCancel);
        mReportSelectDate = findViewById(R.id.reportSelectDate);
        mProgress = findViewById(R.id.progressBar);
        mRepFrom = findViewById(R.id.reportFrom);
        mRepTo = findViewById(R.id.reportTo);
        mTextFrom = findViewById(R.id.textFrom);
        mTextTo = findViewById(R.id.textTo);


        /**hide progress bar to start*/
        mProgress.setVisibility(View.GONE);

        /**
         * Retrieve UID from User Preferences
         * @param UID is the identification of user in Firebase
         */
        pref = SendReport.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        uid = pref.getString("uid", "");

        /**Get Firebase Realtime Database reference*/
        dbRef = FirebaseDatabase.getInstance().getReference();

        /**get Firebase Bucket reference*/
        storageRef =  FirebaseStorage.getInstance().getReference();

        /**Button "SEND REPORT" not available at the start*/
        mReportSelectDate.setEnabled(false);

        /**On click action button "FROM"
         * Set "whatdate = false and call interactive calendar element "datapicker
         * @param whatdate = if false will set date "FROM"*/
        mRepFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatDate = false;
                displayCalendar();
            }
        });

        /**On Click action button "TO"
         * Set "whatdate = true and call interactive calendar element "datapicker
         * @param whatdate = if true will set date "TO"*/
        mRepTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatDate = true;
                displayCalendar();
            }
        });

        /**Listener to get date when user clicks and display*/
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            /**Require min android version "26" to run the method*/
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            /**This method gets user action of selecting date do actions:
             * get format 1 "mm/dd/yyyy" and display to user
             * get format 2 "yyyy/mm/dd" and put in a raviable to check in the database
             * @param year year "yyyy"
             * @param day day "dd"
             * @param month month "mm"
             * */
            public void onDateSet(DatePicker datePicker, int year, int month ,int day){
                month = month + 1;
                /**Get data, format to show user*/
                String displayDate = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "MM/dd/uuuu" ) );
                /** Show data picked during debugging time*/
                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);
                /**Check what date user selected "FROM" or "TO"*/
                if (whatDate == true)
                {
                    /**display picked data to user*/
                    mTextTo.setText(displayDate);

                    /**put picked data in variable at format required to search at database*/
                    reportTo = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "uuuu-MM-dd" ) );
                }
                else{
                    /**display picked data to user*/
                    mTextFrom.setText(displayDate);

                    /**put picked data in variable at format required to search at database*/
                    reportFrom = LocalDate.of( year , month , day ).format( DateTimeFormatter.ofPattern( "uuuu-MM-dd" ) );
                }
                /**call method that check if report can be generated or not*/
                enableReportButton();
            }
        };

        /**CANCEL button send user to home screen*/
        mCancelSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled", Toast.LENGTH_SHORT).show();
                /**method implemented in "Login"*/
                intentBackToHome();
            }
        });

        /**On click  REPORT button */
        mReportSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**double check if user clicked the button
                 * Note: required the cross check to avoid report created on DB changes*/
                isButtonClicked = true;

                /**Set progress bar visible*/
                mProgress.setVisibility(View.VISIBLE);

                /**calls method to search and create report*/
                getTripAndGenerateReport();
            }
        });
    }

    /** Method used for checking permissions.*/
    private void getTripAndGenerateReport()
    {
        if (checkPermission()) {
            getTrips();
        } else {
            requestPermission();
        }
    }

    /** method to search for report at specific range*/
    private void getTrips() {
        /**range of trips
         * Strings get selected dates value*/
        final String startDate = reportFrom;
        final String endDate = reportTo;

        /** Read data from the database and get trips in the range
         * More information at https://firebase.google.com/docs/build*/
        dbRef.child("trips").child(uid).orderByChild("date").startAt(startDate).endAt(endDate).addValueEventListener(
            new ValueEventListener() {
                @Override
                /**Methode to get a snapshot of the DB and check if between the desired dates*/
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, TripData> tripDataList = new HashMap<>();
                    for (DataSnapshot trip : snapshot.getChildren()) {
                        TripData tripData = trip.getValue(TripData.class);
                        tripDataList.put(trip.getKey(), tripData);
                    }
                    /**If there there's at least 1 trip with expense in the tripDataList*/
                    if (tripDataList.size() > 0 && isButtonClicked) {
                        getExpensesImages(startDate, endDate, tripDataList);
                    }
                    /**If there's no trip in the selected range*/
                    else if (isButtonClicked) {
                        mProgress.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "No trip found for selected dates.", Toast.LENGTH_LONG).show();
                    }
                    /**re-set var*/
                    isButtonClicked = false;
                }
                /**Check if any problem during database search*/
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    /**Failed to read value*/
                    Log.w("DataChange", "Failed to read value.", error.toException());
                }
            });
    }

    /**Method to get the images in the Firebase bucket
     * More information at https://firebase.google.com/docs/build*/
    private void getExpensesImages(final String startDate, final String endDate, final HashMap<String, TripData> tripDataMap) {
        /**Initiate a empty var to get Attempts and HashMap for expenses trips*/
        totalDownloadedAttempts = 0;
        final HashMap<String, String>  expensesTripMap = new HashMap<>();
        final List<Pair<Pair<String, String>, Bitmap>> expenseImagesList = new ArrayList<>();

        /**For each image in the report*/
        for(Map.Entry<String, TripData> tripData : tripDataMap.entrySet())
        {
            for(Expense expense : tripData.getValue().getExpenses())
            {
                expensesTripMap.put(expense.imageRef, tripData.getKey());
            }
        }
        /**In case of no images in the report*/
        if (expensesTripMap.size() == 0 ) {
            /**generate report without images*/
            generatePDF(startDate, endDate, tripDataMap, expenseImagesList);
        }
        /**In case of images to insert in the report*/
        else {
            /**Loop to download images and send to generate PDF report*/
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
                    /** Get exception trace*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /**Resize the images to insert in the report
     * More information at https://firebase.google.com/docs/build*/
    private void createBitmapFromImage(final List<Pair<Pair<String, String>, Bitmap>> expenseImagesList, final Map.Entry<String, String> expense, File localFile)
    {
        Bitmap  bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        if (bmp != null)
        {
            Bitmap scaledBmp  = Bitmap.createScaledBitmap(bmp, EXPENSIVE_WIDTH, EXPENSIVE_HEIGHT, false);
            expenseImagesList.add(new Pair(new Pair(expense.getKey(), expense.getValue()), scaledBmp));
        }
    }

    /**create a list of items to create the PDF with trips
     * More information at https://firebase.google.com/docs/build*/
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

    /**Method to create the PDF document
     * @param startDate
     * @param endDate
     * Also the HashMap with trips information and Bitmap Image List*/
    private void generatePDF(String startDate, String endDate, HashMap<String, TripData> tripDataMap, List<Pair<Pair<String, String>, Bitmap>> expenseImagesList)
    {
        /** Space value at the top of the pages*/
        int intialCurrentLineY = 10;

        /**Determine how many pages will be need for trips information*/
        int pageTripRemainder = tripDataMap.size() % NUMBER_TRIPS_PER_PAGE;
        int totalTripPages = (tripDataMap.size() / NUMBER_TRIPS_PER_PAGE) + (pageTripRemainder > 0 ? 1 : 0);

        /**Determina how many pages need for receipt images*/
        int pageExpensesRemainder = expenseImagesList.size() % NUMBER_EXPENSES_PER_PAGE;
        int totalExpensesPages = (expenseImagesList.size() / NUMBER_EXPENSES_PER_PAGE) + (pageExpensesRemainder > 0 ? 1 : 0);

        /**General total of pages need*/
        int totalPages = totalTripPages + totalExpensesPages;

        /**Start at the first position*/
        int tripListIndexStart = 0;

        /**get the current time to print in the page as reference*/
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
        String dateTimeNow = sdf.format(calendar.getTime());

        /** Create an object variable for the PDF*/
        PdfDocument pdfDocument = new PdfDocument();
        /**
         * Add page info to the PDF file in which we will give the pageWidth, pageHeight and number of pages to create the PDF*/
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, totalPages).create();

        /**Loop to print trips resume*/
        for (int pageTripDetails = 0; pageTripDetails < totalTripPages; pageTripDetails++)
        {
            /**Create packages with maximum number of trips to be printed in loop*/
            int tripListIndexEnd = tripListIndexStart + ((tripListIndexStart + NUMBER_TRIPS_PER_PAGE) > tripDataMap.size() ? pageTripRemainder : NUMBER_TRIPS_PER_PAGE);
            List<TripData> tripDataListToPrint = getValuesInRange(tripDataMap, tripListIndexStart,  tripListIndexEnd);

            /**Set start page*/
            PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

            /** create variable for canvas*/
            Canvas canvas = myPage.getCanvas();

            /**Prints title of the page*/
            int currentLineY = intialCurrentLineY;
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "TRIPTRACKER APP REPORT - TRIP DETAILS", getTitleFontBold(), LINE_HEIGHT_TITLE);
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "Dates from " + startDate + " to " + endDate, getTitleFontItalic(), LINE_HEIGHT_TITLE);
            currentLineY = writeLine(canvas, currentLineY);

            /**Loop to print trips in packages*/
            for (TripData tripData : tripDataListToPrint)
            {
                currentLineY = writeTrip(tripData, canvas, currentLineY);
                currentLineY = writeLineBetweenTrips(canvas, currentLineY);
            }
            /**Footer of the page*/
            writeFooter(canvas, pageTripDetails + 1 , totalPages);

            /** Finish a page*/
            pdfDocument.finishPage(myPage);

            /**Set index to next trip to be printed*/
            tripListIndexStart += NUMBER_TRIPS_PER_PAGE;
        }

        /**Set first image to be printed in the report*/
        int expenseListIndexStart = 0;

        /**Loop to print images*/
        for (int pageExpense = 0; pageExpense < totalExpensesPages; pageExpense++)
        {
            /**Create packages with maximum number of images to be printed in loop*/
            int expenseListIndexEnd = expenseListIndexStart + ((expenseListIndexStart + NUMBER_EXPENSES_PER_PAGE) > expenseImagesList.size() ? pageExpensesRemainder : NUMBER_EXPENSES_PER_PAGE);
            List<Pair<Pair<String, String>, Bitmap>>  expenseListToPrint = expenseImagesList.subList(expenseListIndexStart,  expenseListIndexEnd);

            /**Set start page*/
            PdfDocument.Page myPage = pdfDocument.startPage(myPageInfo);

            /** create variable for canvas*/
            Canvas canvas = myPage.getCanvas();

            /**Prints title of the page*/
            int currentLineY = intialCurrentLineY;
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "TRIPTRACKER APP REPORT - TRIP EXPENSES", getTitleFontBold(), LINE_HEIGHT_TITLE);
            currentLineY = writeTextNextLine(canvas, PAGE_WIDTH/2, currentLineY, "From: " + startDate + " To: " + endDate, getTitleFontItalic(), LINE_HEIGHT_TITLE);
            currentLineY = writeLine(canvas, currentLineY);
            /**Loop to print images in packages*/
            for (Pair<Pair<String, String>, Bitmap> expense : expenseListToPrint)
            {
                /**expense information*/
                String tripKey = expense.first.second;
                String expenseRef = expense.first.first;
                /**Trip reference*/
                TripData tripData = tripDataMap.get(tripKey);
                Expense expenseData = tripData.expenses.get(expenseRef);
                /***Expense image*/
                Bitmap bitmap = expense.second;
                canvas.drawBitmap(bitmap, 25, currentLineY, new Paint());
                writeExpenseData(tripData, expenseData, canvas, currentLineY);
                currentLineY += EXPENSIVE_HEIGHT + 25;
            }
            /** Print Footer page*/
            writeFooter(canvas, pageExpense + totalTripPages + 1 , totalPages);

            /** Finish a page*/
            pdfDocument.finishPage(myPage);

            /**Set index to next image to be printed*/
            expenseListIndexStart += NUMBER_EXPENSES_PER_PAGE;
        }
        /**Create document and save*/
        File report  = createPdfDocument(dateTimeNow, pdfDocument);

         /** finish PDF document reation*/
        pdfDocument.close();

        /**hide progressbar*/
        mProgress.setVisibility(View.GONE);
        /**Call intent allowing user to transfer the report*/
        shareReport(report);
    }

    /**Method to share the PDF document
     * @param report PDF document created containing trips resume and images
     */
    private void shareReport(File report)
    {
        /**Uri with the document address inside of the mobile*/
        Uri reportURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.triptracker.provider", report);

        /**Initiate a intent to share the pdf*/
        Intent iSend = new Intent(Intent.ACTION_SEND);
        /**Type of intent that allow user to select any application that can handle PDF*/
        iSend.setType("application/pdf");
        /**Put the PDF report in the intent*/
        iSend.putExtra(Intent.EXTRA_STREAM, reportURI);

        /** Try send PDF report and handle exception if the case*/
        try {
            startActivity(Intent.createChooser(iSend, "Send report"));
        } catch (Exception ex) {
            Toast.makeText(this, "Error in sending PDF, please try again.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    /**Method to share the PDF document
     * @param canvas area of the page
     * @param currentPage Number of current page
     * @param totalPage Number of total pages
     */
    private void writeFooter(Canvas canvas, int currentPage, int totalPage)
    {
        /**Get current time*/
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dateTimeNow = sdf.format(calendar.getTime());

        /**Set current line to the needed position down to top*/
        int currentLineY = PAGE_HEIGHT - 40;
        currentLineY = writeLine(canvas, currentLineY);
        currentLineY += 10;
        /**Draw line to separate footer from content*/
        canvas.drawText(dateTimeNow, 25, currentLineY, getFooterFontLeft());
        canvas.drawText("Page " + currentPage + " of " + totalPage, PAGE_WIDTH - 50, currentLineY, getFooterFontRight());
    }

    /**Method to draw a line on top and bottom of the page
     * @param canvas area of the page
     * @param y position to start the line
     */
    private int writeLine(Canvas canvas, int y)
    {
        y += 10;
        canvas.drawLine(0, y, PAGE_WIDTH, y, new Paint());
        return y + 10;
    }

    /**Method to draw a line dashed between trips and images
     * @param canvas area of the page
     * @param y position to start the line
     */
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


    /**Method used to draw the text in the PDF document
     * @param canvas area of the page
     * @param x horizontal position
     * @param y vertical Position
     * @param text text to be printed
     * @param fontInfo font type + format
     * @param lineHeigth space between lines (including text)
     */
    private int writeTextNextLine(Canvas canvas, int x, int y, String text, Paint fontInfo, int lineHeigth)
    {
        y += lineHeigth;
        canvas.drawText(text, x, y, fontInfo);
        return y;
    }

    /** Method to send packages with trips information to be printed
     * @param canvas area of the page
     * @param currentLineY position of the pointer
     * @param tripData Date from Firebase Realtime Database
     */
    private int writeTrip(TripData tripData, Canvas canvas, int currentLineY)
    {
        /**Set margin left side*/
        int startX = 25;
        /**Set margin at the middle of the page*/
        int middleX = PAGE_WIDTH/2;

        /**Set the line to be printed line by line*/
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Date: " + tripData.date , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Destination: " + tripData.destination , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "User: " + tripData.name , getFont(), LINE_HEIGHT_TEXT);
        /**Set the line to be printed line by line*/
        writeTextNextLine(canvas, startX, currentLineY, "Company: " + tripData.company , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Car reference: " + tripData.carRef , getFont(), LINE_HEIGHT_TEXT);
        /**Set the line to be printed line by line*/
        writeTextNextLine(canvas, startX, currentLineY, "Distance: " + tripData.distance + " km", getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Autonomy: " + tripData.kml + " km", getFont(), LINE_HEIGHT_TEXT);
        /**Set the line to be printed line by line*/
        writeTextNextLine(canvas, startX, currentLineY, "Fuel: " + tripData.fuel , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Consumed fuel: " + tripData.getConsumedFuel() + " Liter(s)", getFont(), LINE_HEIGHT_TEXT);

        /**Set the line to be printed line by line*/
        writeTextNextLine(canvas, startX, currentLineY, "Expenses count: " + tripData.getExpensesCount() , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, middleX, currentLineY, "Expenses total: " + df.format(tripData.getExpensesSum()) , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, tripData.getExpenseInfo(), getFontItalic(), LINE_HEIGHT_TEXT);

        /**return pointer*/
        return currentLineY;
    }

    /** Method to send packages with expenses information to be printed
     * @param canvas area of the page
     * @param currentLineY position of the pointer
     * @param tripData Date from Firebase Realtime Database (refer to trip info)
     * @param expenseData Date from Firebase Realtime Database (refer to expense info)
     */
    private void writeExpenseData(TripData tripData, Expense expenseData, Canvas canvas, int currentLineY)
    {
        /**Set margin after image width*/
        int startX = EXPENSIVE_WIDTH + 40;

        /**Set the line to be printed line by line*/
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Trip Date: " + tripData.date , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Trip Destination: " + tripData.destination , getFont(), LINE_HEIGHT_TEXT);
        currentLineY = writeTextNextLine(canvas, startX, currentLineY, "Expense value: " + expenseData.value , getFont(), LINE_HEIGHT_TEXT);
        writeTextNextLine(canvas, startX, currentLineY, "Expense description: " + expenseData.description , getFont(), LINE_HEIGHT_TEXT);
    }

    /**Method to set title font Bold*/
    private Paint getTitleFontBold()
    {
        Paint title = getTitleBaseFont();
        title.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        return title;
    }
    /**Method to set title font Italic*/
    private Paint getTitleFontItalic()
    {
        Paint title = getTitleBaseFont();
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        return title;
    }

    /**Method to set title font size and alignment*/
    private Paint getTitleBaseFont()
    {
        Paint font = getBaseFont();
        font.setTextSize(22);
        font.setTextAlign(Paint.Align.CENTER);
        return font;
    }
    /**Method to set body font colour*/
    private Paint getBaseFont()
    {
        Paint font = new Paint();
        font.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        font.setColor(ContextCompat.getColor(this, R.color.black));
        return font;
    }

    /**Method to set the body font size*/
    private Paint getFont()
    {
        Paint font = getBaseFont();
        font.setTextSize(15);
        return font;
    }
    /**Method to set the body font italic*/
    private Paint getFontItalic()
    {
        Paint font = getBaseFont();
        font.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        font.setTextSize(15);
        return font;
    }

    /**Method to set the footer font italic*/
    private Paint getFooterFontLeft()
    {
        Paint font = getFooterFont();
        font.setTextAlign(Paint.Align.LEFT);
        return font;
    }

    /**Method to set the footer font alignment*/
    private Paint getFooterFontRight()
    {
        Paint font = getFooterFont();
        font.setTextAlign(Paint.Align.RIGHT);
        return font;
    }

    /**Method to set the footer font size*/
    private Paint getFooterFont()
    {
        Paint font = getBaseFont();
        font.setTextSize(11);
        return font;
    }

    /**Method to create the PDF document
     * @param fileName get the name reference
     * @param pdfDocument get the document
     */
    private File createPdfDocument(String fileName, PdfDocument pdfDocument) {
        File file = null;
        try
        {
            /**get the location and name of the file*/
            file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),  fileName + ".pdf");

            /**Create the document to be write*/
            pdfDocument.writeTo(new FileOutputStream(file));

            /**show the user if successful*/
            Toast.makeText(this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            /**get any exception*/
            e.printStackTrace();
        }
        return  file;
    }

    /**Method to check if permission already granted*/
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    /**Method requesting permissions if not provided.*/
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                /**Create reference variables*/
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                /**If both permissions granted*/
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    getTripAndGenerateReport();
                }
                /**if permissions not granted*/
                else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    /**Show progressbar*/
                    mProgress.setVisibility(View.GONE);
                }
            } else {
                /**hide progressbar*/
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    /**Method to show calendar visual element*/
    private void displayCalendar(){
        /**Instantiate calendar*/
        Calendar cal = Calendar.getInstance();
        /**Create variables for elements day, month and year*/
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        /**Set type/style and show calendar*/
        DatePickerDialog dialog = new DatePickerDialog(
                SendReport.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**Method to check if user selected two dates, otherwise does not allow to generate report*/
    public void enableReportButton(){
        if(mTextFrom.getText().toString().equals("") || mTextTo.getText().toString().equals(""))
        {
            mReportSelectDate.setEnabled(false);
            return;
        }
        else
            mReportSelectDate.setEnabled(true);
    }

    /**Method to get trip data from Firebase Realtime Database*/
    public List<TripData> getValuesInRange(HashMap<String, TripData> map, Integer start, Integer end) {
        ArrayList<TripData> list =  new ArrayList<>(map.values());
        return list.subList(start, end);
    }
}
