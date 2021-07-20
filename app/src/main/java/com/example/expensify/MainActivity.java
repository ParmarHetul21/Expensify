package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout dl;
    ImageView iv,empty_content,empty_budget;
    FloatingActionButton fap1;
    Animation fapopen,fapclose;
    TextView textView;
    Boolean isOpen=false;
    RecyclerView recyclerView,show_data_budget;
    ArrayList<Integer> amount;
    ArrayList<String> date;
    ArrayList<String> type;
    Cursor cursor,cursor1,cursor2,budgetcursor;
    int i=0;
    Button expense, income;
    int expens_amt = 0;
    int income_amt = 0;
    LinearLayout linearLayout,HideLayout_budget;
    ArrayList<Integer> image;
    ArrayList<String> paymentmode,transaction_id,spinner,note;
    TextView getTextView,view,addhomebudget,no_budget,tap_budget;
    CardView month_card,week_Card,year_card;
    ArrayList<String> budgetdate,budgetamount,budgetid;
    ArrayList<Integer> totalamount;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    int status = 0;
    dbHelper helper_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = findViewById(R.id.drawer_layout);
        iv = findViewById(R.id.menu1);
        fap1=findViewById(R.id.fltincome);
        helper_category = new dbHelper(this);
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        int id = sharedPreferences.getInt("Id",0);
        if(id == 0){
            helper_category.AddCategoryAll();
            myEdit.putInt("Id", 1);
            myEdit.commit();
        } else {
            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        }
        expense = findViewById(R.id.Expense_Amount);
        income = findViewById(R.id.Income_amount);
        empty_content = findViewById(R.id.empty_content);
        linearLayout = findViewById(R.id.HideLayout);
        view = findViewById(R.id.tap_trans);
        getTextView = findViewById(R.id.no_trans);
        recyclerView = findViewById(R.id.show_data_home);
        show_data_budget = findViewById(R.id.show_data_budget);
        textView = findViewById(R.id.SeeAllData);

        HideLayout_budget = findViewById(R.id.HideLayout_budget);
        empty_budget = findViewById(R.id.empty_budget);
        no_budget = findViewById(R.id.no_budget);
        tap_budget = findViewById(R.id.tap_budget);

        addhomebudget = findViewById(R.id.addhomebudget);
        addhomebudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Budget.class);
                startActivity(intent);
            }
        });

        month_card = findViewById(R.id.month_card);
        week_Card = findViewById(R.id.week_card);
        year_card = findViewById(R.id.year_card);

        budgetid = new ArrayList<>();
        budgetamount = new ArrayList<>();
        totalamount = new ArrayList<>();
        budgetdate = new ArrayList<>();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AllTransactions.class);
                startActivity(intent);
            }
        });

        amount = new ArrayList<>();
        date   = new ArrayList<>();
        type   = new ArrayList<>();
        image  = new ArrayList<>();
        transaction_id = new ArrayList<>();
        paymentmode = new ArrayList<>();
        spinner = new ArrayList<>();
        note = new ArrayList<>();

        dbHelper dbHelper = new dbHelper(MainActivity.this);
        cursor = dbHelper.GetTransaction();
        cursor1 = dbHelper.ExpenseSumAmount();
        cursor2 = dbHelper.IncomeSumAmount();
        budgetcursor = dbHelper.GetBudget();

        while (cursor1.moveToNext()) {
            expens_amt = cursor1.getInt(0);
        }
        expense.setText(String.valueOf(expens_amt));

        while (cursor2.moveToNext()){
            income_amt = cursor2.getInt(0);
        }
        income.setText(String.valueOf(income_amt));

        while (cursor.moveToNext()) {
            if(i < 4) {
                transaction_id.add(String.valueOf(cursor.getInt(0)));
                paymentmode.add(cursor.getString(4));
                amount.add(cursor.getInt(2));
                date.add(cursor.getString(1));
                type.add(cursor.getString(3));
                image.add(cursor.getInt(7));
                spinner.add(cursor.getString(4));
                note.add(cursor.getString(5));
                i = i + 1;
            }
        }

//        All transactionTextView
        MyTransactionHomeAdapter myTransactionHomeAdapter = new MyTransactionHomeAdapter(spinner, note, transaction_id,paymentmode,amount,date,type,image, MainActivity.this);
        if(myTransactionHomeAdapter.getItemCount() == 0) {
            linearLayout.setPadding(0,0,0,150);
            empty_content.setVisibility(View.VISIBLE);
            empty_content.setImageResource(R.drawable.calculate);
            empty_content.setAdjustViewBounds(true);
            getTextView.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            linearLayout.setPadding(0,0,0,150);
        }
        recyclerView.setAdapter(myTransactionHomeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

//      Monthly Budget

        while (budgetcursor.moveToNext()){
            budgetid.add(budgetcursor.getString(0));
            budgetdate.add(budgetcursor.getString(1));
            budgetamount.add(budgetcursor.getString(2));
            totalamount.add(budgetcursor.getInt(3));
        }

        BudgetAdapter budgetAdapter =new BudgetAdapter(MainActivity.this,budgetid,budgetdate,budgetamount,totalamount);
        if(budgetAdapter.getItemCount() == 0){
            HideLayout_budget.setPadding(0,0,0,150);
            empty_budget.setVisibility(View.VISIBLE);
            empty_content.setImageResource(R.drawable.budget);
            empty_budget.setAdjustViewBounds(true);
            no_budget.setVisibility(View.VISIBLE);
            tap_budget.setVisibility(View.VISIBLE);
            HideLayout_budget.setPadding(0,0,0,150);
        }
        show_data_budget.setAdapter(budgetAdapter);
        show_data_budget.setLayoutManager(new LinearLayoutManager(MainActivity.this));

//      month Analysis
        month_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MonthAnalysis.class);
                startActivity(intent);
            }
        });

//      week Analysis
        week_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,WeekAnalysis.class);
                startActivity(intent);
            }
        });

//      year Analysis
        year_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,YearAnalysis.class);
                startActivity(intent);
            }
        });

        fap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("key", true);
                Intent i = new Intent();
                i.putExtras(bundle);
                i.setClass(MainActivity.this,addincome.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }

    public void ClickMenu(View v) {
        openDrawer(dl);
    }
    public static void openDrawer(DrawerLayout dl) { dl.openDrawer(GravityCompat.START); }
    public void ClickLogo(View v) {
        closeDrawer(dl);
    }
    public static void closeDrawer(DrawerLayout dl) {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        }
    }
    public void Home(View v) {
        recreate();
    }
    public static void redirectActivity(Activity activity, Class aclass) {
        Intent i = new Intent(activity, aclass);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
    }
    public void AllTransactions(View v) { redirectActivity(this, AllTransactions.class); }
    public void DayView(View v) {
        redirectActivity(this, DayView.class);
    }
    public void MonthView(View v) {
        redirectActivity(this, MonthView.class);
    }
    public void CustomView(View v) {
        redirectActivity(this, CustomView.class);
    }
    public void Budget(View v) {
        redirectActivity(this, Budget.class);
    }
    public void Category(View v) {
        redirectActivity(this, Category.class);
    }
    public void Settings(View v) {
        redirectActivity(this, Settings.class);
    }
    protected void onPause() {
        super.onPause();
        dl.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        if(isOpen) {
            fap1.startAnimation(fapclose);
            fap1.setClickable(false);
            isOpen=false;
        }
        else{
            fap1.startAnimation(fapopen);
            fap1.setClickable(true);
            isOpen=true;
        }
    }
}