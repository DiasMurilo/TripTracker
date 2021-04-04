package com.example.triptracker;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;



public class ImageViewer extends MainActivity {


    private static final int REQUEST_TAKE_PHOTO = 2;
    private Uri cameraImageURI;

    //initialising widgets (views) for use in this class
    private String iTripRef, iDate, iReason, iDestination;
    private TextView mtripRef, mDate, mReason, mDestination;
    private ImageView mImageViewer;
    private Button mButtonCancelImageViewer, mCamera;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);


        mButtonCancelImageViewer = findViewById(R.id.cancelImageViewer);
        mCamera = findViewById(R.id.cameraImageViewer);
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
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                intentBackToHome();
                // Call 3_HOME
            }
        });

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(v);
            }
        });

    }

    /**
     * Intent Create file, call camera and save picture
     * Create the File where the photo shall go
     * Continue only if the File was successfully created
     * Call class again (recursive action)
     * @param v
     */

    //Intent Create file, call camera and save picture
    private void dispatchTakePictureIntent(View v)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            // Create the File where the photo shall go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    cameraImageURI = FileProvider.getUriForFile(getApplicationContext(), "Android/data/com.example.triptracker/files/Pictures", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Call class again (recursive action)
        else {
            dispatchTakePictureIntent(v);
        }
    }

    /**
     * Recognise the action of piture taken and set in the ImageView layout
     * @param requestCode
     * @param resultCode
     * @param data
     */
    // Recognise the action of piture taken and set in the ImageView layout
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            mImageViewer.setImageURI(cameraImageURI);
        }
    }



    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ImageViewer.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   // prefix
                ".jpg",   // suffix
                storageDir      // directory
        );
        return image;
    }

}