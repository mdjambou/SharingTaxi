package com.android.sharingtaxi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.adapter.UserAdapter;
import com.android.sharingtaxi.business.UserRepository;

public class InscriptionActivity extends Activity
{

    private Button inscrire;
    EditText telephone;
    EditText mdp;
    EditText pseudo;
    String textV;
    private UserAdapter adapter;
    private UserRepository userRepository;
    private int monTel = 0;
    private String monPassword;

    public boolean monNumeroEstValide(String monNumero){
        
        return monNumero.matches("[0-9]*");
   
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        
        Bundle infoUser = this.getIntent().getExtras();
        inscrire = (Button)findViewById(R.id.inscrire);
        if(infoUser != null){
            String tel = infoUser.getString("telephone");  
            monPassword = infoUser.getString("password");
            telephone = (EditText)findViewById(R.id.tel);
                //telephone.getText().toString();
            if(tel != null && !tel.isEmpty()){
               monTel = Integer.parseInt(tel);  
               telephone.setText(String.valueOf(monTel));
            }
            mdp = (EditText)findViewById(R.id.mdp);
                //mdp.getText().toString();
            if(monPassword != null)
               mdp.setText(monPassword);
        }
        
        userRepository = new UserRepository(this);
        
        inscrire.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                pseudo = (EditText)findViewById(R.id.pseudo);
                String monTel = telephone.getText().toString();
                String monMDP = mdp.getText().toString();
                String monPseudo = pseudo.getText().toString();
                
                if((monPseudo != null) && (monTel != null) && (monMDP != null) && (monNumeroEstValide(monTel))){                    
                    int tel = Integer.parseInt(monTel);
                    userRepository.Open();
                    if(userRepository.GetById(tel) == null){
                        userRepository.Save(new User(monPseudo, tel, monMDP));
                        userRepository.Close();       
                        /*Intent intent = new Intent(InscriptionActivity.this, CompteUserActivity.class);
                        intent.putExtra("telephone", tel);
                        intent.putExtra("password", monMDP);
                        intent.putExtra("pseudo", monPseudo);
                        startActivityForResult(intent,1);*/
                        Intent intent = new Intent(InscriptionActivity.this, Main2Activity.class);              
                        intent.putExtra("userID", tel);
                        startActivityForResult(intent,1);
                    }
                    else{
                        String identification="Ce numéro de téléphone est déjà utilisé!";
                        textV=identification; 
                        Toast.makeText(getApplicationContext(), textV, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String identification="Login , mot de passe ou Numéro de téléphone invalide";
                    textV=identification; 
                    Toast.makeText(getApplicationContext(), textV, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_inscription, menu);
        return true;
    }*/

}
