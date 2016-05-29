package com.android.sharingtaxi;

import com.android.sharingtaxi.adapter.User;
import com.android.sharingtaxi.adapter.UserAdapter;
import com.android.sharingtaxi.business.UserBOpenHelper;
import com.android.sharingtaxi.business.UserRepository;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ResultatActivity extends Activity
{

    private Button connecter;
    EditText telephone;
    EditText mdp;
    private UserAdapter adapter;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        connecter = (Button)findViewById(R.id.inscrire);
        userRepository = new UserRepository(this);
        
        connecter.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                telephone = (EditText)findViewById(R.id.tel);
                mdp = (EditText)findViewById(R.id.mdp);
                String monTel = telephone.getText().toString();
                int tel = Integer.parseInt(monTel);
                String monMDP = mdp.getText().toString();
                userRepository.Open();
                User user = userRepository.GetById(tel);
                userRepository.Close();

                Intent intent = new Intent(ResultatActivity.this, CompteUserActivity.class);
                intent.putExtra("telephone", tel);
                intent.putExtra("password", monMDP);
                intent.putExtra("pseudo", user.getPseudo());
                startActivityForResult(intent,1);
            }
        });

    }
}