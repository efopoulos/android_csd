package com.example.wallet;

import static com.example.wallet.DBHandler.COLUMN_DAYS;
import static com.example.wallet.DBHandler.COLUMN_ENTERTAINMENT;
import static com.example.wallet.DBHandler.COLUMN_HOME;
import static com.example.wallet.DBHandler.COLUMN_SUPERMARKET;
import static com.example.wallet.DBHandler.COLUMN_VALUE;
import static com.example.wallet.DBHandler.TABLE_VALUES;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MonthExpenses  extends MainActivity{
    TextView monthTextView;
    TextView monthSupermarketTextView;
    TextView monthEntertainmentTextView;
    TextView monthHomeTextView;
    TextView monthTotalTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        monthTextView = findViewById(R.id.month_id);
        monthSupermarketTextView = findViewById(R.id.monthsupermarket_value);
        monthEntertainmentTextView = findViewById(R.id.monthentertainment_value);
        monthHomeTextView = findViewById(R.id.monthhome_value);
        monthTotalTextView = findViewById(R.id.monthtotal_value);

        Intent intent = getIntent();
        String month = intent.getStringExtra("month");

        monthTextView.setText(month);

        int monthlySupermarket= 0;
        int monthlyEntertainment= 0;
        int monthlyHome= 0;
        int monthlyTotal= 0;

        String MainMonth = month;

        DBHandler dbHelper = new DBHandler(this, null, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);


        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                String supermarket = cursor.getString(cursor.getColumnIndex(COLUMN_SUPERMARKET));
                String entertainment = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERTAINMENT));
                String home = cursor.getString(cursor.getColumnIndex(COLUMN_HOME));

                String DBDay = days;
                String DBDayWithoutFirstCharacter = DBDay.substring(2);
                //αφαιρούμε τα κενά
                DBDayWithoutFirstCharacter = DBDayWithoutFirstCharacter.trim();
                MainMonth = MainMonth.trim();

                if (DBDayWithoutFirstCharacter.equals(MainMonth)) {
                    monthlySupermarket = monthlySupermarket + Integer.parseInt(supermarket);
                    monthlyEntertainment = monthlyEntertainment + Integer.parseInt(entertainment);
                    monthlyHome = monthlyHome + Integer.parseInt(home);
                    monthlyTotal = monthlyTotal + Integer.parseInt(value);
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        monthTotalTextView.setText(String.valueOf(monthlyTotal));
        monthSupermarketTextView.setText(String.valueOf(monthlySupermarket));
        monthEntertainmentTextView.setText(String.valueOf(monthlyEntertainment));
        monthHomeTextView.setText(String.valueOf(monthlyHome));

        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.animateXY(1000, 1000);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(monthlySupermarket, "Supermarket"));
        entries.add(new PieEntry(monthlyEntertainment, "Entertainment"));
        entries.add(new PieEntry(monthlyHome, "Home"));

        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("totalExpenses", monthTotalTextView.getText().toString());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("totalExpenses", monthTotalTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }
}