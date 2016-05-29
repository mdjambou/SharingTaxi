package com.android.sharingtaxi.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class AlerteBOpenHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "TaxiSharedDatabase.db";
    public static final String COLUMN_ID = "ID";
    public static final int NUM_COLUMN_ID = 0;
    static final String KEY_ALERTE_COLUMN_USER = "user";
    public static final int NUM_COLUMN_USER = 1;
    static final String KEY_ALERTE_COLUMN_TRAJET = "trajet";
    public static final int NUM_COLUMN_TRAJET = 2;
    static final String KEY_TRAJET_COLUMN_NB_PASSAGER = "nb_passager";
    public static final int NUM_COLUMN_PASSAGER = 3;
 

    static final String ALERTE_TABLE = "Alerte";

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_CREATE = "CREATE TABLE " 
        + ALERTE_TABLE + " (" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + KEY_ALERTE_COLUMN_USER 
        + " TEXT NOT NULL, "
        + KEY_ALERTE_COLUMN_TRAJET + " TEXT NOT NULL, "
        + KEY_TRAJET_COLUMN_NB_PASSAGER + " INTEGER NOT NULL);"; 
        //+ " ,FOREIGN KEY ("+KEY_ALERTE_COLUMN_USER+") REFERENCES "+USER_TABLE+" ("+KEY_USER_COLUMN_TELEPHONE+");"; 
    
    AlerteBOpenHelper (Context context, CursorFactory factory){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    
    
    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
    }
    
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("TaskDBAdapter", "Mise à jour de la version" +
              oldVersion+ " vers la version " +
              newVersion+ ", ce qui détruira toutes les anciennes données");
        db.execSQL("DROP TABLE IF IT EXISTS" + ALERTE_TABLE);
        onCreate(db);
    }

}

