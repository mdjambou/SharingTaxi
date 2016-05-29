package com.android.sharingtaxi;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.Trajet;
import com.android.sharingtaxi.adapter.TrajetAdapter;
import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.TrajetRepository;
import com.android.sharingtaxi.business.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RechercheActivity extends Activity
{
    private ListView listeViewTrajet;
    private ListView listeViewOtherTrajet;
    private TrajetAdapter adapter;
    private TrajetAdapter adapter2;
    private TrajetRepository trajetRepository;
    private AlerteRepository alerteRepository;
    private UserRepository userRepository;
    int userID;
    EditText telephone;
    EditText mdp;
    User user = null;
    String departSelect;
    String arriveeSelect;
    String datedepSelect;
    String heureSelect;
    
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
        
        setContentView(R.layout.activity_recherche);
        Bundle infoTrajet = this.getIntent().getExtras();     
        if(infoTrajet != null){
            userID = infoTrajet.getInt("userID");
            departSelect = infoTrajet.getString("pointDepart");
            arriveeSelect = infoTrajet.getString("pointArrivee");
            datedepSelect = infoTrajet.getString("dateDepart");
            heureSelect = infoTrajet.getString("heureDepart");
            
            departSelect.replace("'", "\'");
            String dateS[] = datedepSelect.split("/");
            String day = dateS[0];
            int sizeDay = day.length();
            String month = dateS[1];
            int sizeMonth = month.length();
            String year = dateS[2];
            int sizeYear = year.length();
            year = year.substring(0,2); 
           
            int days = Integer.parseInt(day);
            if(days<10){
                day = "0"+days;
            }
            
            int months = Integer.parseInt(month);
            if(months<10){
                month = "0"+months;
            }
            datedepSelect = day + "/" + month + "/" + dateS[2];
        }
           
        // Bouton
       /* creerAlerte = (Button) findViewById(R.id.creerAlerte);
        creerAlerte.setOnClickListener(new OnClickListener() {
        @Override
          public void onClick(View v) {
              AjouterItem();
          }
        });*/
        

        // Listview
        listeViewTrajet = (ListView) findViewById(R.id.listViewTrajet);
        listeViewOtherTrajet = (ListView) findViewById(R.id.listViewOtherTrajet);
        trajetRepository = new TrajetRepository(this);
        
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
            double distanceDep = calculateDistanceBetweenAddress(depart, departSelect);
            double distanceArr = calculateDistanceBetweenAddress(arrivee, arriveeSelect);
            int comparisonDate =compareDate(datedep,datedepSelect);
            if ((distanceDep <=300) && (distanceArr <=300) && (comparisonDate>=0)){
                mesTrajets.add(lesTrajets.get(i));
            }
            else if(((distanceDep <=300) || (distanceArr <=300)) && (comparisonDate>=0))
                trajetsIdem.add(lesTrajets.get(i));
            /*if(arriveeSelect != null && !arriveeSelect.isEmpty()){
                if(depart.equals(departSelect) && arrivee.equals(arriveeSelect) && datedep.equals(datedepSelect)){
                    mesTrajets.add(lesTrajets.get(i));
                }else if(datedepSelect.contains(datedep))
                    trajetsIdem.add(lesTrajets.get(i));
            }
            else{
                if(depart.equals(departSelect) && datedepSelect.contains(datedep)){
                    mesTrajets.add(lesTrajets.get(i));
                }else if(datedepSelect.contains(datedep))
                    trajetsIdem.add(lesTrajets.get(i));
            }*/
        }
        
        if(mesTrajets.isEmpty()){
            LinearLayout tb = (LinearLayout)findViewById(R.id.viewTraj);           
            TableLayout t = (TableLayout)findViewById(R.id.tableRowRes);
            tb.removeView(t);
            adapter = new TrajetAdapter(this, mesTrajets, userID,"Recherche");
            AjouterItem(departSelect, arriveeSelect,datedepSelect,userID);
//            adapter = new TrajetAdapter(this, trajetRepository.GetAll(), userID);
        }
        else{
            LinearLayout tb = (LinearLayout)findViewById(R.id.viewTraj);           
            Button t = (Button)findViewById(R.id.noResultTrajet);
            tb.removeView(t);
            adapter = new TrajetAdapter(this, mesTrajets, userID,"Recherche");

            listeViewTrajet.setAdapter(adapter);    
            registerForContextMenu(listeViewTrajet);
        }

        if(!trajetsIdem.isEmpty()){
            adapter2 = new TrajetAdapter(this, trajetsIdem, userID,"Recherche");
    
            listeViewOtherTrajet.setAdapter(adapter2);    
            registerForContextMenu(listeViewOtherTrajet);   
        }   
        trajetRepository.Close();
        
        Button backRecherche = (Button)findViewById(R.id.noResultTrajet);
       if(backRecherche != null){ 
           backRecherche.setOnClickListener(new OnClickListener(){          
            public void onClick(View view){
                Intent intent = new Intent(RechercheActivity.this, Main2Activity.class);              
                intent.putExtra("userID", userID);
                intent.putExtra("pointDepart", departSelect);
                intent.putExtra("pointArrivee", arriveeSelect);
                startActivityForResult(intent,1);
            }
        });
       }
    }

    /**
     * Ajout d'une produit
     */
    private User Connexion() {

        // Création de la boite de dialogue
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.activity_connexion);
        dialog.setTitle("Vous devez être connecté!");

        telephone = (EditText) dialog.findViewById(R.id.numTel);
        mdp = (EditText) dialog.findViewById(R.id.mdp);

        
        Button connect = (Button) dialog.findViewById(R.id.connecter);   
        Button inscrire = (Button) dialog.findViewById(R.id.inscrire);   
        userRepository = new UserRepository(this);
       
        inscrire.setOnClickListener(new OnClickListener(){
            public void onClick(View view){    
                String monTel = telephone.getText().toString();
                Intent intent = new Intent(RechercheActivity.this, InscriptionActivity.class);              
                if(monTel != null){
                    if(monNumeroEstValide(monTel)){
                        intent.putExtra("telephone", monTel);
                    }
                    else{
                        String identification="Numéro de téléphone invalide";
                        Toast.makeText(getApplicationContext(), identification, Toast.LENGTH_SHORT).show();

                    }
                }
               String monMDP = mdp.getText().toString();
                if(monMDP != null ){
                    intent.putExtra("password", monMDP);
                }
                startActivityForResult(intent,1);
            }
        });
        connect.setOnClickListener(new OnClickListener(){          
            public void onClick(View view){
                String monTel = telephone.getText().toString();
                if(monNumeroEstValide(monTel)){
                    int tel = Integer.parseInt(monTel);
                    String monMDP = mdp.getText().toString();
                    userRepository.Open();
                    user = userRepository.GetById(tel);
                    userRepository.Close();
                    
                    if(user != null && user.getPassword().equals(monMDP)){
                        dialog.dismiss();
                    }
                    else{
                        String identification="Numéro de téléphone ou mot de passe incorrect";
                        Toast.makeText(getApplicationContext(), identification, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String identification="Numéro de téléphone invalide";
                    Toast.makeText(getApplicationContext(), identification, Toast.LENGTH_SHORT).show();

                }
            }
        });
        
        dialog.show();
        return user;            

    }

    private void AjouterItem(String depart, String arrivee, String dateDep, int userID) {

        // Création de la boite de dialogue
        if(userID ==0){
            User user = Connexion();
            if (user != null)
                userID = user.getTelephone();
        }

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
    
    private void UpdateAdapter() {
        trajetRepository.Open();
        adapter.setTrajets(trajetRepository.GetAll());
        trajetRepository.Close();
        adapter.notifyDataSetChanged();
    }
    
    public boolean monNumeroEstValide(String monNumero){
        
        return monNumero.matches("[0-9]*");
   
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main2, menu);
        return true;
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
    
}
