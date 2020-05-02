package com.example.myapplication;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
ProgressBar pb;
Button sum,min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
           pb = findViewById(R.id.pB);
        min = findViewById(R.id.minus);
        sum = findViewById(R.id.plus);
        min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frag frag1 = new Frag();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.frag2, frag1);
                ft.commit();
                Toast toast = Toast.makeText(getApplicationContext(),"Пора покормить кота!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }



}
