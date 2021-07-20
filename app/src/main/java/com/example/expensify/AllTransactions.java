package com.example.expensify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class AllTransactions extends AppCompatActivity {
    DrawerLayout dl;
    RecyclerView recyclerView;
    ArrayList<String> dates,amount,typeoftransaction,spinner,note,paymentmode;
    String dates1,amount1,typeoftransaction1,spinner1,note1,paymentmode1,bd_date;
    int transactionid1,image1,remain_bef_delete,remain_aft_delete,total_budget,budgetid;
    ArrayList<Integer> transactionid,image,bid,remain_amt,total_amt;
    ArrayList<String> bt_date;
    dbHelper myDB;
    FloatingActionButton fab1;
    ImageView imageView;
    TextView textView;
    boolean flag=true;
    MyTransactionItemAdapter myTransactionItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        dl = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.transaction);
        fab1 = findViewById(R.id.addtrans);
        imageView = findViewById(R.id.imgtrans);
        textView = findViewById(R.id.txttrans);
        myDB = new dbHelper(AllTransactions.this);
        transactionid = new ArrayList<>();
        dates = new ArrayList<>();
        amount = new ArrayList<>();
        typeoftransaction = new ArrayList<>();
        spinner = new ArrayList<>();
        note = new ArrayList<>();
        paymentmode  = new ArrayList<>();
        image = new ArrayList<>();
        bid = new ArrayList<>();
        bt_date = new ArrayList<>();
        remain_amt = new ArrayList<>();
        total_amt = new ArrayList<>();
        displayData();
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("key", true);
                Intent i = new Intent();
                i.putExtras(bundle);
                i.setClass(AllTransactions.this,addincome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        myTransactionItemAdapter = new MyTransactionItemAdapter(this,transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
        recyclerView.setAdapter(myTransactionItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllTransactions.this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int data = (Integer) viewHolder.itemView.getTag();
                final int position = viewHolder.getLayoutPosition();
                flag = true;
                transactionid1 = transactionid.remove(position);
                dates1 = dates.remove(position);
                amount1 = amount.remove(position);
                typeoftransaction1 = typeoftransaction.remove(position);
                spinner1 = spinner.remove(position);
                note1 = note.remove(position);
                paymentmode1 = paymentmode.remove(position);
                image1 = image.remove(position);
                myTransactionItemAdapter.setCards(transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
                myTransactionItemAdapter.notifyItemRemoved(position);
                Snackbar snackbar = Snackbar.make(recyclerView,"Transaction is Deleted",Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", v -> { flag = false;
                    transactionid.add(position,transactionid1);
                    dates.add(position,dates1);
                    amount.add(position,amount1);
                    typeoftransaction.add(position,typeoftransaction1);
                    spinner.add(position,spinner1);
                    note.add(position,note1);
                    paymentmode.add(position,paymentmode1);
                    image.add(position,image1);
                    myTransactionItemAdapter.setCards(transactionid,dates,amount,typeoftransaction,spinner,note,paymentmode,image);
                    myTransactionItemAdapter.notifyItemInserted(position);
                });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if(flag) {
                           deleteData(data);
                        }
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();

//                recreate();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AllTransactions.this,MainActivity.class));
        finishAffinity();
    }

    private void deleteData(int data) {
        Cursor cursor = myDB.GetBudget();
        if(cursor.getCount() == 0) { long result = myDB.DeleteTransaction(data);  }
        else{
            while (cursor.moveToNext()) {
                bid.add(cursor.getInt(0));
                bt_date.add(cursor.getString(1));
                remain_amt.add(cursor.getInt(2));
                total_amt.add(cursor.getInt(3));
            }
            for (int i = 0; i < bid.size() ; i++) {
                budgetid = bid.get(i);
                bd_date = bt_date.get(i);
                remain_bef_delete = remain_amt.get(i);
                total_budget = total_amt.get(i);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
            try {
                Date date = simpleDateFormat.parse(bd_date);
                Date date1 = simpleDateFormat.parse(dates1);
                String dformat = simpleDateFormat.format(date);
                String sformat =simpleDateFormat.format(date1);
                if(sformat.equals(dformat)){
                    if(typeoftransaction1.equals("Expense")){
                        remain_aft_delete = remain_bef_delete + Integer.parseInt(amount1);
                        long update_budget = myDB.UpdateBudget(budgetid,remain_aft_delete,total_budget);
                        long result = myDB.DeleteTransaction(data);
                    }
                    else{  long result = myDB.DeleteTransaction(data);   }
                }
                else { long result = myDB.DeleteTransaction(data);    }
            } catch (ParseException e) { e.printStackTrace();      }
        }
    }

    void displayData()
    {
        Cursor cursor = myDB.GetTransaction();
        if(cursor.getCount() == 0) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext())
            {
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                transactionid.add(cursor.getInt(0));
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
    public void ClickMenu(View v)
    {
        MainActivity.openDrawer(dl);
    }
    public void ClickLogo(View v)
    {
        MainActivity.closeDrawer(dl);
    }
    public void AllTransactions(View v)
    {
        recreate();
    }
    public void Home(View v)
    {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void DayView(View v) { MainActivity.redirectActivity(this,DayView.class); }
    public  void MonthView(View v)
    {
        MainActivity.redirectActivity(this,MonthView.class);
    }
    public void CustomView(View v)
    {
        MainActivity.redirectActivity(this,CustomView.class);
    }
    public void Budget(View v)
    {
        MainActivity.redirectActivity(this,Budget.class);
    }
    public void Category(View v)
    {
        MainActivity.redirectActivity(this,Category.class);
    }
    public void Settings(View v)
    {
        MainActivity.redirectActivity(this,Settings.class);
    }
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(dl);
    }
}