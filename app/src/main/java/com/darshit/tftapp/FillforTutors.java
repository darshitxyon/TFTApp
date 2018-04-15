package com.darshit.tftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillforTutors extends Activity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddToDatabase";
    EditText name1;
    Button go;
    TextView name;
    TextView college;
    Spinner spin;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    String[] categories = { "MIT", "IIT", "VIT", "NIT", "IIIT" };
    String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillfor_tutors);
        spin = (Spinner) findViewById(R.id.spinner);
        name1=(EditText)findViewById(R.id.editText3);
        go=(Button) findViewById(R.id.button5);
        name=(TextView)findViewById(R.id.textView3);
        college=(TextView)findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: Added information to database: \n" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Log.d(TAG, "onClick: Submit pressed.");
                String name = name1.getText().toString();
                String College =item;


                //Log.d(TAG, "onClick: Attempting to submit to database: \n" + "name: " + name + "\n");

                /*if(!name.equals("") && !College.equals("") && !course.equals("")){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    myRef.child("users").child(userID).child("Name").setValue(name);
                    myRef.child("users").child(userID).child("College").setValue(College);
                    myRef.child("users").child(userID).child("Course").setValue(course);
                    myRef.child("users").child(userID).child("DownloadURL").setValue(downloadlink);
                    toastMessage("Adding to database...");
                //reset the text
                name1.setText("");}
                else{
                    toastMessage("Fill out all the fields");
                }*/
                Intent activityChangeIntent = new Intent(FillforTutors.this, UploadPdf.class);
                //Create the bundle
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("Stuff", name);
                bundle.putString("Stuff1",College);

                //Add the bundle to the intent
                activityChangeIntent.putExtras(bundle);
                startActivity(activityChangeIntent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = categories[position];
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO Auto
    }

}
