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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListActivity extends CalculateActivity {
    TextView dayTextView;
    TextView supermarketTextView;
    TextView entertainmentTextView;
    TextView homeTextView;
    TextView totalTextView;
    DayFragment dayFragment = new DayFragment();
    MonthFragment monthFragment = new MonthFragment();
    private Button dayExpenses;
    private Button monthExpenses;
    private boolean buttonId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager finalFragmentManager = fragmentManager;

        dayExpenses = findViewById(R.id.day_expenses);
        monthExpenses = findViewById(R.id.month_expenses);

        dayTextView = findViewById(R.id.month_id);
        supermarketTextView = findViewById(R.id.supermarket_value);
        entertainmentTextView = findViewById(R.id.entertainment_value);
        homeTextView = findViewById(R.id.home_value);
        totalTextView = findViewById(R.id.total_value);


        dayExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalFragmentManager.beginTransaction();

                finalFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, DayFragment.class, null)
                        .setReorderingAllowed(true).addToBackStack("name").commit();

            }
        });


        Intent intent = getIntent();
        String day = intent.getStringExtra("day");
        String dateText = day;
        String month = "";
        String monthName = null;
        //παίρνουμε τον μήνα
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            Date date = dateFormat.parse(dateText);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            month = String.valueOf(calendar.get(Calendar.MONTH)); // Αντλούμε τον μήνα
            int year = calendar.get(Calendar.YEAR);
            // Εμφάνιση του μήνα
            DateFormatSymbols symbols = new DateFormatSymbols();
            String[] monthNames = symbols.getMonths();
            monthName = monthNames[Integer.parseInt(month)];
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, dayFragment)
                .addToBackStack(null)
                .commit();

        //αφαιρούμε τον αριθμό της ημέρας, μας απασχολεί μόνο μήνας-χρόνος
        String MainDay = day;
        String DayWithoutFirstCharacter = MainDay.substring(2);

        DBHandler dbHelper = new DBHandler(this, null, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);

        int monthlySupermarket= 0;
        int monthlyEntertainment= 0;
        int monthlyHome= 0;
        int monthlyTotal= 0;
        String days;
        String value;
        String supermarket;
        String entertainment;
        String home;

        String thatValue = null;
        String thatSupermarket = null;
        String thatEntertainment = null;
        String thatHome = null;

        if (cursor.moveToFirst()) {
            do {
                days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                supermarket = cursor.getString(cursor.getColumnIndex(COLUMN_SUPERMARKET));
                entertainment = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERTAINMENT));
                home = cursor.getString(cursor.getColumnIndex(COLUMN_HOME));

                //αφαιρούμε τον αριθμό που δείχνει την ημέρα από την ημέρα που πήραμε από την βάση
                String DBDay = days;
                String DBDayWithoutFirstCharacter = DBDay.substring(2);

                //αφαιρούμε τα κενά
                DayWithoutFirstCharacter = DayWithoutFirstCharacter.trim();
                DBDayWithoutFirstCharacter = DBDayWithoutFirstCharacter.trim();

                if (DayWithoutFirstCharacter.equals(DBDayWithoutFirstCharacter)) {
                    monthlySupermarket = monthlySupermarket + Integer.parseInt(supermarket);
                    monthlyEntertainment = monthlyEntertainment + Integer.parseInt(entertainment);
                    monthlyHome = monthlyHome + Integer.parseInt(home);
                    monthlyTotal = monthlyTotal + Integer.parseInt(value);
                }
                if (days.equals(day)) {
                    //κρατάμε σε περιτωση επανεμφάνισης
                    thatValue = value;
                    thatSupermarket = supermarket;
                    thatHome = home;
                    thatEntertainment = entertainment;
                    displayDayFragment(day, value, supermarket, entertainment, home);
                }
            } while (cursor.moveToNext());

            Log.d("Telos", String.valueOf(monthlySupermarket));
            Log.d("Telos", String.valueOf(monthlyEntertainment));
            Log.d("Telos", String.valueOf(monthlyHome));
            Log.d("Telos", String.valueOf(monthlyTotal));

            //προαπαιτούμενο για την υλοποιηση της displayMonthFragment()
            int finalMonthlyTotal = monthlyTotal;
            String finalMonthName = monthName;
            int finalMonthlySupermarket = monthlySupermarket;
            int finalMonthlyEntertainment = monthlyEntertainment;
            int finalMonthlyHome = monthlyHome;

            //πατώντας το κουμπί για τους μήνες αλλάζει το fragment
            monthExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new MonthFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    displayMonthFragment(finalMonthName, finalMonthlyTotal, finalMonthlySupermarket, finalMonthlyEntertainment, finalMonthlyHome);
                }
            });

            //προαπαιτούμενο για την υλοποιηση της displayDayFragment()
            String finalEntertainment = thatEntertainment;
            String finalSupermarket = thatSupermarket;
            String finalHome = thatHome;
            String finalValue = thatValue;

            //πατώντας το κουμπί για τις μερες (εναλλαγή από το monthFrangment) εμφανίζει πάλι τις μέρες
            dayExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new DayFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    displayDayFragment(day, finalSupermarket, finalEntertainment, finalHome, finalValue);
                }
            });

            cursor.close();
            db.close();
        }
    }

    //κλάση που εμφανίζει το DayFragment
    private void displayDayFragment(String day, String value, String supermarket, String entertainment, String home) {
        Bundle bundle = new Bundle();
        bundle.putString("day", day);
        bundle.putString("total", value);
        bundle.putString("supermarket", supermarket);
        bundle.putString("entertainment", entertainment);
        bundle.putString("home", home);
        dayFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, dayFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //κλάση που εμφανίζει το MonthFragment
    private void displayMonthFragment(String month, int value, int supermarket, int entertainment, int home) {
        Bundle bundle = new Bundle();
        bundle.putString("month", month);
        bundle.putInt("total", value);
        bundle.putInt("supermarket", supermarket);
        bundle.putInt("entertainment", entertainment);
        bundle.putInt("home", home);
        monthFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, monthFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    /*public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Εδώ ορίστε το Activity προς το οποίο θέλετε να μεταβείτε.
            Intent intent = new Intent(this, CalculateActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("totalExpenses", dayFragment.toString());
        super.onSaveInstanceState(outState);
    }
}