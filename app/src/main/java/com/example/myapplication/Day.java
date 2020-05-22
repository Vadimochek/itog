package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class Day extends AppCompatActivity {
TextView text;
DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        text = findViewById(R.id.txt);
        dbHelper = new DBHelper(this);
    }

            public void onClick(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c=db.query("datatable",null,null,null,null,null,null);
        if (c.moveToFirst()) {
            int value = c.getColumnIndex("dayvalue");
           text.setText(c.getInt(value));
        }
        else Toast.makeText(getApplicationContext(), "Ничего", Toast.LENGTH_LONG).show();
        c.close();
        dbHelper.close();
    }
}
