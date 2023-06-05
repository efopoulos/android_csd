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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class CalculateActivity extends AppCompatActivity {
    //εισαγωγή τιμής
    EditText priceEditText;
    //αυτό που δείχνει την μέρα
    TextView dayTextView;
    DayValue dayValue;
    Spinner category_spinner;
    String day;
    private int position;
    //============
    private TabLayout tabLayout;
    private ViewPager viewPager;

    CalculatorDayFragment calculatorDayFragment = new CalculatorDayFragment();
    TotalExpensesFragment totalExpensesFragment = new TotalExpensesFragment();


    //============
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        tabLayout=findViewById(R.id.tablelayout_calculator);
        viewPager=findViewById(R.id.viewpager_calculator);

        Intent intent = getIntent();

        day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);
        //dayTextView.setText(day);
//====================================//

        int dayTotal = 0;
        int daySupermarket= 0;
        int dayEntertainment= 0;
        int dayHome= 0;

        DBHandler dbHelper = new DBHandler(this, null, null, 1);
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
                            Integer.parseInt(home)


                    );
                    dayTotal = Integer.parseInt(total);
                    daySupermarket = Integer.parseInt(supermarket);
                    dayEntertainment = Integer.parseInt(entertainment);
                    dayHome = Integer.parseInt(home);
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        setupViewPager();
        calculatorDayFragment.setData(day);
        totalExpensesFragment.setData(day, dayTotal, daySupermarket, dayEntertainment, dayHome);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    }


    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(calculatorDayFragment, "Calculator");
        vpAdapter.addFragment(totalExpensesFragment, "Day Expenses");
        viewPager.setAdapter(vpAdapter);
    }


}