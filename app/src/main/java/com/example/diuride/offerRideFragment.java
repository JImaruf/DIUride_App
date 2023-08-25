package com.example.diuride;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class offerRideFragment extends Fragment {

    Calendar calendar ;
    EditText timeEt;






    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_offer_ride, container, false);
        timeEt = v.findViewById(R.id.timeet);


        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hours =calendar.get(Calendar.HOUR_OF_DAY);
                int minute =calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                       calendar.set(Calendar.HOUR_OF_DAY,hour);
                       calendar.set(Calendar.MINUTE,min);
                       calendar.setTimeZone(TimeZone.getDefault());


                       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                       String time = simpleDateFormat.format(calendar.getTime());

                       timeEt.setText(time);
                        Toast.makeText(getActivity(), "time : "+time, Toast.LENGTH_SHORT).show();

                    }
                },hours,minute,false);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });

        return v;
    }
}