package com.example.expensify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.sql.DriverManager.println;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class  AddBudget extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabutton;
    TextView addbudget;
    EditText et1, et2;
    String date;
    int amount;
    String total_amount;
    DatePickerDialog.OnDateSetListener setListener;
    String up_dt;
    String startDateStr,endDateStr;
    Date formatter;
    ArrayList<Integer> amount_bd,transaction_id,bid_bd;
    int amt,id,remain_amt,up_bid,up_amt,remain_diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        fabutton = findViewById(R.id.save_budget);
        et1 = findViewById(R.id.bgtdate);
        et2 = findViewById(R.id.bgtamount);

        transaction_id = new ArrayList<>();
        amount_bd = new ArrayList<>();
        bid_bd = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean value = bundle.getBoolean("key");
            if(value) {
                addbudget = findViewById(R.id.addbudget);
                addbudget.setText("Add Budget");
                et1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                        Calendar calendar = getInstance();
                        final int year = calendar.get(YEAR);
                        final int month = calendar.get(MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dp1 = new DatePickerDialog(AddBudget.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month += 1;
                                String date = year + "/" + month + "/" + day;
                                try {
                                    formatter = simpleDateFormat.parse(date);
                                    date = simpleDateFormat.format(formatter);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                et1.setText(date);
                            }
                        }, year, month, day);
                        dp1.show();
                    }
                });
                setListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                        month += 1;
                        String date = year + "/" + month + "/" + day;
                        try {
                            formatter = simpleDateFormat.parse(date);
                            date = simpleDateFormat.format(formatter);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        et1.setText(date);
                    }
                };
                fabutton.setOnClickListener(this);
            } else {
                addbudget = findViewById(R.id.addbudget);
                addbudget.setText("Update Budget");
                if (getIntent().hasExtra("Id") && getIntent().hasExtra("date") && getIntent().hasExtra("amount") && getIntent().hasExtra("total_amount")) {
                    id = Integer.parseInt(getIntent().getStringExtra("Id"));
                    date = getIntent().getStringExtra("date");
                    amount = Integer.parseInt(getIntent().getStringExtra("amount"));
                    total_amount = getIntent().getStringExtra("total_amount");

                    et1.setText(date);
                    et1.setEnabled(false);
                    et2.setText(total_amount);
                    et1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                            Calendar calendar = getInstance();
                            final int year = calendar.get(YEAR);
                            final int month = calendar.get(MONTH);
                            final int day = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog dp1 = new DatePickerDialog(AddBudget.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    month += 1;
                                    String date = year + "/" + month + "/" + day;
                                    try {
                                        formatter = simpleDateFormat.parse(date);
                                        date = simpleDateFormat.format(formatter);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    et1.setText(date);
                                }
                            }, year, month, day);
                            dp1.show();
                        }
                    });
                    setListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                            month += 1;
                            String date = year + "/" + month + "/" + day;
                            try {
                                formatter = simpleDateFormat.parse(date);
                                date = simpleDateFormat.format(formatter);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            et1.setText(date);
                        }
                    };
                } else {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                fabutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et2.getText().toString().isEmpty()) {
                            Toast.makeText(AddBudget.this, "You have not entered Proper data", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dbHelper updateDB = new dbHelper(AddBudget.this);
                            String date = et1.getText().toString();
                            int amount1 = Integer.parseInt(et2.getText().toString());

                            if (amount1 <= Integer.parseInt(total_amount)) {
                                remain_diff = Integer.parseInt(total_amount) - amount1;
                                amount -= remain_diff;
                            } else {
                                remain_diff = amount1 - Integer.parseInt(total_amount);
                                amount += remain_diff;
                            }
                            long result = updateDB.UpdateBudget(id, amount, amount1);
                            if (result == -1) {
                                Toast.makeText(AddBudget.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddBudget.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                i.setClass(AddBudget.this, Budget.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()) {
            Toast.makeText(this, "You have not entered Proper data", Toast.LENGTH_SHORT).show();
            et1.setText(null);
            et2.setText(null);
        }
        else {
            String date = et1.getText().toString();
            int amount = Integer.parseInt(et2.getText().toString());
            dbHelper db = new dbHelper(AddBudget.this);
            Boolean result = db.AddBudget(date, amount, amount);

            try {
                Date dd = new SimpleDateFormat("yyyy/mm/dd").parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                int year = Integer.parseInt(dateFormat.format(dd));
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("mm");
                int month = Integer.parseInt(dateFormat1.format(dd));

                Calendar calendar = Calendar.getInstance();
                calendar.set(YEAR, year);
                calendar.set(MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date monthFirstDay = calendar.getTime();
                calendar.set(YEAR, year);
                calendar.set(MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date monthLastDay = calendar.getTime();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                startDateStr = df.format(monthFirstDay);
                endDateStr = df.format(monthLastDay);
            } catch (ParseException e) { e.printStackTrace();  }

            if (result) {
                Cursor cursor_bd = db.GetMonthTransaction(startDateStr, endDateStr);
                while (cursor_bd.moveToNext()) {
                    amount_bd.add(cursor_bd.getInt(0));
                    transaction_id.add(cursor_bd.getInt(1));
                    bid_bd.add(cursor_bd.getInt(2));
                }

                Cursor cursor_update = db.GetSelecteddata(startDateStr, endDateStr);
                while (cursor_update.moveToNext()) {
                    if (cursor_update.getCount() == 0) {
                        Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
                    } else {
                        up_bid = cursor_update.getInt(0);
                        up_dt = cursor_update.getString(1);
                        up_amt = cursor_update.getInt(2);
                    }
                }

                for (int i = 0; i < bid_bd.size(); i++) { amt = amt + amount_bd.get(i);   }

                remain_amt = amount - amt;
                long updated_id;
                for (int j = 0; j < amount_bd.size(); j++) {
                    updated_id = db.updatebudg_id(transaction_id.get(j), up_bid);
                }
            } else { Toast.makeText(this, "Budget Not-added", Toast.LENGTH_SHORT).show();   }
            dbHelper updateDB = new dbHelper(AddBudget.this);
            long result1 = updateDB.UpdateBudget(up_bid, remain_amt, amount);
            if (result1 == -1) {
                Toast.makeText(AddBudget.this, "Update Failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddBudget.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent();
            i.setClass(AddBudget.this, Budget.class);
            startActivity(i);
            finish();
        }
    }
}

