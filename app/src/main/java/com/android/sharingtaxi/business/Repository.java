package com.android.sharingtaxi.business;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Repository<T> implements IRepository<T> {
    // Base de données
    protected SQLiteDatabase maBDD;
    
    protected SQLiteOpenHelper sqLiteOpenHelper;
    
    /**
     * Constructeur par défaut
     */
    public Repository() {
        
    }
    
    /**
     * Ouverture de la connection
     */
    public void Open() {
        maBDD = sqLiteOpenHelper.getWritableDatabase();
    }

    /**
     * Fermeture de la connection
     */
    public void Close() {
        maBDD.close();
    }
}
