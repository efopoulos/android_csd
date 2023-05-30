package com.example.wallet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private String month;
    private int total;
    private int supermarket;
    private int entertainment;
    private int home;
    private static final String MONTH = "month";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";

    PieChart pieChart;
    BarChart barChart;

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
        }

    }

    public void setData(String month, int total, int supermarket, int entertainment, int home) {
        this.month = month;
        this.total = total;
        this.supermarket = supermarket;
        this.entertainment = entertainment;
        this.home = home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.barChart);

        ArrayList<BarEntry> visitors = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Integer> totals = new ArrayList<>();

        DBHandler dbHandler = new DBHandler(getActivity(), null, null, 1);
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String query = "SELECT " + DBHandler.COLUMN_DAYS + ", " + DBHandler.COLUMN_VALUE +
                " FROM " + DBHandler.TABLE_VALUES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String day = cursor.getString(0);
                int total = cursor.getInt(1);

                days.add(day);
                totals.add(total);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            int total = totals.get(i);
            visitors.add(new BarEntry(i, total));
        }

        BarDataSet barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        barChart.animateY(1000);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
        barChart.animateY(1000);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();

        pieChart.animateXY(1000, 1000);
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        entries1.add(new PieEntry(supermarket, "Supermarket"));
        entries1.add(new PieEntry(entertainment, "Entertainment"));
        entries1.add(new PieEntry(home, "Home"));
        PieDataSet dataSet = new PieDataSet(entries1, "Expenses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        return view;

    }

}