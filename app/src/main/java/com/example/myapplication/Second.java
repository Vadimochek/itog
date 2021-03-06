package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    TextView itog, ost;
    Button ras, exit;
    CalendarView calendarView;
   static public int DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        itog = findViewById(R.id.itog);
        text = findViewById(R.id.editText);
        calendarView = findViewById(R.id.calendarView);
        ost = findViewById(R.id.ostatok);
        ost.setText("Остаток денег: " + MainActivity.teleport);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                //Отработка нажатия по календарю - получаем количество миллисекунд по нажатому дню и вычисляем разницу между днями
                int ye = year;
                int mo = month + 1;
                int da = day + 1;
                Date currentdate = new Date();
                DateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy");
                String finaldate = Integer.toString(mo) + "." + Integer.toString(da) + "." + Integer.toString(ye);
                Date date2;
                try {
                    date2 = dateformat.parse(finaldate);
                    if(date2.getTime() - currentdate.getTime()<0) Toast.makeText(getApplicationContext(),"Неправильная дата",Toast.LENGTH_SHORT).show();
                    else {
                        long difference = date2.getTime() - currentdate.getTime();
                        long difDate = difference / (24 * 60 * 60 * 1000);
                        itog.setText("Вы должны тратить не больше " + MainActivity.teleport / difDate + " рублей в день");
                        DAY = (int) (MainActivity.teleport / difDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        ras = findViewById(R.id.but);
        ras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (text.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Поле не должно быть пустым", Toast.LENGTH_SHORT).show();
                else {
                    int day = Integer.parseInt(text.getText().toString());
                    text.setText("");
                    itog.setText("Вы должны тратить не больше " + MainActivity.teleport / day + " рублей в день");
                    DAY = MainActivity.teleport / day;
                }
            }

        });


        exit = findViewById(R.id.ex);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
    }
}

