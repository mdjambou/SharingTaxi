package com.android.sharingtaxi;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CoCabActivity extends Activity
{
    private Button home;
    private Button mesrecherches;
    private Button mespropositions;
    private Button favoris;
    private Button profil;
    private Button avis;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // remove action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //Fin remove action bar
        setContentView(R.layout.activity_co_cab);
        
        home = (Button)findViewById(R.id.home);
        mesrecherches = (Button)findViewById(R.id.mesrecherches);
        mespropositions = (Button)findViewById(R.id.mespropositions);
        favoris = (Button)findViewById(R.id.favoris);
        profil = (Button)findViewById(R.id.profil);
        avis = (Button)findViewById(R.id.avis);
        
        home.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, Main2Activity.class);
                startActivityForResult(intent,1);
            }
        });
        
        mesrecherches.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, MesDemandesEnvoyeesActivity.class);
                startActivityForResult(intent,1);
            }
        });
        
        mespropositions.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, MesDemandesRecuesActivity.class);
                startActivityForResult(intent,1);
            }
        });
        
        favoris.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, Main2Activity.class);
                startActivityForResult(intent,1);
            }
        });
        
        profil.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, CompteUserActivity.class);
                startActivityForResult(intent,1);
            }
        });
        
        avis.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CoCabActivity.this, Main2Activity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_co_cab, menu);
        return true;
    }

}
