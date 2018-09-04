package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ToggleButton;

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

public class StrengthFragment extends Fragment {
    private static final String TAG = "StrengthFragment";

    //private Button btnTEST;

    private ToggleButton chestBtn;
    private ToggleButton backBtn;
    private ToggleButton armsBtn;
    private ToggleButton legsBtn;

    //firebase stuff
    private DatabaseReference mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DataSnapshot initShot;

    //popUp fields
    Spinner musclegroups;
    EditText nameField;
    EditText setsField;
    EditText repsField;
    EditText weightField;
    public PopupWindow mPopupWindow;
    public Button newaddbtn;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_strength,container,false);

        //send data to firebase DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        final String userID = user.getUid();
        mref = mFirebaseDatabase.getInstance().getReference().child(userID);

        buttonSetUp(view);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final DataSnapshot strengthExersdb = dataSnapshot.child("ExerChars").child(userID).child("Exers").child("Strength");
                final List<String> listOnChange = createExerList(strengthExersdb);
                final Spinner exerDropDownOnChange = fillSpinner(listOnChange, view);

                exerDropDownOnChange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //final View mainView = view;
                        if(i==0){
                            // ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                        }

                        if (exerDropDownOnChange.getItemAtPosition(i).equals("Add New Exercise")){

                            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            // Inflate the custom layout/view
                            final View customView = inflater.inflate(R.layout.strength_popup_window_layout,null);

                            mPopupWindow = new PopupWindow(
                                    customView,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    true
                            );

                            //final EditText exerNameField = (EditText)customView.findViewById(R.id.newExerName);
                            final Spinner groups = setMuscleGroupSpinner(customView);
                            newaddbtn = (Button)customView.findViewById(R.id.newExerAddbtn);
                            newaddbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //final String name = exerNameField.getText().toString();
                                    //addNewExercise(name);
                                    nameField = (EditText)customView.findViewById(R.id.newExerName);
                                    weightField = (EditText)customView.findViewById(R.id.weightEdittext);
                                    repsField = (EditText)customView.findViewById(R.id.repsEdittext);
                                    setsField = (EditText)customView.findViewById(R.id.setsEdittext);

                                    String name = nameField.getText().toString();
                                    String weight = weightField.getText().toString();
                                    String reps = repsField.getText().toString();
                                    String sets = setsField.getText().toString();
                                    String mgrp = groups.getSelectedItem().toString();

                                    addNewExercise(name, weight, reps, sets, mgrp);
                                    Intent intent = new Intent(getActivity(), logHome.class);
                                    mPopupWindow.dismiss();
                                    startActivity(intent);
                                }
                            });
                            mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                            Toasts("I COMPLETED THE POPUP ");
                        }
