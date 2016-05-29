package com.android.sharingtaxi.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UserRepository extends Repository<User> {
    
    public UserRepository(Context context) {
        sqLiteOpenHelper = new DoItNowOpenHelper(context, null);
    }

    /**
     * Suppression d'un produit
     * 
     * @param id
     */
    public void DeleteUser(int telephone) {
        maBDD.delete(DoItNowOpenHelper.USER_TABLE,
                        DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE + "=?",
                new String[] { String.valueOf(telephone) });
    }

    /**
     * Récupération de la liste de tous les produits
     */
    @Override
    public List<User> GetAll() {
        // Récupération de la liste des courses
        Cursor cursor = maBDD.query(DoItNowOpenHelper.USER_TABLE,
                new String[] { DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD}, null, null, null,
                null, null);

        return ConvertCursorToListObject(cursor);
    }

    /**
     * Retourne un seul produit
     */
    @Override
    public User GetById(int id) {
        Cursor cursor = maBDD.query(DoItNowOpenHelper.USER_TABLE,
                new String[] { DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD},
                DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE + "=?",
                new String[] { String.valueOf(id) }, null, null, null);

        return ConvertCursorToObject(cursor);
    }

    @Override
    public User GetId(String pseudo, String password,String tel)
    {
        String[] result_columns = new String[]{ DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO,
                        DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD
        };
        
        String where = DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO + "='" + pseudo
                        +"' AND "+DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD + "='" + password + "'";
        
        Cursor cursor = maBDD.query(DoItNowOpenHelper.USER_TABLE,result_columns,where,null,null,null,null);

        return ConvertCursorToObject(cursor);
    }

    /**
     * Enregistre en produit dans la base
     */
    @Override
    public void Save(User entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE, entite.getTelephone());
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO, entite.getPseudo());
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD, entite.getPassword());

        maBDD.insert(DoItNowOpenHelper.USER_TABLE, null, contentValues);
    }

    /**
     * Met à jour un produit
     */
    @Override
    public void Update(User entite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE, entite.getTelephone());
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_PSEUDO, entite.getPseudo());
        contentValues.put(DoItNowOpenHelper.KEY_USER_COLUMN_PASSWORD, entite.getPassword());


        maBDD.update(DoItNowOpenHelper.USER_TABLE, contentValues,
                        DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE + "=?",
                new String[] { String.valueOf(entite.getTelephone()) });
    }

    /**
     * Supprime un produit
     */
    @Override
    public void Delete(int id) {
        maBDD.delete(DoItNowOpenHelper.USER_TABLE,
                        DoItNowOpenHelper.KEY_USER_COLUMN_TELEPHONE + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Converti un curseur en une liste de produits
     */
    @Override
    public List<User> ConvertCursorToListObject(Cursor c) {
        List<User> liste = new ArrayList<User>();

        // Si la liste est vide
        if (c.getCount() == 0)
            return liste;

        // position sur le premeir item
        c.moveToFirst();

        // Pour chaque item
        int i = 0;
        while (i<c.getCount()){

            User user = new User(
                            c.getString(DoItNowOpenHelper.NUM_COLUMN_PSEUDO),
                            c.getInt(DoItNowOpenHelper.NUM_COLUMN_TELEPHONE),
                            c.getString(DoItNowOpenHelper.NUM_COLUMN_PASSWORD));

            liste.add(user);
            i++;
            c.moveToNext();
        }

        // Fermeture du curseur
        c.close();

        return liste;
    }

    /**
     * Méthode utilisée par ConvertCursorToObject et ConvertCursorToListObject
     */
    @Override
    public User ConvertCursorToObject(Cursor c) {
        User user = null;
        
       if(c!=null){
           if(c.moveToFirst()){
               user = new User(
                               c.getString(DoItNowOpenHelper.NUM_COLUMN_PSEUDO),
                               c.getInt(DoItNowOpenHelper.NUM_COLUMN_TELEPHONE),
                               c.getString(DoItNowOpenHelper.NUM_COLUMN_PASSWORD));
           }
       }
                

        return user;
    }

    /**
     * Converti un curseur en un produit
     */
    @Override
    public User ConvertCursorToOneObject(Cursor c) {
        c.moveToFirst();

        User user = ConvertCursorToObject(c);

        c.close();
        return user;
    }

    @Override
    public List<User> GetAllByAttribute(int attr, String typeAttr)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
