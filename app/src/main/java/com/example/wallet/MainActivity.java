package com.example.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    //Σημερινή ημερομηνία
    private LocalDate selectedDate;
    private Button totalMonthExpenses;
    private ArrayList<String> totalDaysInMonthArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        totalMonthExpenses = findViewById(R.id.month_expenses);

        selectedDate = LocalDate.now();

        setMonthView();
        String sum = Integer.toString(sum());
        totalMonthExpenses.setText(sum);

    }
    private int sum(){
        int sum = 0;
        for (int i = 0; i < totalDaysInMonthArray.size(); i++) {
            if ( totalDaysInMonthArray.get(i)!= null) {
                sum += Integer.parseInt(totalDaysInMonthArray.get(i));
            }
        }
        return sum;
    }

    private void setMonthView() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String value = intent.getStringExtra("value");
        int position = intent.getIntExtra("position", 0);

        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList<String> totalDaysInMonth = totalDaysInMonthArray(selectedDate, date, value, position);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, totalDaysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //δημιουργία λίστας με τις μέρες του μήνα
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        //edo 3anadimiourgeite den prepei
        //υπολογισμός μέρες του μήνα
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        //μέρα της εβδομάδας που ξεκινάει ο μήνας
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //γεμίζει την λίστα με τις μέρες της εδβομάδας
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    //ελεγχος για κάθε θέση του πίνακα
    private ArrayList<String> totalDaysInMonthArray(LocalDate date, String insertedDate, String value, int position) {
        DBHandler dbHandler = new DBHandler(this, null, null, 4);

        totalDaysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //ελέγχεται για κάθε θέση αν αντιστοιχεί σε μια ημέρα του τρέχοντος μήνα
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                totalDaysInMonthArray.add(null);
            } else {
                //διαλέγει τον μηνα που θα εμφανισει συμφωνα με την σημερινη ημερομηνια
                //διαλέγει την ημέρα (position) του μηνα που θα εμφανίσει
                String key = (i - 1) + " " + monthYearFromDate(selectedDate);
                DayValue dayValue = dbHandler.findDay(key);

                if(dayValue != null){
                    String dbValue = dayValue.getValue();
                    String dbDate = dayValue.getDay();
                    totalDaysInMonthArray.add(dbValue);
                }else{
                    totalDaysInMonthArray.add("0");
                }
            }
        }

        return totalDaysInMonthArray;
    }

    //επιστροφή μήνα και έτος
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    //προηγοημενος-επομενος μηνας
    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }
    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    //με κλικ στο ημερολόγιο -> CalculateActivity
    //υπολογίζει την ημερομηνία που επιλέχθηκε
    @Override
    public void onItemClick(int position, String dayText) {
        //Toast.makeText(this, buttonText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CalculateActivity.class);
        String date = dayText + " " + monthYearFromDate(selectedDate);
        intent.putExtra("buttonText", date);
        intent.putExtra("position", position);
        startActivity(intent);
    }


}








