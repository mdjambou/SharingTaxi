package com.android.sharingtaxi.adapter;

public class User
{
    private int telephone;
    private String pseudo;
    private String password;
        
    public User(String pseudo, int telephone, String password) {
        this.pseudo = pseudo;
        this.telephone = telephone;
        this.password = password;
    }
    
    public int getTelephone() {
        return telephone;
    }
    public void setTelephone(int tel) {
        this.telephone = tel;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
    
}
