package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomView extends AppCompatActivity {
    String transactionlist,expenselist,date1,date2;
    ArrayList<Integer> imagelist,amountlist;
    ArrayList<String> datelist;
    FloatingActionButton fbtn;
    RecyclerView recyclerView;
    DrawerLayout dl;
    dbHelper helper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        dl = findViewById(R.id.drawer_layout);
        fbtn = findViewById(R.id.fltfilter);
        recyclerView = findViewById(R.id.show_custom_view);

        imagelist = new ArrayList<>();
        amountlist = new ArrayList<>();
        datelist = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);

        helper = new dbHelper(CustomView.this);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CustomView.this,custom_filter.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            cursor = helper.GetDateByTransaction(startDateStr,endDateStr);
            if(cursor.getCount() == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()){
                    datelist.add(cursor.getString(0));
                    amountlist.add(cursor.getInt(1));
                    imagelist.add(cursor.getInt(2));
                }
                CustomViewAdapter customViewAdapter = new CustomViewAdapter(CustomView.this,datelist,amountlist,imagelist);
                recyclerView.setAdapter(customViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(CustomView.this));
            }
        } else {
            expenselist = bundle.getString("data");
            transactionlist = bundle.getString("data1");
            date1 = bundle.getString("from");
            date2 = bundle.getString("to");
            if (expenselist.endsWith(",")) {
                expenselist = expenselist.substring(0,expenselist.length() - 1);
            }
            if(transactionlist.endsWith(",")){
                transactionlist = transactionlist.substring(0,transactionlist.length()-1);
            }

            if (expenselist.contentEquals("'All Categories'") && transactionlist.contentEquals("'All Payment Modes'")){
                cursor = helper.FetchAllTransaction(date1 ,date2);
                if(cursor.getCount() == 0) {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()){
                        datelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                        imagelist.add(cursor.getInt(2));
                    }
                    CustomViewAdapter customViewAdapter = new CustomViewAdapter(CustomView.this,datelist,amountlist,imagelist);
                    recyclerView.setAdapter(customViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CustomView.this));
                }
            }

            if (expenselist.contentEquals("'All Categories'")) {
                String query = "Select t.date,t.amount,c.image" +
                        " from addtransaction t,addcategory c where t.cid=c.categoryid AND " +
                        " t.paymentmode IN ("+transactionlist+") AND (t.date>='"+date1+"' AND t.date<='"+date2+"') ORDER BY t.transactionid DESC";
                cursor = helper.getWritableDatabase().rawQuery(query,null);
                if(cursor.getCount() == 0) {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()){
                        datelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                        imagelist.add(cursor.getInt(2));
                    }
                    CustomViewAdapter customViewAdapter = new CustomViewAdapter(CustomView.this,datelist,amountlist,imagelist);
                    recyclerView.setAdapter(customViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CustomView.this));
                }
            }

            if (transactionlist.contentEquals("'All Payment Modes'")) {
                String query = "Select t.date,t.amount,c.image" +
                        " from addtransaction t,addcategory c where t.cid=c.categoryid AND " +
                        " t.categroryoftrans IN("+expenselist+") AND (t.date>='"+date1+"' AND t.date<='"+date2+"') ORDER BY t.transactionid DESC";
                cursor = helper.getWritableDatabase().rawQuery(query,null);
                if(cursor.getCount() == 0) {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()){
                        datelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                        imagelist.add(cursor.getInt(2));
                    }
                    CustomViewAdapter customViewAdapter = new CustomViewAdapter(CustomView.this,datelist,amountlist,imagelist);
                    recyclerView.setAdapter(customViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CustomView.this));
                }
            }
            String query = "Select t.date,t.amount,c.image " +
                    "from addtransaction t,addcategory c where categroryoftrans IN("+expenselist+") AND t.paymentmode IN("+transactionlist+") AND " +
                    " (t.date>='"+date1+"' AND t.date<='"+date2+"') AND t.cid=c.categoryid ORDER BY transactionid DESC";

            cursor = helper.getWritableDatabase().rawQuery(query,null);
            if(cursor.getCount() == 0) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()){
                    datelist.add(cursor.getString(0));
                    amountlist.add(cursor.getInt(1));
                    imagelist.add(cursor.getInt(2));
                }
                CustomViewAdapter customViewAdapter = new CustomViewAdapter(CustomView.this,datelist,amountlist,imagelist);
                recyclerView.setAdapter(customViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(CustomView.this));
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomView.this,MainActivity.class));
        finishAffinity();
    }
    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }
    public void CustomView(View v) {
        recreate();
    }
    public void Home(View v) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }
    public void AllTransactions(View v) { MainActivity.redirectActivity(this, AllTransactions.class); }
    public void DayView(View v) {
        MainActivity.redirectActivity(this, DayView.class);
    }
    public void MonthView(View v) {
        MainActivity.redirectActivity(this, MonthView.class);
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