//                        else if(i != 0){
//                           // final String name = exerDropDownOnChange.getItemAtPosition(i).toString();
//                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                weightField = (EditText)view.findViewById(R.id.strengthWeight);
                repsField = (EditText)view.findViewById(R.id.strengthReps);
                setsField = (EditText)view.findViewById(R.id.strengthSets);
                Button addbtn = (Button)view.findViewById(R.id.addStrength);

                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name = exerDropDownOnChange.getSelectedItem().toString();
                        String weight = weightField.getText().toString();
                        String reps = repsField.getText().toString();
                        String sets = setsField.getText().toString();
                        ToggleButton checkedButton = findCheckedButton();
                        String mgrp = checkedButton.getText().toString();
                        addNewExercise(name, weight, reps, sets, mgrp);
                        Intent intent = new Intent(getActivity(), logHome.class);
                        startActivity(intent);
                    }
                });

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    public Spinner setMuscleGroupSpinner(View view){
        List<String> mgSpinner = new ArrayList<String>();
        mgSpinner.add("Select Muscle Group");
        mgSpinner.add("Chest");
        mgSpinner.add("Back");
        mgSpinner.add("Arms");
        mgSpinner.add("Legs");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, mgSpinner){
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
        Spinner sItems = (Spinner)view.findViewById(R.id.muscleGroupSpinner);
        sItems.setAdapter(adapter);
        return sItems;
    }
    /*---------------------------------------------creates a list of exercises from known exercises in db, based on given filters------------------------------------------------*/

    public List<String> createExerList(DataSnapshot existingExers){
        List<String> spinnerArray = new ArrayList<String>();

        spinnerArray.add("Please Select Exercise...");
        //add exers in the middle so "add new exercise" button stays at last position
        if(existingExers != null) {
            for (DataSnapshot entries : existingExers.getChildren()) {
                spinnerArray.add(entries.getKey());

            }
        }

        spinnerArray.add("Add New Exercise");
        return spinnerArray;
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
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
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
        Spinner sItems = (Spinner)view.findViewById(R.id.strengthSpinner);
        sItems.setAdapter(adapter);
        return sItems;

    }
    /*-------------------------------------add new exercise to the db-------------------------------*/
    public void addNewExercise(String newExerName, String weight, String reps, String sets, String mgroup){


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userID = user.getUid();
        Date currDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("MM-dd-yyyy");
        String mDate = sdf.format(currDate);

        mref.child("userExer").child(userID).child("Exercises").child(newExerName).child(mDate).setValue(true);
        mref.child("calendar").child(userID).child(mDate).child(newExerName).child("Weight").setValue(weight);
        mref.child("calendar").child(userID).child(mDate).child(newExerName).child("Sets").setValue(sets);
        mref.child("calendar").child(userID).child(mDate).child(newExerName).child("Reps").setValue(reps);
        mref.child("ExerChars").child(userID).child("Exers").child("Strength").child(newExerName).child("Muscle Group").setValue(mgroup);
        Toasts("Saved Exercise!");


    }

    /*-------------------------------------filter list based on Toggle Button picked-------------------------------*/
    public void filterList(String muscleGroup){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String userID = user.getUid();
        //DataSnapshot itemsByMuscleGroup = child("ExerChars").child(userID).child("Strength");



    }
    /*-------------------------------------init start up variables-------------------------------*/
    public void init(){
        //send data to firebase DB
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        final String userID = user.getUid();
        mref = mFirebaseDatabase.getInstance().getReference().child(userID);
    }


    /*-------------------------------------SetUp all muscle group Buttons-------------------------------*/
    public void buttonSetUp(View view){

        chestBtn = (ToggleButton) view.findViewById(R.id.chestBtn);
        backBtn = (ToggleButton) view.findViewById(R.id.backBtn);
        armsBtn = (ToggleButton) view.findViewById(R.id.armsBtn);
        legsBtn = (ToggleButton) view.findViewById(R.id.legsBtn);

        chestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                btnToggle(chestBtn);
                //if(chestBtn.isChecked()){
                    //filterList();
                //}
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                btnToggle(backBtn);
            }
        });
        armsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                btnToggle(armsBtn);
            }
        });
        legsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                btnToggle(legsBtn);
            }
        });


    }

    public void btnToggle(ToggleButton btn){
        if (btn.isChecked()==false){
            uncheckAllBtns();
        }
        else{
            uncheckAllBtns();
            btn.setChecked(true);
        }
    }

    public void uncheckAllBtns(){
        chestBtn.setChecked(false);
        backBtn.setChecked(false);
        armsBtn.setChecked(false);
        legsBtn.setChecked(false);
    }

    public ToggleButton findCheckedButton(){
        List<ToggleButton>mgbuttons = new ArrayList<ToggleButton>();
        mgbuttons.add(chestBtn);
        mgbuttons.add(backBtn);
        mgbuttons.add(armsBtn);
        mgbuttons.add(legsBtn);

        for (ToggleButton curr : mgbuttons){
            if(curr.isChecked()){
                return curr;
            }
        }
        return null;


    }
    //simple method to display custom toasts
    private void Toasts(String msg){

        Toast.makeText(getView().getContext(), msg, Toast.LENGTH_LONG).show();
    }

}
