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

/*
Κύρια δραστηριότητα εφαρμογής με βασικές λειτουργίες
 - Εμφάνιση ημερολογίου
 - Υπολογισμός μηνιαίου συνολικού ποσου
 - Αποθηκεύση/Ανάκτηση budget
 */
public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
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
        //έλεγχος ύπαρξης προηγούμενης κατάστασης και ανάκτηση προϋπολογισμού
        if(savedInstanceState != null) {
            budget = savedInstanceState.getInt("budget");
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        totalTextView = findViewById(R.id.total_textView);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        budgetButton = findViewById(R.id.budget_button);
        selectedDate = LocalDate.now();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        budget = sharedPreferences.getInt("budget", 0);

        setMonthView();
        monthlySum();

        //Ενέργειες για τη δημιουργία και τον ορισμό των επιλογών της Bottom Navigation View
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

    //Υπολογιμός συνολικών εξόδων (total) για τον μήνα
    private int calculateMonthlySum(){
        int sum = 0;
        for (int i = 0; i < totalDaysInMonthArray.size(); i++) {
            if (totalDaysInMonthArray.get(i) != null) {
                sum += Integer.parseInt(totalDaysInMonthArray.get(i));
            }
        }
        return sum;
    }

    //Ορισμός του κειμένου κουμπιού budgetButton και αλλαγή
    //χρώματος του συνολικού ποσού στην περίπτωση που υπερβεί το budgte
    private void monthlySum() {
        int sum = calculateMonthlySum();

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

    //Ορίζει τις ημερίσιες τιμές για κάθε μέρα ξεχωριστά
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

    //Δημιουργία λίστας με τις μέρες του μήνα
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        //υπολογισμός μέρες του μήνα
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        //Μέρα της εβδομάδας που ξεκινάει ο μήνας
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //Γεμίζει την λίστα με τις μέρες της εδβομάδας
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    //Έλεγχος για κάθε θέση του πίνακα
    private ArrayList<String> totalDaysInMonthArray(LocalDate date, String insertedDate, String value, int position) {
        DBHandler dbHandler = new DBHandler(this, null, null, 5);

        totalDaysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int emptyCells = 0;
        //Ελέγχεται για κάθε θέση αν αντιστοιχεί σε μια ημέρα του τρέχοντος μήνα
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                totalDaysInMonthArray.add(null);
                emptyCells = i;
            } else {
                //Διαλέγει τον μηνα που θα εμφανισει συμφωνα με την σημερινη ημερομηνια
                //Διαλέγει την ημέρα (position) του μηνα που θα εμφανίσει
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

    //Επιστροφή μήνα και έτος
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    //Προηγοημενος μηνας
    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    //Επομενος μηνας
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