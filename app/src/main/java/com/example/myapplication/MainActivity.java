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
import android.view.View;
import android.widget.Button;
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
    DBHelper dbHelper;
    String DATE = "", DIRECTION = "";
    int DAILY, VALUE;
    //  private JSONParser JSONP;
    // private final String baseUrl = "http://192.168.43.215:8080"; // сюда нужно будет вписать ваш IP из ipconfig в CMD


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
        dbHelper = new DBHelper(this);
        summy.setText("Всего: " + account.summary);
        dif.setText("Потрачено: " + account.waste);
        itogo.setText("Осталось: " + (account.summary - account.waste));
        if (account.summary - account.waste == 0) teleport = 0;


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
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            pb.setProgress(account.waste);
            teleport = account.summary - account.waste;
            dif.setText("Потрачено: " + account.waste);
            itogo.setText("Осталось: " + teleport);
            ContentValues cv = new ContentValues();
            cv.put("value", value);
            cv.put("direction", "Write-downs");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy   HH:mm");
            Date date = new Date();
            String dateTime = dateFormat.format(date);
            cv.put("date", dateTime);
            db.insert("datatable", null, cv);
            dbHelper.close();
            DATE = dateTime;
            VALUE = value;
            DIRECTION = "Write-downs";
            new Load().execute();
            Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void fragsum(int value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
        cv.put("date", dateTime);
        db.insert("datatable", null, cv);
        dbHelper.close();
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


