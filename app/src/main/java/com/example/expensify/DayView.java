package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DayView extends AppCompatActivity {
    DrawerLayout dl;
    String date1,date2,storedate;
    private EditText date_day;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener setListener;
    private RecyclerView rcv;
    ArrayList<String> transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode;
    ArrayList<Integer> image;
    dbHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        dl = findViewById(R.id.drawer_layout);
        date_day = findViewById(R.id.et_date_day);
        rcv = findViewById(R.id.rcvieList);
        myDB = new dbHelper(DayView.this);

        transactionid = new ArrayList<>();
        dates = new ArrayList<>();
        amount = new ArrayList<>();
        typeoftransaction = new ArrayList<>();
        spinner = new ArrayList<>();
        note = new ArrayList<>();
        paymentmode  = new ArrayList<>();
        image = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        date1 = formatter.format(date);
        DisplayDayTransaction(date1);
        date_day.setText(date1);
        ViewAdapter adapter = new ViewAdapter(DayView.this,transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(DayView.this));

        date_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(DayView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = year + "/" + month + "/" + day;
                        try {
                            Date formateddate = formatter.parse(date);
                            date1 = formatter.format(formateddate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date_day.setText(date1);
                        storedate = date_day.getText().toString();
                        DisplayDayTransaction(storedate);
                        ViewAdapter adapter = new ViewAdapter(DayView.this,transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
                        rcv.setAdapter(adapter);
                        rcv.setLayoutManager(new LinearLayoutManager(DayView.this));
                    }
                }, year, month, day);
                dpd1.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                month += 1;
                String date = year + "/" + month + "/" + day;
                try {
                    Date formateddate = formatter.parse(date);
                    date2 = formatter.format(formateddate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date_day.setText(date2);
                storedate = date_day.getText().toString();
                DisplayDayTransaction(storedate);
                ViewAdapter adapter = new ViewAdapter(DayView.this,transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
                rcv.setAdapter(adapter);
                rcv.setLayoutManager(new LinearLayoutManager(DayView.this));
            }
        };

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DayView.this,MainActivity.class));
        finishAffinity();
    }

    private void DisplayDayTransaction(String storedate) {
        transactionid.clear();
        dates.clear();
        amount.clear();
        typeoftransaction.clear();
        spinner.clear();
        note.clear();
        paymentmode.clear();
        image.clear();
        Cursor cursor = myDB.GetDayView(storedate);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                transactionid.add(cursor.getString(0));
                dates.add(cursor.getString(1));
                amount.add(cursor.getString(2));
                typeoftransaction.add(cursor.getString(3));
                spinner.add(cursor.getString(4));
                note.add(cursor.getString(5));
                paymentmode.add(cursor.getString(6));
                image.add(cursor.getInt(7));
            }
        }
    }


    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }
    public void DayView(View v) {
        recreate();
    }
    public void Home(View v) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }
    public void AllTransactions(View v) {
        MainActivity.redirectActivity(this, AllTransactions.class); }
    public void MonthView(View v) {
        MainActivity.redirectActivity(this, MonthView.class);
    }
    public void CustomView(View v) {
        MainActivity.redirectActivity(this, CustomView.class);
    }
    public void Budget(View v) {
        MainActivity.redirectActivity(this, Budget.class);
    }
    public void Category(View v) { MainActivity.redirectActivity(this, Category.class); }
    public void Settings(View v) { MainActivity.redirectActivity(this, Settings.class); }
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(dl);
    }
}