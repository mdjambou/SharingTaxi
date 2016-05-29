package com.android.sharingtaxi;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.DemandeTrajet;
import com.android.sharingtaxi.adapter.Trajet;
import com.android.sharingtaxi.adapter.TrajetAdapter;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.DemandeTrajetRepository;
import com.android.sharingtaxi.business.TrajetRepository;
import com.google.android.maps.GeoPoint;
/*import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;*/


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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Main2Activity extends Activity
{

    private Button cocab;
    private Button trajet;
    private Button tarif;
    private Button aide;
    
    
    //List view param
    private ListView listeViewTrajet;
    private TrajetAdapter adapter;
    private TrajetRepository trajetRepository;
    private AlerteRepository alerteRepository;
    //Fin list view param
   // private MapController mapcontroller;

    private Button rechercheRapide;
    private Button rechercheAvancee;

    private int userID = 0;
    
    
    EditText arrivee;
    EditText depart;
    
    private Button mDateDisplay;

    String addressString ="Départ";
    private int mYear;
    private int mMonth;
    private int mDay;
    //private MyLocationOverlay myLocation = null;
    
    private Button mTimeDisplay;

    private int mHour;
    private int mMinute;

    static final int DATE_DIALOG_ID = 0;
    static final int CALENDAR_VIEW_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    
    /**********************/
    private static final String LOG_TAG = "ExampleApp";
    
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyAYbCBcY-lrLGgzzVCplWeKTQrOgupPXfM";

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;
        
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:fr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            
            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        
        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;
        
        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        
        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }
   /*************************/ 
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //action bar
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle("CoCab");
        //Fin  action bar
        setContentView(R.layout.activity_main2);
        
        AutoCompleteTextView depart = (AutoCompleteTextView) findViewById(R.id.depart);
        depart.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
 
        AutoCompleteTextView arrivee = (AutoCompleteTextView) findViewById(R.id.arrivee);
        arrivee.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        
        Bundle infoUser = this.getIntent().getExtras();
        if (infoUser != null){
            userID = infoUser.getInt("userID"); 
            if(infoUser.getString("pointDepart") != null){
                String ptDep =infoUser.getString("pointDepart");
                depart = (AutoCompleteTextView) findViewById(R.id.depart);
                depart.setText(ptDep);
            }
            if(infoUser.getString("pointArrivee") != null){
                String ptArr =infoUser.getString("pointArrivee");
                arrivee = (AutoCompleteTextView) findViewById(R.id.arrivee);
                arrivee.setText(ptArr);
            }
        }

