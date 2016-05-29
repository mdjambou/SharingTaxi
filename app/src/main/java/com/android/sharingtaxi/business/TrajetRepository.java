package com.android.sharingtaxi.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.android.sharingtaxi.adapter.Alerte;
import com.android.sharingtaxi.adapter.Trajet;

public class TrajetRepository extends Repository<Trajet> {
    
    public TrajetRepository(Context context) {
        sqLiteOpenHelper = new MyTaxiSharedOpenHelper(context, null);
    }
    
    /**
     * Suppression d'un produit
     * 
     * @param id
     */
    public void DeleteTrajet(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.TRAJET_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Récupération de la liste de tous les produits
     */
    @Override
    public List<Trajet> GetAll() {
        // Récupération de la liste des courses
        String orderBy = MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART + " DESC";

        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.TRAJET_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                              MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART,
                              MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NBALERTE,
                              MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE,
                              MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART }, null, null, null,
                null, orderBy);

        return ConvertCursorToListObject(cursor);
    }

    /**
     * Retourne un seul produit
     */
    @Override
    public Trajet GetById(int id) {
        String orderBy = MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART + " DESC";

        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.TRAJET_TABLE,
                new String[] { MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NBALERTE,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART },
                MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, orderBy);

        return ConvertCursorToObject(cursor);
    }
    
    @Override
    public Trajet GetId(String depart, String arrivee, String dateDep) {
        String[] result_columns = new String[]{
                        MyTaxiSharedOpenHelper.COLUMN_ID,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NBALERTE,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE,
                        MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART
        };
        
        String where = MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART +"='" + depart
                        +"' AND "+MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE + "='" + arrivee
                        +"' AND "+MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART + "='" + dateDep + "'";
        
        String orderBy = MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART + " DESC";

        Cursor cursor = maBDD.query(MyTaxiSharedOpenHelper.TRAJET_TABLE,result_columns,where,null,null,null,orderBy);

        return ConvertCursorToObject(cursor);
    }

    /**
     * Enregistre en produit dans la base
     */
    @Override
    public void Save(Trajet entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART, entite.getDepart());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NBALERTE, entite.getNbAlerte());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE, entite.getArrivee());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART, entite.getDateDepart());
        maBDD.insert(MyTaxiSharedOpenHelper.TRAJET_TABLE, null, contentValues);
    }

    /**
     * Met à jour un produit
     */
    @Override
    public void Update(Trajet entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DEPART, entite.getDepart());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_NBALERTE, entite.getNbAlerte());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_ARRIVEE, entite.getArrivee());
        contentValues.put(MyTaxiSharedOpenHelper.KEY_TRAJET_COLUMN_DATE_DEPART, entite.getDateDepart());
        
        maBDD.update(MyTaxiSharedOpenHelper.TRAJET_TABLE, contentValues,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] {String.valueOf(entite.getId())});
    }
    
    /**
     * Supprime un produit
     */
    @Override
    public void Delete(int id) {
        maBDD.delete(MyTaxiSharedOpenHelper.TRAJET_TABLE,
                        MyTaxiSharedOpenHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Converti un curseur en une liste de produits
     */
    @Override
    public List<Trajet> ConvertCursorToListObject(Cursor c) {
        List<Trajet> liste = new ArrayList<Trajet>();

        if(c!=null){
                // Si la liste est vide
                if (c.getCount() == 0)
                    return liste;
        
                // position sur le premeir item
                c.moveToFirst();
        
                // Pour chaque item
                int i = 0;
                while (i<c.getCount()){
                    Trajet trajet = new Trajet(
                                    c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_DEPART),
                                    c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_ARRIVEE),
                                    c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_DATE_DEPART));
                       
                        trajet.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                        trajet.setNbAlerte(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_NBALERTE));
                    //Trajet trajet = ConvertCursorToObject(c);
                    liste.add(trajet);
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
     * @throws ParseException 
     */
    @Override
    public Trajet ConvertCursorToObject(Cursor c){

        Trajet trajet = null;
        if(c!=null){
           if(c.moveToFirst()){
                trajet = new Trajet(
                            c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_DEPART),
                            c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_ARRIVEE),
                            c.getString(MyTaxiSharedOpenHelper.NUM_COLUMN_DATE_DEPART));
               
                trajet.setId(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_ID));
                trajet.setNbAlerte(c.getInt(MyTaxiSharedOpenHelper.NUM_COLUMN_NBALERTE));
           }
        }

        return trajet;
    }

    /**
     * Converti un curseur en un produit
     */
    @Override
    public Trajet ConvertCursorToOneObject(Cursor c) {
        c.moveToFirst();

        Trajet trajet = ConvertCursorToObject(c);

        c.close();
        return trajet;
    }

    @Override
    public List<Trajet> GetAllByAttribute(int attr, String typeAttr)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
