package com.example.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText productEditText;
    EditText priceEditText;
    TextView productsTextView;
    TextView totalTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productEditText = findViewById(R.id.product_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        productsTextView = findViewById(R.id.products);
        totalTextView = findViewById(R.id.total);

    }

    public void Calculate(View view) {
        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        int prevPrice = Integer.parseInt(totalTextView.getText().toString());
        String total = ""+(newPrice+prevPrice);
        totalTextView.setText(total);

        String newProduct = productEditText.getText().toString();
        String prevproduct = productsTextView.getText().toString();
        String products = prevproduct + "\n" + newProduct;
        productsTextView.setText(products);
    }
}