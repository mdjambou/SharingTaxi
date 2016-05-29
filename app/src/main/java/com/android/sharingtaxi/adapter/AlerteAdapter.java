package com.android.sharingtaxi.adapter;

import java.util.List;

import com.android.sharingtaxi.DetailPassagerActivity;
import com.android.sharingtaxi.R;
import com.android.sharingtaxi.adapter.TrajetAdapter.ViewHolder;
import com.android.sharingtaxi.business.AlerteRepository;
import com.android.sharingtaxi.business.DemandeTrajetRepository;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AlerteAdapter extends BaseAdapter 
{
    private List<Alerte> listeAlerte;
    private List<Trajet> listeTrajet;
    private LayoutInflater inflater;
    private Context context;
    int userID;

    public void setAlertes(List<Alerte> listeAlerte) {
        this.listeAlerte = listeAlerte;
    }

    public AlerteAdapter(Context context,List<Trajet> listeTraj, List<Alerte> listeAlertee, int userID) {
        this.listeAlerte = listeAlertee;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listeTrajet = listeTraj;
        this.userID = userID;
  }

    @Override
    public int getCount() {
        return listeAlerte.size();
    }

    @Override
    public Object getItem(int position) {
        return listeAlerte.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listeAlerte.get(position).getId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
         final ViewHolder holder;

         if (view == null) {
             holder = new ViewHolder();
             view = inflater.inflate(R.layout.layout_item, null);

            
             holder.open = (Button) view
                     .findViewById(R.id.openButton);
             

             view.setTag(holder);
         } else {
             holder = (ViewHolder) view.getTag();
         }
         
         holder.open.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View v) {
                 
                 Intent intent = new Intent(context, DetailPassagerActivity.class);
                 intent.putExtra("Depart", listeTrajet.get(position).getDepart());
                 intent.putExtra("Arrivee", listeTrajet.get(position).getArrivee());               
                 intent.putExtra("dateDepart", listeTrajet.get(position).getDateDepart());
                 intent.putExtra("userID", userID);
                 context.startActivity(intent); 
            }
         });
         
         /*holder.delete.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View v) {                
                 SelectChoice(listeAlerte.get(position).getId(), userID, listeTrajet.get(position).getDepart(), listeTrajet.get(position).getArrivee(), listeTrajet.get(position).getDateDepart());
            }
         });  */
         
         String passager;
         if(listeAlerte.get(position).getPassager() >1)
             passager ="passagers";
         else
             passager = "passager";
         String text = listeTrajet.get(position).getDepart()+ '-' + 
                       listeTrajet.get(position).getArrivee() + "\n" + "Le " +
                       listeTrajet.get(position).getDateDepart() + "\n"+
                       listeAlerte.get(position).getPassager() + ' ' + passager;
         
         holder.open.setText(text);

         return view;
     }

    private class ViewHolder {
        public Button open;

    }
    
    
    /*private void SelectChoice(final int alerteID, final int userID, final String depart, final String arrivee, final String datedep) {

        // Création de la boite de dialogue
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialogannul);
        dialog.setTitle("Supprimer ce trajet?");

        final Button oui = (Button) dialog.findViewById(R.id.buttonYes);
        final Button buttonWait = (Button) dialog.findViewById(R.id.buttonCancel);
        
        oui.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                AlerteRepository alerteRepository;
                alerteRepository = new AlerteRepository(context);
                alerteRepository.Open();                
                alerteRepository.Delete(alerteID);
                alerteRepository.Close(); 
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
    }*/

    

}
