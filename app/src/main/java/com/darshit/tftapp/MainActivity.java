package com.darshit.tftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends Activity {
    public static final String TAG ="Email_password" ;
    private FirebaseAuth mAuth;
    EditText email_user;
    EditText password_user;
    Button login;
    Button signup,btnSignOut;
    Button tutorlogin;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mAuth = FirebaseAuth.getInstance();
            email_user=(EditText)findViewById(R.id.editText);
            password_user=(EditText)findViewById(R.id.editText2);
            login=(Button) findViewById(R.id.button2);
            signup=(Button) findViewById(R.id.button3);
            btnSignOut=(Button) findViewById(R.id.btnout);
            tutorlogin=(Button) findViewById(R.id.button4);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name1 = user.getDisplayName();
            String email1 = user.getEmail();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        }
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signing Out...", Toast.LENGTH_SHORT).show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAccount(email_user.getText().toString(), password_user.getText().toString());
                //Toast.makeText(getApplicationContext(), "Create Button", Toast.LENGTH_LONG).show();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(email_user.getText().toString(), password_user.getText().toString());
                //Toast.makeText(getApplicationContext(), "Login Button", Toast.LENGTH_LONG).show();
                Intent activityChangeIntent = new Intent(MainActivity.this, SelectForStudent.class);
                startActivity(activityChangeIntent);
            }
        });
        tutorlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                //signIn(email_user.getText().toString(), password_user.getText().toString());
                Intent activityChangeIntent = new Intent(MainActivity.this, TutorLogin.class);
                startActivity(activityChangeIntent);
            }
        });


        }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        String email = email_user.getText().toString();
        String password = password_user.getText().toString();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

        public boolean validateForm(){
            boolean valid = true;


            if (TextUtils.isEmpty(email_user.getText().toString())) {
                email_user.setError("Required.");
                valid = false;
            } else {
                email_user.setError(null);
            }

            if (TextUtils.isEmpty(password_user.getText().toString())) {
                password_user.setError("Required.");
                valid = false;
            } else {
                password_user.setError(null);
            }

            return valid;
        }
}