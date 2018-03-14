package com.stufeed.android.view.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by sowmitras on 8/5/17.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public static int day;
    public static int month;
    public static int year;
    public static String currentdate;
    TextView textView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH)+1;
         day = c.get(Calendar.DAY_OF_MONTH);
        currentdate = ""+day+"/"+month+"/"+year;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //Toast.makeText(getActivity(), ""+day+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();
        this.day = day;
        this.month = month;
        this.year = year;
        this.currentdate = ""+day+"/"+(month+01)+"/"+year;
        getDateSet(textView);
    }

    public static void getDateSet(TextView textView) {
        textView.setText(currentdate);
    }

    public DatePickerFragment(TextView textView) {
        this.textView = textView;
    }

}
