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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class Budget extends AppCompatActivity {
    DrawerLayout dl;
    FloatingActionButton fbbgt1;
    RecyclerView rv;
    //BudgetAdapter ba;
    dbHelper myDB;
    LinearLayout hidel_budget;
    TextView no_budget,tap_budget;
    ImageView budget_empty_icon;
    ArrayList<String> date,amount,budgetid;
    ArrayList<String> date_bd;
    ArrayList<Integer> total_amount,amount_bd,bid_bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        dl = findViewById(R.id.drawer_layout);
        fbbgt1 = findViewById(R.id.addbugetclick);
        rv=findViewById(R.id.rv);
        hidel_budget=findViewById(R.id.hidel_budget);
        no_budget=findViewById(R.id.no_budget);
        tap_budget=findViewById(R.id.tap_budget);
        budget_empty_icon=findViewById(R.id.budget_empty_icon);

        myDB = new dbHelper(Budget.this);
        budgetid = new ArrayList<>();
        date = new ArrayList<>();

        total_amount = new ArrayList<>();
        amount = new ArrayList<>();
        date_bd = new ArrayList<>();
        amount_bd = new ArrayList<>();
        bid_bd = new ArrayList<>();

        displayData();
        fbbgt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("key", true);
                Intent i = new Intent();
                i.putExtras(bundle);
                i.setClass(Budget.this,AddBudget.class);
                startActivity(i);
            }
        });
    }
    void displayData()
    {
        Cursor cursor = myDB.GetBudget();
        while (cursor.moveToNext())
        {
            budgetid.add(cursor.getString(0));
            date.add(cursor.getString(1));
            amount.add(cursor.getString(2));
            total_amount.add(Integer.valueOf(cursor.getString(3)));
        }
        BudgetAdapter ba =new BudgetAdapter(Budget.this,budgetid,date,amount,total_amount);
        if(ba.getItemCount() == 0){
            hidel_budget.setPadding(50,600,50,50);
            budget_empty_icon.setVisibility(View.VISIBLE);
            budget_empty_icon.setImageResource(R.drawable.budget);
            budget_empty_icon.setAdjustViewBounds(true);
            no_budget.setVisibility(View.VISIBLE);
            tap_budget.setVisibility(View.VISIBLE);
            hidel_budget.setPadding(50,600,50,50);
        }
        rv.setAdapter(ba);
        rv.setLayoutManager(new LinearLayoutManager(Budget.this));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Budget.this,MainActivity.class));
        finishAffinity();
    }
    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }
    public void Budget(View v) {
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
    public void CustomView(View v) {
        MainActivity.redirectActivity(this, CustomView.class);
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