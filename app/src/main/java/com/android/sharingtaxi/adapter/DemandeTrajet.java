package com.android.sharingtaxi.adapter;

public class DemandeTrajet
{
    private int userID;
    private int alerteID;
    private int id;
    // etat = -1 <=> alerte annulé ou terminé
    // etat = 4 <=> alerte complet
    private int etat;
        
    public DemandeTrajet(int alerteID, int userID) {
        this.userID = userID;
        this.alerteID = alerteID;
        this.etat = 2; // 0 refusé, 1 accepté, 2 en attente
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getAlerteID() {
        return alerteID;
    }
    public void setAlerteID(int alerteID) {
        this.alerteID = alerteID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getEtat() {
        return etat;
    }
    public void setEtat(int etat) {
        this.etat = etat;
    }
    
}
