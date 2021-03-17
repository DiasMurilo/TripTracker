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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mLogin = findViewById(R.id.buttonLogin);
        mEmail = findViewById(R.id.editTextEmail);
        mPass = findViewById(R.id.editTextPassword);
        mRegister = findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();

        updateFieldsLogin();

        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(getApplicationContext(), "User Registered with Success", Toast.LENGTH_SHORT).show();
                    savePreferencesLogin();
                    intentBackToSettings();
                }
            }
        });
    }

    public void intentBackToHome(){
        Intent backToHomeScreen = new Intent(getApplicationContext(), home.class);
        startActivity(backToHomeScreen);
    }

    public void intentBackToSettings(){
        Intent intentToSettingsScreen = new Intent(getApplicationContext(), settings.class);
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


   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/




// FIREBASE
private void signIn(String email, String password) {
    Log.d(TAG, "signIn:" + email + "and Pass: " + password);
    if (!validateForm()) {
        return;
    }

    //showProgressBar();

    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getApplicationContext(), "Authentication Successful.",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        intentBackToHome();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        //checkForMultiFactorFailure(task.getException());
                    }

                    /*if (!task.isSuccessful()) {
                        mBinding.status.setText(R.string.auth_failed);
                    }*/
                    //hideProgressBar();
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
        updateUI(null);
        Toast.makeText(getApplicationContext(), "Logout Done", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressBar();
        if (user != null) {
            /*mBinding.status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mBinding.emailPasswordButtons.setVisibility(View.GONE);
            mBinding.emailPasswordFields.setVisibility(View.GONE);
            mBinding.signedInButtons.setVisibility(View.VISIBLE);*/

            if (user.isEmailVerified()) {
                //mBinding.verifyEmailButton.setVisibility(View.GONE);
            } else {
                //mBinding.verifyEmailButton.setVisibility(View.VISIBLE);
            }
        } else {
           // mBinding.status.setText(R.string.signed_out);
            //mBinding.detail.setText(null);

            //mBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
           // mBinding.emailPasswordFields.setVisibility(View.VISIBLE);
           // mBinding.signedInButtons.setVisibility(View.GONE);
        }
    }
}




