package com.android.sharingtaxi;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.List;

import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.AlerteAdapter;
import com.android.sharingtaxi.adapter.Trajet;
import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.TrajetRepository;
import com.android.sharingtaxi.business.UserRepository;
import com.tutorial.menuList.animations.CollapseAnimation;
import com.tutorial.menuList.animations.ExpandAnimation;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MesTrajetsActivity extends Activity
{
    private ListView listeViewTrajet;
    private AlerteAdapter adapter;
    private TrajetRepository trajetRepository;
    private AlerteRepository alerteRepository;
    int userID;
    //Slide menu test
    private ImageButton deconnexion;
    private LinearLayout MenuList;
    private ImageButton btnToggleMenuList;
    private int screenWidth;
    private boolean isExpanded;
    private Button creerTrajet;
    private Button mesTrajetsList;
    private Button mesDemandes;
    private Button mesReponses;
    private Button aide2;
    private Button tarification2;
    private Button connexion2;
    private Button trajets2; 
    private Button compte2;
    //Fin slide menu test
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // remove action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //Fin remove action bar
        
        setContentView(R.layout.activity_mes_trajets);
        Bundle infoUser = this.getIntent().getExtras();     
        if(infoUser != null)
            userID = infoUser.getInt("userID");
        

        // Listview
        listeViewTrajet = (ListView) findViewById(R.id.listViewMesTrajets);
        trajetRepository = new TrajetRepository(this);
        alerteRepository = new AlerteRepository(this);
        
        trajetRepository.Open();
        alerteRepository.Open();
        
        List<Alerte> lesAlertes = new ArrayList<Alerte>();
        List<Alerte> mesAlertes = new ArrayList<Alerte>();

        lesAlertes = alerteRepository.GetAll();
        //.GetAllByAttribute(userID, "UserID");
        List<Trajet> mesTrajets = new ArrayList<Trajet>();
        for(int i = 0;i<lesAlertes.size();i++){
            int uuid = lesAlertes.get(i).getUserID();
            if(uuid == userID){
                Trajet tr = trajetRepository.GetById(lesAlertes.get(i).getTrajetID());
                mesTrajets.add(tr);
                mesAlertes.add(lesAlertes.get(i));
            }
        }
        adapter = new AlerteAdapter(this, mesTrajets, mesAlertes,userID);
        trajetRepository.Close();
        alerteRepository.Close();

        listeViewTrajet.setAdapter(adapter);

        registerForContextMenu(listeViewTrajet);
        
        //Slide menu test
        MenuList = (LinearLayout) findViewById(R.id.linearLayout2);
        btnToggleMenuList = (ImageButton) findViewById(R.id.button1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        btnToggleMenuList.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (isExpanded) {
                    isExpanded = false;
                    MenuList.startAnimation(new CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
                    MenuList.removeAllViews();
                }else {
                    isExpanded = true;
                    MenuList.startAnimation(new ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
                    
                    compte2 = subMenuButton("Mon compte",R.drawable.compteicon);
                    MenuList.addView(compte2);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( 
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,                
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);                
                    MenuList.addView(subTextView("Trajets"),lp);
                    creerTrajet = subMenuButton("Proposer un trajet",R.drawable.addicon);
                    mesTrajetsList = subMenuButton("Trajets Proposés",R.drawable.searchicon);
                    mesDemandes = subMenuButton("Demandes reçues",R.drawable.reponseicon);
                    mesReponses = subMenuButton("Réponses reçues",R.drawable.demandeouticon);

                    MenuList.addView(creerTrajet);
                    MenuList.addView(mesTrajetsList);
                    MenuList.addView(mesDemandes);
                    MenuList.addView(mesReponses);
                    trajets2 = subMenuButton("Tous les trajets",R.drawable.searchicon);
                    MenuList.addView(trajets2);
                   
                    aide2 = subMenuButton("Aide",R.drawable.aideicon);
                    tarification2 = subMenuButton("Tarifs",R.drawable.tarificon);
                    MenuList.addView(subTextView("Autres"),lp);
                    
                    MenuList.addView(tarification2);
                    MenuList.addView(aide2);
                   
                    creerTrajet.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                                AjouterTrajet();
                        }
                    });
                    
                    mesTrajetsList.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                            Intent intent = new Intent(MesTrajetsActivity.this, MesTrajetsActivity.class);
                            intent.putExtra("userID", userID);
                            startActivityForResult(intent,1);
                        }
                    });
                    
                    mesDemandes.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                            Intent intent = new Intent(MesTrajetsActivity.this, MesDemandesRecuesActivity.class);
                            intent.putExtra("userID", userID);
                            startActivityForResult(intent,1);
                        }
                    });
                    
                    mesReponses.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                            Intent intent = new Intent(MesTrajetsActivity.this, MesDemandesEnvoyeesActivity.class);
                            intent.putExtra("userID", userID);
                            startActivityForResult(intent,1);
                        }
                    });
                    compte2.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                            Intent intent = new Intent(MesTrajetsActivity.this, CompteUserActivity.class);                
                            intent.putExtra("userID", userID);
                            startActivityForResult(intent,1);
                        }
                    });
                    
                    trajets2.setOnClickListener(new OnClickListener(){
                        public void onClick(View view){
                            Intent intent = new Intent(MesTrajetsActivity.this, TousLesTrajetsActivity.class);
                                intent.putExtra("userID", userID);
                            
                            startActivityForResult(intent,1);
                        }
                    });

                }
                }
        });
        //Fin slide menu test
        deconnexion = (ImageButton)findViewById(R.id.deconnexion);
        deconnexion.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MesTrajetsActivity.this, ConnexionActivity.class);              
                startActivityForResult(intent,1);
            }
        });
    }

    //Slide menu fonction
    private Button subMenuButton(String str, int icon) {
        Button button = new Button(this);
        button.setHeight(WRAP_CONTENT);
        button.setText(str);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        Drawable img = getBaseContext().getResources().getDrawable(icon);
        img.setBounds( 0, 0, 60, 60 );
        button.setCompoundDrawables( img, null, null, null );
        
        return button;
    }
    
    private TextView subTextView(String str) {
        TextView text = new TextView(this);
        text.setHeight(WRAP_CONTENT);
        text.setText(str);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        return text;
    }
   private void AjouterTrajet() {

        // Création de la boite de dialogue
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialogadd);
        dialog.setTitle("Créer une alerte pour un trajet");

        final Button buttonAdd = (Button) dialog.findViewById(R.id.buttonAdd);
        final Button buttonCancel = (Button) dialog
                .findViewById(R.id.buttonCancel);

        buttonAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // creation du trajet
                trajetRepository.Open();
                alerteRepository.Open();
                String depart = ((EditText) dialog
                        .findViewById(R.id.editTextDepart)).getText()
                        .toString();
                String arrivee = ((EditText) dialog
                                .findViewById(R.id.editTextArrivee)).getText()
                                .toString();
                
                DatePicker dateDepart = ((DatePicker) dialog.findViewById(R.id.dateDepart));          
                //String dateDep = dateDepart.toString();
                int day = dateDepart.getDayOfMonth();
                int month = dateDepart.getMonth() + 1;
                String dayText = String.valueOf(day);
                String monthText = String.valueOf(month);
                
                if(day<10)
                    dayText = "0"+day;
                
                if(month<10)
                    monthText = "0"+month;
                
                String dateDep = dayText + "/" +  monthText + "/" + dateDepart.getYear();
                //creation trajet
                if (trajetRepository.GetId(depart, arrivee, dateDep)== null){
                    Trajet trajet = new Trajet(depart, arrivee, dateDep);
                    trajet.setNbAlerte(0);
                    trajetRepository.Save(trajet);
                }
                
                Trajet trajetFind = trajetRepository.GetId(depart, arrivee, dateDep);

                String trajID = String.valueOf(trajetFind.getId());
                String IDuser = String.valueOf(userID);
                
                if(alerteRepository.GetId(trajID, IDuser,"0") == null){
                    //creation alerte
                    int alerteNb = trajetFind.getNbAlerte() +1;
                    trajetFind.setNbAlerte(alerteNb);
                    trajetRepository.Update(trajetFind);
                    
                    Alerte alerte = new Alerte(userID, trajetFind.getId());
                    alerteRepository.Save(alerte);
                    /*Alerte alerteFind = alerteRepository.GetId(String.valueOf(alerte.getTrajetID()), String.valueOf(alerte.getUserID()),String.valueOf(alerte.getPassager()));
                    List<Alerte> lesAlertes = new ArrayList<Alerte>();
                    lesAlertes = alerteRepository.GetAll();*/
                }

                alerteRepository.Close();
                

               /* int trajetID = trajet.getId();
                List<Trajet> lesTrajets = new ArrayList<Trajet>();
                lesTrajets = trajetRepository.GetAll();
                Iterator<Trajet> listeTrajets = lesTrajets.iterator();
                int find = 0;
                while (listeTrajets.hasNext() && find == 0){
                    Trajet leTrajet = listeTrajets.next();
                    if((leTrajet.getArrivee().equals(arrivee))&&
                                    (leTrajet.getDepart().equals(depart))&&
                                    (leTrajet.getDateDepart().equals(dateDep))){
                        find =1;
                        //creation alerte
                        alerteRepository.Save(new Alerte(monTel, leTrajet.getId()));
                        alerteRepository.Close();
                    }
                }*/
                
                trajetRepository.Close();

                
               

                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Clic sur le bouton annuler
                dialog.dismiss();
            }
        });

        dialog.show();
    }
   //Fin Slide menu fonction
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_mes_trajets, menu);
        return true;
    }

}
