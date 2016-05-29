package com.android.sharingtaxi.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class TrajetBOpenHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "TaxiSharedDatabase.db";
    public static final String COLUMN_ID = "ID";
    public static final int NUM_COLUMN_ID = 0;
    static final String KEY_TRAJET_COLUMN_DEPART = "depart";
    public static final int NUM_COLUMN_DEPART = 1;
    static final String KEY_TRAJET_COLUMN_ARRIVEE = "arrivee";
    public static final int NUM_COLUMN_ARRIVEE = 2;
    static final String KEY_TRAJET_COLUMN_DATE_DEPART = "datedepart";
    public static final int NUM_COLUMN_DATE_DEPART = 3;

    static final String TRAJET_TABLE = "Trajet";

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_CREATE = "CREATE TABLE " 
        + TRAJET_TABLE + " (" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + KEY_TRAJET_COLUMN_DEPART
        + " TEXT NOT NULL, "
        + KEY_TRAJET_COLUMN_ARRIVEE + " TEXT NOT NULL, "
        + KEY_TRAJET_COLUMN_DATE_DEPART + " TEXT NOT NULL);"; 
    
    TrajetBOpenHelper (Context context, CursorFactory factory){
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
        db.execSQL("DROP TABLE IF IT EXISTS" + TRAJET_TABLE);
        onCreate(db);
    }

}

