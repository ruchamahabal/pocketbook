package com.example.rucha.pocketbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mChart = (PieChart) findViewById(R.id.category_pie_chart);
        Legend l=mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        setUpPieChart();
    }

    private ArrayList<Integer> getGraphDataPrice(){
        DBHelper db = new DBHelper(GraphsActivity.this);
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<Integer> priceNewData = new ArrayList<Integer>();
        String query="SELECT Categories.category_name, SUM(Receipts.price) AS totalAmount FROM Receipts INNER JOIN Categories ON Receipts.category_id = Categories.category_id GROUP BY Receipts.category_id;";
        Cursor cursor = database.rawQuery(query,null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            priceNewData.add(cursor.getInt(cursor.getColumnIndexOrThrow("totalAmount")));
        }
        cursor.close();
        return priceNewData;
    }

    private ArrayList<String> getGraphDataCategories(){
        DBHelper db = new DBHelper(GraphsActivity.this);
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<String> categoryNewData = new ArrayList<String>();
        String query="SELECT Categories.category_name, SUM(Receipts.price) AS totalAmount FROM Receipts INNER JOIN Categories ON Receipts.category_id = Categories.category_id GROUP BY Receipts.category_id;";
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
        int[] colors = this.getResources().getIntArray(R.array.graph);
        dataSet.setColors(colors, 255);

        PieData data=new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.animateY(1000);
        mChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent goToMainActivity = new Intent(GraphsActivity.this,MainActivity.class);
            goToMainActivity.addCategory(Intent.CATEGORY_HOME);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToMainActivity);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SaveSharedPreferences.clear(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        // Handle navigation view item clicks here.
        switch(item.getItemId())
        {
            case R.id.nav_receipts:
                intent = new Intent(GraphsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.nav_subscriptions:
//                intent = new Intent(GraphsActivity.this, SubscriptionsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.nav_graphs:
//                intent = new Intent(GraphsActivity.this, GraphsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.nav_statistics:
                intent = new Intent(GraphsActivity.this, StatisticsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
