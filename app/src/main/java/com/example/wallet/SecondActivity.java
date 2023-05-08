package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    Spinner day_spinner;
    EditText productEditText;
    EditText priceEditText;
    TextView productsTextView;
    TextView totalTextView;
    TextView dayTextView;

    DayValue dayValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        day_spinner=findViewById(R.id.day_spinner);
        dayValue=new DayValue();
        productEditText = findViewById(R.id.product_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        productsTextView = findViewById(R.id.products);
        totalTextView = findViewById(R.id.total);
    }

    public void Calculate(View view) {

        String day = day_spinner.getSelectedItem().toString();

        dayValue.setDay(day);

        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        int prevPrice = Integer.parseInt(totalTextView.getText().toString());
        String total = ""+(newPrice+prevPrice);

        totalTextView.setText(total);
        dayValue.setSupermarket(total);
        dayValue.setValue(total);


        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        String newProduct = productEditText.getText().toString();
        String prevproduct = productsTextView.getText().toString();
        String products = prevproduct + "\n" + newProduct;
        productsTextView.setText(products);

        if (!total.equals("") && !dayValue.getDay().equals("")){
            dbHandler.addNewValue(dayValue);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("total", total);
        intent.putExtra("day", (String) dayTextView.getText());
        intent.putExtra("supermarket", (String) total);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("totalExpenses", totalTextView.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

}