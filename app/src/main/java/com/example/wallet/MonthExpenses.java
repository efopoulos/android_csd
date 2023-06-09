package com.example.wallet;

import static com.example.wallet.DBHandler.COLUMN_DAYS;
import static com.example.wallet.DBHandler.COLUMN_ENTERTAINMENT;
import static com.example.wallet.DBHandler.COLUMN_HOME;
import static com.example.wallet.DBHandler.COLUMN_OTHER;
import static com.example.wallet.DBHandler.COLUMN_SUPERMARKET;
import static com.example.wallet.DBHandler.COLUMN_TRANSPORTATION;
import static com.example.wallet.DBHandler.COLUMN_VALUE;
import static com.example.wallet.DBHandler.TABLE_VALUES;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
/*
Δραστηριότητα για εμφάνιση των μηνιαίων εξόδων και στατιστικών πληροφοριών
 */
public class MonthExpenses extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    BottomNavigationView bottomNavigationView;
    TotalExpensesFragment totalExpensesFragment = new TotalExpensesFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();

    //Ανάκτηση των μηνιαίων εξόδων από τη βάση
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        getSupportActionBar().hide();

        tabLayout=findViewById(R.id.tablelayout);
        viewPager=findViewById(R.id.viewpager);

        Intent intent = getIntent();
        String month = intent.getStringExtra("month");

        int monthlySupermarket= 0;
        int monthlyEntertainment= 0;
        int monthlyHome= 0;
        int monthlyTotal= 0;
        int monthlyTransportation = 0;
        int monthlyOther = 0;
        String MainMonth = month;
        DBHandler dbHelper = new DBHandler(this, null, null, 5);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);
        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                String supermarket = cursor.getString(cursor.getColumnIndex(COLUMN_SUPERMARKET));
                String entertainment = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERTAINMENT));
                String home = cursor.getString(cursor.getColumnIndex(COLUMN_HOME));
                String transportation = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSPORTATION));
                String other = cursor.getString(cursor.getColumnIndex(COLUMN_OTHER));

                String DBDay = days;
                String DBDayWithoutFirstCharacter = DBDay.substring(2);
                DBDayWithoutFirstCharacter = DBDayWithoutFirstCharacter.trim();
                MainMonth = MainMonth.trim();
                if (DBDayWithoutFirstCharacter.equals(MainMonth)) {
                    monthlySupermarket = monthlySupermarket + Integer.parseInt(supermarket);
                    monthlyEntertainment = monthlyEntertainment + Integer.parseInt(entertainment);
                    monthlyHome = monthlyHome + Integer.parseInt(home);
                    monthlyTransportation = monthlyTransportation + Integer.parseInt(transportation);
                    monthlyOther = monthlyOther + Integer.parseInt(other);
                    monthlyTotal = monthlyTotal + Integer.parseInt(value);
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        setupViewPager();
        totalExpensesFragment.setData(month, monthlyTotal, monthlySupermarket, monthlyEntertainment, monthlyHome, monthlyTransportation, monthlyOther, false);
        statisticsFragment.setData(month, monthlyTotal, monthlySupermarket, monthlyEntertainment, monthlyHome, monthlyTransportation, monthlyOther);

        //Ενημέρωση του UI ανάλογα με την επιλεγμένη καρτέλα
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.viewpager, totalExpensesFragment)
                            .commit();

                } else if (tab.getPosition() == 1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.viewpager, statisticsFragment)
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


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_month_expenses);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        Intent mainIntent = new Intent(MonthExpenses.this, MainActivity.class);
                        startActivity(mainIntent);
                        return true;
                    case R.id.action_month_expenses:
                        Intent monthExpensesIntent = new Intent(MonthExpenses.this, MonthExpenses.class);
                        monthExpensesIntent.putExtra("month", month);
                        startActivity(monthExpensesIntent);
                        return true;
                    case R.id.action_commitment:
                        Intent commitmentIntent = new Intent(MonthExpenses.this, CommitmentActivity.class);
                        commitmentIntent.putExtra("month", month);
                        startActivity(commitmentIntent);
                        return true;
                }
                return false;
            }
        });

    }

    //Δημιουργία και ρύθμιση του ViewPager και του Adapter
    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(totalExpensesFragment, "TotalExpenses");
        vpAdapter.addFragment(statisticsFragment, "Statistics");
        viewPager.setAdapter(vpAdapter);
    }


}