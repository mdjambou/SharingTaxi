package com.android.sharingtaxi.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.sharingtaxi.adapter.Alerte;

public class AlerteRepository extends Repository<Alerte> {
    
    public AlerteRepository(Context context) {
        sqLiteOpenHelper = new MyTaxiSharedOpenHelper(context, null);
    }

    /**
     * Suppression d'un produit
     * 
     * @param id
     */
    public void DeleteAlerte(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.ALERTE_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Récupération de la liste de tous les produits
     */
    @Override
    public List<Alerte> GetAll() {
        // Récupération de la liste des courses
        String orderBy = MyTaxiSharedOpenHelper.COLUMN_ID + " DESC";
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.ALERTE_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER}, null, null, null, null, orderBy);

        return ConvertCursorToListObject(cursor);
    }

    /**
     * Retourne un seul produit
     */
    @Override
    public Alerte GetById(int id) {
        String orderBy = MyTaxiSharedOpenHelper.COLUMN_ID + " DESC";
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.ALERTE_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER},
                MyTaxiSharedOpenHelper.COLUMN_ID+ "=?",
                new String[] { String.valueOf(id) }, null, null, orderBy);

        return ConvertCursorToObject(cursor);
    }
    
    @Override
    public Alerte GetId(String trajetID, String userID, String nbPassager)
    {
        String orderBy = MyTaxiSharedOpenHelper.COLUMN_ID + " DESC";
        String[] result_columns = new String[]{
                        MyTaxiSharedOpenHelper.COLUMN_ID, MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER
        };
        
        int trajID = Integer.parseInt(trajetID);
        int uzID = Integer.parseInt(userID);
        int nb = Integer.parseInt(nbPassager);
        String where = MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER +"='" + uzID
                        +"' AND "+MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET + "='" + trajID + "'";
        
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.ALERTE_TABLE,result_columns,where,null,null,null,orderBy);

        return ConvertCursorToObject(cursor);
    }

    @Override
    public List<Alerte> GetAllByAttribute(int attr, String typeAttr)
    {
        String[] result_columns = new String[]{
                        MyTaxiSharedOpenHelper.COLUMN_ID, MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER,
                        MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER
        };
        String where =null;
        
        if(typeAttr.equals("TrajetID")){
            where = MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET + "=?" + String.valueOf(attr);
        }
        
        if(typeAttr.equals("UserID")){
            where = MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER + "=?" + String.valueOf(attr);
        }
        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.ALERTE_TABLE,result_columns,where,null,null,null,null);

        return ConvertCursorToListObject(cursor);
    }
    /**
     * Enregistre en produit dans la base
     */
    @Override
    public void Save(Alerte entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET, entite.getTrajetID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER, entite.getUserID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER,
                entite.getPassager());

        maBDD.insert(MyTaxiSharedOpenHelper.ALERTE_TABLE, null, contentValues);
    }

    /**
     * Met à jour un produit
     */
    @Override
    public void Update(Alerte entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_TRAJET, entite.getTrajetID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_ALERTE_COLUMN_USER, entite.getUserID());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NB_PASSAGER,
                entite.getPassager());

        maBDD.update(MyTaxiSharedOpenHelper.ALERTE_TABLE, contentValues,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(entite.getId()) });
    }

    /**
     * Supprime un produit
     */
    @Override
    public void Delete(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.ALERTE_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Converti un curseur en une liste de produits
     */
    @Override
    public List<Alerte> ConvertCursorToListObject(Cursor c) {
        List<Alerte> liste = new ArrayList<Alerte>();

        if(c!=null){
            // Si la liste est vide
            if (c.getCount() == 0)
                return liste;
    
            // position sur le premeir item
            c.moveToFirst();
    
            // Pour chaque item
            int i = 0;
            while (i<c.getCount()){
    
                Alerte alerte = new Alerte(
                       c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_TRAJET),
                       c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_USER));
                alerte.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                alerte.setPassager(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_PASSAGER));
    
                liste.add(alerte);
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
    public Alerte ConvertCursorToObject(Cursor c) {

        Alerte alerte = null;
        if(c!=null){
            if(c.moveToFirst()){
                alerte = new Alerte(
                c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_TRAJET),
                c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_USER));
       
                alerte.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                alerte.setPassager(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_PASSAGER));
            }
        }
        return alerte;
    }

    /**
     * Converti un curseur en un produit
     */
    @Override
    public Alerte ConvertCursorToOneObject(Cursor c) {
        c.moveToFirst();

        Alerte alerte = ConvertCursorToObject(c);

        c.close();
        return alerte;
    }

}
