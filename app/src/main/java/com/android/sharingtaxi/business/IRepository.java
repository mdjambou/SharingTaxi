package com.android.sharingtaxi.business;

import java.util.List;

import com.android.sharingtaxi.adapter.Alerte;

import android.database.Cursor;

public interface IRepository<T>
{

    public List<T> GetAll();
    public T GetById(int id);
    public T GetId(String arg1, String arg2, String arg3);
    public List<T> GetAllByAttribute(int attr, String typeAttr);
    
    public void Save(T entite);
    public void Update(T entite);
    public void Delete(int id);
    
    public List<T> ConvertCursorToListObject(Cursor c);
    public T ConvertCursorToObject(Cursor c);
    public T ConvertCursorToOneObject(Cursor c);
}
