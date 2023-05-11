package com.example.wallet;

import static com.example.wallet.DBHandler.COLUMN_DAYS;
import static com.example.wallet.DBHandler.COLUMN_VALUE;
import static com.example.wallet.DBHandler.TABLE_VALUES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WeekActivity extends AppCompatActivity {

    Button purchaseButtonMonday;
    Button purchaseButtonTuesday;
    Button purchaseButtonWednesday;
    Button purchaseButtonThursday;
    Button purchaseButtonFriday;
    Button purchaseButtonSaturday;
    Button purchaseButtonSunday;
    TextView totalMondayTextView;
    TextView totalTuesdayTextView;
    TextView totalWednesdayTextView;
    TextView totalThursdayTextView;
    TextView totalFridayTextView;
    TextView totalSaturdayTextView;
    TextView totalSundayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //εντολές για ευρεση των αντίστοιχων Buttons
        purchaseButtonMonday = findViewById(R.id.Monday_button);
        //εντολές για ευρεση του κειμένου (για εμφάνιση αποτελέσματος)
        totalMondayTextView = findViewById(R.id.total_Monday);

        purchaseButtonTuesday = findViewById(R.id.Tuesday_button);
        totalTuesdayTextView = findViewById(R.id.total_Tuesday);

        purchaseButtonWednesday = findViewById(R.id.Wednesday_button);
        totalWednesdayTextView = findViewById(R.id.total_Wednesday);

        purchaseButtonThursday = findViewById(R.id.Thursday_button);
        totalThursdayTextView = findViewById(R.id.total_Thursday);

        purchaseButtonFriday = findViewById(R.id.Friday_button);
        totalFridayTextView = findViewById(R.id.total_Friday);

        purchaseButtonSaturday = findViewById(R.id.Saturday_button);
        totalSaturdayTextView = findViewById(R.id.total_Saturday);

        purchaseButtonSunday = findViewById(R.id.Sunday_button);
        totalSundayTextView = findViewById(R.id.total_Sunday);

        DBHandler dbHelper = new DBHandler(this, null, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);
        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                switch(days) {
                    case "Monday":
                        totalMondayTextView.setText(value);
                        break;
                    case "Tuesday":
                        totalTuesdayTextView.setText(value);
                        break;
                    case "Wednesday":
                        totalWednesdayTextView.setText(value);
                        break;
                    case "Thursday":
                        totalThursdayTextView.setText(value);
                        break;
                    case "Friday":
                        totalFridayTextView.setText(value);
                        break;
                    case "Saturday":
                        totalSaturdayTextView.setText(value);
                        break;
                    case "Sunday":
                        totalSundayTextView.setText(value);
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    //Συνάρτηση που μας μεταφέρει στο SecondActivity
    public void Purchase(View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        Button button = (Button)view;
        String day = (String) button.getText();
        //Toast.makeText(this, buttonText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CalculateActivity.class);
        intent.putExtra("buttonText", button.getText());
        startActivity(intent);
    }
}