package com.example.wallet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
/*
Framgent για την εμφάνιση των γραφημάτων με μηνιαία στατιστικά δεδομένα
 */
public class StatisticsFragment extends Fragment {

    private String month;
    private int total;
    private int supermarket;
    private int entertainment;
    private int home;
    private int transportation;
    private int other;
    private static final String MONTH = "month";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";
    private static final String TRANSPORTATION = "transportation";
    private static final String OTHER = "other";
    BudgetManager budget;

    PieChart pieChart;
    BarChart barChart;
    private String MainMonth;
    private String DBDayWithoutFirstCharacter;

    String saveDay;
    String saveMonth;

    public StatisticsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            month = bundle.getString(MONTH);
            total = bundle.getInt(TOTAL);
            supermarket = bundle.getInt(SUPERMARKET);
            entertainment = bundle.getInt(ENTERTAINMENT);
            home = bundle.getInt(HOME);
            transportation = bundle.getInt(TRANSPORTATION);
            other = bundle.getInt(OTHER);
        }
        if(savedInstanceState != null){
            saveDay = savedInstanceState.getString("saveDay");
            saveMonth = savedInstanceState.getString("saveMonth");
            Log.d("paok", "onCreate: DAY: "+ saveDay);
            Log.d("paok", "onCreate: MONTH: "+ saveMonth);
        }
    }

    public void setData(String month, int total, int supermarket, int entertainment, int home, int transportation, int other) {
        this.month = month;
        this.total = total;
        this.supermarket = supermarket;
        this.entertainment = entertainment;
        this.home = home;
        this.transportation = transportation;
        this.other = other;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("saveDay", DBDayWithoutFirstCharacter);
        outState.putString("saveMonth", MainMonth);
        super.onSaveInstanceState(outState);
    }

    //Δημιουργία και παρουσίαση του fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.barChart);

        ArrayList<BarEntry> expenses = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Integer> totals = new ArrayList<>();
        MainMonth = month;

        DBHandler dbHandler = new DBHandler(getActivity(), null, null, 5);
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String query = "SELECT " + DBHandler.COLUMN_DAYS + ", " + DBHandler.COLUMN_VALUE +
                " FROM " + DBHandler.TABLE_VALUES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String day = cursor.getString(0);
                int total = cursor.getInt(1);
                String DBDay = String.valueOf(day);
                DBDayWithoutFirstCharacter = DBDay.substring(2);

                if(DBDayWithoutFirstCharacter == null){
                    DBDayWithoutFirstCharacter = saveDay;
                }

                if(MainMonth == null){
                    MainMonth = saveMonth;
                }

                DBDayWithoutFirstCharacter = DBDayWithoutFirstCharacter.trim();

                MainMonth = MainMonth.trim();

                if (DBDayWithoutFirstCharacter.equals(MainMonth)) {
                    days.add(day);
                    totals.add(total);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        for (int i = 0; i < days.size(); i++) {
            int total = totals.get(i);
            expenses.add(new BarEntry(i, total));
        }

        //Ταξινόμηση των ημερών
        Comparator<String> dateComparator = new Comparator<String>() {
            @Override
            public int compare(String date1, String date2) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                    Date d1 = sdf.parse(date1);
                    Date d2 = sdf.parse(date2);
                    return ((Date) d1).compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        Collections.sort(days, dateComparator);

        //Σχεδιασμός του γραφήματος με μπάρες\
        //Ενεργοποίηση εξόνων
        //Ορισμός ετικετών με βάση τη λίστα days
        //Ορισμός μεγεθών και δεδομένων
        //Προσθεση κίνησης
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setHorizontalScrollBarEnabled(true);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setLabelCount(days.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(7f);
        barChart.getXAxis().setLabelRotationAngle(80);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);

        BarDataSet barDataSet = new BarDataSet(expenses, "Expenses");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
        barChart.animateY(1000);

        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDragEnabled(true);


        pieChart.animateXY(1000, 1000);
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        entries1.add(new PieEntry(supermarket, "Supermarket"));
        entries1.add(new PieEntry(entertainment, "Entertainment"));
        entries1.add(new PieEntry(home, "Home"));
        entries1.add(new PieEntry(transportation, "Transportation"));
        entries1.add(new PieEntry(other, "Other"));
        PieDataSet dataSet = new PieDataSet(entries1, "Expenses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();
        return view;

    }

}