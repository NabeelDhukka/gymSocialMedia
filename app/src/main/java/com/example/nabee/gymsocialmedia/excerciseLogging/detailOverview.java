package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabee.gymsocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nabee on 6/16/2018.
 */

public class detailOverview extends AppCompatActivity {

    String TAG = "detail OverView Activity";
    //declare UI elements
    GraphView graph;
    TextView name;

    //Firebase reference
    private DatabaseReference mDbRef;
    public FirebaseDatabase mFirebaseDatabase;
    public FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_overview_layout);
        init();


        // Read from the database
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Object value = dataSnapshot.getValue();
//                Log.d(TAG, "\nValue is: VALUE IS " + value + "\n");
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                String userID = user.getUid();
                extractData(dataSnapshot, userID);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    /*-----------------------simple method to read user workout stats from db--------------------------------------*/
    public void extractData(DataSnapshot dataSnapshot, String uid){

        //Data point series declaration
        LineGraphSeries<DataPoint> series;

        DataSnapshot shot = dataSnapshot.child("userExer").child(uid).child("Exercises");
        Log.d(TAG, "extractData: NEW SNAPSHOT---------------->"+shot);
        //array List to store all entries of workouts for a given Exercise
        ArrayList<String> entries = new ArrayList<>();

        //iterate through each Exercise the data snapshot holds
        for( DataSnapshot child : shot.getChildren()){

            Log.d(TAG, "extractData: WHAT IS CHILD DATASNAPSHOT------>"+ child);


            //if we found the correct exercise and its not emptry then look for date entries
            if(child != null && child.getKey().equals("bench press")){
                Log.d(TAG, "extractData: INSIDE IF STATMENT TO ADD ENTRIES");
                    //now look inside the child data snapshot to extract dates of workouts
                    for (DataSnapshot dates : child.getChildren()){
                        Log.d(TAG, "extractData: INSIDE FOR LOOP TO GET DATES");
                           entries.add(dates.getKey());
                        Log.d(TAG, "extractData: ENTRIES ARE AS FOLLOWS ---------------->"+entries);

                    }

            }

        }

        //get snapshot of calandar tree to search for exercise stats
        //array list to hold exerciseStats objects to populate graph
        ArrayList<exerciseStats> points = new ArrayList<>();
        DataSnapshot shot2 = dataSnapshot.child("calendar").child(uid);
        for (DataSnapshot x : shot2.getChildren()){
            Log.d(TAG, "extractData: INSIDE CALENDAR!!!!!!!!!!!!!!!!");
            Log.d(TAG, "extractData: SNAPSHOT HAS ---------->"+ x);
            Log.d(TAG, "extractData: BLAHABLAFBADJO-------------->"+ x.child("bench press").child("reps").getValue());
            //int l = Integer.valueOf(x.child("bench press").child("reps").getValue().toString());
            //Log.d(TAG, "extractData: SSSSSSSSSSSSSSSSSSSSSSSSSSSSS---------------->"+l);
                //look through our found entries to see if a date on the calendar matches
                for(String i : entries){
                    //match found! so stats for exercise must be here
                    Log.d(TAG, "extractData: STRING I IS--------------------->"+i);
                    if (i.equals(x.getKey())){
                        Log.d(TAG, "extractData: FOUND A MATCH YO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        //get values of exercise we want at found date
                        exerciseStats stats = new exerciseStats();

                        Log.d(TAG, "extractData: REPS IS THIS VALUE------------------------------>"+ x.child("bench press").child("reps"));
                        double reps = Double.valueOf(x.child("bench press").child("reps").getValue().toString());
                        Log.d(TAG, "extractData: REPS IS THIS NOW----------------->"+reps);
                        double weight = Double.valueOf(x.child("bench press").child("weight").getValue().toString());
                        Log.d(TAG, "extractData: WEIGHT AND REPS ARE -------------->"+weight+" AND "+reps);
                        stats.setReps(reps);
                        stats.setWeight(weight);
                        points.add(stats);
                    }

                }

        }

        //plot found data points for reps and weight
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        int count = points.size();
        int index;
        DataPoint[] values = new DataPoint[count];
        Log.d(TAG, "extractData: POINTS HOLDS--------------------->"+points);
        index = 0;
        for (exerciseStats dp : points) {
            //DataPoint v = new DataPoint(dp.getReps(), dp.getWeight());
            //Log.d(TAG, "extractData: REPS ARE --------------->"+dp.getReps());
//            if(index < count){
//            //values[index] = v;
//                Log.d(TAG, "extractData: VALUES ADDED ARE ------------------->"+index+"\n DATA POINT IT------------------------>"+v);
//                Log.d(TAG, "extractData: VALUES AT THIS INDEX IS ------------------------>"+ values[index]);
//            }
//            index++;
//            Log.d(TAG, "extractData: DATAPOINT ARRAY HAS -------------------------->"+ values);
//
                series.appendData(new DataPoint(index,dp.getWeight()),true, count);
                index++;
        }

        graph.addSeries(series);


        Log.d(TAG, "extractData: ARRAY OF DATES HOLDS--------->"+entries);



    }

    /*-----------------------simple method to plot data points--------------------------------------*/
//    public DataPoint[] generateData( ArrayList<exerciseStats> pts) {
//        int count = pts.size();
//        int index;
//        DataPoint[] values = new DataPoint[count];
//        for (index = 0; index < count ; index++) {
//
//            DataPoint v = new DataPoint(pts.get(index).getReps(), pts.get(index).getWeight());
//            values[index] = v;
//
//        }
//        return values;
//    }

    /*-----------------------simple method to show toasts--------------------------------------*/
    private void Toasts(String msg){

        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }

    /*-----------------------simple method to initialize UI components--------------------------*/
    public void init(){
        //graph = (GraphView) findViewById(R.id.graph);
        name = (TextView)findViewById(R.id.exerciseName);
        //initialize firebase stuff
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }
}
