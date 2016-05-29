package com.android.sharingtaxi.adapter;

import java.util.List;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sharingtaxi.DetailPassagerActivity;
import com.android.sharingtaxi.InscriptionActivity;
import com.android.sharingtaxi.MesDemandesRecuesActivity;
import com.android.sharingtaxi.R;
import com.android.sharingtaxi.adapter.TrajetAdapter.ViewHolder;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.DemandeTrajetRepository;
import com.android.sharingtaxi.business.TrajetRepository;

public class UserAdapter extends BaseAdapter 
{
    private List<User> listeUser;
    private List<Alerte> listeAlerte;
    private LayoutInflater inflater;
    private Context context;
    String type ="mesPassagers";
    int alerteID;
    int userID;
    Trajet trajet;

    public void setUsers(List<User> listeUser) {
        this.listeUser = listeUser;
    }

    public UserAdapter(Context context, List<User> listeUser, String type, int alerteID, int userID,List<Alerte> listeAlerte, Trajet tr ) {
        this.listeUser = listeUser;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
        this.alerteID = alerteID;
        this.userID = userID;
        this.trajet = tr;
        this.listeAlerte = listeAlerte;
    }

    @Override
    public int getCount() {
        return listeUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listeUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listeUser.get(position).getTelephone();
    }


   @Override
   public View getView(final int position, View view, ViewGroup viewGroup) {
       final ViewHolder holder;

       if (view == null) {
           holder = new ViewHolder();
           if(type.equals("mesPassagers")){

               view = inflater.inflate(R.layout.layout_item_2_bis, null);
               
               holder.infosUser2 = (Button) view
                   .findViewById(R.id.infosUser2);
               
               holder.contact = (ImageButton) view
               .findViewById(R.id.contactButton);
               
               holder.delete = (ImageButton) view
               .findViewById(R.id.deleteButton);

           }
           else{
               view = inflater.inflate(R.layout.layout_item_2, null);
               
               holder.TrajText = (TextView) view
               .findViewById(R.id.TrajText);
               
               holder.infosUser = (ImageView) view
                   .findViewById(R.id.infosUser);
               
           }
           view.setTag(holder);
       } else {
           holder = (ViewHolder) view.getTag();
       }
       
       String text="";
 
       if(type.equals("mesPassagers")){
           holder.contact.setOnClickListener(new OnClickListener() {

               @Override
               public void onClick(View v) {
                // menuMesPassagers(listeUser.get(position).getTelephone(), alerteID);
                 
               }

           });    
           
           holder.delete.setOnClickListener(new OnClickListener() {

               @Override
               public void onClick(View v) {
                 menuMesPassagers(listeUser.get(position).getTelephone(), alerteID);
                 
               }

           });   
           text = "Passager : "+listeUser.get(position).getPseudo();

       }
       else{
           String passager;
           if(listeAlerte.get(position).getPassager() >1)
               passager ="passagers";
           else
               passager = "passager";
           
           text = listeUser.get(position).getPseudo();
           String textSuiv = text + "\n"+trajet.getDepart()+"/"+trajet.getArrivee()
           +"\n"+ "Distance:**** m";
           holder.TrajText.setText(textSuiv);
       }
       
       //holder.infosUser.setText(text);
      // holder.Telephone.setText(String.valueOf(listeUser.get(position).getTelephone()));
       
       
       return view;
   }

   public class ViewHolder {
       public TextView Pseudo;
       public TextView TrajText;
       public TextView infosUser2;
       public TextView Telephone;
       public ImageView infosUser;
       public ImageButton contact;
       public ImageButton delete;
       
   }
   
   private void menuMesPassagers(final int currentPassagerID, final int alerteID) {

       // Création de la boite de dialogue
       final Dialog dialog = new Dialog(context);

       dialog.setContentView(R.layout.dialogchoices2);
       dialog.setTitle("Supprimer ce passager?");

       final Button oui = (Button) dialog.findViewById(R.id.buttonYes);        
       final Button buttonWait = (Button) dialog.findViewById(R.id.buttonCancel);
       
       oui.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               
               DemandeTrajetRepository demandeTrajetRepository;
               demandeTrajetRepository = new DemandeTrajetRepository(context);
               demandeTrajetRepository.Open();      
               DemandeTrajet demande = demandeTrajetRepository.GetId(String.valueOf(alerteID), String.valueOf(currentPassagerID), String.valueOf(1));
               demande.setEtat(0);
               demandeTrajetRepository.Update(demande);
               demandeTrajetRepository.Close(); 
               dialog.dismiss();        
           }
       });
       
       buttonWait.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
       
           }
       });

       dialog.show();
   }
}
