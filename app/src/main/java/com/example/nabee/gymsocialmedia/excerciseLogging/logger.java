package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabee.gymsocialmedia.R;
import com.example.nabee.gymsocialmedia.Social.socialFeed;
import com.example.nabee.gymsocialmedia.profilePage.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Nabee on 6/8/2018.
 */

public class logger extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public final String TAG = "Logger activity";
    //user input fields
    public BottomNavigationView navigationView;
    public EditText exercise;
    public EditText weight;
    public EditText sets;
    public EditText reps;
    public Button addbtn, graphbtn;

    //firebase stuff
    private DatabaseReference mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logger_layout);
        init();

        //declare bottom navigation view and make sure proper item is higlighted for current activity
        navigationView = (BottomNavigationView) findViewById(R.id.botNavBar);
        navigationView.getMenu().getItem(1).setChecked(true);

        //Navigation to take us to other activities
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.profile) {
                    Intent intent = new Intent(logger.this, profile.class);
                    navigationView.getMenu().getItem(2).setChecked(true);
                    startActivity(intent);

                } else if (itemid == R.id.socialFeed) {
                    Intent intent = new Intent(logger.this, socialFeed.class);
                    navigationView.getMenu().getItem(1).setChecked(true);
                    startActivity(intent);

                }

                return true;
            }
        });

        //gather data from all edit texts and launch saveData() on Click of addbtn
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currExercise = exercise.getText().toString();
                String tmpWeight = weight.getText().toString();
                String tmpSets = sets.getText().toString();
                String tmpReps = reps.getText().toString();

                int currWeight = Integer.parseInt(tmpWeight);
                int currSets = Integer.parseInt(tmpSets);
                int currReps = Integer.parseInt(tmpReps);
                saveData(currExercise, currWeight, currSets, currReps);


            }
        });

        //Navigate to detailOverview page so user can see their graphed data
        graphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logger.this, detailOverview.class);
                //intent.putExtra("ref", mref);
                startActivity(intent);
            }
        });
    }

    //save data to firebase
    public void saveData(String ex, int w, int s, int r){

        //send data to firebase DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();

         //Read from the database
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //write to DB
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userID = user.getUid();

        //create formatted date to send up to DB
        //formats date to MM-dd-yyyy in a String object ex: 06/15/2018
          Date currDate = new Date(System.currentTimeMillis());
          SimpleDateFormat sdf;
          sdf = new SimpleDateFormat("MM-dd-yyyy");
          String mDate = sdf.format(currDate);

          mref.child("userExer").child(userID).child("Exercises").child(ex).child(mDate).setValue(true);
          mref.child("calendar").child(userID).child(mDate).child(ex).child("weight").setValue(w);
          mref.child("calendar").child(userID).child(mDate).child(ex).child("sets").setValue(s);
          mref.child("calendar").child(userID).child(mDate).child(ex).child("reps").setValue(r);


        //clear data in fields when saved
        exercise.setText("", TextView.BufferType.EDITABLE);
        sets.setText("", TextView.BufferType.EDITABLE);
        weight.setText("", TextView.BufferType.EDITABLE);
        reps.setText("", TextView.BufferType.EDITABLE);



        //success Toast
        Toasts("EXERCISE SAVED!");






    }

    /*-----------------------simple method to show toasts--------------------------------------*/
    private void Toasts(String msg){

        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }

    //inititalize variables
    public void init(){

        //user data
        exercise = (EditText)findViewById(R.id.exerciseName);
        weight = (EditText)findViewById(R.id.weight);
        sets =(EditText)findViewById(R.id.sets);
        reps=(EditText)findViewById(R.id.reps);
        addbtn =(Button)findViewById(R.id.addWorkout);
        graphbtn = (Button)findViewById(R.id.detailView);



    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected. Consider setting non-selectable items as disabled preemptively to
     * make them appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
