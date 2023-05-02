package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button purchaseButton;
    TextView totalMondayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        purchaseButton = findViewById(R.id.mondayButton);
        totalMondayTextView = findViewById(R.id.mondayTotal);

        Intent intent = getIntent();
        if( intent.getStringExtra("totalMonday") != null) {
            String totalMonday = intent.getStringExtra("totalMonday");
            totalMondayTextView.setText(totalMonday);
        }
    }
    public void Purchase(View view) {
        Button button = (Button)view;
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("buttonText", button.getText());
        intent.putExtra("total", totalMondayTextView.getText());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("total", (String) totalMondayTextView.getText());
        super.onSaveInstanceState(outState);
    }
}