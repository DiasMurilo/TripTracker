package com.example.triptracker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

/** <h1>ImageViewer: Class to get receipt picture, display and sava at database</h1>
 * <p>This class allow user to call camera from intent, set picture to user check, add expense description and value to save at database <p>
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 */
public class ImageViewer extends Login {

    /**Initiate variable to be used by camera intent*/
    private static final int REQUEST_TAKE_PHOTO = 2;
    /**Camera image path Uri*/
    private Uri cameraImageURI;
    /**Create boolean*/
    private boolean isPicTaken;
    /**Create String*/
    private String iTripRef, iDate, iReason, iDestination, iURL, value, imageRef, description;
    /**View element textView*/
    private TextView mtripRef, mDate, mReason, mDestination;
    /**View element editText*/
    private EditText mValue, mDescription;
    /**View element ImageViewer*/
    private ImageView mImageViewer;
    /**View element Button*/
    private Button mButtonCancelImageViewer, mCamera, mSave;

    /**Get firebase bucket storage reference*/
    FirebaseStorage storage = FirebaseStorage.getInstance();
    /**Get firebase Realtime Database reference*/
    StorageReference storageRef = storage.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);

        /**link between view elements and code*/
        mButtonCancelImageViewer = findViewById(R.id.cancelImageViewer);
        mCamera = findViewById(R.id.cameraImageViewer);
        mtripRef = findViewById(R.id.camera_tripRef);
        mDate = findViewById(R.id.camera_date);
        mReason = findViewById(R.id.camera__reason);
        mDestination = findViewById(R.id.camera_destination);
        mImageViewer = findViewById(R.id.imageViewerField);
        mSave = findViewById(R.id.saveImageViewer);
        mValue = findViewById(R.id.field_Value);
        mDescription = findViewById(R.id.field_Description);

        /**Initiate shared preferences Editor*/
        pref = ImageViewer.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**Get strings from intent*/
        iTripRef = getIntent().getExtras().get("tripRef").toString();
        iDate = getIntent().getExtras().get("date").toString();
        iReason = getIntent().getExtras().get("reason").toString();
        iDestination = getIntent().getExtras().get("destination").toString();
        iURL = getIntent().getExtras().get("urlLink").toString();

        /**Set information in the textView*/
        mtripRef.setText(iTripRef);
        mDate.setText(iDate);
        mReason.setText(iReason);
        mDestination.setText(iDestination);

        isPicTaken = false;

        /**Set On Click Listener on CANCEL button*/
        mButtonCancelImageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Display toast message to user*/
                Toast.makeText(getApplicationContext(), "Action Canceled",
                        Toast.LENGTH_SHORT).show();
                /**Call intent to Home screen*/
                intentBackToHome();
            }
        });

        /**Set On Click Listener on CAMERA button*/
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Start intent to get picture*/
                dispatchTakePictureIntent(v);
            }
        });

        /**Set On Click Listener on SAVE button*/
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Get string values from fields*/
                value = mValue.getText().toString();
                description = mDescription.getText().toString();

                /**Check if field 'value' is empty and display to user*/
                if (value.equals("")) {
                    Toast.makeText(getBaseContext(), "Field 'Value' is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**Check if field 'description' is empty and display to user*/
                else if (description.equals("")) {
                    Toast.makeText(getBaseContext(), "Field 'Description' is empty'", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    /**Check if picture was taken before save*/
                    if (isPicTaken == true) {
                        /**Initiate calendar to get exclusive name to picture based on time*/
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
                        String currentDate = sdf.format(calendar.getTime());
                        /**Retrieve UID reference from Realtime Database*/
                        final String uid = pref.getString("uid", "");
                        /**Gets current time as receipt name*/
                        String receiptName = currentDate;
                        /**Gets receipt path to save the picture & send to database*/
                        String receiptPathName = uid + "/" + receiptName;
                        StorageReference tripTrackerRef = storageRef.child(receiptPathName);
                        mImageViewer.setDrawingCacheEnabled(true);
                        mImageViewer.buildDrawingCache();
                        //Bitmap bitmap = ((BitmapDrawable) mImageViewer.getDrawable()).getBitmap();
                        Bitmap bitmap = ((BitmapDrawable) mImageViewer.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = tripTrackerRef.putBytes(data);
                        /**On Failure display to user*/
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getBaseContext(), "Upload failure check Internet", Toast.LENGTH_SHORT).show();
                                Log.w("onFailure", "Failed to read value.");
                            }
                        })
                                /**On Success display to user*/
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getBaseContext(), "Upload done with success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        /** Starts to updated in database receipt reference*/
                        /** Creates the path to adds image reference in the database */
                        imageRef = receiptName;
                        String fullURL = iURL;
                        String excludePath = "https://triptracker-821dc-default-rtdb.europe-west1.firebasedatabase.app/";
                        String pathToFirebase = fullURL.replace(excludePath, "");
                        /**Path created*/
                        pathToFirebase = pathToFirebase;
                        /**Calls Expense.Class to put data in the */
                        Expense newExpense = new Expense(imageRef, value, description);
                        Map<String, Object> expense = newExpense.toMap();
                        /**Get firebase reference*/
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(pathToFirebase);
                        Map<String, Object> childUpdates = new HashMap<>();
                        /**Prepare path to send data*/
                        childUpdates.put("/expenses/" + receiptName + "/", expense);
                        /**Send data to database*/
                        myRef.updateChildren(childUpdates)
                                /**On Success display to user*/
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getBaseContext(), "Trip Register successfully added!", Toast.LENGTH_SHORT).show();
                                                intentBackToHome();
                                            }
                                        })
                                /**On Success display to user*/
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), "Trip Register Failed, Check you connection", Toast.LENGTH_SHORT).show();
                                        Log.w("onFailure", "Failed to read value.");
                                    }
                                });
                    }
                    /**In case of not taken yet*/
                    else {
                        Toast.makeText(getBaseContext(), "No Picture taken yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Intent Create file, call camera and save picture
     * Create the File where the photo shall go
     * Continue only if the File was successfully created
     * Call class again (recursive action)
     */
    private void dispatchTakePictureIntent(View v)
    {
        /**Create intent to camera*/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**Check if camera intent crated with success*/
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            /**Create the File where the photo shall go*/
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                /**Continue only if the File was successfully created*/
                if (photoFile != null) {
                    cameraImageURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.triptracker.provider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
            /**Get and handle exception*/
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**Call class again (recursive action)*/
        else {
            dispatchTakePictureIntent(v);
        }
    }

    /**Recognise the action of picture taken and set in the ImageView layout*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            /**Allow to save picture*/
            isPicTaken = true;
            /**Set image in the Image Viewer element*/
            mImageViewer.setImageURI(cameraImageURI);
        }
    }

    /**Method to create file*/
    private File createImageFile() throws IOException
    {
        /**File name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        /**Get path to save file*/
        File storageDir = ImageViewer.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        /**Creates file*/
        File image = File.createTempFile(
                imageFileName,   // prefix
                ".jpg",   // suffix
                storageDir      // directory
        );
        return image;
    }



}