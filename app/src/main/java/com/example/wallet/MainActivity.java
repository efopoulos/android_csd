package com.example.wallet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    
    Button purchaseButton;
    TextView totalMondayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        purchaseButton = findViewById(R.id.button_id);
        totalMondayTextView = findViewById(R.id.total_moday_textView);

        Intent intent = getIntent();
        if(intent != null) {
            String totalMonday = intent.getStringExtra("totalMonday");
            totalMondayTextView.setText(totalMonday);
        }
    }

    public void Purchase(View view) {
        Button button = (Button)view;
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("buttonText", button.getText());
        startActivity(intent);
    }

}