package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.jtds.jdbc.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity implements Mesage,Summary {
    Connection con;
    ProgressBar pb;
    Button sum, min;
    TextView summy, itogo, dif;
    FragmentTransaction ft;
    DialogFragment frag1;
    DialogFragment frag2;
    DialogFragment QUEST;
    Values account;
    static public int teleport;
    String DATE = "", DIRECTION = "";
    int VALUE;
    JSONArray resultSet1 = new JSONArray();


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
        QUEST = new Question();
        summy = findViewById(R.id.summa);
        itogo = findViewById(R.id.itogo);
        dif = findViewById(R.id.different);
        summy.setText("Всего: " + account.summary);
        dif.setText("Потрачено: " + account.waste);
        itogo.setText("Осталось: " + (account.summary - account.waste));
        if (account.summary - account.waste == 0) teleport = 0;
        new Start().execute();


    }

    public void onClick(View v) {
        //ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.minus:
                if (account.summary == 0)
                    Toast.makeText(getApplicationContext(), "Сначала внесите сумму", Toast.LENGTH_SHORT).show();
                else
                    frag1.show(getSupportFragmentManager(), "frag1");//Показ диалогового окна с вводом суммы
                break;
            case R.id.plus:
                frag2.show(getSupportFragmentManager(), "frag2");// Ещё один диалог
                break;
            case R.id.ras:
                Intent intent1 = new Intent(MainActivity.this, Second.class);
                startActivity(intent1);
                break;
            case R.id.info:
                Intent intent2 = new Intent(MainActivity.this, Information.class);
                startActivity(intent2);
                break;
            case R.id.day:
                Intent intent3 = new Intent(MainActivity.this, Day.class);
                startActivity(intent3);
                break;
            case R.id.quest:
                QUEST.show(getSupportFragmentManager(), "quest");//Диалог-памятка приложения
                break;
            default:
                break;
        }
    }

    //Методы интерфейсов для задания значений остатка, всей суммы, потраченной суммы
    @Override
    public void fragmentvalue(int value) {
        if (account.summary - account.waste - value < 0)
            Toast.makeText(getApplicationContext(), "Вы превысили свой лимит,введите меньшую сумму или внесите её", Toast.LENGTH_SHORT).show();
        else {
            account.waste = account.waste + value;
            pb.setProgress(account.waste);
            teleport = account.summary - account.waste;
            dif.setText("Потрачено: " + account.waste);
            itogo.setText("Осталось: " + teleport);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   HH:mm");
            Date date = new Date();
            String dateTime = dateFormat.format(date);
            DATE = dateTime;
            VALUE = value;
            DIRECTION = "Write-downs";
            new Load().execute();
            Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void fragsum(int value) {
        account.summary += value;
        pb.setMax(account.summary);
        summy.setText("Всего: " + account.summary);
        teleport = account.summary - account.waste;
        itogo.setText("Осталось: " + teleport);
        ContentValues cv = new ContentValues();
        cv.put("value", value);
        cv.put("direction", "Receipt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   HH:mm");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        DATE = dateTime;
        VALUE = value;
        DIRECTION = "Receipt";
        new Load().execute();
        Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
    }
    class Load extends AsyncTask<Void, Void, Void> {
        JSONArray resultSet = new JSONArray();
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection con = connectionclass();
                String query = "INSERT INTO [DB_A61C90_data].[dbo].[database] (date, value, direction) VALUES "+"('"+DATE+"', "+"'"+VALUE+"', '"+DIRECTION+"')";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
            } catch (SQLException se) {
                Log.e("error here 1 : ", se.getMessage());
            }

            return null;
        }
    }

    class Start extends AsyncTask<Void, String, JSONArray> {
        Connection con;
        Statement stmt,s2;
        ResultSet rs1;
        ResultSet rs2;

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray resultSet = new JSONArray();
            try {
                con = connectionclass();
                String  query = "SELECT direction FROM [DB_A61C90_data].[dbo].[database]";
                String query2= "SELECT value FROM [DB_A61C90_data].[dbo].[database]";
                stmt = con.createStatement();
                s2=con.createStatement();
                rs1 = stmt.executeQuery(query);
                rs2 = s2.executeQuery(query2);
                if(rs1!=null) {
                    int columnCount = rs1.getMetaData().getColumnCount();
                    while (rs1.next()) {
                        JSONObject values = new JSONObject();
                        JSONObject values2 = new JSONObject();
                        for (int i = 1; i <= columnCount; i++) {
                            values.put(rs1.getMetaData().getColumnName(i), (rs1.getString(i) != null) ? rs1.getString(i) : "");
                            if (rs2.next()) values2.put(rs2.getMetaData().getColumnName(i), (rs2.getString(i) != null) ? rs2.getString(i) : "");
                        }
                        resultSet1.put(values2);
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
                    if (stmt != null) stmt.close();
                    if (s2 != null) s2.close();
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
                int sum=0,difer=0;
                for (int i=0;i<result.length();i++) {
                    String call2;
                    call2=resultSet1.get(i).toString().substring(10,resultSet1.get(i).toString().length()-2);
                    if (result.get(i).toString().substring(14,21).equals("Receipt"))
                        sum+=Integer.parseInt(call2);
                    else
                    difer +=Integer.parseInt(call2);
                }
                summy.setText("Всего: "+ sum);
                itogo.setText("Осталось: " + (sum-difer));
                dif.setText("Потрачено: "+(difer));
                account.summary+=sum;
                account.waste+=difer;
                teleport+=sum-difer;
                pb.setMax(account.summary);
                pb.setProgress(account.waste);
            }catch(JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
        @SuppressLint("NewApi")
        public Connection connectionclass() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = null;
            String ConnectionURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                ConnectionURL = "jdbc:jtds:sqlserver://sql5047.site4now.net;database=DB_A61C90_data;user=DB_A61C90_data_admin;password=Ndbyrkcnelbj4;";
//            ConnectionURL = "jdbc:jtds:sqlserver://192.168.1.9;database=datatable;instance=SQLEXPRESS;Network Protocol=NamedPipes" ;


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


