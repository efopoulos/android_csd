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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class CalculateActivity extends AppCompatActivity {
    String day;
    private int position;
    BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    CalculatorDayFragment calculatorDayFragment = new CalculatorDayFragment();
    TotalExpensesFragment totalExpensesFragment = new TotalExpensesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        tabLayout=findViewById(R.id.tablelayout_calculator);
        viewPager=findViewById(R.id.viewpager_calculator);


        Intent intent = getIntent();
        String month = intent.getStringExtra("month");
        day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);

        DBHandler dbHelper = new DBHandler(this, null, null, 4);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);

        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String total = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                String supermarket = cursor.getString(cursor.getColumnIndex(COLUMN_SUPERMARKET));
                String entertainment = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERTAINMENT));
                String home = cursor.getString(cursor.getColumnIndex(COLUMN_HOME));
                if(days.equals(day)) {
                    totalExpensesFragment.setData(days,
                            Integer.parseInt(total),
                            Integer.parseInt(supermarket),
                            Integer.parseInt(entertainment),
                            Integer.parseInt(home),
                            true
                    );
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        setupViewPager();
        calculatorDayFragment.setData(day);

        //Ελεγχος και συνονισμός των καρτελών
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //αν η καρτέλα βρίσκεται στη θέση 0 τότε εισάγεται το calculatorDayFragment
            //αν βρίσεκται στη θέση 1 τότε εισάγεται το totalFragment
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.viewpager_calculator, calculatorDayFragment)
                            .commit();

                } else if (tab.getPosition() == 1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.viewpager_calculator, totalExpensesFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });


        //Κώδικας για την δημιουργία του BottomNavigationBar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        Intent mainIntent = new Intent(CalculateActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        return true;
                    case R.id.action_month_expenses:
                        Intent monthExpensesIntent = new Intent(CalculateActivity.this, MonthExpenses.class);
                        monthExpensesIntent.putExtra("month", month);
                        startActivity(monthExpensesIntent);
                        return true;
                    case R.id.action_commitment:
                        Intent commitmentIntent = new Intent(CalculateActivity.this, CommitmentActivity.class);
                        commitmentIntent.putExtra("month", month);
                        startActivity(commitmentIntent);
                        return true;
                }
                return false;
            }
        });

    }


    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(calculatorDayFragment, "Calculator");
        vpAdapter.addFragment(totalExpensesFragment, "Day Expenses");
        viewPager.setAdapter(vpAdapter);
    }


}