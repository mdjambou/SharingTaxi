package com.android.sharingtaxi.adapter;

import java.util.List;

import com.android.sharingtaxi.DetailAlerteActivity;
import com.android.sharingtaxi.DetailPassagerActivity;
import com.android.sharingtaxi.Main2Activity;
import com.android.sharingtaxi.R;
import com.android.sharingtaxi.TousLesTrajetsActivity;
import com.android.sharingtaxi.business.AlerteRepository;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TrajetAdapter extends BaseAdapter 
{
    private List<Trajet> listeTrajet;
    private LayoutInflater inflater;
    int userID;
    private Context context;
    private String previousActivity;

    public void setTrajets(List<Trajet> listeTrajet) {
        this.listeTrajet = listeTrajet;
    }

    public TrajetAdapter(Context context, List<Trajet> listeTrajet, int userID, String main) {
        this.listeTrajet = listeTrajet;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.userID = userID;
        this.previousActivity=main;
    }

    @Override
    public int getCount() {
        return listeTrajet.size();
    }

    @Override
    public Object getItem(int position) {
        return listeTrajet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listeTrajet.get(position).getId();
    }


   @Override
   public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_item_bis, null);
            
            holder.open = (Button) view
                    .findViewById(R.id.openButton);
            holder.textSuiv = (TextView) view
            .findViewById(R.id.TrajText);            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        
       holder.open.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {   
               if(previousActivity.equals("Main")){
                   Intent intent = new Intent(context, Main2Activity.class);
                   intent.putExtra("pointDepart", listeTrajet.get(position).getDepart());
                   intent.putExtra("pointArrivee", listeTrajet.get(position).getArrivee());               
                        //intent.putExtra("dateDepart", listeTrajet.get(position).getDateDepart());
                   intent.putExtra("userID", userID);
                   context.startActivity(intent);
    
               }else{         
                   Intent intent = new Intent(context, DetailAlerteActivity.class);
                   intent.putExtra("Depart", listeTrajet.get(position).getDepart());
                   intent.putExtra("Arrivee", listeTrajet.get(position).getArrivee());               
                   intent.putExtra("dateDepart", listeTrajet.get(position).getDateDepart());
                   intent.putExtra("userID", userID);
                   context.startActivity(intent);    
               }
           }
       });
        
        String alerte="";
        if(listeTrajet.get(position).getNbAlerte()>1)
            alerte ="personnes intéressées";
        else
            alerte = "personne intéressée";

        String text = "De-> "+ listeTrajet.get(position).getDepart()+ "\nA  -> " + listeTrajet.get(position).getArrivee(); 
        if(previousActivity.equals("Main")){
            holder.open.setText(text);
        }else{
            String textSuiv = "\nAller le "+ listeTrajet.get(position).getDateDepart() + "\n"+
            listeTrajet.get(position).getNbAlerte() + ' ' + alerte;
            holder.open.setText(text+"\t \n"+textSuiv);
        }
       // holder.textSuiv.setText(textSuiv);
       /* holder.arrivee.setText(listeTrajet.get(position).getArrivee());
        holder.dateDep.setText(listeTrajet.get(position).getDateDepart()+ "");
        String nb = String.valueOf(listeTrajet.get(position).getNbAlerte());
        holder.nbAlerte.setText(nb);*/

        
        return view;
    }

    public class ViewHolder {
        //public TextView depart;
        public Button open;
        public TextView nbAlerte;
        public TextView textSuiv;
        public TextView dateDep;

    }
    
}
