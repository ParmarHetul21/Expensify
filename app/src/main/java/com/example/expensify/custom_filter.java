package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class custom_filter extends AppCompatActivity {

    ChipGroup chipGroup,chipGroup2;
    String expense = "";
    String transaction = "";
    Button button;
    TextView fdate,tdate;
    Button btn1,btn2;
    String Odate,Odate1,date2;
    DatePickerDialog.OnDateSetListener setListener;
    StringBuilder expenselist,paymentlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_filter);

        chipGroup = findViewById(R.id.expense_chipgrp);
        chipGroup2 = findViewById(R.id.payment_chipgrp);
        button = findViewById(R.id.click_changes);

        btn1 = findViewById(R.id.btn_fromdate_custom);
        btn2 = findViewById(R.id.btn_todate_custom);
        fdate = findViewById(R.id.txt_fromdate_custom);
        tdate = findViewById(R.id.txt_todate_custom);
        expenselist = new StringBuilder();
        paymentlist = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);

        btn1.setText(startDateStr);
        btn2.setText(endDateStr);
        fdate.setText(startDateStr);
        tdate.setText(endDateStr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if(chip.isChecked()){
                     expense = chip.getText().toString();
                     expenselist.append("'"+expense+"'");
                     if(i!=chipGroup.getChildCount()-1){
                        expenselist.append(",");
                     }
                    }

                }
                for (int i = 0; i < chipGroup2.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup2.getChildAt(i);
                    if(chip.isChecked()){
                        transaction = chip.getText().toString();
                        paymentlist.append("'"+transaction+"'");
                        if(i!=chipGroup2.getChildCount()-1){
                            paymentlist.append(",");
                        }

                    }
                }
                // if expense or transaction is empty
                if (expenselist.toString().isEmpty() || paymentlist.toString().isEmpty()){
                    expenselist.append("All Categories");
                    paymentlist.append("All Payment Modes");
                }

                // if All Categories expense
                if (expenselist.equals("All Categories")) { }

//              if All Payment modes
                if (paymentlist.equals("All Payment Modes")) { }

                Intent intent = new Intent();
                intent.setClass(custom_filter.this,CustomView.class);
                intent.putExtra("data",expenselist.toString());
                intent.putExtra("data1",paymentlist.toString());
                intent.putExtra("from",fdate.getText().toString());
                intent.putExtra("to",tdate.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void btnFromDate(View v) {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dateNow = calendar.get(Calendar.DAY_OF_MONTH);

        Locale id = new Locale("in", "id");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd",id);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                fdate.setText(simpleDateFormat.format(calendar.getTime()));
                btn1.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yearNow, monthNow, dateNow);
        datePickerDialog.show();
    }

    public void btnToDate(View view) {
        Calendar c = Calendar.getInstance();
        int yNow = c.get(Calendar.YEAR);
        int mNow = c.get(Calendar.MONTH);
        int dNow = c.get(Calendar.DAY_OF_MONTH);

        Locale ID = new Locale("in", "ID");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd",ID);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(year,month,dayOfMonth);
                tdate.setText(simpleDateFormat.format(c.getTime()));
                btn2.setText(simpleDateFormat.format(c.getTime()));
            }
        },yNow, mNow, dNow);
        datePickerDialog.show();
    }

    public void showFromDate(View view) {
        fdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd1 = new DatePickerDialog(custom_filter.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date2 = year + "/" + month + "/" + day;
                        try {
                            Date date1 = formatter.parse(date2);
                            Odate = formatter.format(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fdate.setText(Odate);
                        btn1.setText(Odate);
                    }
                }, year, month, day);
                dpd1.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String date = year + "/" + month + "/" + day;
                try {
                    Date date1 = formatter.parse(date2);
                    Odate1 = formatter.format(date1);
                    btn2.setText(Odate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fdate.setText(Odate1);
            }
        };
    }
}