package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Information extends AppCompatActivity  {
    DBHelper dbHelper;
    Button query;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        query=findViewById(R.id.query);
        text=findViewById(R.id.id);
        dbHelper=new DBHelper(this);
    }


    public void onClick(View v){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor c=db.query("DataBase",null,null,null,null,null,null);
       /* if (c.moveToFirst()){
            int id =c.getColumnIndex("id");
            int val=c.getColumnIndex("value");
            int direct=c.getColumnIndex("direction");
            do{
                text.setText(c.getString(id)+" "+c.getString(direct)+" "+c.getString(val));
            }while(c.moveToNext());
        }
        else Toast.makeText(getApplicationContext(),"Ничего",Toast.LENGTH_LONG).show();*/
        c.close();
        dbHelper.close();
    }
}
