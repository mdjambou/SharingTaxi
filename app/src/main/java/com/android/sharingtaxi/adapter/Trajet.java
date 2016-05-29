package com.android.sharingtaxi.adapter;

import java.util.Date;

public class Trajet
{
    private String depart;
    private String arrivee;
    private String dateDepart;
    private int nbAlerte;
    private int id;
        
    public Trajet(String depart, String arrivee, String dateDep) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.dateDepart = dateDep;
        this.nbAlerte = 0;
       // this.id = Integer.parseInt(depart) + Integer.parseInt(arrivee);

    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDepart() {
        return depart;
    }
    public void setDepart(String depart) {
        this.depart = depart;
    }
    public String getArrivee() {
        return arrivee;
    }
    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }
    public String getDateDepart() {
        return dateDepart;
    }
    public void setdateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }
    
    public int getNbAlerte() {
        return nbAlerte;
    }
    public void setNbAlerte(int nbAlerte) {
        this.nbAlerte = nbAlerte;
    }
}
