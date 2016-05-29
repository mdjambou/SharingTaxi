package com.android.sharingtaxi.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserBOpenHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "DoItNow.db";
     
    static final String KEY_USER_COLUMN_CELLNUMBER = "cellNumber";
    public static final int NUM_COLUMN_CELLNUMBER = 0;
    static final String KEY_USER_COLUMN_PSEUDO = "pseudo";
    public static final int NUM_COLUMN_PSEUDO = 1;
    static final String KEY_USER_COLUMN_PASSWORD = "password";
    public static final int NUM_COLUMN_PASSWORD = 2;
    static final String KEY_USER_COLUMN_PICTURE= "picture";
    public static final int NUM_COLUMN_PICTURE = 3;
    static final String KEY_USER_COLUMN_STATUS = "status";
    public static final int NUM_COLUMN_STATUS = 4;
    

    static final String USER_TABLE = "User";

    private static final int DATABASE_VERSION =1;
    
    private static final String DATABASE_CREATE = "CREATE TABLE " 
            + USER_TABLE + " (" + KEY_USER_COLUMN_CELLNUMBER
            + " INTEGER PRIMARY KEY, "
            + KEY_USER_COLUMN_PSEUDO + " TEXT NOT NULL, "
            + KEY_USER_COLUMN_PASSWORD + " TEXT NOT NULL, "
            + KEY_USER_COLUMN_PICTURE + " BLOB, "
            + KEY_USER_COLUMN_STATUS + " TEXT);"; 
        
    UserBOpenHelper (Context context, CursorFactory factory){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    
    
    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
    }
    
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("TaskDBAdapter", "Mise a  jour de la version" +
              oldVersion+ " vers la version " +
              newVersion+ ", ce qui detruira toutes les anciennes donnees");
        db.execSQL("DROP TABLE IF IT EXISTS" + USER_TABLE);
        onCreate(db);
    }

}
