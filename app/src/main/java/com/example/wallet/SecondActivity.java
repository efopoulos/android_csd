package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    Spinner day_spinner;
    EditText priceEditText;
    Spinner category_spinner;
    TextView totalTextView;
    DayValue dayValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        totalTextView = findViewById(R.id.total);

        day_spinner = findViewById(R.id.day_spinner);
        dayValue = new DayValue();
        category_spinner = findViewById(R.id.category_spinner);
        priceEditText = findViewById(R.id.price_edit_text);
    }

    public void Calculate(View view) {
        String day = day_spinner.getSelectedItem().toString();
        String selectedCategory = category_spinner.getSelectedItem().toString();
        dayValue.setDay(day);
        dayValue.setCategory(selectedCategory);

        int newPrice = Integer.parseInt(priceEditText.getText().toString());
        int prevPrice = Integer.parseInt(totalTextView.getText().toString());
        String total = ""+(newPrice+prevPrice);

        totalTextView.setText(total);
        if (selectedCategory.equals("Supermarket")) {
            dayValue.setSupermarket(total);
        } else if (selectedCategory.equals("Entertainment")) {
            dayValue.setEntertainment(total);
        }else{
            dayValue.setHome(total);
        }

        dayValue.setValue(total);
        dayValue.setCategory(selectedCategory);
        DBHandler dbHandler = new DBHandler(this, null, null, 3);

        if (!total.equals("") && !dayValue.getDay().equals("")){
            dbHandler.addNewValue(dayValue);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("total", total);
        //intent.putExtra("supermarket", (String) total);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("totalExpenses", totalTextView.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

}