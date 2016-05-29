package com.android.sharingtaxi;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;

import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.Trajet;
import com.android.sharingtaxi.adapter.TrajetAdapter;
import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.TrajetRepository;
import com.android.sharingtaxi.business.UserRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

import GMap.GMapV2GetRouteDirection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;



/**
 *
 * @author VIJAYAKUMAR M
 *This class for display route current location to hotel location on google map V2
 */
public class MainActivity extends FragmentActivity  implements OnMarkerClickListener{

     
      List<Overlay> mapOverlays;
      GeoPoint point1, point2;
      LocationManager locManager;
      Drawable drawable;
      Document document;
      GMapV2GetRouteDirection v2GetRouteDirection;
      LatLng fromPosition;
      LatLng toPosition;
      GoogleMap mGoogleMap;
      MarkerOptions markerOptions;
      Location location ;
      int userID;
      String departSelect;
      String arriveeSelect;
      String datedepSelect;
      String heureSelect;
      Marker myMarker;
      
      private TrajetRepository trajetRepository;
      private AlerteRepository alerteRepository;
      private UserRepository userRepository;
      User user = null;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //action bar
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle("CoCab");
            //Fin  action bar

            Bundle infoTrajet = this.getIntent().getExtras();     
            if(infoTrajet != null){
                userID = infoTrajet.getInt("userID");
                departSelect = infoTrajet.getString("pointDepart");
                arriveeSelect = infoTrajet.getString("pointArrivee");
                datedepSelect = infoTrajet.getString("dateDepart");
                heureSelect = infoTrajet.getString("heureDepart");
            }
            setContentView(R.layout.activity_main);
            v2GetRouteDirection = new GMapV2GetRouteDirection();
          SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
            mGoogleMap = supportMapFragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
           // mGoogleMap.setTrafficEnabled(true);
            //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            markerOptions = new MarkerOptions();
            fromPosition = getLatitudeLongitude(departSelect);
            toPosition = getLatitudeLongitude(arriveeSelect);
            CameraPosition camPos = new CameraPosition(fromPosition, 12, 0, 0);
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();
      }
      /**
       *
       * @author VIJAYAKUMAR M
       * This class Get Route on the map
       *
       */
      private class GetRouteTask extends AsyncTask<String, Void, String> {
           
            private ProgressDialog Dialog;
            String response = "";
            @Override
            protected void onPreExecute() {
                  Dialog = new ProgressDialog(MainActivity.this);
                  Dialog.setMessage("Loading route...");
                  Dialog.show();
            }

            @Override
            protected String doInBackground(String... urls) {
                  //Get All Route values
                        document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
                        response = "Success";
                  return response;

            }

            @Override
            protected void onPostExecute(String result) {
                  if(response.equalsIgnoreCase("Success")){
                  ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                  PolylineOptions rectLine = new PolylineOptions().width(10).color(
                              Color.RED);

                  for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                  }
                  // Adding route on the map
                  mGoogleMap.addPolyline(rectLine);
                  markerOptions.position(toPosition);
                  markerOptions.draggable(true);
                 
                  /*User userActual = RetrieveUser(userID);
                  if(userActual != null){
                      markerOptions.title(userActual.getPseudo());
                      markerOptions.snippet("Choisis moi!");
                      markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                   }
                  */
                  myMarker = mGoogleMap.addMarker(markerOptions);
                                    
                 /* mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener()
                  {

                      @Override
                      public boolean onMarkerClick(Marker marker) {
                          if (marker.equals(myMarker)) {
                              Intent intent = new Intent(MainActivity.this, DetailAlerteActivity.class);
                              intent.putExtra("Depart", departSelect);
                              intent.putExtra("Arrivee", arriveeSelect);               
                              intent.putExtra("dateDepart", datedepSelect);
                              intent.putExtra("userID", userID);
                              startActivityForResult(intent,1);
                          }
                       return true;
                      }

                  });   */

                }
                 
                  Dialog.dismiss();
            }

      }
      @Override
      protected void onStop() {
            super.onStop();
            finish();
      }
           
      public LatLng getLatitudeLongitude(String address){
          Geocoder geocoder = new Geocoder(getBaseContext());  
          List<Address> addresses;
          double latitude=0;
          double longitude=0;
             try{
                addresses = geocoder.getFromLocationName(address, 20);
                 for(int i = 0; i < addresses.size(); i++) { // MULTIPLE MATCHES
    
                   Address addr = addresses.get(i);
    
                   latitude = addr.getLatitude();
                   longitude = addr.getLongitude(); // DO SOMETHING WITH VALUES
                   if(latitude>0 && longitude >0)
                    return new LatLng(latitude, longitude);

                 }
             }
             catch(IOException e)
             {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
             return new LatLng(latitude, longitude);
      }
      
      public User RetrieveUser(int userID){
          UserRepository userRepository = new UserRepository(this);
          
          userRepository.Open();
          User user = userRepository.GetById(userID);
          return user;
      }
      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_main, menu);
      return true;
      }

    @Override
    public boolean onMarkerClick(Marker arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void RechercheTrajet (String dep, String arr, String dateS, LatLng From, LatLng To){
        TrajetRepository trajetRepository = new TrajetRepository(this);
        
        trajetRepository.Open();
        List<Trajet> lesTrajets = new ArrayList<Trajet>();
        List<Trajet> trajetsIdem = new ArrayList<Trajet>();
        
        lesTrajets = trajetRepository.GetAll();
        //.GetAllByAttribute(userID, "UserID");
        List<Trajet> mesTrajets = new ArrayList<Trajet>();
        for(int i = 0;i<lesTrajets.size();i++){
            String depart = lesTrajets.get(i).getDepart();
            String arrivee = lesTrajets.get(i).getArrivee(); 
            String datedep = lesTrajets.get(i).getDateDepart();
            double distanceDep = calculateDistanceBetweenAddress(depart, dep);
            double distanceArr = calculateDistanceBetweenAddress(arrivee, arr);
            int comparisonDate =compareDate(datedep,dateS);
            /*if ((distanceDep <=300) && (distanceArr <=300) && (comparisonDate>=0)){
                    DrawTrajet(lesTrajets.get(i), From, To,"Green");
            }
            else if(((distanceDep <=300) || (distanceArr <=300)) && (comparisonDate>=0))
                DrawTrajet(lesTrajets.get(i), From, To, "Black");*/
        }
       
    }
    
    /********/
    public static double distance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        //kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        //meterInDec= Integer.valueOf(newFormat.format(meter));
        return meter;
    }

    public double calculateDistanceBetweenAddress(String adr1, String adr2){
        LatLng adr1LatLng =getLatitudeLongitude(adr1);
        LatLng adr2LatLng =getLatitudeLongitude(adr2);
        
        return distance(adr1LatLng, adr2LatLng);
    }
    
    public int compareDate(String date1, String date2){
     
        String dateS[] = date1.split("/");
        int day = Integer.parseInt(dateS[0]);
        int month = Integer.parseInt(dateS[1]);
        int year = Integer.parseInt(dateS[2]); 
        
        String dateS2[] = date2.split("/");
        int day2 = Integer.parseInt(dateS2[0]);
        int month2 = Integer.parseInt(dateS2[1]);
        int year2 = Integer.parseInt(dateS2[2]);
        
        if ((year > year2))
            return 1;
        if((year == year2) && (month > month2))
            return 1;
        if((year == year2) && (month == month2) && (day > day2))
            return 1;
        
        if((year == year2) && (month == month2) && (day == day2))
            return 0;

        return -1;

    }
    
    private void AjouterItem(String depart, String arrivee, String dateDep, int userID) {
        alerteRepository = new AlerteRepository(this);
        trajetRepository = new TrajetRepository(this);

        trajetRepository.Open();
        alerteRepository.Open();               
        //String dateDep = dateDepart.getYear() + "-" + (dateDepart.getMonth() + 1) + "-" + dateDepart.getDayOfMonth();
                
        //creation trajet
        if (trajetRepository.GetId(depart, arrivee, dateDep)== null){
            Trajet trajet = new Trajet(depart, arrivee, dateDep);
            trajet.setNbAlerte(0);
            trajetRepository.Save(trajet);
        }                   
                
        Trajet trajetFind = trajetRepository.GetId(depart, arrivee, dateDep);
        int trajetID = trajetFind.getId();
                
        //creation alerte
        String trajID = String.valueOf(trajetFind.getId());
        String IDuser = String.valueOf(userID);
                
        if(alerteRepository.GetId(trajID, IDuser,"0") == null){
          int alerteNb = trajetFind.getNbAlerte() + 1;
          trajetFind.setNbAlerte(alerteNb);
          trajetRepository.Update(trajetFind);
          alerteRepository.Save(new Alerte(userID, trajetID));
//          UpdateAdapter();
        }
        alerteRepository.Close();
    }
}
