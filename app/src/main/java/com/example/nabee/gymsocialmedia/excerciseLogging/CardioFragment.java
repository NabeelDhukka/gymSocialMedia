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
        View view = inflater.inflate(R.layout.fragment_cardio,container,false);

        return view;
    }

//    public void fillSpinner(){
//        //list to fill spinner
//        ArrayList<String> exers = new ArrayList<>();
//        exers.add("new exer");
//        ArrayAdapter<String> exerListAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item ,exers);
//        exerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Spinner elst = null;
//        Spinner elst = (Spinner)findViewById(R.id.cardioList);
//        elst.setAdapter(exerListAdapter);
//        findVire
//    }
}
