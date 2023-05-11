package com.example.wallet;

import static com.example.wallet.DBHandler.COLUMN_DAYS;
import static com.example.wallet.DBHandler.COLUMN_VALUE;
import static com.example.wallet.DBHandler.TABLE_VALUES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        selectedDate = LocalDate.now();

        setMonthView();
    }

    private void setMonthView() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        String value = intent.getStringExtra("value");
        Log.d("paok", "position: " + position + " value: " + value);

        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList<String> totalDaysInMonth = totalDaysInMonthArray(selectedDate, position, value);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, totalDaysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        //edo 3anadimiourgeite den prepei
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");

            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private ArrayList<String> totalDaysInMonthArray(LocalDate date, int position, String value) {
        ArrayList<String> totalDaysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                totalDaysInMonthArray.add(null);

            } else {
//                String prev = totalDaysInMonthArray.get(position);
//                totalDaysInMonthArray.add("" + Integer.parseInt(prev) + Integer.parseInt(value));



                totalDaysInMonthArray.add("0");


            }
        }
        return totalDaysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
//        selectedDate = selectedDate.minusMonths(1);
//        setMonthView();
        DBHandler dbHandler = new DBHandler(this, null, null, 1);


        DayValue dayValue = dbHandler.findDay("Sunday");

        Toast.makeText(this, dayValue.getDay() + " : " + dayValue.getValue(), Toast.LENGTH_SHORT).show();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {

        //Toast.makeText(this, buttonText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CalculateActivity.class);
        intent.putExtra("buttonText", dayText + " " + monthYearFromDate(selectedDate));
        intent.putExtra("position", position);
        startActivity(intent);
    }


}








