package com.android.sharingtaxi;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AndroidDialogs extends Activity {

    private EditText mDateDisplay;
    private Button mPickDate;

    private EditText mDateDisplay2;
    private Button mPickDate2;

    private int mYear;
    private int mMonth;
    private int mDay;

    private EditText mTimeDisplay;
    private Button mPickTime;

    private int mHour;
    private int mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int CALENDAR_VIEW_ID = 1;
    static final int TIME_DIALOG_ID = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // capture our View elements
        mDateDisplay = (EditText) findViewById(R.id.dateDisplay);
        mPickDate = (Button) findViewById(R.id.pickDate);
        mTimeDisplay = (EditText) findViewById(R.id.timeDisplay);
        mPickTime = (Button) findViewById(R.id.pickTime);


        // add a click listener to the select a date button
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // add a click listener to the select a calendar date button 
        mPickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AndroidDialogs.this, MyCalendar.class); 
                startActivityForResult(intent,CALENDAR_VIEW_ID); 
            }


        });

        // add a click listener to the button
        mPickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });




        // get the current date and time
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // display the current date 
        displayDate();
        displayCalendarViewDate();

        // display the current time
        displayTime();
    }

    // updates the date in the EditText
    private void displayDate() {
        mDateDisplay.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("/")
                .append(mDay).append("/")
                .append(mYear).append(" "));
    }

    // updates the date in the EditText
    private void displayCalendarViewDate() {
        mDateDisplay2.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("/")
                .append(mDay).append("/")
                .append(mYear).append(" "));
    }

    // updates the time we display in the EditText
    private void displayTime() {
        mTimeDisplay.setText(
                new StringBuilder()
                .append(pad(mHour)).append(":")
                .append(pad(mMinute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, 
                int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            displayDate();
        }
    };

    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            displayTime();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this,
                    mTimeSetListener, mHour, mMinute, false);

        }
        return null;
    }   

}