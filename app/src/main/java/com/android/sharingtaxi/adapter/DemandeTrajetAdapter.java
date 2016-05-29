package com.android.sharingtaxi.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.sharingtaxi.CompteUserActivity;
import com.android.sharingtaxi.DetailAlerteActivity;
import com.android.sharingtaxi.DetailPassagerActivity;
import com.android.sharingtaxi.MesDemandesRecuesActivity;
import com.android.sharingtaxi.R;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.DemandeTrajetRepository;
import com.android.sharingtaxi.business.UserRepository;

public class DemandeTrajetAdapter extends BaseAdapter 
{
    private List<DemandeTrajet> listeDemande;
    private List<Trajet> listeTrajet;
    private LayoutInflater inflater;
    int currentUserID;
    private Context context;
    String  typeDemande="Received";

    public void Demande(List<DemandeTrajet> listeDemande) {
        this.listeDemande = listeDemande;
    }

    public DemandeTrajetAdapter(Context context,List<DemandeTrajet> listeDemande, List<Trajet> listeTraj, int currentUserID, String typeDemande) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listeDemande = listeDemande;
        this.listeTrajet = listeTraj;   
        this.currentUserID = currentUserID;
        this.typeDemande = typeDemande;
    }

    @Override
    public int getCount() {
        return listeDemande.size();
    }

    @Override
    public Object getItem(int position) {
        return listeDemande.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listeDemande.get(position).getId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
         final ViewHolder holder;
     
         if (view == null) {
             holder = new ViewHolder();
             if (typeDemande.equals("Received")){
                 view = inflater.inflate(R.layout.layout_item_4, null);
    
                 holder.userPseudo = (TextView) view
                             .findViewById(R.id.ListUserPseudoText);
        
                 holder.accepter = (ImageButton) view
                             .findViewById(R.id.accepterButton);             
        
                 holder.refuser = (ImageButton) view
                             .findViewById(R.id.refuserButton);
             }
             else{
                 view = inflater.inflate(R.layout.layout_item_4_bis, null);
                 
                 holder.userPseudo = (TextView) view
                             .findViewById(R.id.ListUserPseudoText);
        
                 holder.annuler = (ImageButton) view
                             .findViewById(R.id.annulerButton);   
             }
    
                 view.setTag(holder);
             } else {
                 holder = (ViewHolder) view.getTag();
             }
         
         
         String demande="";
         if(listeTrajet.get(position).getNbAlerte() >1)
             demande ="alertes";
         else
             demande = "alerte";
         

         String text="";
         
         if (typeDemande.equals("Received")){
             
             UserRepository userRepository = new UserRepository(context);
             userRepository.Open();  
             User user = userRepository.GetById(listeDemande.get(position).getUserID());
             
             text = user.getPseudo() +" souhaiterais participer au"
                     +" trajet \n du "+listeTrajet.get(position).getDepart()+" au "+ listeTrajet.get(position).getArrivee()+ "\n" 
                     +" le " +listeTrajet.get(position).getDateDepart();
             
             holder.accepter.setOnClickListener(new OnClickListener() {
    
                 @Override
                 public void onClick(View v) {
                     
                     DemandeTrajetRepository demandeTrajetRepository;
                     demandeTrajetRepository = new DemandeTrajetRepository(context);
                     demandeTrajetRepository.Open();
                     
                     DemandeTrajet demande = demandeTrajetRepository.GetById(listeDemande.get(position).getId());
                     demande.setEtat(1);
                     demandeTrajetRepository.Update(demande);
                     demandeTrajetRepository.Close(); 
                     
                     Intent intent = new Intent(context, CompteUserActivity.class);
                     intent.putExtra("userID", currentUserID);
                     context.startActivity(intent); 
                }
    
             });
             
             holder.refuser.setOnClickListener(new OnClickListener() {
                 
                 @Override
                 public void onClick(View v) {
                     
                     DemandeTrajetRepository demandeTrajetRepository;
                     demandeTrajetRepository = new DemandeTrajetRepository(context);
                     demandeTrajetRepository.Open();
                     
                     DemandeTrajet demande = demandeTrajetRepository.GetById(listeDemande.get(position).getId());
                     demande.setEtat(0);
                     demandeTrajetRepository.Update(demande);
                     demandeTrajetRepository.Close();    
                 
                     Intent intent = new Intent(context, CompteUserActivity.class);
                     intent.putExtra("userID", currentUserID);
                     context.startActivity(intent); 
                 }
    
             });

         }else{
             
             AlerteRepository alerteRepository = new AlerteRepository(context);
             alerteRepository.Open();  
             Alerte alerte = alerteRepository.GetById(listeDemande.get(position).getAlerteID());
             
             UserRepository userRepository = new UserRepository(context);
             userRepository.Open();  
             User user = userRepository.GetById(alerte.getUserID());
            
             String state="En attente";
             int etatTr = listeDemande.get(position).getEtat();
             if (etatTr == 0)
                 state = "Refusé";
             else{
                 if(etatTr == 1)
                     state = "Accepté";
             }
             
             text =" Trajet du "+listeTrajet.get(position).getDepart()+" au "+ listeTrajet.get(position).getArrivee()+ "\n" 
                             +" le " +listeTrajet.get(position).getDateDepart()+"\n"
                             +" proposé par l'utilisateur "+ user.getPseudo() + "\n"
                             +"Etat de la demande: "+state;
     
             

             holder.annuler.setOnClickListener(new OnClickListener() {
                 
                 @Override
                 public void onClick(View v) {
                     AnnulerDemande(listeDemande.get(position).getId(),currentUserID);
                 }
    
             });
         }

         holder.userPseudo.setText(text);

         return view;
     }

    private class ViewHolder {
        public TextView userPseudo;
        public ImageButton accepter;
        public ImageButton refuser;
        public ImageButton annuler;

    }
    
    private void AnnulerDemande(final int demandeID, int userID) {

        // Création de la boite de dialogue
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialogannul);
        dialog.setTitle("Se désinscrire du trajet");

        final Button buttonYes = (Button) dialog.findViewById(R.id.buttonYes);
        final Button buttonWait = (Button) dialog.findViewById(R.id.buttonCancel);

        buttonYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                DemandeTrajetRepository demandeTrajetRepository;
                demandeTrajetRepository = new DemandeTrajetRepository(context);
                demandeTrajetRepository.Open();
                
                DemandeTrajet demande = demandeTrajetRepository.GetById(demandeID);
                demandeTrajetRepository.Delete(demande.getId());
                demandeTrajetRepository.Close(); 
                dialog.dismiss();
                
                Intent intent = new Intent(context, CompteUserActivity.class);
                intent.putExtra("userID", currentUserID);
                context.startActivity(intent); 

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
