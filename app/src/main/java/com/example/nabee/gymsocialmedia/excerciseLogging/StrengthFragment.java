package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nabee.gymsocialmedia.R;

public class StrengthFragment extends Fragment {
    private static final String TAG = "StrengthFragment";

    //private Button btnTEST;

    private ToggleButton chestBtn;
    private ToggleButton backBtn;
    private ToggleButton armsBtn;
    private ToggleButton legsBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strength,container,false);

        chestBtn = (ToggleButton) view.findViewById(R.id.chestBtn);
        backBtn = (ToggleButton) view.findViewById(R.id.backBtn);
        armsBtn = (ToggleButton) view.findViewById(R.id.armsBtn);
        legsBtn = (ToggleButton) view.findViewById(R.id.legsBtn);

        chestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                btnToggle(chestBtn);
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


        return view;
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
}
