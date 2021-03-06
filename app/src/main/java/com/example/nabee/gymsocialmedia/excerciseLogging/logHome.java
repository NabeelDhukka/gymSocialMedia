package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nabee on 6/8/2018.
 */

public class logHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public final String TAG = "Logger activity";

    //TEST
    private overviewAdapter mAdapter;

    //user input fields
    public BottomNavigationView navigationView;
    public FloatingActionButton inputExer;
    public Button dateButton;
    public ListView currWorkouts;
    public PopupWindow mPopupWindow;

    //firebase stuff
    private DatabaseReference mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_home_layout);
        setNavigationView();
        init();
        //send data to firebase DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        final String userID = user.getUid();
        mref = mFirebaseDatabase.getInstance().getReference().child(userID);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String userID = user.getUid();
                fillListView(dataSnapshot,userID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //onClick launch Exercise input form
        inputExer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(logHome.this, exerciseForm.class);
                startActivity(intent1);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                // Inflate the custom layout/view
                final View customView = inflater.inflate(R.layout.calendar_popup,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                );
                mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
            }
        });

    }

    /*--------------------------------------------fill todays exercise to list view----------------------------------------------------*/
    public void fillListView(DataSnapshot dataSnapshot, String uid){

        //final String userID = uid;
        DataSnapshot shot3 = dataSnapshot.child("calendar").child(uid);
        Date currDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("MM-dd-yyyy");
        String mDate = sdf.format(currDate);
        ArrayList<getOverviewStats> customEntry = new ArrayList<>();

        for (DataSnapshot date : shot3.getChildren()){
            if(date.getKey().equals(mDate)) {

                for (DataSnapshot todayExer : date.getChildren()) {
                    //names.add(todayExer.getKey());
                    getOverviewStats newStats = new getOverviewStats();
                    String liName = todayExer.getKey();
                    newStats.setName(liName);
                    for(DataSnapshot stat : todayExer.getChildren()){
                        if (stat.getKey().equals("Reps")){
                            newStats.setReps(stat.getValue().toString());
                        }
                        else if (stat.getKey().equals("Sets")){
                            newStats.setSets(stat.getValue().toString());
                        }
                        else if (stat.getKey().equals("Weight")){
                            newStats.setWeight(stat.getValue().toString());
                        }
                        else if (stat.getKey().equals("dist")){
                            newStats.setDist(stat.getValue().toString());
                        }
                        else if (stat.getKey().equals("time")){
                            newStats.setTime(stat.getValue().toString());
                        }
                    }
                    Log.d(TAG, "fillListView: ARRAY LIST STARTING HERE!!!!!!!!!!!!!!!!!!!!!");
                    Log.d(TAG, "fillListView: ARRAY LIST name ----------------------------->"+newStats.getName());
                    Log.d(TAG, "fillListView: ARRAY LIST reps ----------------------------->"+newStats.getReps());
                    Log.d(TAG, "fillListView: ARRAY LIST sets ----------------------------->"+newStats.getSets());
                    Log.d(TAG, "fillListView: ARRAY LIST weight ----------------------------->"+newStats.getWeight());


                    customEntry.add(newStats);

                }


            }
        }

        mAdapter = new overviewAdapter(this,customEntry);
        currWorkouts.setAdapter(mAdapter);


       // ArrayAdapter<getOverviewStats> arrayAdapter = new ArrayAdapter<getOverviewStats>(this,android.R.layout.custom_list_item,customEntry);
        //currWorkouts.setAdapter(arrayAdapter);

    }
    /*--------------------------------------------initialize UI Elements/Starting Variables----------------------------------------------------*/
    public void init(){

        inputExer = (FloatingActionButton)findViewById(R.id.logExer);
        dateButton = (Button)findViewById(R.id.dateButton);
        currWorkouts = (ListView)findViewById(R.id.todaysExerList);


    }
    /*--------------------------------------------Set up Navigation bottom view----------------------------------------------------*/
    public void setNavigationView(){
        //declare bottom navigation view and make sure proper item is higlighted for current activity
        navigationView = (BottomNavigationView) findViewById(R.id.botNavBar);
        navigationView.getMenu().getItem(1).setChecked(true);

        //Navigation to take us to other activities
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.profile) {
                    Intent intent = new Intent(logHome.this, profile.class);
                    navigationView.getMenu().getItem(2).setChecked(true);
                    startActivity(intent);
                    overridePendingTransition(0,0);

                } else if (itemid == R.id.socialFeed) {
                    Intent intent = new Intent(logHome.this, socialFeed.class);
                    navigationView.getMenu().getItem(1).setChecked(true);
                    startActivity(intent);
                    overridePendingTransition(0,0);

                }

                return true;
            }
        });


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
        return true;
    }

    //simple method to display custom toasts
    private void Toasts(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }


}
