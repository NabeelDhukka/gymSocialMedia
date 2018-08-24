package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;


import com.example.nabee.gymsocialmedia.R;
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
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CardioFragment extends Fragment {
    private static final String TAG = "CardioFragment";

    public PopupWindow mPopupWindow;
    public Button newaddbtn;

    //firebase stuff
    private DatabaseReference mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cardio,container,false);

        //send data to firebase DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();



        //write to DB
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        final String userID = user.getUid();


        //Read from the database
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                final DataSnapshot cardioExersdb = dataSnapshot.child("ExerChars").child(userID).child("Exers").child("Cardio");
                final List<String> list = createExerList(cardioExersdb);
                final Spinner exerDropDown = fillSpinner(list, view);

                exerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        final AdapterView<?> tmp = adapterView;
                        if(i==0){
                            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                        }

                        if (exerDropDown.getItemAtPosition(i).equals("Add New Exercise")){

                            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            // Inflate the custom layout/view
                            View customView = inflater.inflate(R.layout.cardio_popup_window_layout,null);

                            mPopupWindow = new PopupWindow(
                                    customView,
                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT,
                                    true
                            );

                            final EditText exerNameField = (EditText)customView.findViewById(R.id.newExerName);

                            newaddbtn = (Button)customView.findViewById(R.id.newExerAddbtn);
                            newaddbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String name = exerNameField.getText().toString();
                                    addNewExercise(name);
                                    //exerDropDown.setSelection(selectNewExerOnSpinner(cardioExersdb,name));
                                    //((TextView) tmp.getChildAt(selectNewExerOnSpinner(cardioExersdb,name))).setText(name);
                                    //DataSnapshot blah = dataSnapshot.child("ExerChars").child(userID).child("Exers").child("Cardio");
                                    //Toasts(selectNewExerOnSpinner(cardioExersdb,name).toString());
                                    mPopupWindow.dismiss();
                                }
                            });

                            mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                            //((TextView) adapterView.getChildAt();
                            Toasts("I COMPLETED THE POPUP ");



                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        return view;
    }


/*---------------------------------------------fills dropdown menu with a given list of exercises------------------------------------------------*/
public Integer selectNewExerOnSpinner(DataSnapshot snapshot, String target){
    List<String> list = createExerList(snapshot);
    Integer exerPosition = list.indexOf(target);
    return exerPosition;
}

/*---------------------------------------------fills dropdown menu with a given list of exercises------------------------------------------------*/
    public Spinner fillSpinner(List list, View view){

//create array adapter to make our list of exercises usable for the drop down menu
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner)view.findViewById(R.id.cardioList);
        sItems.setAdapter(adapter);
        return sItems;

    }

    public void addNewExercise(String newExerName){


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userID = user.getUid();
        //create formatted date to send up to DB
        //formats date to MM-dd-yyyy in a String object ex: 06/15/2018
//        Date currDate = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf;
//        sdf = new SimpleDateFormat("MM-dd-yyyy");
//        String mDate = sdf.format(currDate);

//        mref.child("userExer").child(userID).child("Exercises").child(ex).child(mDate).setValue(true);
//        mref.child("calendar").child(userID).child(mDate).child(ex).child("weight").setValue(w);
//        mref.child("calendar").child(userID).child(mDate).child(ex).child("sets").setValue(s);
//        mref.child("calendar").child(userID).child(mDate).child(ex).child("reps").setValue(r);

        mref.child("ExerChars").child(userID).child("Exers").child("Cardio").child(newExerName).setValue(true);


    }
    /*---------------------------------------------creates a list of exercises from known exercises in db, based on given filters------------------------------------------------*/

    public List<String> createExerList(DataSnapshot existingExers){
        List<String> spinnerArray = new ArrayList<String>();

        spinnerArray.add("Please Select Exercise...");
        //add exers in the middle so "add new exercise" button stays at last position
        for(DataSnapshot entries : existingExers.getChildren()){
            spinnerArray.add(entries.getKey());

        }

        spinnerArray.add("Add New Exercise");
        //start logic here

        return spinnerArray;
    }

    //simple method to display custom toasts
    private void Toasts(String msg){

        Toast.makeText(getView().getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
