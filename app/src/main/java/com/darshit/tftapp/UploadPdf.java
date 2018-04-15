package com.darshit.tftapp;

import android.app.Activity;
import android.app.ProgressDialog;
import com.google.firebase.database.DataSnapshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UploadPdf extends Activity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "UploadActivity";
    Spinner spinner1;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    String course,name;
    String college,collegefilled,tutorname;
    EditText FileName;
    TextView courseselect;
    private Button btnUpload,btnNext,btnBack;
    String[] categories1 = { "CSE", "IT", "CCE", "Mech", "IP" };
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Object filePath;
    //private String filePath;
    private String filelink;
    private int REQUEST_CODE_DOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        courseselect=(TextView) findViewById(R.id.textView);
        btnBack = (Button) findViewById(R.id.btnBackImage);
        btnNext = (Button) findViewById(R.id.btnNextImage);
        btnUpload = (Button) findViewById(R.id.btnUploadImage);
        FileName=(EditText) findViewById(R.id.imageName) ;
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        //GET EXTRAS
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        collegefilled = bundle.getString("Stuff1");
        tutorname=bundle.getString("Stuff");




        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String userID = user.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "onDataChange: Added information to database: \n" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });


        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                toastMessage("Signing Out...");
                Intent activityChangeIntent = new Intent(UploadPdf.this, MainActivity.class);
                startActivity(activityChangeIntent);
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //myRef.child("users").child(curruserID).child("Course").setValue(course);

                /*DatabaseReference mostafa = myRef.child("users").child(userID).child("College");

                mostafa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        college = dataSnapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                name = FileName.getText().toString();

                //UPLOAD FILE


                //String path = System.getenv("EXTERNAL_STORAGE");
                //Uri file = Uri.fromFile((Uri) filePath);
                //Uri file= Uri.parse(filePath);

                StorageReference StorageRef =   mStorageRef.child("file/pdfs/"+ name + ".pdf");

                StorageRef.putFile((Uri)filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                filelink=downloadUrl.toString();

                                Log.d(TAG, "onClick: Submit pressed.");
                                toastMessage(filelink);
                                //toastMessage("THE FILLED :"+collegefilled);
                                //toastMessage("THE FILLED :"+course);
                                //toastMessage("THE FILLED :"+name);
                                Log.d(TAG, "onClick: Attempting to submit to database: \n" + "name: " + name + "\n");
                                StringBuilder stringBuilder = new StringBuilder();

                                stringBuilder.append(collegefilled);
                                stringBuilder.append(course);


                                String finalString = stringBuilder.toString();

                                if(!name.equals("") && !collegefilled.equals("") && !course.equals("")&& !filelink.equals("")){

                                    FirebaseUser curruser = mAuth.getCurrentUser();
                                    String curruserID = curruser.getUid();
                                    myRef.child("users").child(name).child("TutorName").setValue(tutorname);
                                    myRef.child("users").child(name).child("FileLink").setValue(filelink);
                                    myRef.child("users").child(name).child("filename").setValue(name);
                                    myRef.child("users").child(name).child("College").setValue(collegefilled);
                                    myRef.child("users").child(name).child("course").setValue(course);
                                    myRef.child("users").child(name).child("userID").setValue(finalString);

                                    toastMessage("done");

                                    toastMessage("Adding to database...");
                                    //reset the text
                                    FileName.setText("");}
                                else{
                                    toastMessage("Fill out all the fields");
                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });






            }
        });




    }



    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"),REQUEST_CODE_DOC);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DOC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


        }
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
        // On selecting a spinner item
        course = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + course, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }




}

