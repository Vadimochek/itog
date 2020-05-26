package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Information extends AppCompatActivity  {
    DBHelper dbHelper;
    int[] colors = new int[2];
    String VALUE;
    String DATE,DIRECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
       // text=findViewById(R.id.id);
        dbHelper = new DBHelper(this);
  //      colors[0] = Color.parseColor("#835E5C");
  //      colors[1] = Color.parseColor("#69775F");
    }

//Заполнение информации внесения/списания поля
    public void onClick(View v){
        LinearLayout linLayout = findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor c=db.query("datatable",null,null,null,null,null,null);
        switch (v.getId()) {
            case R.id.query:
                linLayout.removeAllViews();
                if (c.moveToFirst()) {
                int id = c.getColumnIndex("date");
                int val = c.getColumnIndex("value");
                int direct = c.getColumnIndex("direction");
                new unLoad().execute();
           /*      do {
                   View item = ltInflater.inflate(R.layout.list, linLayout, false);
                    TextView tvName = item.findViewById(R.id.center);
                    tvName.setText(c.getString(direct));
                    TextView tvPosition = (TextView) item.findViewById(R.id.date);
                    tvPosition.setText("Дата: " + c.getString(id));
                    TextView tvSalary = (TextView) item.findViewById(R.id.money);
                    tvSalary.setText("Сумма: " + c.getInt(val));
                    item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                  // if (c.getString(direct).equals("Внесение")) item.setBackgroundColor(colors[1]);
                  // else item.setBackgroundColor(colors[0]);
                    linLayout.addView(item);
                } while (c.moveToNext());*/
            } else Toast.makeText(getApplicationContext(), "Ничего", Toast.LENGTH_LONG).show();
            break;
            case R.id.delete:
                db.delete("datatable", null, null);
                linLayout.removeAllViews();
                colors[0]=1;
                //DELETE FROM <table_name>;
                break;
            case R.id.exit:
                if(colors[0]==1) {
                    Intent i = new Intent(Information.this, MainActivity.class);
                    startActivity(i);
                }
               else
                finish();
                break;
        }
        c.close();
        dbHelper.close();
    }
    class unLoad extends AsyncTask<Void, String, Void> {
    int x=0,k=0;
        LinearLayout linLayout = findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection con = connectionclass();
                String query1 = "SELECT date FROM [DB_A61C90_data].[dbo].[database]";
                String query2= "SELECT direction FROM [DB_A61C90_data].[dbo].[database]";
                String query3 = "SELECT value FROM [DB_A61C90_data].[dbo].[database]";
                Statement stmt = con.createStatement();
                ResultSet rs1 = stmt.executeQuery(query1);
                ResultSet rs2 = stmt.executeQuery(query2);
                ResultSet rs3 = stmt.executeQuery(query3);
                DATE=rs1.toString();
                DIRECTION=rs2.toString();
                VALUE=rs3.toString();

                do {
                    publishProgress(DATE,VALUE,DIRECTION);
                } while (rs1.next());
                con.close();
            } catch (SQLException se) {
                Log.e("error here 1 : ", se.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... Params){
            super.onProgressUpdate(Params);

            View item = ltInflater.inflate(R.layout.list, linLayout, false);
            TextView tvName = item.findViewById(R.id.center);
            tvName.setText(DIRECTION);
            TextView tvPosition = (TextView) item.findViewById(R.id.date);
            tvPosition.setText("Дата: " + DATE);
            TextView tvSalary = (TextView) item.findViewById(R.id.money);
            tvSalary.setText("Сумма: " + VALUE);
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            if (DIRECTION.equals("Receipt")) item.setBackgroundColor(colors[1]);
            else item.setBackgroundColor(colors[0]);
            linLayout.addView(item);

        }

    }

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://sql5047.site4now.net;database=DB_A61C90_data;user=DB_A61C90_data_admin;password=Ndbyrkcnelbj4;";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
