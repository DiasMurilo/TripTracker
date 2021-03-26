package com.example.triptracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    // Class wide variables
    private static final String TAG = "EmailPassword";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private FirebaseAuth mAuth;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Button mLogin, mRegister;
    EditText mEmail, mPass;
    ProgressBar mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mLogin = findViewById(R.id.buttonLogin);
        mEmail = findViewById(R.id.editTextEmail);
        mPass = findViewById(R.id.editTextPassword);
        mRegister = findViewById(R.id.buttonRegister);
        mProgress = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        mProgress.setVisibility(View.GONE);

        updateFieldsLogin();

        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                String email = mEmail.getText().toString();
                String password = mPass.getText().toString();
                signIn(email, password);

                /*if(checkEmailAndPass() == true){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    savePreferencesLogin();
                    intentBackToHome();
                }*/
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(checkEmailAndPass() == true){
                    mProgress.setVisibility(View.VISIBLE);
                    String email = mEmail.getText().toString();
                    String password = mPass.getText().toString();
                    createAccount(email, password);


                    //savePreferencesLogin();
                    //intentBackToSettings();
                }
            }
        });
    }


    public void intentBackToHome(){
        Intent backToHomeScreen = new Intent(getApplicationContext(), Home.class);
        startActivity(backToHomeScreen);
    }

    public void intentBackToSettings(){
        Intent intentToSettingsScreen = new Intent(getApplicationContext(), Settings.class);
        startActivity(intentToSettingsScreen);
    }

    public static boolean isValidEmail(CharSequence testEmail) {
        if (testEmail == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(testEmail).matches();
        }
    }

    private boolean checkEmailAndPass(){
        String emailTest = mEmail.getText().toString();
        String passTest = mPass.getText().toString();


        if (isValidEmail(emailTest) == false) {
            Toast.makeText(MainActivity.this, "invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( passTest.length() < 6){
            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void updateFieldsLogin() {
        //check if there are already dates and set it to Fields, otherwise "Edit Text" fields are set empty
        pref = MainActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String pEmail = pref.getString("email", "");
        final String pPass = pref.getString("pass", "");
        mEmail.setText(pEmail);
        mPass.setText(pPass);
    }

    private void savePreferencesLogin(){
        //save the values in the Preferences
        editor = MainActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String myEmail = mEmail.getText().toString();
        String myPass = mPass.getText().toString();
        editor.putString("email", myEmail);
        editor.putString("pass", myPass);
        editor.commit();
    }

    protected boolean checkIfSettingsSaved(){
        pref = MainActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String cEmail = pref.getString("email", "");
        final String cName = pref.getString("name", "");
        final String cCompany = pref.getString("company", "");
        final String cCarRef = pref.getString("carref", "");
        final String ckml = pref.getString("kml", "");
        final String cFuel = pref.getString("fuel", "");

        if (cName.equals("")) {
            return false;
        }
        else if (cEmail.equals("")) {
            return false;
        }
        else if (cCompany.equals("")) {
            return false;
        }
        else if (cCarRef.equals("")) {
            return false;
        }
        else if (ckml.equals("")) {
            return false;
        }
        else if (cFuel.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    // FIREBASE
    private void signIn(String email, String password) {
    Log.d(TAG, "signIn:" + email);
    if (!validateForm()) {
        return;
    }
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getApplicationContext(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        savePreferencesLogin();     // Update user preferences
                        if (checkIfSettingsSaved()==true) {
                            intentBackToHome();         // open home Screen
                        }
                        else {
                            intentBackToSettings();
                        }
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    mProgress.setVisibility(View.GONE);
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPass.setError("Required.");
            valid = false;
        } else {
            mPass.setError(null);
        }
        return valid;
    }

    void signOut() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            savePreferencesLogin();     // Update user preferences
                            intentBackToSettings();         // open home Screen
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        mProgress.setVisibility(View.GONE);
                    }
                });
    }

}




