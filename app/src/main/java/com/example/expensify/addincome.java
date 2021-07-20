package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class addincome extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    DrawerLayout dl;
    Spinner spinner;
    EditText ex1,ex2,ex3;
    FloatingActionButton btn1;
    RadioButton rb,rb2;
    RadioGroup rg1,rg2;
    dbHelper myDB;
    TextView textView;
    Cursor cursor;
    int cid,bid;
    String month;
    int row_id;
    Date date,mdate;
    int budgetamount;
    int remain_amount;
    int diff;
    String name,name1,id,transamount,transdate,transtype,transnote,paymentmode,spinnername,transspinner,add1;

    String  datelt,typelt,catlt,notelt,paylt;
    int tidlt,amount,imagelt,bidlt;

    ArrayList<ExpenseItems> customList = new ArrayList<>();
    ArrayList<IncomeItems> customList1 = new ArrayList<>();
    String sdate1,sdate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addincome);
        dl = findViewById(R.id.income_layout);
        spinner = findViewById(R.id.spinner);
        ex1 = findViewById(R.id.edtdate);
        ex2 = findViewById(R.id.edtamount);
        ex3 = findViewById(R.id.edtnote);
        btn1 = findViewById(R.id.btnadd);

        DatePickerDialog.OnDateSetListener setListener;
        Bundle bundle = getIntent().getExtras();
        myDB = new dbHelper(addincome.this);
        if (bundle != null) {
            boolean value = bundle.getBoolean("key");
            if (value){
                textView = findViewById(R.id.addincome);
                textView.setText("Add Transaction");

                ex1.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                        Calendar calendar = Calendar.getInstance();
                        final int year = calendar.get(Calendar.YEAR);
                        final int month = calendar.get(Calendar.MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dpd1 = new DatePickerDialog(addincome.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month += 1;
                                String date = year + "/" + month + "/" + day;
                                try {
                                    Date formateddate = formatter.parse(date);
                                    sdate1 = formatter.format(formateddate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                ex1.setText(sdate1);
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
                            sdate2 = formatter.format(formateddate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        ex1.setText(sdate2);
                    }
                };
                btn1.setOnClickListener(this);
            } else {

                textView = findViewById(R.id.addincome);
                textView.setText("Update Transaction");
                rg1 = findViewById(R.id.rgroup);
                int radioId = rg1.getCheckedRadioButtonId();
                rb = findViewById(radioId);

                rg2 = findViewById(R.id.rgroup2);
                int radioId2 = rg2.getCheckedRadioButtonId();
                rb2 = findViewById(radioId2);

                if(getIntent().hasExtra("Id") && getIntent().hasExtra("Amount") &&  getIntent().hasExtra("Dates") &&  getIntent().hasExtra("Types") && getIntent().hasExtra("Category") && getIntent().hasExtra("Note") &&  getIntent().hasExtra("PaymentMode"))
                {
                    //Getting Data from Intent
                    id = getIntent().getStringExtra("Id");
                    transamount = getIntent().getStringExtra("Amount");
                    transdate = getIntent().getStringExtra("Dates");
                    transtype = getIntent().getStringExtra("Types");
                    transspinner = getIntent().getStringExtra("Category");
                    transnote = getIntent().getStringExtra("Note");
                    paymentmode = getIntent().getStringExtra("PaymentMode");
                    ex2.setText(transamount);
                    ex3.setText(transnote);
                    ex1.setText(transdate);

                    ex1.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                            Calendar calendar = Calendar.getInstance();
                            final int year = calendar.get(Calendar.YEAR);
                            final int month = calendar.get(Calendar.MONTH);
                            final int day = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog dpd1 = new DatePickerDialog(addincome.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    month += 1;
                                    String date = year + "/" + month + "/" + day;
                                    Date formateddate = null;
                                    try {
                                        formateddate = formatter.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    sdate1 = formatter.format(formateddate);
                                    ex1.setText(sdate1);
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
                            Date formateddate = null;
                            try {
                                formateddate = formatter.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdate1 = formatter.format(formateddate);
                            ex1.setText(sdate1);
                        }
                    };
                    if(transtype.equals("Expense")) {
                        rg2.check(R.id.texpense);
                        cursor = myDB.GetCategory();
                        while (cursor.moveToNext()) {
                            if(cursor.getString(2).equals("Expense")) {
                                customList.add(new ExpenseItems(cursor.getInt(0),cursor.getInt(4), cursor.getString(1)));
                            }
                        }
                        ExpenseAdapter expenseAdapter = new ExpenseAdapter(this,customList);
                        if (spinner != null) {
                            spinner.setAdapter(expenseAdapter);
                            for(int i = 0; i < customList.size(); i++) {
                                if (customList.get(i).getText().equalsIgnoreCase(transspinner)) {
                                    spinner.setSelection(i);
                                }
                            }
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ExpenseItems items1 = (ExpenseItems) parent.getSelectedItem();
                                    name = items1.getText();
                                    cid = items1.getId();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) { }
                            });
                        }
                    }
                    else {
                        rg2.check(R.id.tincome);
                        Cursor cursor = myDB.GetCategory();
                        while (cursor.moveToNext())
                        {
                            if(cursor.getString(2).equals("Income")) {
                                customList1.add(new IncomeItems(cursor.getInt(0),cursor.getInt(4), cursor.getString(1)));
                            }
                        }
                        IncomeAdapter incomeAdapter = new IncomeAdapter(this,customList1);
                        if (spinner != null) {
                            spinner.setAdapter(incomeAdapter);
                            for(int i = 0; i < customList1.size(); i++) {
                                if (customList1.get(i).getSpinnertext().equalsIgnoreCase(transspinner)) {
                                    spinner.setSelection(i);
                                }
                            }
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    IncomeItems items = (IncomeItems) parent.getSelectedItem();
                                    name = items.getSpinnertext();
                                    cid = items.getId();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) { }
                            });
                        }
                    }
                    if(paymentmode.equals("Card")) {
                        rg1.check(R.id.rbcard);
                    }
                    else {
                        rg1.check(R.id.rbcash);
                    }
                }
                else {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ex1.getText().toString().isEmpty() || ex2.getText().toString().isEmpty() || ex3.getText().toString().isEmpty() || rg1.getCheckedRadioButtonId() == -1 || rg2.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(addincome.this, "You have not entered proper data", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dbHelper updateDB = new dbHelper(addincome.this);
                            String update = ex1.getText().toString();
                            int upamount = Integer.parseInt(ex2.getText().toString());
                            String upnote = ex3.getText().toString();
                            rg1 = findViewById(R.id.rgroup);
                            int radioId = rg1.getCheckedRadioButtonId();
                            rb = findViewById(radioId);
                            String uppayentmode = rb.getText().toString();
                            rg2 = findViewById(R.id.rgroup2);
                            int radioId2 = rg2.getCheckedRadioButtonId();
                            rb2 = findViewById(radioId2);
                            if (rb2.isChecked()) {
                                if (rb2.getText().toString().equals("Expense")) {
                                    spinnername = name;
                                } else {
                                    spinnername = name;
                                }
                            }
                            String uptypeoftrans = rb2.getText().toString();

                            Cursor cursor = updateDB.GetBudget();
                            if (cursor.getCount() == 0) {
                                bid = 0;
                            } else {
                                while (cursor.moveToNext()) {
                                    row_id = cursor.getInt(0);
                                    month = cursor.getString(1);
                                    budgetamount = cursor.getInt(2);
                                }

                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");

                                try {
                                    Date date1 = df.parse(month);
                                    Date date2 = df.parse(transdate);
                                    String formatdt1 = df.format(date1);
                                    String formatdt2 = df.format(date2);

                                    boolean demo = formatdt1.equals(formatdt2);
                                    if (demo) {
                                        if (rb2.isChecked()) {
                                            if (rb2.getText().toString().equals("Expense")) {
                                                bid = row_id;
                                                if (upamount <= Integer.parseInt(transamount)) {
                                                    diff = Integer.parseInt(transamount) - upamount;
                                                    budgetamount += diff;
                                                } else {
                                                    diff = upamount - Integer.parseInt(transamount);
                                                    budgetamount -= diff;
                                                }

                                                long result1 = updateDB.deductBudget(row_id, budgetamount);

                                                Toast.makeText(addincome.this, "" + result1, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {

                                    }
                                } catch (ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            }
                            long result = updateDB.updateTrans(id, update, upamount, uptypeoftrans, spinnername, upnote, uppayentmode, cid, bid);
                            if (result == -1) {
                                Toast.makeText(addincome.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(addincome.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                i.setClass(addincome.this, AllTransactions.class);
                                startActivity(i);
                                finishAffinity();
                            }
                        }
                    }
                });

            }
        }
        //End of OnCreate
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(addincome.this,MainActivity.class));
        finishAffinity();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  }

    @Override
    public void onClick(View v) {
        if(ex1.getText().toString().isEmpty() || ex2.getText().toString().isEmpty() || ex3.getText().toString().isEmpty() )
        {
            Toast.makeText(this, "You have not entered proper data", Toast.LENGTH_SHORT).show();
            ex1.setText(null);
            ex2.setText(null);
            ex3.setText(null);
        }
        else {
            String dates = ex1.getText().toString();
            int amount = Integer.parseInt(ex2.getText().toString());
            String note = ex3.getText().toString();
            rg1 = findViewById(R.id.rgroup);
            int radioId = rg1.getCheckedRadioButtonId();
            rb = findViewById(radioId);

            rg2 = findViewById(R.id.rgroup2);
            int radioId2 = rg2.getCheckedRadioButtonId();
            rb2 = findViewById(radioId2);
            if (rb2.isChecked()) {
                if (rb2.getText().toString().equals("Expense")) {
                    spinnername = name1;
                } else {
                    spinnername = name;
                }
            }
            dbHelper db = new dbHelper(this);
            Cursor c = myDB.GetBudget();
            if (c.getCount() == 0) {
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
                bid = 0;
            } else {
                while (c.moveToNext()) {
                    row_id = c.getInt(0);
                    month = c.getString(1);
                    budgetamount = c.getInt(2);
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");
                try {
                    Date date1 = df.parse(month);
                    Date date2 = df.parse(dates);
                    String formatdt1 = df.format(date1);
                    String formatdt2 = df.format(date2);

                    boolean demo = formatdt1.equals(formatdt2);
                    if (demo) {
                        if (rb2.isChecked()) {
                            if (rb2.getText().toString().equals("Expense")) {
                                bid = row_id;
                                remain_amount = budgetamount - amount;
                                long result1 = db.deductBudget(row_id, remain_amount);
                            }
                        }
                    } else { bid = 0;   }
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
            String result = db.AddRecord(dates, amount, rb2.getText().toString(), spinnername, note, rb.getText().toString(), cid, bid);
            Toast.makeText(addincome.this, result, Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setClass(addincome.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        customList.clear();
        customList1.clear();
        switch (view.getId()) {
            case R.id.texpense:
                if (checked) {
                    str = "Cash Selected";
                    cursor = myDB.GetCategory();
                    while (cursor.moveToNext())
                    {
                        if(cursor.getString(2).equals("Expense")) {
                            customList.add(new ExpenseItems(cursor.getInt(0),cursor.getInt(4), cursor.getString(1)));
                        }
                    }

                    ExpenseAdapter expenseAdapter = new ExpenseAdapter(this,customList);
                    if (spinner != null) {
                        spinner.setAdapter(expenseAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ExpenseItems items1 = (ExpenseItems) parent.getSelectedItem();
                                name1 = items1.getText();
                                cid = items1.getId();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });
                    }
                }break;
            case R.id.tincome:
                if (checked) {
                    str = "Card selected";
                    Cursor cursor = myDB.GetCategory();
                    while (cursor.moveToNext())
                    {
                        if(cursor.getString(2).equals("Income")) {
                            customList1.add(new IncomeItems(cursor.getInt(0),cursor.getInt(4), cursor.getString(1)));
                        }
                    }

                    IncomeAdapter incomeAdapter = new IncomeAdapter(this,customList1);
                    if (spinner != null) {
                        spinner.setAdapter(incomeAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                IncomeItems items = (IncomeItems) parent.getSelectedItem();
                                name = items.getSpinnertext();
                                cid = items.getId();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });
                    }
                }break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }
}