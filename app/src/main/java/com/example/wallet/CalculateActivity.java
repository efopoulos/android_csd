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

    TotalExpensesFragment totalExpensesFragment = new TotalExpensesFragment();
    CalculatorDayFragment calculatorDayFragment = new CalculatorDayFragment();


    //============
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        dayValue = new DayValue();
        priceEditText = findViewById(R.id.price_edit_text);
        dayTextView = findViewById(R.id.day_textView);
        category_spinner = findViewById(R.id.category_spinner);

        Intent intent = getIntent();

        day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);
        dayTextView.setText(day);
//====================================//
        int monthlySupermarket= 0;
        int monthlyEntertainment= 0;
        int monthlyHome= 0;
        int monthlyTotal= 0;

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
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }

        setupViewPager();
        //totalExpensesFragment.setData(day, total, monthlySupermarket, monthlyEntertainment, monthlyHome);
        calculatorDayFragment.setData(day);


    }
    public void Calculate(View view) {
        String total = priceEditText.getText().toString();
        //κατηγορία που επιλέχθηκε
        String selectedCategory = category_spinner.getSelectedItem().toString();

        DBHandler dbHandler = new DBHandler(this, null, null, 4);

        if (selectedCategory.equals("Supermarket")) {
            dayValue.setSupermarket(total);
        } else if (selectedCategory.equals("Entertainment")) {
            dayValue.setEntertainment(total);
        }else{
            dayValue.setHome(total);
        }
        dayValue.setDay(dayTextView.getText().toString());
        dayValue.setValue(total);
        dayValue.setCategory(selectedCategory);

        if (!total.equals("") && !dayTextView.getText().equals("")){
            dbHandler.addNewValue(dayValue);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("date", dayTextView.getText());
        intent.putExtra("value", total);
        intent.putExtra("position" ,position);
        startActivity(intent);
    }

    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(totalExpensesFragment, "TotalExpenses");
        vpAdapter.addFragment(calculatorDayFragment, "Statistics");
        viewPager.setAdapter(vpAdapter);
    }

}