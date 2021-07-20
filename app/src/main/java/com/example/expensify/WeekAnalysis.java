package com.example.expensify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekAnalysis extends AppCompatActivity {

    private dbHelper helper = new dbHelper(WeekAnalysis.this);
    private ArrayList<Integer> amountlist;
    private ArrayList<String> typelist;

    // New PieChart
    private PieChart piechart,ipiechart;

    ImageView prev, next,imgtrans;
    TextView txtdate,txttrans;
    String startDate = "", endDate = "";
    int left1 = 0, right1 = 0, center = 0;
    private Cursor cursor,cursor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_analysis);

        prev = findViewById(R.id.imgprev);
        next = findViewById(R.id.imgnext);
        txtdate = findViewById(R.id.txtdate);
        imgtrans = findViewById(R.id.imgtrans);
        txttrans = findViewById(R.id.txttrans);

        piechart = findViewById(R.id.week_chart_analysis);
        ipiechart = findViewById(R.id.week_chart_income);

        amountlist = new ArrayList<>();
        typelist = new ArrayList<>();
        amountlist.clear();
        typelist.clear();

        //calnder week vise
        Calendar cal = Calendar.getInstance();
        int current_week = cal.get(Calendar.WEEK_OF_YEAR);
        int week_start_day = cal.getFirstDayOfWeek(); // this will get the starting day os week in integer format i-e 1 if monday
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        startDate = df1.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        endDate = df1.format(cal.getTime());
        txtdate.setText(startDate + " " + endDate);
        center = 1;

        //Expense
        cursor = helper.GetWeekTransaction(startDate, endDate);
        cursor1 = helper.GetWeekIncome(startDate, endDate);

        if (cursor.getCount() == 0 && cursor1.getCount()==0)
        {
            piechart.clear();
            piechart.setDescription(null);
            piechart.setNoDataText("");
            imgtrans.setVisibility(View.VISIBLE);
            txttrans.setVisibility(View.VISIBLE);
        }else {
            imgtrans.setVisibility(View.GONE);
            txttrans.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                typelist.add(cursor.getString(0));
                amountlist.add(cursor.getInt(1));
            }
            if (typelist.isEmpty() && amountlist.isEmpty())
            {
                piechart.clear();
                piechart.setDescription(null);
                piechart.setNoDataText("");
            } else
            {
                setupPieChart();
                GetPieChart(typelist, amountlist);
            }
        }

        //income

        if (cursor1.getCount() == 0 && cursor.getCount()==0)
        {
            ipiechart.clear();
            ipiechart.setDescription(null);
            ipiechart.setNoDataText("");
            imgtrans.setVisibility(View.VISIBLE);
            txttrans.setVisibility(View.VISIBLE);
        }else {
            imgtrans.setVisibility(View.GONE);
            txttrans.setVisibility(View.GONE);
            typelist.clear();
            amountlist.clear();
            while (cursor1.moveToNext()) {
                typelist.add(cursor1.getString(0));
                amountlist.add(cursor1.getInt(1));
            }
            if (typelist.isEmpty() && amountlist.isEmpty())
            {
                ipiechart.clear();
                ipiechart.setDescription(null);
                ipiechart.setNoDataText("");
            } else
            {
                IncomesetupPieChart();
                IncomeGetPieChart(typelist, amountlist);
            }
        }
        //prev
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piechart.clear();
                piechart.setDescription(null);
                piechart.setNoDataText("");
                left1 = 1;
                if (right1 == 1) {
                    cal.add(Calendar.DATE, -7);
                    right1 = 0;
                }
                if (center == 1) {
                    cal.add(Calendar.DATE, -7);
                    center = 0;
                }
                endDate = df1.format(cal.getTime());
                cal.add(Calendar.DATE, -6);
                startDate = df1.format(cal.getTime());
                cal.add(Calendar.DATE, -1);
                txtdate.setText(startDate + " " + endDate);

                //expense
                cursor = helper.GetWeekTransaction(startDate, endDate);
                cursor1 = helper.GetWeekIncome(startDate, endDate);
                if (cursor.getCount() == 0 && cursor1.getCount()==0) {

                    //Clear the chart
                    piechart.clear();
                    piechart.setDescription(null);
                    piechart.setNoDataText("");
                    imgtrans.setVisibility(View.VISIBLE);
                    txttrans.setVisibility(View.VISIBLE);
                }
                else {
                    imgtrans.setVisibility(View.GONE);
                    txttrans.setVisibility(View.GONE);
                    //Clear the PieChart
                    piechart.clear();
                    typelist.clear();
                    amountlist.clear();
                    piechart.setDescription(null);
                    piechart.setNoDataText("");
                    while (cursor.moveToNext()) {
                        typelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                    }
                    if(typelist.isEmpty() && amountlist.isEmpty()){
                        piechart.clear();
                        piechart.setDescription(null);
                        piechart.setNoDataText("");
                    } else {
                        //Setting up PieChart
                        setupPieChart();
                        GetPieChart(typelist, amountlist);
                    }
                }
                //income
                cursor1 = helper.GetWeekIncome(startDate, endDate);
                if (cursor1.getCount() == 0 && cursor.getCount()==0) {

                    //Clear the chart
                    ipiechart.clear();
                    ipiechart.setDescription(null);
                    ipiechart.setNoDataText("");
                    imgtrans.setVisibility(View.VISIBLE);
                    txttrans.setVisibility(View.VISIBLE);
                } else {
                    imgtrans.setVisibility(View.GONE);
                    txttrans.setVisibility(View.GONE);
                    //Clear the PieChart
                    ipiechart.clear();
                    typelist.clear();
                    amountlist.clear();
                    ipiechart.setDescription(null);
                    ipiechart.setNoDataText("");
                    while (cursor1.moveToNext()) {
                        typelist.add(cursor1.getString(0));
                        amountlist.add(cursor1.getInt(1));
                    }
                    if(typelist.isEmpty() && amountlist.isEmpty()){
                        ipiechart.clear();
                        ipiechart.setDescription(null);
                        ipiechart.setNoDataText("");
                    } else {
                        //Setting up PieChart
                        IncomesetupPieChart();
                        IncomeGetPieChart(typelist, amountlist);
                    }
                }
            }
        });
        //next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piechart.clear();
                piechart.setDescription(null);
                piechart.setNoDataText("");
                right1 = 1;
                if (left1 == 1) {
                    cal.add(Calendar.DATE, 7);
                    left1 = 0;
                }
                cal.add(Calendar.DATE, 1);
                startDate = df1.format(cal.getTime());
                cal.add(Calendar.DATE, 6);
                endDate = df1.format(cal.getTime());
                txtdate.setText(startDate + " " + endDate);
                //expense
                cursor = helper.GetWeekTransaction(startDate, endDate);
                cursor1 = helper.GetWeekIncome(startDate, endDate);
                if (cursor.getCount()==0 && cursor1.getCount()==0) {
                    piechart.clear();
                    piechart.setDescription(null);
                    piechart.setNoDataText("");
                    imgtrans.setVisibility(View.VISIBLE);
                    txttrans.setVisibility(View.VISIBLE);
                }
                else {
                    imgtrans.setVisibility(View.GONE);
                    txttrans.setVisibility(View.GONE);
                    typelist.clear();
                    amountlist.clear();
                    while (cursor.moveToNext()) {
                        typelist.add(cursor.getString(0));
                        amountlist.add(cursor.getInt(1));
                    }
                    if (typelist.isEmpty() && amountlist.isEmpty()) {
                        //Clear the chart
                        piechart.clear();
                        piechart.setDescription(null);
                        piechart.setNoDataText("");
                    } else {
                        //Setting up the PieChart
                        setupPieChart();
                        GetPieChart(typelist, amountlist);
                    }
                }
                //income
                cursor1 = helper.GetWeekIncome(startDate, endDate);
                if (cursor1.getCount()==0 && cursor.getCount()==0) {
                    ipiechart.clear();
                    ipiechart.setDescription(null);
                    ipiechart.setNoDataText("");
                    imgtrans.setVisibility(View.VISIBLE);
                    txttrans.setVisibility(View.VISIBLE);
                }
                else {
                    imgtrans.setVisibility(View.GONE);
                    txttrans.setVisibility(View.GONE);
                    typelist.clear();
                    amountlist.clear();
                    while (cursor1.moveToNext()) {
                        typelist.add(cursor1.getString(0));
                        amountlist.add(cursor1.getInt(1));
                    }
                    if (typelist.isEmpty() && amountlist.isEmpty()) {
                        //Clear the chart
                        ipiechart.clear();
                        ipiechart.setDescription(null);
                        ipiechart.setNoDataText("");
                    } else {
                        //Setting up the PieChart
                        IncomesetupPieChart();
                        IncomeGetPieChart(typelist, amountlist);
                    }
                }
            }
        });
    }
    //expense
    private void setupPieChart() {
        piechart.setDrawHoleEnabled(true);
        piechart.setUsePercentValues(true);
        piechart.setEntryLabelTextSize(12);
        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setCenterText("Spending by Category");
        piechart.setCenterTextSize(24);

        Legend l = piechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    //expense
    private void GetPieChart (ArrayList <String> typelist, ArrayList <Integer> amountlist) {
        //adding the ListData into PieEntry
        List<PieEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < amountlist.size(); i++) {
            dataEntries.add(new PieEntry(amountlist.get(i), typelist.get(i)));
        }

        //setting up the colour
        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        //setting the data
        PieDataSet pieDataSet = new PieDataSet(dataEntries,"Expense Category");
        pieDataSet.setColors(colors);

        //Setting Up the PieChart
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(piechart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);
        piechart.animateXY(800,2000);
        piechart.animate();
        piechart.setData(pieData);
        piechart.setDrawSliceText(false);
        piechart.invalidate();

    }
    //income
    private void IncomesetupPieChart() {
        ipiechart.setDrawHoleEnabled(true);
        ipiechart.setUsePercentValues(true);
        ipiechart.setEntryLabelTextSize(12);
        ipiechart.setEntryLabelColor(Color.BLACK);
        ipiechart.setCenterText("Income By Category");
        ipiechart.setCenterTextSize(24);

        Legend li = ipiechart.getLegend();
        li.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        li.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        li.setOrientation(Legend.LegendOrientation.VERTICAL);
        li.setDrawInside(false);
        li.setEnabled(true);

    }
    //income
    private void IncomeGetPieChart(ArrayList <String> typelist, ArrayList <Integer> amountlist) {
        //adding the ListData into PieEntry
        List<PieEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < amountlist.size(); i++) {
            dataEntries.add(new PieEntry(amountlist.get(i), typelist.get(i)));
        }

        //setting up the colour
        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color : ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        //setting the data
        PieDataSet pieDataSet1 = new PieDataSet(dataEntries,"Income Category");
        pieDataSet1.setColors(colors);

        //Setting Up the PieChart
        PieData ipieData = new PieData(pieDataSet1);
        ipieData.setDrawValues(true);
        ipieData.setValueFormatter(new PercentFormatter(ipiechart));
        ipieData.setValueTextSize(12f);
        ipieData.setValueTextColor(Color.BLACK);
        ipiechart.animateXY(800,2000);
        ipiechart.animate();
        ipiechart.setData(ipieData);
        ipiechart.setDrawSliceText(false);
        ipiechart.invalidate();
    }
}