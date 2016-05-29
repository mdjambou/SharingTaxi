package com.android.sharingtaxi.adapter;

public class Alerte
{
    private int userID;
    private int trajetID;
    private int id;
    // passager = -1 <=> trajet annulé ou terminé
    // passager = 4 <=> trajet complet
    private int passager;
        
    public Alerte(int userID, int trajetID) {
        this.userID = userID;
        this.trajetID = trajetID;
        this.passager = 1;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getTrajetID() {
        return trajetID;
    }
    public void setTrajetID(int trajetID) {
        this.trajetID = trajetID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPassager() {
        return passager;
    }
    public void setPassager(int passager) {
        this.passager = passager;
    }
    
}
