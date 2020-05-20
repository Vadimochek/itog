package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Information extends AppCompatActivity  {
    DBHelper dbHelper;
    int[] colors = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
       // text=findViewById(R.id.id);
        dbHelper = new DBHelper(this);
        colors[0] = Color.parseColor("#FFF74D4D");
        colors[1] = Color.parseColor("#FF307703");
    }


    public void onClick(View v){
        LinearLayout linLayout = findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor c=db.query("datatable",null,null,null,null,null,null);
        switch (v.getId()) {
            case R.id.query:
            if (c.moveToFirst()) {
                int id = c.getColumnIndex("date");
                int val = c.getColumnIndex("value");
                int direct = c.getColumnIndex("direction");
                do {
                    View item = ltInflater.inflate(R.layout.list, linLayout, false);
                    TextView tvName = item.findViewById(R.id.center);
                    tvName.setText(c.getString(direct));
                    TextView tvPosition = (TextView) item.findViewById(R.id.date);
                    tvPosition.setText("Дата: " + c.getString(id));
                    TextView tvSalary = (TextView) item.findViewById(R.id.money);
                    tvSalary.setText("Сумма: " + c.getInt(val));
                    item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                   if (c.getString(direct).equals("Внесение")) item.setBackgroundColor(colors[1]);
                   else item.setBackgroundColor(colors[0]);
                    linLayout.addView(item);
                } while (c.moveToNext());
            } else Toast.makeText(getApplicationContext(), "Ничего", Toast.LENGTH_LONG).show();
            break;
            case R.id.delete:
                db.delete("datatable", null, null);
                linLayout.removeAllViews();
                break;
        }
        c.close();
        dbHelper.close();
    }
}