//Localisation
 
        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
        
        Location l = locationManager.getLastKnownLocation(provider);
        
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
       // updateWithNewLocation(l);
        
        //locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
        locationManager.requestSingleUpdate(provider, locationListener, null);
        
     // Date et heure
        mDateDisplay = (Button) findViewById(R.id.dateDisplay);
        mTimeDisplay = (Button) findViewById(R.id.timeDisplay);


        // add a click listener to the select a date button
        mDateDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // add a click listener to the button
        mTimeDisplay.setOnClickListener(new View.OnClickListener() {
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
 
        rechercheRapide = (Button)findViewById(R.id.rechercheRapide);

        rechercheRapide.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                AutoCompleteTextView ptdepart = (AutoCompleteTextView) findViewById(R.id.depart);
                AutoCompleteTextView ptarrivee = (AutoCompleteTextView) findViewById(R.id.arrivee);;
                String monDepart = ptdepart.getText().toString();
                String monArrivee = ptarrivee.getText().toString();
                String dateDepart = mDateDisplay.getText().toString();
                String heureDepart = mTimeDisplay.getText().toString();
                if((monDepart == null || monDepart.equals("")) || (monArrivee == null || monArrivee.equals(""))){
                    Toast.makeText(getApplicationContext(), "Veuillez saisir les adresses de départ et d'arrivée!", Toast.LENGTH_SHORT).show();  
                }else{   
                    Calendar currentDate = Calendar.getInstance(); //Get the current date
                    SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yy"); //format it as per your requirement
                    String dateNow = formatter.format(currentDate.getTime());
                    
                    Date dateChoosen = null;
                    ParsePosition pos = new ParsePosition(0);
                    dateChoosen = formatter.parse(dateDepart,pos);
                    Date dateJour = currentDate.getTime();
                    int day = dateJour.getDay();
                    dateJour.setDate(day+1);
                    
                    if(dateChoosen.compareTo(dateJour)<0){
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner une date de départ ultérieure à la date actuelle.", Toast.LENGTH_SHORT).show();  
                    }
                    else{
                        Intent intent = new Intent(Main2Activity.this, RechercheActivity.class);
                        intent.putExtra("pointDepart", monDepart);
                        intent.putExtra("pointArrivee", monArrivee);
                        intent.putExtra("dateDepart", dateDepart);
                        intent.putExtra("heureDepart", heureDepart);
                        intent.putExtra("userID", userID);
                        startActivityForResult(intent,1);                    }
                }
            }
        });
        
        
        rechercheAvancee = (Button)findViewById(R.id.rechercheAvancee);
        rechercheAvancee.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                AutoCompleteTextView ptdepart = (AutoCompleteTextView) findViewById(R.id.depart);
                AutoCompleteTextView ptarrivee = (AutoCompleteTextView) findViewById(R.id.arrivee);;
                String monDepart = ptdepart.getText().toString();
                String monArrivee = ptarrivee.getText().toString();
                String dateDepart = mDateDisplay.getText().toString();
                String heureDepart = mTimeDisplay.getText().toString();
                if((monDepart == null || monDepart.equals("")) || (monArrivee == null || monArrivee.equals(""))){
                    Toast.makeText(getApplicationContext(), "Veuillez saisir les adresses de départ et d'arrivée!", Toast.LENGTH_SHORT).show();  
                }else{   
                    Calendar currentDate = Calendar.getInstance(); //Get the current date
                    SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yy"); //format it as per your requirement
                    String dateNow = formatter.format(currentDate.getTime());
                    
                    Date dateChoosen = null;
                    ParsePosition pos = new ParsePosition(0);
                    dateChoosen = formatter.parse(dateDepart,pos);
                    Date dateJour = currentDate.getTime();
                    int day = dateJour.getDay();
                    dateJour.setDate(day+1);
                    
                    if(dateChoosen.compareTo(dateJour)<0){
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner une date de départ ultérieure à la date actuelle.", Toast.LENGTH_SHORT).show();  
                    }
                    else{
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        intent.putExtra("pointDepart", monDepart);
                        intent.putExtra("pointArrivee", monArrivee);
                        intent.putExtra("dateDepart", dateDepart);
                        intent.putExtra("heureDepart", heureDepart);
                        intent.putExtra("userID", userID);
                        startActivityForResult(intent,1);                    }
                    }
                }
        });
        
        // Listview tous les trajets
        listeViewTrajet = (ListView) findViewById(R.id.listViewTrajet);
        trajetRepository = new TrajetRepository(this);
        alerteRepository = new AlerteRepository(this);
        
        trajetRepository.Open();
        alerteRepository.Open();
        
        List<Alerte> mesAlertes = new ArrayList<Alerte>();
        List<Trajet> mesTrajets = new ArrayList<Trajet>();
        mesAlertes = alerteRepository.GetAll();
        int i = 0;
        while((i<mesAlertes.size()) && (mesTrajets.size()<5)){
            if(mesAlertes.get(i).getUserID() == userID){
                Trajet traj = trajetRepository.GetById(mesAlertes.get(i).getTrajetID());
                if(traj != null)
                        mesTrajets.add(traj);
            }
            i++;
        }
        
        if(!mesTrajets.isEmpty()){
            adapter = new TrajetAdapter(this, mesTrajets, userID,"Main");
            trajetRepository.Close();   
            listeViewTrajet.setAdapter(adapter);    
            registerForContextMenu(listeViewTrajet);
            alerteRepository.Close();
            TableLayout tb = (TableLayout)findViewById(R.id.tableRowResult);           
            TableRow t = (TableRow)findViewById(R.id.tableNoFound);
            tb.removeView(t);
        }
      
    }
  
    //List veiw fonction
    private void UpdateAdapter() {
        trajetRepository.Open();
        adapter.setTrajets(trajetRepository.GetAll());
        trajetRepository.Close();
        adapter.notifyDataSetChanged();
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
    
    //Fin list view fonction
    
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location){
            updateWithNewLocation(location);           
        }
        
        public void onProviderDisabled(String provider){
            updateWithNewLocation(null);
        }
        
        public void onProviderEnabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras){}

    };
    
    private void updateWithNewLocation(Location location) {
        EditText depart = (EditText) findViewById(R.id.depart);

        String latLongString;

        if (location != null) {
            // Update my location marker
            //myLocation.setLocation(location);

            // Update the map location.
            Double geoLat = location.getLatitude() * 1E6;
            Double geoLng = location.getLongitude() * 1E6;
            GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

            //mapcontroller.animateTo(point);

            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;

            Geocoder gc = new Geocoder(this, Locale.getDefault());

            try {

                List<Address> addresses = gc.getFromLocation(lat, lng, 1);
                StringBuilder sb = new StringBuilder();

                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    //Récupérer l'adresse complète de localisation google
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i));

                    //sb.append(address.getCountryName());                    
                    
                }
                addressString = sb.toString();
            } catch (IOException e) {
            }
        } else {

            // Place the CellID here
            latLongString = "No location found";
        }
        // This commented out line will include latitute and longtitute
        // myLocationText.setText("Your Phone is Currently at.. \n" + latLongString + "\n" +
        // addressString);

        depart.setText(addressString);
        //setAddress(addressString);

    }

    protected boolean isRouteDisplayed() {
        return false;
    }

 // updates the date in the EditText
    private void displayDate() {
        String dayFinal= String.valueOf(mDay);
        int mMonth2 = mMonth+1;
        String monthFinal= String.valueOf(mMonth2);
        String yearFinal= String.valueOf(mYear);
        
        if(mDay<10){
            dayFinal = "0"+String.valueOf(mDay);
        }
        
        if(mMonth2<10){
            monthFinal = "0"+monthFinal;
        }        
        yearFinal = yearFinal.substring(2);

        mDateDisplay.setText(
                new StringBuilder()
                // Month is 0 based so add 1
                .append(dayFinal).append("/")
                .append(monthFinal).append("/")
                //.append(mDay).append("/")
                .append(yearFinal));
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
        getMenuInflater().inflate(R.menu.activity_main2, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_compte : 
                //openHome(); 
                Intent intent = new Intent(Main2Activity.this, CompteUserActivity.class);
                intent.putExtra("userID", userID);
                startActivityForResult(intent,1);
                return true;
            case R.id.menu_tarif:
             //toDo
             return true;
            case R.id.menu_taco:
            // find taxi meet point
            case R.id.menu_aide:
            // FAQ & guide d'utilisation
            case R.id.menu_settings:
            //    
        }

        // TODO Auto-generated method stub
        return super.onMenuItemSelected(featureId, item);
    }

}
