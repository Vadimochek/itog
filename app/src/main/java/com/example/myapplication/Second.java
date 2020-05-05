package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Second extends Activity {
    EditText text;
    TextView itog;
    Button ras;
CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        itog = findViewById(R.id.itog);
        text=findViewById(R.id.editText);
        calendarView=findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,int day){
                int ye=year;
                int mo=month;
                int da=day;
                Date currentdate=new Date();
                DateFormat dateformat=new SimpleDateFormat("dd/MM/yyyy");
                String datestring=dateformat.format(currentdate);
                String finaldate=Integer.toString(da)+"/"+Integer.toString(mo)+"/"+Integer.toString(ye);
                Date date2,date1;
                try {
                    date2 = dateformat.parse(finaldate);
                    date1 = dateformat.parse(datestring);
                    Toast.makeText(getApplicationContext(),"Hier "+date1,Toast.LENGTH_SHORT).show();
                    long difference=Math.abs(date2.getTime()-date1.getTime());
                    long difDate=difference/(24*60*60*1000);
                    itog.setText("Вы должны тратить не больше " + MainActivity.teleport / difDate + " рублей в день");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        ras=findViewById(R.id.but);
        ras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (text.getText().toString().equals("")) Toast.makeText(getApplicationContext(),"Поле не должно быть пустым",Toast.LENGTH_SHORT).show();
                    else{
                    int day = Integer.parseInt(text.getText().toString());
                    text.setText("");
                    itog.setText("Вы должны тратить не больше " + MainActivity.teleport / day + " рублей в день");
                }
            }
    });
    }
}

