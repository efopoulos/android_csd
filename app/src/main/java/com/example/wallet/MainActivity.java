package com.example.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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


    //save the calculator's result
    //button_id = Monday button
    //totalMondayTextView = result of Calculator
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Intent intent = getIntent();

        if(intent != null) {
            //λαμβάνουμε το όνομα της ημέρας για την οποία εκτελέστηκε η SecondAtivity
            String day=intent.getStringExtra("day");
            if(day!=null){
                if(day.equals("Monday")){
                String totalMonday = intent.getStringExtra("total");
                totalMondayTextView.setText(totalMonday);
                } else if (day.equals("Tuesday")) {
                    String totalTuesday = intent.getStringExtra("total");
                    totalTuesdayTextView.setText(totalTuesday);
                } else if (day.equals("Wednesday")) {
                    String totalWednesday = intent.getStringExtra("total");
                    totalWednesdayTextView.setText(totalWednesday);
                }else if (day.equals("Thursday")) {
                    String totalThursday = intent.getStringExtra("total");
                    totalThursdayTextView.setText(totalThursday);
                }else if (day.equals("Friday")) {
                    String totalFriday = intent.getStringExtra("total");
                    totalFridayTextView.setText(totalFriday);
                }else if (day.equals("Saturday")) {
                    String totalSaturday = intent.getStringExtra("total");
                    totalSaturdayTextView.setText(totalSaturday);
                }else{
                    String totalSunday = intent.getStringExtra("total");
                    totalSundayTextView.setText(totalSunday);
                }
            }
        }

    }

    //Συνάρτηση που μας μεταφέρει στο SecondActivity
    public void Purchase(View view) {
        //takes a button as argument
        Button button = (Button)view;
        //Toast.makeText(this, buttonText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("buttonText", button.getText());
        startActivity(intent);
    }

}