package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends AppCompatActivity {

    EditText productEditText;
    EditText priceEditText;
    TextView productsTextView;
    TextView totalTextView;
    TextView dayTextView;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        productEditText = findViewById(R.id.product_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        productsTextView = findViewById(R.id.products);
        totalTextView = findViewById(R.id.total);
        dayTextView = findViewById(R.id.day_textView);

        Intent intent = getIntent();
        if(savedInstanceState != null) {
            Log.d("paok", " MPHKE ");
            String totalExpenses = savedInstanceState.getString("totalExpenses");
            totalTextView.setText(totalExpenses);
        }else{
            Log.d("paok", "DE MPHKE ");
        }

        String day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);
//        Toast.makeText(this, " "+position, Toast.LENGTH_SHORT).show();
        dayTextView.setText(day);
    }

    public void Calculate(View view) {
        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        int prevPrice = Integer.parseInt(totalTextView.getText().toString());
        String total = ""+(newPrice+prevPrice);
        totalTextView.setText(total);

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        String newProduct = productEditText.getText().toString();
        String prevproduct = productsTextView.getText().toString();
        String products = prevproduct + "\n" + newProduct;
        productsTextView.setText(products);

        if (!total.equals("") && !dayTextView.getText().equals("")){
            DayValue expenses = new DayValue(dayTextView.getText(), total);
            dbHandler.addNewValue(expenses);
        }

        //Μέσω intent στέλνουμε στην Main το συνολικό ποσό και σε ποια μέρα βρισκόμαστε
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("value", total);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("totalExpenses", totalTextView.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

}