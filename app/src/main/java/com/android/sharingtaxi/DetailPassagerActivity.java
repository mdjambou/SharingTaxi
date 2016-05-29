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
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DetailPassagerActivity extends Activity
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
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView departC;
    private TextView arriveeC;
    private EditText mDateDisplay;
    private Button mPickDate;
    static final int DATE_DIALOG_ID = 0;
    static final int CALENDAR_VIEW_ID = 1;
    private ImageButton ajoutPassager;
    
    int userID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_passager);

        // Listview
        departC = (TextView) findViewById(R.id.departChanged);
        arriveeC = (TextView) findViewById(R.id.arriveeChanged);
        mDateDisplay = (EditText) findViewById(R.id.dateDisplayChanged);
        mPickDate = (Button) findViewById(R.id.pickDateChanged);
        
        listeViewUser = (ListView) findViewById(R.id.listViewMesUsers);
        trajetRepository = new TrajetRepository(this);
        alerteRepository = new AlerteRepository(this);
        userRepository = new UserRepository(this);
        demandeTrajetRepository = new DemandeTrajetRepository(this);

        trajetRepository.Open();
        alerteRepository.Open();
        demandeTrajetRepository.Open();
        userRepository.Open();
        
        Bundle infoTrajet = this.getIntent().getExtras();     
        if(infoTrajet != null){
            userID = infoTrajet.getInt("userID");
            depart = infoTrajet.getString("Depart");
            arrivee = infoTrajet.getString("Arrivee");               
            dateDepart = infoTrajet.getString("dateDepart");
            
            ajoutPassager = (ImageButton) findViewById(R.id.ajoutPassagerButton);

            ajoutPassager.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    
                    Intent intent = new Intent(DetailPassagerActivity.this, MesDemandesRecuesActivity.class);
                    intent.putExtra("Depart", depart);
                    intent.putExtra("Arrivee", arrivee);               
                    intent.putExtra("dateDepart", dateDepart);
                    intent.putExtra("userID", userID);
                    startActivityForResult(intent,1);
               }
            });
            
            Trajet tr = trajetRepository.GetId(depart, arrivee, dateDepart);
            
            departC.setText(depart);
            arriveeC.setText(arrivee);
        
            mPickDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(DATE_DIALOG_ID);
                }
            });
            
            String myDate[] = dateDepart.split("/");
            mDay = Integer.parseInt(myDate[0]);
            mMonth = Integer.parseInt(myDate[1]);
            mYear = Integer.parseInt(myDate[2]);
            displayDate();
            
            Alerte monAlerte = alerteRepository.GetId(String.valueOf(tr.getId()), String.valueOf(userID), "1"); 
            
            List<DemandeTrajet> lesDemandes = new ArrayList<DemandeTrajet>();      
            lesDemandes = demandeTrajetRepository.GetAll(); 
            //lesDemandes = demandeTrajetRepository.GetAllByAttribute(monAlerte.getId(), "AlerteID"); 

            List<User> mesUsers = new ArrayList<User>();

            for(int i = 0;i<lesDemandes.size();i++){
                if((lesDemandes.get(i).getUserID() != userID) && (lesDemandes.get(i).getAlerteID() == monAlerte.getId())){
                   User utilisateur = userRepository.GetById(lesDemandes.get(i).getUserID());
                   mesUsers.add(utilisateur);
                }
            }
            adapter = new UserAdapter(this,mesUsers,"mesPassagers", monAlerte.getId(), userID, null,null);
            trajetRepository.Close();
            alerteRepository.Close();
            userRepository.Close();
            demandeTrajetRepository.Close();
        }
        
       // trajetDetail.setText("Trajet : "+depart+" - "+arrivee+" - "+dateDepart);
        listeViewUser.setAdapter(adapter);

        registerForContextMenu(listeViewUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_mes_trajets, menu);
        return true;
    }
    
    private void displayDate() {
       
        if(mMonth>10){
            mDateDisplay.setText(
            new StringBuilder()
            // Month is 0 based so add 1
            .append(mDay).append("/")
            .append(mMonth).append("/")
            //.append(mDay).append("/")
            .append(mYear).append(" "));
        }
        else{
            mDateDisplay.setText(
            new StringBuilder()
            .append(mDay).append("/")
            .append("0").append(mMonth).append("/")
            .append(mYear).append(" "));
        }
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
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DATE_DIALOG_ID)
            return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
        return null;
    } 

}
