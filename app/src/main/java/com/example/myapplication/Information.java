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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Information extends AppCompatActivity {
    DBHelper dbHelper;
    int[] colors = new int[2];
    String VALUE="";
    String DATE="", DIRECTION="";

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
    public void onClick(View v) {
        LinearLayout linLayout = findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("datatable", null, null, null, null, null, null);
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
                new Delete().execute();
                db.delete("datatable", null, null);
                linLayout.removeAllViews();
                colors[0] = 1;
                break;
            case R.id.exit:
                if (colors[0] == 1) {
                    Intent i = new Intent(Information.this, MainActivity.class);
                    startActivity(i);
                } else
                    finish();
                break;
        }
        c.close();
        dbHelper.close();
    }
    JSONArray resultSet1 = new JSONArray();
    JSONArray resultSet2 = new JSONArray();
//Класс для выполнения запроса не в главном потоке
    class unLoad extends AsyncTask<Void, String, JSONArray> {
        LinearLayout linLayout = findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();
        Connection con;
        Statement stmt,s2,s3;
        ResultSet rs1;
        ResultSet rs2;
        ResultSet rs3;

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray resultSet = new JSONArray();
            try {
                con = connectionclass();
               String  query = "SELECT direction FROM [DB_A61C90_data].[dbo].[database]";
              String query2= "SELECT date FROM [DB_A61C90_data].[dbo].[database]";
               String query3 = "SELECT value FROM [DB_A61C90_data].[dbo].[database]";
                stmt = con.createStatement();
                s2=con.createStatement();
                   s3=con.createStatement();
                rs1 = stmt.executeQuery(query);
                rs2 = s2.executeQuery(query2);
                 rs3 = s3.executeQuery(query3);
                if(rs1!=null) {
                    int columnCount = rs1.getMetaData().getColumnCount();
                    while (rs1.next()) {
                        JSONObject values = new JSONObject();
                        JSONObject values2 = new JSONObject();
                        JSONObject values3 = new JSONObject();
                        for (int i = 1; i <= columnCount; i++) {
                            values.put(rs1.getMetaData().getColumnName(i), (rs1.getString(i) != null) ? rs1.getString(i) : "");
                           if(rs2.next()) values2.put(rs2.getMetaData().getColumnName(i), (rs2.getString(i) != null) ? rs2.getString(i) : "");
                            if(rs3.next()) values3.put(rs3.getMetaData().getColumnName(i), (rs3.getString(i) != null) ? rs3.getString(i) : "");
                        }
                        resultSet1.put(values2);
                        resultSet2.put(values3);
                        resultSet.put(values);
                    }
                }
            } catch (SQLException se) {
                Log.e("error here 1 : ", se.getMessage());
            } catch (JSONException js) {
                js.printStackTrace();
            } finally {
                try {
                    if (rs1 != null) rs1.close();
                    if (rs2 != null) rs2.close();
                    if (rs3 != null) rs3.close();
                    if (stmt != null) stmt.close();
                    if (s2 != null) s2.close();
                    if (s3 != null) s3.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return resultSet;
        }
        @Override
        protected void onPostExecute(JSONArray result) {
            try{
             for (int i=0;i<result.length();i++) {
                 View item = ltInflater.inflate(R.layout.list, linLayout, false);
                 TextView tvName = item.findViewById(R.id.center);
                 String call,call2,call3;
                 if (result.get(i).toString().substring(14,21).equals("Receipt"))
                     call=result.get(i).toString().substring(14,21);
                 else call=result.get(i).toString().substring(14,24);
                 call2=resultSet1.get(i).toString().substring(9,27);
                 call3=resultSet2.get(i).toString().substring(10,14);
                 call3=call3.replace("\"","");
                 tvName.setText(call);
                 TextView tvPosition = (TextView) item.findViewById(R.id.date);
                 tvPosition.setText("Дата: " + call2);
                 TextView tvSalary = (TextView) item.findViewById(R.id.money);
                 tvSalary.setText("Сумма: " + call3);
                 item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                 linLayout.addView(item);
                 }
             }catch(JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


        /*protected void onProgressUpdate(String... Params){
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
*/
        class Delete extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Connection con = connectionclass();
                    String query = "DELETE [DB_A61C90_data].[dbo].[database]";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException se) {
                    Log.e("error here 1 : ", se.getMessage());
                }

                return null;
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
