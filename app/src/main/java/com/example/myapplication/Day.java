package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class Day extends AppCompatActivity implements Mesage {
TextView text,ost,wast;
Button minus,exit;
Frag dialog;
ProgressBar pro;
Values account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        text = findViewById(R.id.txt);
        ost = findViewById(R.id.ostatok);
        pro=findViewById(R.id.progressBar);
        wast = findViewById(R.id.potracheno);
        minus = findViewById(R.id.min);
        dialog = new Frag();
        account =new Values();
        account.summary=Second.DAY;
        pro.setMax(account.summary);
        text.setText("Сумма на сегодня: "+account.summary);
        minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "frag1");
            }
            }
        );
        exit = findViewById(R.id.day);
        exit.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View v) {
                                      finish();
                                     }
                                 }
        );
    }
    @Override
    public void fragmentvalue(int value){
        if (account.summary - account.waste - value < 0)
            Toast.makeText(getApplicationContext(), "Вы превысили свой лимит,введите меньшую сумму или внесите её", Toast.LENGTH_SHORT).show();
        else {
            account.waste += value;
            wast.setText("Потрачено: " + account.waste);
            pro.setProgress(account.waste);
            ost.setText("Остаток: " + (account.summary - account.waste));
        }
    }
    
}
