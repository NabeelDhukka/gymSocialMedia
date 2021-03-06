package com.example.nabee.gymsocialmedia.excerciseLogging;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nabee.gymsocialmedia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nabee on 9/6/2018.
 */

public class overviewAdapter extends ArrayAdapter<getOverviewStats> {


        private Context mContext;
        private List<getOverviewStats> statList = new ArrayList<>();

        public overviewAdapter(@NonNull Context context, @LayoutRes ArrayList<getOverviewStats> list) {
            super(context, 0 , list);
            mContext = context;
            statList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;


            getOverviewStats currentExer = statList.get(position);

            if(listItem == null) {
                if (statList.get(position).getWeight() == null && statList.get(position).getSets() == null && statList.get(position).getReps() == null) {
                    listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_cardio, parent, false);

                    TextView liName = (TextView) listItem.findViewById(R.id.nameLi);
                    liName.setText(currentExer.getName());

                    TextView liDist = (TextView) listItem.findViewById(R.id.distValue);
                    liDist.setText(currentExer.getDist());

                    TextView liTime = (TextView) listItem.findViewById(R.id.timeValue);
                    liTime.setText(currentExer.getTime());

                } else {
                    listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_strength, parent, false);

                    TextView liName = (TextView) listItem.findViewById(R.id.nameLi);
                    liName.setText(currentExer.getName());

                    TextView liSetRep = (TextView) listItem.findViewById(R.id.setRepValue);
                    liSetRep.setText(currentExer.getSets()+"/"+currentExer.getReps());

                    TextView liWeight = (TextView) listItem.findViewById(R.id.weightValue);
                    liWeight.setText(currentExer.getWeight());
                }
            }




            return listItem;
        }
    }

