package com.example.wallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    //Σημερινή ημερομηνία
    private LocalDate selectedDate;
    private Button budgetButton;
    private TextView totalTextView;

    BottomNavigationView bottomNavigationView;

    int budget ;

    String value;
    private ArrayList<String> totalDaysInMonthArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Na to doyme
        if(savedInstanceState != null) {
            budget = savedInstanceState.getInt("budget");
        }
        setContentView(R.layout.activity_main);

        totalTextView = findViewById(R.id.total_textView);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        budgetButton = findViewById(R.id.budget_button);
        selectedDate = LocalDate.now();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        budget = sharedPreferences.getInt("budget", 0);

        setMonthView();
        monthlySum();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_main:
                    Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    return true;
                case R.id.action_month_expenses:
                    Intent monthExpensesIntent = new Intent(MainActivity.this, MonthExpenses.class);
                    monthExpensesIntent.putExtra("month", monthYearFromDate(selectedDate));
                    startActivity(monthExpensesIntent);
                    return true;
                case R.id.action_commitment:
                    Intent commitmentIntent = new Intent(MainActivity.this, CommitmentActivity.class);
                    commitmentIntent.putExtra("month", monthYearFromDate(selectedDate));
                    startActivity(commitmentIntent);
                    return true;
            }
            return false;
        });

    }

    private int calculateMonthlySum(){
        int sum = 0;
        for (int i = 0; i < totalDaysInMonthArray.size(); i++) {
            if (totalDaysInMonthArray.get(i) != null) {
                sum += Integer.parseInt(totalDaysInMonthArray.get(i));
            }
        }
        return sum;
    }

    private void monthlySum() {

        int sum = calculateMonthlySum();
        Intent intent = getIntent();

        String userInput = BudgetManager.getBudget();

        if (userInput != null) {

            budget = Integer.parseInt(userInput);
        }
        budgetButton.setText(Integer.toString(budget));
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (budget < sum) {
            totalTextView.setTextColor(Color.RED);
        } else {
            totalTextView.setTextColor(Color.GREEN);
        }

    }

    private void setMonthView() {

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        value = intent.getStringExtra("value");
        int position = intent.getIntExtra("position", 0);

        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList<String> totalDaysInMonth = totalDaysInMonthArray(selectedDate, date, value, position);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, totalDaysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        totalTextView.setText("" + calculateMonthlySum());
        monthlySum();
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
        DBHandler dbHandler = new DBHandler(this, null, null, 5);

        totalDaysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int emptyCells = 0;
        //ελέγχεται για κάθε θέση αν αντιστοιχεί σε μια ημέρα του τρέχοντος μήνα
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                totalDaysInMonthArray.add(null);
                emptyCells = i;
            } else {
                //διαλέγει τον μηνα που θα εμφανισει συμφωνα με την σημερινη ημερομηνια
                //διαλέγει την ημέρα (position) του μηνα που θα εμφανίσει
                String key = (i - emptyCells) + " " + monthYearFromDate(selectedDate);
                DayValue dayValue = dbHandler.findDay(key);

                if (dayValue != null) {
                    String dbValue = dayValue.getValue();
                    String dbDate = dayValue.getDay();
                    totalDaysInMonthArray.add(dbValue);
                } else {
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
    //@Override
    public void onItemClick(int position, String dayText) {
        Intent intent = new Intent(this, CalculateActivity.class);
        String date = dayText + " " + monthYearFromDate(selectedDate);
        intent.putExtra("buttonText", date);
        intent.putExtra("position", position);
        intent.putExtra("month", monthYearFromDate(selectedDate));
        startActivity(intent);
    }
    public void MonthExpenses(View view){
        Intent intent = new Intent(this, MonthExpenses.class);
        intent.putExtra("month", monthYearFromDate(selectedDate));
        startActivity(intent);
    }

    public void ChangeBudgetActivity(View view){
        Intent intent = new Intent(this, ChangeBudgetActivity.class);
        intent.putExtra("budgetNow", budgetButton.getText().toString());
        startActivity(intent);
    }
    private void saveBudgetValue(int budget) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("budget", budget);
        editor.apply();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveBudgetValue(budget);
    }
}