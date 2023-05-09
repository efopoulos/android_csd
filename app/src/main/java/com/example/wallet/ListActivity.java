package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class List extends AppCompatActivity {
    TextView dayTextView;
    TextView supermarketTextView;
    TextView entertainmentTextView;
    TextView homeTextView;
    TextView totalTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dayTextView = findViewById(R.id.day_id);
        supermarketTextView = findViewById(R.id.supermarket_id);
        entertainmentTextView = findViewById(R.id.entertainment_id);
        homeTextView = findViewById(R.id.home_id);
        totalTextView = findViewById(R.id.total_id);

        Intent intent = getIntent();
        String day = intent.getStringExtra("buttonText");
        dayTextView.setText(day);
    }
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("totalExpenses", totalTextView.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
