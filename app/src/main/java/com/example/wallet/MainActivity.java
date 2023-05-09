package com.example.wallet;

import static com.example.wallet.DBHandler.COLUMN_DAYS;
import static com.example.wallet.DBHandler.COLUMN_VALUE;
import static com.example.wallet.DBHandler.TABLE_VALUES;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button purchaseButtonMonday;
    Button purchaseButtonTuesday;
    Button purchaseButtonWednesday;
    Button purchaseButtonThursday;
    Button purchaseButtonFriday;
    Button purchaseButtonSaturday;
    Button purchaseButtonSunday;
    Button addNewPurchase;
    DayValue dayValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayValue = new DayValue();

        purchaseButtonMonday = findViewById(R.id.Monday_button);
        purchaseButtonTuesday = findViewById(R.id.Tuesday_button);
        purchaseButtonWednesday = findViewById(R.id.Wednesday_button);
        purchaseButtonThursday = findViewById(R.id.Thursday_button);
        purchaseButtonFriday = findViewById(R.id.Friday_button);
        purchaseButtonSaturday = findViewById(R.id.Saturday_button);
        purchaseButtonSunday = findViewById(R.id.Sunday_button);

        addNewPurchase = findViewById(R.id.add_btn);

        DBHandler dbHelper = new DBHandler(this, null, null, 3);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);

        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));

                dayValue.setDay(days);
                dayValue.setValue(value);
                //dayValue.setSupermarket(value);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    public void List(View view){
        Button button = (Button)view;
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("buttonText", button.getText());
        startActivity(intent);
    }

    public void Purchase(View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 3);
        Button button = (Button)view;
        String day = (String) button.getText();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}