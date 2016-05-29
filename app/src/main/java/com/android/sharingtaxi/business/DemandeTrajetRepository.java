package com.android.sharingtaxi.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sharingtaxi.adapter.DemandeTrajet;
import com.android.sharingtaxi.adapter.DemandeTrajet;

public class DemandeTrajetRepository extends Repository<DemandeTrajet> {

    public DemandeTrajetRepository(Context context) {
        sqLiteOpenHelper = new MyTaxiSharedOpenHelper(context, null);
    }

    /**
     * Suppression d'un produit
     * 
     * @param id
     */
    public void DeleteDemandeTrajet(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Récupération de la liste de tous les produits
     */
    @Override
    public List<DemandeTrajet> GetAll() {
        // Récupération de la liste des courses
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT}, null, null, null, null, null);

        return ConvertCursorToListObject(cursor);
    }

    /**
     * Retourne un seul produit
     */
    @Override
    public DemandeTrajet GetById(int id) {
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT},
                MyTaxiSharedOpenHelper.COLUMN_ID+ "=?",
                new String[] { String.valueOf(id) }, null, null, null);

        return ConvertCursorToObject(cursor);
    }
    
    @Override
    public DemandeTrajet GetId(String alerteID, String userID, String etatDemande)
    {
        String[] result_columns = new String[]{
                        MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT
        };
        
        int alertID = Integer.parseInt(alerteID);
        int uzID = Integer.parseInt(userID);
        int etat = Integer.parseInt(etatDemande);
        String where = MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER +"='" + uzID
                        +"' AND "+MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE + "='" + alertID + "'";
        
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,result_columns,where,null,null,null,null);

        return ConvertCursorToObject(cursor);
    }

    @Override
    public List<DemandeTrajet> GetAllByAttribute(int attr, String typeAttr)
    {
        String[] result_columns = new String[]{
                        MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT
        };
        String where =null;
        
        if(typeAttr.equals("AlerteID")){
            where = MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE + "=?" + attr;
        }
        
        if(typeAttr.equals("UserID")){
            where = MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER + "=?" + attr;
        }
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,result_columns,where,null,null,null,null);

        return ConvertCursorToListObject(cursor);
    }
    /**
     * Enregistre en produit dans la base
     */
    @Override
    public void Save(DemandeTrajet entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE, entite.getAlerteID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER, entite.getUserID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT,
                entite.getEtat());

        maBDD.insert(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE, null, contentValues);
    }

    /**
     * Met à jour un produit
     */
    @Override
    public void Update(DemandeTrajet entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ALERTE, entite.getAlerteID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_USER, entite.getUserID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_DEMANDETRAJET_COLUMN_ETAT,
                entite.getEtat());

        maBDD.update(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE, contentValues,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(entite.getId()) });
    }

    /**
     * Supprime un produit
     */
    @Override
    public void Delete(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.DEMANDETRAJET_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Converti un curseur en une liste de produits
     */
    @Override
    public List<DemandeTrajet> ConvertCursorToListObject(Cursor c) {
        List<DemandeTrajet> liste = new ArrayList<DemandeTrajet>();

        if(c!=null){
            // Si la liste est vide
            if (c.getCount() == 0)
                return liste;
    
            // position sur le premeir item
            c.moveToFirst();
    
            // Pour chaque item
            int i = 0;
            while (i<c.getCount()){
    
                DemandeTrajet DemandeTrajet = new DemandeTrajet(
                       c.getInt(MyTaxiSharedOpenHelper.NUM_DEMANDETRAJET_ALERTE),
                       c.getInt(MyTaxiSharedOpenHelper.NUM_DEMANDETRAJET_USER));
                DemandeTrajet.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                DemandeTrajet.setEtat(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ETAT));
    
                liste.add(DemandeTrajet);
                i++;
                c.moveToNext();
            }
        }

        // Fermeture du curseur
        c.close();

        return liste;
    }

    /**
     * Méthode utilisée par ConvertCursorToObject et ConvertCursorToListObject
     */
    @Override
    public DemandeTrajet ConvertCursorToObject(Cursor c) {

        DemandeTrajet DemandeTrajet = null;
        if(c!=null){
            if(c.moveToFirst()){
                DemandeTrajet = new DemandeTrajet(
                c.getInt(MyTaxiSharedOpenHelper.NUM_DEMANDETRAJET_ALERTE),
                c.getInt(MyTaxiSharedOpenHelper.NUM_DEMANDETRAJET_USER));
       
                DemandeTrajet.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                DemandeTrajet.setEtat(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ETAT));
            }
        }
        return DemandeTrajet;
    }

    /**
     * Converti un curseur en un produit
     */
    @Override
    public DemandeTrajet ConvertCursorToOneObject(Cursor c) {
        c.moveToFirst();

        DemandeTrajet DemandeTrajet = ConvertCursorToObject(c);

        c.close();
        return DemandeTrajet;
    }

}
