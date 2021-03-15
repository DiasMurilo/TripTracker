package com.example.triptracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Button mLogin, mRegister;
    EditText mEmail, mPass;

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //mAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.buttonLogin);
        mEmail = findViewById(R.id.editTextEmail);
        mPass = findViewById(R.id.editTextPassword);
        mRegister = findViewById(R.id.buttonRegister);

        updateFieldsLogin();

        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(checkEmailAndPass() == true){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    savePreferencesLogin();
                    intentBackToHome();
                }
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


    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }





// Create a new createAccount method which takes in an email address and password, validates them and then creates a new user with the createUserWithEmailAndPassword method.
mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

            // ...
        }
    });*/



}

