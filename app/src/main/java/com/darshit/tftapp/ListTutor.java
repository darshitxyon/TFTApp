package com.darshit.tftapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ListTutor extends Activity {
    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    String college,course,collegefilled,coursefilled;
    private ListView mListView;
    private ArrayList<String> array,array1;
    String link,value;
    Context C;
    Button btndown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tutor);
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        collegefilled = bundle.getString("Stuff");
        coursefilled = bundle.getString("Stuff1");
        btndown = (Button) findViewById(R.id.buttondownload);
        mListView = (ListView) findViewById(R.id.listview);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(collegefilled);
        stringBuilder.append(coursefilled);

        String finalString = stringBuilder.toString();
        toastMessage(finalString);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        array1=new ArrayList<>();

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
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        array  = new ArrayList<>();

        myRef.child("users").orderByChild("userID").equalTo(finalString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*myRef.child("users").orderByChild("userID").equalTo(finalString).addListenerForSingleValueEvent(new ValueEventListener(){

            //in data change method you do the rest of your code
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                String savedCardUser = (String) dataSnapshot.child("Card").getValue();
                toastMessage("onChildAdded");
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            //on canceled.......

        });*/


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                value = mListView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Selected File: " + value, Toast.LENGTH_SHORT).show();
                link=array1.get(position);
                toastMessage(link);

                /*myRef.child("users").child("filename").equalTo(value).addListenerForSingleValueEvent(new ValueEventListener() {

                    //in data change method you do the rest of your code
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot ds = (DataSnapshot) dataSnapshot.getChildren();
                        UserInformation uInfo = new UserInformation();
                        uInfo.setFileLink(ds.getValue(UserInformation.class).getFileLink());
                        link = uInfo.getFileLink();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });*/


            }
        });
        /*btndown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(link));
               startActivity(i);
            }
        });*/




    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            UserInformation uInfo = new UserInformation();
            uInfo.setCollege(ds.getValue(UserInformation.class).getCollege()); //set the name
            uInfo.setCourse(ds.getValue(UserInformation.class).getCourse()); //set the email
            uInfo.setFileLink(ds.getValue(UserInformation.class).getFileLink()); //set the phone_num
            uInfo.setFilename(ds.getValue(UserInformation.class).getFilename());
            uInfo.setTutorName(ds.getValue(UserInformation.class).getTutorName());
            uInfo.setUserID(ds.getValue(UserInformation.class).getUserID());
            //toastMessage(uInfo.getFilename());
            //display all the information
            Log.d(TAG, "showData: College: " + uInfo.getCollege());
            Log.d(TAG, "showData: course: " + uInfo.getCourse());
            Log.d(TAG, "showData: FileLink: " + uInfo.getFileLink());
            Log.d(TAG, "showData: Filename: " + uInfo.getFilename());
            Log.d(TAG, "showData: TutorName: " + uInfo.getTutorName());
            Log.d(TAG, "showData: UserID: " + uInfo.getUserID());
            StringBuilder stringBuilder1 = new StringBuilder();

            stringBuilder1.append(uInfo.getTutorName());
            stringBuilder1.append(" : ");
            stringBuilder1.append(uInfo.getFilename());

            String finalString1 = stringBuilder1.toString();

            //if(uInfo.getCollege()==college && uInfo.getCourse()==course) {

                array.add(finalString1);
                array1.add(uInfo.getFileLink());

            //}
            final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);


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


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void download(View v)
    {
        new DownloadFile().execute(link, value+".pdf");
        Toast.makeText(getApplicationContext(), "DOWNLOADING COMPLETED", Toast.LENGTH_SHORT).show();
    }

    public void view(View v) throws FileNotFoundException {
        //File pdfFile = new File(Environment.getExternalStorageDirectory().toString() + "/testthreepdf/" + value +".pdf");  // -> filename = maven.pdf
        //Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().toString() + "/testthreepdf/" + value +".pdf"), "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = link;
            //toastMessage(strings[0]);// -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = value+".pdf";
            //toastMessage(fileName);// -> maven.pdf
            String extStorageDirectory = null;
            extStorageDirectory = Environment.getExternalStorageDirectory().toString() ;

            File folder = new File(extStorageDirectory);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }
    }



}
