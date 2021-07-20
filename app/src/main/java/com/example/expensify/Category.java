package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Category extends AppCompatActivity {
    DrawerLayout dl;
    FloatingActionButton fab1;
    RecyclerView recyclerView;
    ArrayList<String> categoryid,categorynm,categorytype,categorydesc;
    ArrayList<Integer> categoryimage;
    dbHelper myDB;
    CategoryItemAdapter categoryItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dl = findViewById(R.id.drawer_layout);
        fab1 = findViewById(R.id.addcategory);
        recyclerView = findViewById(R.id.showcategory);
        myDB = new dbHelper(Category.this);
        categoryid = new ArrayList<>();
        categorynm = new ArrayList<>();
        categorytype = new ArrayList<>();
        categorydesc = new ArrayList<>();
        categoryimage = new ArrayList<>();

        displayData();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("key", true);
                Intent i = new Intent();
                i.putExtras(bundle);
                i.setClass(Category.this,AddCategory.class);
                startActivity(i);
            }
        });
        categoryItemAdapter = new CategoryItemAdapter(Category.this,categoryid,categorynm,categorytype,categorydesc,categoryimage);
        recyclerView.setAdapter(categoryItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Category.this));

    }
    void displayData()
    {
        Cursor cursor = myDB.GetCategory();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext())
            {
                categoryid.add(cursor.getString(0));
                categorynm.add(cursor.getString(1));
                categorytype.add(cursor.getString(2));
                categorydesc.add(cursor.getString(3));
                categoryimage.add(cursor.getInt(4));
            }
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Category.this,MainActivity.class));
        finishAffinity();
    }
    public void ClickMenu(View v) {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v) {
        MainActivity.closeDrawer(dl);
    }
    public void Category(View v) { recreate(); }
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
    public void Budget(View v) {
        MainActivity.redirectActivity(this, Budget.class);
    }
    public void Settings(View v) {
        MainActivity.redirectActivity(this, Settings.class);
    }
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(dl);
    }
}