package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context,"DataBase",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table datatable (" +"date text,"+"value integer,"+"direction text"+");");
        final int oldVersion =1;
    }
    final int newVersion=2;
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion == 1 && newVersion == 2) {
            try {
                // создаем таблицу должностей
                db.execSQL("create table datatable (" +"date text,"+"value integer,"+"direction text,"+"dayvalue integer"+");");
            }catch (Exception e){
                e.printStackTrace();
            }
    }
    }
}
