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
import com.google.firebase.storage.UploadTask;




public class ImageViewer extends MainActivity {


    private static final int REQUEST_TAKE_PHOTO = 2;
    private Uri cameraImageURI;

    //initialising widgets (views) for use in this class
    private String iTripRef, iDate, iReason, iDestination, iURL, value, imageRef, description;   // iURL is the address to send image link
    private TextView mtripRef, mDate, mReason, mDestination;
    private EditText mValue, mDescription;
    private ImageView mImageViewer;
    private Button mButtonCancelImageViewer, mCamera, mSave;

    // [START storage_field_initialization]
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();



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
        mSave = findViewById(R.id.saveImageViewer);
        mValue = findViewById(R.id.field_Value);
        mDescription = findViewById(R.id.field_Description);

        //Retrieve User Preferences
        pref = ImageViewer.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        iTripRef = getIntent().getExtras().get("tripRef").toString();
        iDate = getIntent().getExtras().get("date").toString();
        iReason = getIntent().getExtras().get("reason").toString();
        iDestination = getIntent().getExtras().get("destination").toString();
        iURL = getIntent().getExtras().get("urlLink").toString();

        //dbRef = FirebaseDatabase.getInstance().getReference();
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

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = mValue.getText().toString();
                description = mDescription.getText().toString();

                if (value.equals(""))
                {
                    // add toast: please fill fields Value & Description
                }
                else if(description.equals(""))
                {
                    // add toast: please fill fields Value & Description
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
                    String currentDate = sdf.format(calendar.getTime());

                    final String uid = pref.getString("uid", "");

                /*Uri file = Uri.fromFile(new File(String.valueOf(cameraImageURI)));
                StorageReference triptrackerRef = storageRef.child(String.valueOf(uid) + file.getLastPathSegment());
                uploadTask = triptrackerRef.putFile(file);*/
                    String receiptName = currentDate;
                    String receiptPathName = uid + "/" + receiptName;
                    StorageReference tripTrackerRef = storageRef.child(receiptPathName);
                    mImageViewer.setDrawingCacheEnabled(true);
                    mImageViewer.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) mImageViewer.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = tripTrackerRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });

                    imageRef = receiptName;
                    String fullURL = iURL;
                    String excludePath = "https://triptracker-821dc-default-rtdb.europe-west1.firebasedatabase.app/";
                    String pathToFirebase = fullURL.replace(excludePath, "");
                    pathToFirebase = pathToFirebase ;//+ "/expenses/" + receiptName + "/";
                    AddExpenses newExpense = new AddExpenses(imageRef, value, description);
                    Map<String, Object> expense = newExpense.toMap();

                    //String key = dbRef.child("trips").push().getKey();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(pathToFirebase);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/expenses/" + receiptName + "/", expense);

                    myRef.updateChildren(childUpdates)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText( getBaseContext(), "Trip Register successfully added!", Toast.LENGTH_SHORT).show();
                                            intentBackToHome();
                                        }
                                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText( getBaseContext(), "Trip Register Failed, Check you connection", Toast.LENGTH_SHORT).show();
                                    Log.w("onFailure", "Failed to read value.");
                                }
                            });

                    //FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //DatabaseReference myRef = database.getReference(pathToFirebase);
                    //myRef.setValue(receiptName);
                    //childUpdates.put("/trips/" + uid + "/" + key, trip);
                }
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