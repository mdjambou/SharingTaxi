package com.android.sharingtaxi;

import java.util.ArrayList;
import java.util.List;


import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.AlerteAdapter;
import com.android.sharingtaxi.adapter.DemandeTrajet;
import com.android.sharingtaxi.adapter.Trajet;
import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.adapter.UserAdapter;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.DemandeTrajetRepository;
import com.android.sharingtaxi.business.TrajetRepository;
import com.android.sharingtaxi.business.UserRepository;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

public class DetailAlerteActivity extends Activity
{
    private ListView listeViewUser;
    private UserAdapter adapter;
    private TrajetRepository trajetRepository;
    private AlerteRepository alerteRepository;
    private UserRepository userRepository;
    private DemandeTrajetRepository demandeTrajetRepository;
    String depart;
    String arrivee;               
    String dateDepart;
    TextView textV;
    boolean alerteExistUserID =false;
    int alerteUUID;
    private TextView trajetDetail;
    int userID;
    private Button inscriptAlerte;
    private Button addAlerte;
    Trajet tr;
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
        
        setContentView(R.layout.activity_detail_alerte);
           
        // Listview
        //trajetDetail = (TextView) findViewById(R.id.trajetDetail);
        listeViewUser = (ListView) findViewById(R.id.listViewMesUsers);
        trajetRepository = new TrajetRepository(this);
        alerteRepository = new AlerteRepository(this);
        userRepository = new UserRepository(this);
        demandeTrajetRepository = new DemandeTrajetRepository(this);
      
        demandeTrajetRepository.Open();
        trajetRepository.Open();
        alerteRepository.Open();
        userRepository.Open();
        
        Bundle infoTrajet = this.getIntent().getExtras();     
        if(infoTrajet != null){
            userID = infoTrajet.getInt("userID");
            depart = infoTrajet.getString("Depart");
            arrivee = infoTrajet.getString("Arrivee");               
            dateDepart = infoTrajet.getString("dateDepart");
            tr = trajetRepository.GetId(depart, arrivee, dateDepart);
            List<Alerte> lesAlertes = new ArrayList<Alerte>();
            lesAlertes = alerteRepository.GetAll();
            
            List<User> mesUsers = new ArrayList<User>();
        
            //.GetAllByAttribute(userID, "UserID");
            List<Alerte> mesAlertes = new ArrayList<Alerte>();
            for(int i = 0;i<lesAlertes.size();i++){
                int trajUUID = lesAlertes.get(i).getTrajetID();
                if(trajUUID == tr.getId()){
                    mesAlertes.add(lesAlertes.get(i));
                    User user = userRepository.GetById(lesAlertes.get(i).getUserID());
                    mesUsers.add(user);
                    if(lesAlertes.get(i).getUserID() == userID){
                        alerteUUID = lesAlertes.get(i).getId();
                        alerteExistUserID = true;
                    }

                }
            }
            adapter = new UserAdapter(this,mesUsers,"lesTrajets",0, userID, mesAlertes,tr);
    
            
            /*trajetDetail.setText("Trajet : Paris"+depart+" - Paris"+arrivee+" \nLe "+dateDepart);
            LinearLayout.LayoutParams  lllp=(LinearLayout.LayoutParams)trajetDetail.getLayoutParams();
            lllp.gravity=Gravity.CENTER;
            trajetDetail.setLayoutParams(lllp);    
            */
            listeViewUser.setAdapter(adapter);
    
            registerForContextMenu(listeViewUser);
           
   
            // ajout du user au trajet
            final Dialog dialog = new Dialog(this);

            addAlerte = (Button)findViewById(R.id.AddAlerte);
            addAlerte.setOnClickListener(new OnClickListener(){ 
                public void onClick(View view){
                    // Création de la boite de dialogue
                    
                    String textval="";
                    if(alerteExistUserID){
                        textval ="Vous êtes déjà passager de ce trajet";
                        Toast.makeText(getApplicationContext(), textval, Toast.LENGTH_SHORT).show();
                    }
                    else{         
                        dialog.setContentView(R.layout.dialogannul);
                        dialog.setTitle("Inscription au trajet:");
            
                        final Button oui = (Button) dialog.findViewById(R.id.buttonYes);
                        final Button non = (Button) dialog.findViewById(R.id.buttonCancel);        
            
                        oui.setOnClickListener(new OnClickListener() {
            
                            @Override
                            public void onClick(View v) {
                            String textval2="";      
                            demandeTrajetRepository.Open();
                            alerteRepository.Open();
                            trajetRepository.Open();
                            DemandeTrajet DemandeTrajetFind = demandeTrajetRepository.GetId(String.valueOf(alerteUUID), String.valueOf(userID), "2");
                            if(DemandeTrajetFind == null){
                                    DemandeTrajet DT = new DemandeTrajet(alerteUUID, userID);
                                    demandeTrajetRepository.Save(DT);
                                    textval2 =  "Inscription réussie!";
                                    Alerte alerteNew = new Alerte(userID, tr.getId());
                                    alerteRepository.Save(alerteNew);
                                    int nbAlerteOld = tr.getNbAlerte();
                                    tr.setNbAlerte(nbAlerteOld+1);
                                    trajetRepository.Update(tr);
                            }
                            else{
                                textval2 = "Vous êtes déjà inscrit pour ce trajet";
                           }
                           if(!textval2.equals("")) 
                               Toast.makeText(getApplicationContext(), textval2, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
    
                    }
                });
                
                non.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                
                    }
                });

                dialog.show();
              
              }
           }
                
           });

        }

        trajetRepository.Close();
        alerteRepository.Close();
        userRepository.Close();
        demandeTrajetRepository.Close();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main2, menu);
        return true;
    }

}
