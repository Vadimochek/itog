package com.example.myapplication;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements Mesage,Summary {
    ProgressBar pb;
    Button sum, min;
    FragmentTransaction ft;
    Frag frag1;
    Frag2 frag2;
    Values account;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.pB);
        min = findViewById(R.id.minus);
        sum = findViewById(R.id.plus);
        account = new Values();
        frag1 = new Frag();
        frag2 = new Frag2();




    }

    public void onClick(View v) {
        ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.minus:
                if (count == 0) {
                    count = 1;
                    ft.add(R.id.frag2, frag1);
                } else ft.replace(R.id.frag2, frag1);
                ft.commit();
                break;
            case R.id.plus:
                if (count == 0) {
                    count = 1;
                    ft.add(R.id.frag2, frag2);
                } else ft.replace(R.id.frag2, frag2);
                ft.commit();
                break;
            case R.id.ras:
                Intent i=new Intent(MainActivity.this, Second.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void fragmentvalue(int value) {
        account.waste =account.waste+value;
        pb.setProgress(account.waste);

    }


    @Override
    public void fragsum(int value) {
        account.summary += value;
        pb.setMax(account.summary);
    }
}