package com.android.sharingtaxi;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.android.sharingtaxi.adapter.TrajetAdapter;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.TrajetRepository;
/*import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
*/
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MonTrajetActivity extends Activity
{

    EditText arrivee;
    EditText depart;
    
    private EditText mDateDisplay;
    private ImageButton mPickDate;

    String addressString ="Départ";
    private int mYear;
    private int mMonth;
    private int mDay;
    
    private EditText mTimeDisplay;
    private ImageButton mPickTime;

    private int mHour;
    private int mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int CALENDAR_VIEW_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    private Button home;
    private int userID = 0;

    private Button rechercher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // remove action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //Fin remove action bar
        
        Bundle infoUser = this.getIntent().getExtras();
        if (infoUser != null)
            userID = infoUser.getInt("userID"); 
        
        setContentView(R.layout.activity_mon_trajet);
        
        
        rechercher = (Button)findViewById(R.id.rechercher);

        home = (Button)findViewById(R.id.home);
        
        home.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MonTrajetActivity.this, Main2Activity.class);
                startActivityForResult(intent,1);
            }
        });
        
        rechercher.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MonTrajetActivity.this, RechercheActivity.class);
                startActivityForResult(intent,1);
            }
        });
        
     // capture our View elements
        mDateDisplay = (EditText) findViewById(R.id.dateDisplay);
        mPickDate = (ImageButton) findViewById(R.id.pickDate);
        mTimeDisplay = (EditText) findViewById(R.id.timeDisplay);
        mPickTime = (ImageButton) findViewById(R.id.pickTime);


        // add a click listener to the select a date button
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
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

        // display the current time
        displayTime();

        rechercher = (Button)findViewById(R.id.rechercher);

        rechercher.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                depart = (EditText)findViewById(R.id.depart);
                arrivee = (EditText)findViewById(R.id.arrivee);
                String monDepart = depart.getText().toString();
                String monArrivee = arrivee.getText().toString();
                String dateDepart = mDateDisplay.getText().toString();
                String heureDepart = mTimeDisplay.getText().toString();
                Intent intent = new Intent(MonTrajetActivity.this, RechercheActivity.class);
                intent.putExtra("pointDepart", monDepart);
                intent.putExtra("pointArrivee", monArrivee);
                intent.putExtra("dateDepart", dateDepart);
                intent.putExtra("heureDepart", heureDepart);
                intent.putExtra("userID", userID);
                startActivityForResult(intent,1);
            }
        });
      
    }
  
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //getMenuInflater().inflate(R.menu.menu_course, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
 
            return super.onContextItemSelected(item);
    }
    
    
 
   
 // updates the date in the EditText
    private void displayDate() {
        if(mMonth>10){
                mDateDisplay.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("/")
                .append(mMonth + 1).append("/")
                //.append(mDay).append("/")
                .append(mYear).append(" "));
        }
        else{
            mDateDisplay.setText(
            new StringBuilder()
            .append(mDay).append("/")
            .append("0").append(mMonth + 1).append("/")
            .append(mYear).append(" "));
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_mon_trajet, menu);
        return true;
    }

}
