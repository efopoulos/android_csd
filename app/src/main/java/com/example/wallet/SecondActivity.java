package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    EditText priceEditText;
    TextView productsTextView;
    TextView totalTextView;
    TextView dayTextView;
    TextView selectedCategoryTextView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button isClickedButton;
    String total;

    ArrayList<String> products = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        productsTextView = findViewById(R.id.products);
        totalTextView = findViewById(R.id.total);
        dayTextView = findViewById(R.id.day_textView);
        priceEditText = findViewById(R.id.priceEditText);
        selectedCategoryTextView = findViewById(R.id.catTextView);


        if(savedInstanceState != null) {
            String totalExpenses = savedInstanceState.getString("total");
            totalTextView.setText(totalExpenses);
        }

        Intent intent = getIntent();
        String day = intent.getStringExtra("buttonText");
        String total = intent.getStringExtra("total");
        totalTextView.setText(total);
        dayTextView.setText(day);
//        productsTextView.setText(products.get);


        button1 = findViewById(R.id.cat_1);
        button2 = findViewById(R.id.cat_2);
        button3 = findViewById(R.id.cat_3);
        button4 = findViewById(R.id.cat_4);
        button5 = findViewById(R.id.cat_5);
        button6 = findViewById(R.id.cat_6);
        button7 = findViewById(R.id.cat_7);
        button8 = findViewById(R.id.cat_8);
        button9 = findViewById(R.id.cat_9);
    }

    public void Calculate(View view) {
        int newPrice = Integer.parseInt(priceEditText.getText().toString());

        int prevPrice;
        if(totalTextView.getText() != ""){
            Toast.makeText(this, totalTextView.getText(), Toast.LENGTH_SHORT).show();
            prevPrice = Integer.parseInt(totalTextView.getText().toString());
        }else{
            prevPrice = 0;
        }
        total = ""+(newPrice+prevPrice);
        totalTextView.setText(total);

        String prevproduct;
        if(totalTextView.getText() != null){
            prevproduct = productsTextView.getText().toString();
        }else{
            prevproduct = "";
        }

        String newProduct = isClickedButton.getText().toString();

        String products = prevproduct + "\n" + newProduct;
        productsTextView.setText(products);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String string = totalTextView.getText().toString();
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        outState.putString("total", string);
        super.onSaveInstanceState(outState);
    }

    public void cat1Button(View view) {
        isClickedButton = (Button) view;
        selectedCategoryTextView.setText(isClickedButton.getText().toString());
    }

    public void Save(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("totalMonday", total);
        startActivity(intent);
    }
}