package com.android.sharingtaxi;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.android.sharingtaxi.adapter.Alerte;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompteUserActivity extends Activity
{
    EditText textV;
    TextView textV2;
    EditText textV3;
    private int userID;
    private String monPseudo;
    private int monTel;
    private String monPassword;
    private Button trajets;
    private Button alerteTrajet;
    private Button modifCompte;
    private Button demandesIN;
    private Button demandesOUT;
    private ImageView accueil;
    private ImageButton deconnexion;
    private UserRepository userRepository;
    private TrajetRepository trajetRepository;
    private AlerteRepository alerteRepository;
    //Slide menu test
    private LinearLayout MenuList;
    private ImageButton btnToggleMenuList;
    private int screenWidth;
    private boolean isExpanded;
    private Button creerTrajet;
    private Button mesTrajets;
    private Button mesDemandes;
    private Button mesReponses;
    private Button aide2;
    private Button tarification2;
    private Button connexion2;
    private Button trajets2; 
    private Button compte2;
    //Fin slide menu test
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
      //action bar
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle("CoCab");
        //Fin  action bar
        
        setContentView(R.layout.activity_compte_user);
        Bundle infoUser = this.getIntent().getExtras();
        userRepository = new UserRepository(this);
        
        if(infoUser != null){
            userID = infoUser.getInt("userID");
            if(userID != 0)
            {
                userRepository.Open();
                User user = userRepository.GetById(userID);
                userRepository.Close();
                monPseudo = user.getPseudo();  
                monTel = user.getTelephone();  
                monPassword = user.getPassword();
            }
            else{
                monPseudo = infoUser.getString("pseudo");  
                monTel = infoUser.getInt("telephone");  
                monPassword = infoUser.getString("password");  
                userID = monTel; 
            }
        }
        textV = (EditText)findViewById(R.id.textLoginUser);
        textV2 = (TextView)findViewById(R.id.textNumbUser);
        textV.setText(monPseudo);
        textV2.setText(String.valueOf(monTel));
        textV3 = (EditText)findViewById(R.id.textMdpUser);
        textV3.setText(monPassword);
        

        modifCompte = (Button)findViewById(R.id.modifCompte);

       modifCompte.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                userRepository.Open();
                String pseudo = textV.getText().toString();
                String mdp2 = textV3.getText().toString();
                User user2 = new User(pseudo, monTel, mdp2);
                userRepository.Update(user2);
                userRepository.Close();                
            }
        });

    }

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
                    
                    Alerte alerte = new Alerte(monTel, trajetFind.getId());
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

}
