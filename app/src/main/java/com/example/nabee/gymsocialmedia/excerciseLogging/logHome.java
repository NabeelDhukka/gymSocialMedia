package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nabee.gymsocialmedia.R;
import com.example.nabee.gymsocialmedia.Social.socialFeed;
import com.example.nabee.gymsocialmedia.profilePage.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Nabee on 6/8/2018.
 */

public class logHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public final String TAG = "Logger activity";
    //user input fields
    public BottomNavigationView navigationView;
    public FloatingActionButton inputExer;
    public ListView currWorkouts;

    //firebase stuff
    private DatabaseReference mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_home_layout);
        setNavigationView();
        init();

        //onClick launch Exercise input form
        inputExer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(logHome.this, exerciseForm.class);
                startActivity(intent1);
            }
        });
    }

    /*--------------------------------------------fill todays exercise to list view----------------------------------------------------*/
    public void fillListView(final DataSnapshot dataSnapshot, String uid){

        final String userID = uid;
        final DataSnapshot shot3 = dataSnapshot.child("userExer").child(userID).child("Exercises");
        ArrayList<String> names = new ArrayList<>();
        for (DataSnapshot s : shot3.getChildren()){
            names.add(s.getKey());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        currWorkouts.setAdapter(arrayAdapter);

    }
    /*--------------------------------------------initialize UI Elements/Starting Variables----------------------------------------------------*/
    public void init(){

        inputExer = (FloatingActionButton)findViewById(R.id.logExer);
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
