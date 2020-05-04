package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Second extends Activity {
    EditText text;
    TextView itog;
    Button ras;
  //  Values account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        itog = findViewById(R.id.itog);
        text=findViewById(R.id.editText);
        //account=new Values();
        // ras=findViewById(R.id.but);
        //  ras.setOnClickListener(new View.OnClickListener() {
    }
            public void onClick(View v) {
                int day = Integer.parseInt(text.getText().toString());
                text.setText("");
            //    Toast.makeText(getApplicationContext(),"(account.summary-account.waste)/day"+/day,Toast.LENGTH_LONG).show();
            //    Toast.makeText(getApplicationContext(),"Расчёт...",Toast.LENGTH_LONG).show();
                itog.setText("Вы должны тратить не больше");

            }
    }

