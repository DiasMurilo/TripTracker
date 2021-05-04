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

/** <h1>Login: Initial Class to register or login</h1>
 * <p>This class allow user to register or login <p>
 * Citation:
 * Class contains code adapted from
 * URL: https://firebase.google.com/docs/auth
 * Permission:  Creative Commons Attribution 4.0 License under the Apache 2.0 License
 * Retrieved on: 15 Feb 2021
 * @author  Murilo Dias
 * @version 1.0
 * @since   2021-04-11
 *
 *
 */
public class Login extends AppCompatActivity {

    /**Creates a variable to use during debugging time*/
    private static final String TAG = "Login";
    /**Initiate shared preferences Editor*/
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    /**Initiate firebase Authentication service*/
    private FirebaseAuth mAuth;
    /**Declare Editor to edit shared pref data*/
    SharedPreferences.Editor editor;
    /**Declare shared pref var itself*/
    SharedPreferences pref;
    /**View element Button*/
    Button mLogin, mRegister;
    /**View element Edittext*/
    EditText mEmail, mPass;
    /**View element progressbar*/
    ProgressBar mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /**link between view elements and code*/
        mLogin = findViewById(R.id.buttonLogin);
        mEmail = findViewById(R.id.editTextEmail);
        mPass = findViewById(R.id.editTextPassword);
        mRegister = findViewById(R.id.buttonRegister);
        mProgress = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        /**Start with enabled buttons*/
        mLogin.setEnabled(true);
        mRegister.setEnabled(true);

        /**Set element invisible*/
        mProgress.setVisibility(View.GONE);

        /**Calls method to Autofill fields*/
        updateFieldsLogin();

