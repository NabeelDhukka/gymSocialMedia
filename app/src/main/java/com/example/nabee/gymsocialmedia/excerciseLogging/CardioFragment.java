package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabee.gymsocialmedia.R;

import java.util.ArrayList;
import java.util.List;

public class CardioFragment extends Fragment {
    private static final String TAG = "CardioFragment";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cardio,container,false);
        final List<String> list = createExerList();
        final Spinner exerDropDown = fillSpinner(list, view);
        exerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                }

                if (exerDropDown.getItemAtPosition(i).equals("Add New Exercise")){

                    //((TextView) adapterView.getChildAt(0)).setText("Custom Exercise ");
                    Toasts("I COMPLETED THE POPUP ");



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
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

    /*---------------------------------------------creates a list of exercises from known exercises in db, based on given filters------------------------------------------------*/

    public List<String> createExerList(){
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Please Select Exercise...");
        spinnerArray.add("Add New Exercise");
        //start logic here

        return spinnerArray;
    }

    //simple method to display custom toasts
    private void Toasts(String msg){

        Toast.makeText(getView().getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
