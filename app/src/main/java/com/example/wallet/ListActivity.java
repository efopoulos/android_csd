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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    TextView dayTextView;
    TextView supermarketTextView;
    TextView entertainmentTextView;
    TextView homeTextView;
    TextView totalTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dayTextView = findViewById(R.id.day_id);
        supermarketTextView = findViewById(R.id.supermarket_value);
        entertainmentTextView = findViewById(R.id.entertainment_value);
        homeTextView = findViewById(R.id.home_value);
        totalTextView = findViewById(R.id.total_value);

        Intent intent = getIntent();

        String day = intent.getStringExtra("buttonText");
        dayTextView.setText(day);

        DBHandler dbHelper = new DBHandler(this, null, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VALUES, null);

        if (cursor.moveToFirst()) {
            do {
                String days = cursor.getString(cursor.getColumnIndex(COLUMN_DAYS));
                String value = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                String supermarket = cursor.getString(cursor.getColumnIndex(COLUMN_SUPERMARKET));
                String entertainment = cursor.getString(cursor.getColumnIndex(COLUMN_ENTERTAINMENT));
                String home = cursor.getString(cursor.getColumnIndex(COLUMN_HOME));

                if(days.equals(day)) {
                    totalTextView.setText(value);
                    supermarketTextView.setText(supermarket);
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
    }
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("totalExpenses", totalTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
