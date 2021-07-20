package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthView extends AppCompatActivity {
    DrawerLayout dl;
    private dbHelper helper = new dbHelper(MonthView.this);
    String formattedDate,startdate,enddate;
    private ArrayList<Integer> amountlist,imagelist,amountlist1,imagelist1;
    private ArrayList<String> typelist,typelist1;
    ImageView prev,next;
    TextView txt;
    private Cursor cursor,cursor1;
    dbHelper myDB;
    MonthIncomeViewAdapter adapter1;
    MonthViewAdapter adapter;
    private RecyclerView rcv,rcv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
        dl = findViewById(R.id.drawer_layout);
        prev = findViewById(R.id.imgprev);
        next = findViewById(R.id.imgnext);
        txt = findViewById(R.id.txtdate);

        rcv = findViewById(R.id.rcvieexpenseList);
        rcv1 = findViewById(R.id.rcvieincomeList);

        myDB = new dbHelper(MonthView.this);

        amountlist = new ArrayList<>();
        typelist = new ArrayList<>();
        imagelist = new ArrayList<>();
        amountlist1 = new ArrayList<>();
        typelist1 = new ArrayList<>();
        imagelist1 = new ArrayList<>();

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int y = Calendar.getInstance().get(Calendar.YEAR);
        SimpleDateFormat df = new SimpleDateFormat("MMM - yyyy");
        formattedDate = df.format(c.getTime());
        txt.setText(formattedDate);

////////////////////////////////////////////////////////////////////////////////////
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR,y);
        calendar1.set(Calendar.MONTH,m);
        calendar1.set(Calendar.DAY_OF_MONTH,calendar1.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat df1 =new SimpleDateFormat("yyyy/MM/dd");
        startdate = df1.format(calendar1.getTime());
//////////////////////////////////////////////////////////////////////////////////
        Calendar cend = Calendar.getInstance();
        cend.set(Calendar.YEAR,y);
        cend.set(Calendar.MONTH,m);
        cend.set(Calendar.DAY_OF_MONTH,cend.getActualMaximum(Calendar.DAY_OF_MONTH));
        enddate = df1.format(cend.getTime());
///////////////////////////////////////////////////////////////////////

        //for current month expense
        cursor = helper.GetExpenseMonthView(startdate, enddate);
        if(cursor.getCount() == 0)
        {
//            amountlist.clear();
//            typelist.clear();
//            imagelist.clear();
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else
        {
//            amountlist.clear();
//            typelist.clear();
//            imagelist.clear();
            while (cursor.moveToNext())
            {
                typelist.add(cursor.getString(0));
                amountlist.add(cursor.getInt(1));
                imagelist.add(cursor.getInt(2));
            }
            adapter = new MonthViewAdapter(MonthView.this,typelist,amountlist,imagelist);
            rcv.setAdapter(adapter);
            rcv.setLayoutManager(new LinearLayoutManager(MonthView.this));
            Toast.makeText(this, "Current Month Record:"+typelist+amountlist+imagelist , Toast.LENGTH_SHORT).show();
        }
        cursor1 = helper.GetIncomeMonthView(startdate,enddate);
        //for current month income
        if(cursor1.getCount() == 0)
        {
//            amountlist1.clear();
//            typelist1.clear();
//            imagelist1.clear();
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else
        {
//            amountlist1.clear();
//            typelist1.clear();
//            imagelist1.clear();
            while (cursor1.moveToNext())
            {
                typelist1.add(cursor1.getString(0));
                amountlist1.add(cursor1.getInt(1));
                imagelist1.add(cursor1.getInt(2));
            }
            adapter1 = new MonthIncomeViewAdapter(MonthView.this,typelist1,amountlist1,imagelist1);
            rcv1.setAdapter(adapter1);
            rcv1.setLayoutManager(new LinearLayoutManager(MonthView.this));
            Toast.makeText(this, "Current Month Record:"+typelist1+amountlist1+imagelist1 , Toast.LENGTH_SHORT).show();
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountlist.clear();
                typelist.clear();
                imagelist.clear();
                amountlist1.clear();
                typelist1.clear();
                imagelist1.clear();
//                adapter.ClearData();
//                adapter1.ClearData();
                c.add(Calendar.MONTH, -1);
                formattedDate = df.format(c.getTime());
                txt.setText(formattedDate);
                calendar1.add(Calendar.MONTH,-1);
                startdate = df1.format(calendar1.getTime());
                cend.add(Calendar.MONTH,-1);
                enddate = df1.format(cend.getTime());
                //for previous month expense
                cursor = helper.GetExpenseMonthView(startdate, enddate);
                if(cursor.getCount() == 0)
                {
                    rcv.setVisibility(View.GONE);
                    Toast.makeText(MonthView.this, "No data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    rcv.setVisibility(View.VISIBLE);
                    typelist.clear();
                    amountlist.clear();
                    imagelist.clear();
                    while (cursor.moveToNext())
                    {
                        typelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                        imagelist.add(cursor.getInt(2));
                    }
                    adapter = new MonthViewAdapter(MonthView.this,typelist,amountlist,imagelist);
                    rcv.setAdapter(adapter);
                    rcv.setLayoutManager(new LinearLayoutManager(MonthView.this));
                    Toast.makeText(MonthView.this, "Previous Month Record:"+typelist+amountlist+imagelist , Toast.LENGTH_SHORT).show();
                }
                //for previous month income
                cursor1 = helper.GetIncomeMonthView(startdate,enddate);
                if(cursor1.getCount() == 0)
                {
                    rcv1.setVisibility(View.GONE);
                    Toast.makeText(MonthView.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    rcv1.setVisibility(View.VISIBLE);
                    typelist1.clear();
                    amountlist1.clear();
                    imagelist1.clear();
                    while (cursor1.moveToNext())
                    {
                        typelist1.add(cursor1.getString(0));
                        amountlist1.add(cursor1.getInt(1));
                        imagelist1.add(cursor1.getInt(2));
                    }
                    adapter1 = new MonthIncomeViewAdapter(MonthView.this,typelist1,amountlist1,imagelist1);
                    rcv1.setAdapter(adapter1);
                    rcv1.setLayoutManager(new LinearLayoutManager(MonthView.this));
                    Toast.makeText(MonthView.this, "Previous Month Record:"+typelist1+amountlist1+imagelist1 , Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                amountlist.clear();
                typelist.clear();
                imagelist.clear();
                amountlist1.clear();
                typelist1.clear();
                imagelist1.clear();
//                adapter.ClearData();
//                adapter1.ClearData();
                c.add(Calendar.MONTH, 1);
                formattedDate = df.format(c.getTime());
                txt.setText(formattedDate);
                calendar1.add(Calendar.MONTH,1);
                startdate = df1.format(calendar1.getTime());
                cend.add(Calendar.MONTH,1);
                enddate = df1.format(cend.getTime());
                //for next month expense
                cursor = helper.GetExpenseMonthView(startdate, enddate);
                if(cursor.getCount() == 0)
                {
                    rcv.setVisibility(View.GONE);
                    Toast.makeText(MonthView.this, "No data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    rcv.setVisibility(View.VISIBLE);
                    while (cursor.moveToNext())
                    {
                        typelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                        imagelist.add(cursor.getInt(2));
                    }
                    adapter = new MonthViewAdapter(MonthView.this,typelist,amountlist,imagelist);
                    rcv.setAdapter(adapter);
                    rcv.setLayoutManager(new LinearLayoutManager(MonthView.this));
                    Toast.makeText(MonthView.this, "Next Month Record:"+typelist+amountlist+imagelist , Toast.LENGTH_SHORT).show();
                }
                cursor1 = helper.GetIncomeMonthView(startdate,enddate);
                //for current month income
                if(cursor1.getCount() == 0)
                {
                    rcv1.setVisibility(View.GONE);
                    Toast.makeText(MonthView.this, "No Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    rcv1.setVisibility(View.VISIBLE);
                    while (cursor1.moveToNext())
                    {
                        typelist1.add(cursor1.getString(0));
                        amountlist1.add(cursor1.getInt(1));
                        imagelist1.add(cursor1.getInt(2));
                    }
                    adapter1 = new MonthIncomeViewAdapter(MonthView.this,typelist1,amountlist1,imagelist1);
                    rcv1.setAdapter(adapter1);
                    rcv1.setLayoutManager(new LinearLayoutManager(MonthView.this));
                    Toast.makeText(MonthView.this, "Next Month Record:"+typelist1+amountlist1+imagelist1 , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MonthView.this,MainActivity.class));
        finishAffinity();
    }
    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }
    public void MonthView(View v) {
        recreate();
    }
    public void Home(View v) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }
    public void AllTransactions(View v) { MainActivity.redirectActivity(this, AllTransactions.class); }
    public void DayView(View v) {
        MainActivity.redirectActivity(this, DayView.class);
    }
    public void CustomView(View v) {
        MainActivity.redirectActivity(this, CustomView.class);
    }
    public void Budget(View v) {
        MainActivity.redirectActivity(this, Budget.class);
    }
    public void Category(View v) {
        MainActivity.redirectActivity(this, Category.class);
    }
    public void Settings(View v) {
        MainActivity.redirectActivity(this, Settings.class);
    }
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(dl);
    }
}