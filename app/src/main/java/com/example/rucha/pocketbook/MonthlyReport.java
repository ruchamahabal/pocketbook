package com.example.rucha.pocketbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthlyReport extends Fragment {

    PieChart mChart;
    int month, year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monthly_report, container, false);
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;
        Log.e("month",String.valueOf(month));
        year = c.get(Calendar.YEAR);
        Log.e("year", String.valueOf(year));
        mChart = (PieChart) rootView.findViewById(R.id.category_pie_chart);
        Legend l=mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        setUpPieChart();
        return rootView;
    }

    private ArrayList<Integer> getGraphDataPrice(){
        DBHelper db = new DBHelper(getActivity());
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<Integer> priceNewData = new ArrayList<Integer>();
        String query="SELECT Categories.category_name, SUM(Receipts.price) AS totalAmount FROM Receipts INNER JOIN Categories ON Receipts.category_id = Categories.category_id WHERE Receipts.month =" + month + " and Receipts.year ="+ year +" GROUP BY Receipts.category_id;";
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            priceNewData.add(cursor.getInt(cursor.getColumnIndexOrThrow("totalAmount")));
        }
        cursor.close();
        return priceNewData;
    }

    private ArrayList<String> getGraphDataCategories(){
        DBHelper db = new DBHelper(getActivity());
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<String> categoryNewData = new ArrayList<String>();
        String query="SELECT Categories.category_name, SUM(Receipts.price) AS totalAmount FROM Receipts INNER JOIN Categories ON Receipts.category_id = Categories.category_id WHERE Receipts.month =" + month + " and Receipts.year ="+ year + " GROUP BY Receipts.category_id;";
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            categoryNewData.add(cursor.getString(cursor.getColumnIndexOrThrow("category_name")));
        }
        cursor.close();
        return categoryNewData;
    }


    private void setUpPieChart() {
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        for(int i=0; i<getGraphDataPrice().size(); i++)
        {
            pieEntries.add(new PieEntry(getGraphDataPrice().get(i),getGraphDataCategories().get(i)));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "Expenses");
        dataSet.setSliceSpace(3); // space between each arc slice
        dataSet.setSelectionShift(5);
        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        int[] colors = getActivity().getResources().getIntArray(R.array.graph);
        dataSet.setColors(colors, 255);
        PieData data=new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.animateY(1000);
        mChart.invalidate();
    }
}
