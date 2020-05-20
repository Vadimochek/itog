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
        db.execSQL("create table datatable (" +"date text,"+"value integer,"+"direction text,"+"day integer"+");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}
}