        /**Set On Click Listener to login*/
        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /**Disable buttons*/
                mLogin.setEnabled(false);
                mRegister.setEnabled(false);
                /**Show progressbar*/
                mProgress.setVisibility(View.VISIBLE);
                /**Get fields values*/
                String email = mEmail.getText().toString();
                String password = mPass.getText().toString();
                /**Call method to login*/
                signIn(email, password);

            }
        });

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(checkEmailAndPass() == true){
                    /**Disable buttons*/
                    mLogin.setEnabled(false);
                    mRegister.setEnabled(false);
                    /**Show progressbar*/
                    mProgress.setVisibility(View.VISIBLE);
                    /**Get fields values*/
                    String email = mEmail.getText().toString();
                    String password = mPass.getText().toString();
                    /**Call method to create account*/
                    createAccount(email, password);
                }
            }
        });
    }

    /**Method to create intent to call Home Screen*/
    public void intentBackToHome(){
        Intent backToHomeScreen = new Intent(getApplicationContext(), Home.class);
        startActivity(backToHomeScreen);
    }

    /**Method to create intent to call Settings Screen*/
    public void intentBackToSettings(){
        Intent intentToSettingsScreen = new Intent(getApplicationContext(), Settings.class);
        startActivity(intentToSettingsScreen);
    }

    /**Method to check the email format*/
    public static boolean isValidEmail(CharSequence testEmail) {
        if (testEmail == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(testEmail).matches();
        }
    }

    /**Method to check email and password*/
    private boolean checkEmailAndPass(){
        /**Get fields values to string*/
        String emailTest = mEmail.getText().toString();
        String passTest = mPass.getText().toString();

        /**Check if valid email*/
        if (isValidEmail(emailTest) == false) {
            Toast.makeText(Login.this, "invalid Email", Toast.LENGTH_SHORT).show();
            /**Enable buttons*/
            mLogin.setEnabled(true);
            mRegister.setEnabled(true);
            /**Set element invisible*/
            mProgress.setVisibility(View.GONE);
            return false;
        }
        /**Check if password valid*/
        else if( passTest.length() < 6){
            Toast.makeText(Login.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            /**Enable buttons*/
            mLogin.setEnabled(true);
            mRegister.setEnabled(true);
            /**Set element invisible*/
            mProgress.setVisibility(View.GONE);
            return false;
        }
        /**Return positive*/
        else {
            return true;
        }
    }

    /**Method set previous values in the fields if there's*/
    private void updateFieldsLogin() {
        /**Initiate Shared preferences*/
        pref = Login.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**get the String values from Shared Preferences*/
        final String pEmail = pref.getString("email", "");
        final String pPass = pref.getString("pass", "");
        /**Set values to Text Fields and textEditor*/
        mEmail.setText(pEmail);
        mPass.setText(pPass);
    }
    /**Method updates the Shared preferences*/
    private void savePreferencesLogin(String userId){
        /**Initiate shared preferences Editor*/
        editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        /**get the String values from fields*/
        String myEmail = mEmail.getText().toString();
        String myPass = mPass.getText().toString();
        /**Save Strings values in the Shared Preferences*/
        editor.putString("uid", userId);
        editor.putString("email", myEmail);
        editor.putString("pass", myPass);
        editor.commit();
    }
    /**method to check if mandatory fields already filled*/
    protected boolean checkIfSettingsSaved(){
        /**Initiate Shared preferences*/
        pref = Login.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /**get the String values from Shared Preferences*/
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

    /***/
    private void signIn(String email, String password) {

    if (!validateForm()) {
        return;
    }
    /**Calls Authentication in the Firebase*/
    mAuth.signInWithEmailAndPassword(email, password)
            /**Check the result of the action*/
            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    /**Sign in successful, update UI with the signed-in information*/
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "signInWithEmail:success");
                        /**gets the user reference from Authentication Firebase service*/
                        FirebaseUser user = mAuth.getCurrentUser();
                        /**Updates user in shared preferences*/
                        savePreferencesLogin(user.getUid());
                        if (checkIfSettingsSaved()==true) {
                            /**Calls intent to Home screen*/
                            intentBackToHome();
                        }
                        else {
                            /**Calls intent to Settings*/
                            intentBackToSettings();
                        }
                    }
                    else {
                        /**If sign in fails, display a message to the user*/
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        /**Set element invisible*/
                        mProgress.setVisibility(View.GONE);
                    }
                    /**set progressbar as invisible*/
                    mProgress.setVisibility(View.GONE);
                    /**Enable buttons*/
                    mLogin.setEnabled(true);
                    mRegister.setEnabled(true);
                }
            });
    }

    /**Check the format of email and and pass*/
    private boolean validateForm() {
        boolean valid = true;
        /**get email from field*/
        String email = mEmail.getText().toString();
        /**Check the email*/
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            /**Hide progressbar*/
            mProgress.setVisibility(View.GONE);
            valid = false;
        } else {
            /**Hide progressbar*/
            mProgress.setVisibility(View.GONE);
            mEmail.setError(null);
        }
        /**get password from field*/
        String password = mPass.getText().toString();
        /**Check the password*/
        if (TextUtils.isEmpty(password)) {
            /**Hide progressbar*/
            mProgress.setVisibility(View.GONE);
            mPass.setError("Required.");
            valid = false;
        } else {
            /**Hide progressbar*/
            mProgress.setVisibility(View.GONE);
            mPass.setError(null);
        }
        /**Hide progressbar*/
        mProgress.setVisibility(View.GONE);
        /**Return Valid*/
        return valid;
    }

    /**Method to logout*/
    void signOut() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
    }
    /**Method to create account*/
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        /**Check if valid email and password*/
        if (!validateForm()) {
            return;
        }
        /**Check the result of the action*/
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /**created successful, update UI with the signed-in information*/
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /**gets the user reference from Authentication Firebase service*/
                            savePreferencesLogin(user.getUid());     // Update user preferences
                            intentBackToSettings();         // open home Screen
                        }
                        else {
                            /**If sign in fails, display a message to the user*/
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        /**Enable buttons*/
                        mLogin.setEnabled(true);
                        mRegister.setEnabled(true);
                        /**Hide progressbar*/
                        mProgress.setVisibility(View.GONE);
                    }
                });
    }
}




