package com.android.sharingtaxi;

//import com.google.android.gcm.GCMRegistrar;
import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.business.UserRepository;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConnexionActivity extends Activity {
    
    private static final String SENDER_ID = "779086340353";
    private Button connecter;
    private Button inscrire;
    private UserRepository userRepository;
    EditText telephone;
    EditText mdp;
    String textV;
    User user = null;
    int tel;
    String monMDP;
    
    public boolean monNumeroEstValide(String monNumero){
        
        return monNumero.matches("[0-9]*");
   
    }
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        
        //GCM part
       /* GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, SENDER_ID);
        } else {
          Log.v("RegID", "Already registered");
        }*/
        
        //Connexion part
        telephone = (EditText)findViewById(R.id.numTel);
        mdp = (EditText)findViewById(R.id.mdp);

        
        connecter = (Button)findViewById(R.id.connecter);
        userRepository = new UserRepository(this);
       
        inscrire = (Button)findViewById(R.id.inscrire);       
        inscrire.setOnClickListener(new OnClickListener(){
            public void onClick(View view){    
                String monTel = telephone.getText().toString();
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);              
                if(monTel != null){
                    if(monNumeroEstValide(monTel)){
                        //tel = Integer.parseInt(monTel);  
                        intent.putExtra("telephone", monTel);
                    }
                    else{
                        String identification="Numéro de téléphone invalide";
                        textV= identification ; 
                        Toast.makeText(getApplicationContext(), textV, Toast.LENGTH_SHORT).show();

                    }
                }
                monMDP = mdp.getText().toString();
                if(monMDP != null ){
                    intent.putExtra("password", monMDP);
                }
                startActivityForResult(intent,1);
            }
        });
        connecter.setOnClickListener(new OnClickListener(){          
            public void onClick(View view){
                String monTel = telephone.getText().toString();
                if(monNumeroEstValide(monTel)){
                    tel = Integer.parseInt(monTel);
                    monMDP = mdp.getText().toString();
                    userRepository.Open();
                    user = userRepository.GetById(tel);
                    userRepository.Close();
                    
                    if(user != null && user.getPassword().equals(monMDP)){
                        /*Intent intent = new Intent(ConnexionActivity.this, CompteUserActivity.class);
                        intent.putExtra("telephone", tel);
                        intent.putExtra("password", monMDP);
                        intent.putExtra("pseudo", user.getPseudo());
                        startActivityForResult(intent,1);*/
                        Intent intent = new Intent(ConnexionActivity.this, Main2Activity.class);              
                        intent.putExtra("userID", tel);
                        startActivityForResult(intent,1);
                    }
                    else{
                        String identification="Numéro de téléphone ou mot de passe incorrect";
                        textV = identification; 
                        Toast.makeText(getApplicationContext(), textV, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String identification="Numéro de téléphone invalide";
                    textV = identification; 
                    Toast.makeText(getApplicationContext(), textV, Toast.LENGTH_SHORT).show();

                }
            }
        });
  
        }
}