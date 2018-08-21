package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.nabee.gymsocialmedia.R;

import java.util.ArrayList;
import java.util.List;

public class CardioFragment extends Fragment {
    private static final String TAG = "CardioFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cardio,container,false);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("push ups");
        spinnerArray.add("bench press");
        spinnerArray.add("flys");
        spinnerArray.add("dumbell press");
        spinnerArray.add("new exercise");


        //create array adapter to make our list of exercises usable for the drop down menu
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner)view.findViewById(R.id.cardioList);
        sItems.setAdapter(adapter);

        return view;
    }



    public void fillSpinner(List list){


    }

    public void createExerList(){


    }
}
