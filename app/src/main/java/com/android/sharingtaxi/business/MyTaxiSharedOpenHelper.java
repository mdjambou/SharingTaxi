package com.android.sharingtaxi.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyTaxiSharedOpenHelper extends SQLiteOpenHelper{
   
    static final String DATABASE_NAME = "ParisTaxiShared2.db";
    
    static final String KEY_USER_COLUMN_TELEPHONE = "telephone";
    public static final int NUM_COLUMN_TELEPHONE = 0;
    static final String KEY_USER_COLUMN_PSEUDO = "pseudo";
    public static final int NUM_COLUMN_PSEUDO = 1;
    static final String KEY_USER_COLUMN_PASSWORD = "password";
    public static final int NUM_COLUMN_PASSWORD = 2;
    
    public static final String COLUMN_ID = "ID";
    public static final int NUM_COLUMN_ID = 0;
    public static final String ID_TRAJET = "IDTRAJET";
    public static final int NUM_ID_TRAJET = 0;
    static final String KEY_TRAJET_COLUMN_DEPART = "depart";
    public static final int NUM_COLUMN_DEPART = 1;
    static final String KEY_TRAJET_COLUMN_NBALERTE = "nbAlerte";
    public static final int NUM_COLUMN_NBALERTE = 2;    
    static final String KEY_TRAJET_COLUMN_ARRIVEE = "arrivee";
    public static final int NUM_COLUMN_ARRIVEE = 3;
    static final String KEY_TRAJET_COLUMN_DATE_DEPART = "datedepart";
    public static final int NUM_COLUMN_DATE_DEPART = 4;
    
    static final String KEY_ALERTE_COLUMN_USER = "user";
    public static final int NUM_COLUMN_USER = 1;
    static final String KEY_ALERTE_COLUMN_TRAJET = "trajet";
    public static final int NUM_COLUMN_TRAJET = 2;
    static final String KEY_TRAJET_COLUMN_NB_PASSAGER = "nb_passager";
    public static final int NUM_COLUMN_PASSAGER = 3;
 
    static final String KEY_DEMANDETRAJET_COLUMN_ALERTE = "idAlerte";
    public static final int NUM_DEMANDETRAJET_ALERTE = 1;
    static final String KEY_DEMANDETRAJET_COLUMN_USER = "idUser";
    public static final int NUM_DEMANDETRAJET_USER = 2;
    static final String KEY_DEMANDETRAJET_COLUMN_ETAT = "etat";
    public static final int NUM_COLUMN_ETAT = 3;
    
    static final String USER_TABLE = "User";
    static final String ALERTE_TABLE = "Alerte";
    static final String TRAJET_TABLE = "Trajet";
    static final String DEMANDETRAJET_TABLE = "DemandeTrajet";

    private static final int DATABASE_VERSION =1;
    private static final String USER_CREATE = "CREATE TABLE " 
        + USER_TABLE + " (" + KEY_USER_COLUMN_TELEPHONE
        + " INTEGER PRIMARY KEY, "
        + KEY_USER_COLUMN_PSEUDO + " TEXT NOT NULL, "
        + KEY_USER_COLUMN_PASSWORD + " TEXT NOT NULL);"; 
    
    private static final String TRAJET_CREATE = "CREATE TABLE " 
        + TRAJET_TABLE + " (" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + KEY_TRAJET_COLUMN_DEPART
        + " TEXT NOT NULL, "
        + KEY_TRAJET_COLUMN_NBALERTE + " INTEGER NOT NULL, "
        + KEY_TRAJET_COLUMN_ARRIVEE + " TEXT NOT NULL, "
        + KEY_TRAJET_COLUMN_DATE_DEPART + " TEXT NOT NULL);"; 
    
    private static final String ALERTE_CREATE = "CREATE TABLE " 
        + ALERTE_TABLE + " (" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + KEY_ALERTE_COLUMN_USER 
        + " INTEGER NOT NULL, "
        + KEY_ALERTE_COLUMN_TRAJET + " INTEGER NOT NULL, "
        + KEY_TRAJET_COLUMN_NB_PASSAGER + " INTEGER NOT NULL);"; 
        //+ " ,FOREIGN KEY ("+KEY_ALERTE_COLUMN_USER+") REFERENCES "+USER_TABLE+" ("+KEY_USER_COLUMN_TELEPHONE+");"; 
    
    
    private static final String DEMANDETRAJET_CREATE = "CREATE TABLE " 
        + DEMANDETRAJET_TABLE + " (" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + KEY_DEMANDETRAJET_COLUMN_ALERTE 
        + " INTEGER NOT NULL, "
        + KEY_DEMANDETRAJET_COLUMN_USER + " INTEGER NOT NULL, "
        + KEY_DEMANDETRAJET_COLUMN_ETAT + " INTEGER NOT NULL);"; 
    
    MyTaxiSharedOpenHelper (Context context, CursorFactory factory){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    
    
    @Override
    public void onCreate (SQLiteDatabase db){
        Log.v("INFO1","creating db");
        db.execSQL(USER_CREATE);
        db.execSQL(TRAJET_CREATE);
        db.execSQL(ALERTE_CREATE);
        db.execSQL(DEMANDETRAJET_CREATE);

    }
    
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("TaskDBAdapter", "Mise à jour de la version" +
              oldVersion+ " vers la version " +
              newVersion+ ", ce qui détruira toutes les anciennes données");
        db.execSQL("DROP TABLE IF IT EXISTS" + USER_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS" + TRAJET_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS" + ALERTE_TABLE);
        db.execSQL("DROP TABLE IF IT EXISTS" + DEMANDETRAJET_TABLE);
        db.execSQL("DROP TABLE IF EXISTS android_metadata");

        onCreate(db);
    }

}

