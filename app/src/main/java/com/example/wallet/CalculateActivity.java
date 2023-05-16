package com.example.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CalculateActivity extends AppCompatActivity {
    //εισαγωγή τιμής
    EditText priceEditText;
    //αυτό που δείχνει την μέρα
    TextView dayTextView;
    DayValue dayValue;
    Spinner category_spinner;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        dayValue = new DayValue();
        priceEditText = findViewById(R.id.price_edit_text);
        dayTextView = findViewById(R.id.day_textView);
        category_spinner = findViewById(R.id.category_spinner);

        Intent intent = getIntent();

        String day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);
        dayTextView.setText(day);
    }
    //προσθήκη τιμή δαπάνης στη βαση δεδομένων και επιστροφή της στην κύρια
    //οθόνη
    public void Calculate(View view) {
        String total = priceEditText.getText().toString();
        //κατηγορία που επιλέχθηκε
        String selectedCategory = category_spinner.getSelectedItem().toString();

        DBHandler dbHandler = new DBHandler(this, null, null, 4);

        if (selectedCategory.equals("Supermarket")) {
            dayValue.setSupermarket(total);
        } else if (selectedCategory.equals("Entertainment")) {
            dayValue.setEntertainment(total);
        }else{
            dayValue.setHome(total);
        }
        dayValue.setDay(dayTextView.getText().toString());
        dayValue.setValue(total);
        dayValue.setCategory(selectedCategory);

        if (!total.equals("") && !dayTextView.getText().equals("")){
            dbHandler.addNewValue(dayValue);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("date", dayTextView.getText());
        intent.putExtra("value", total);
        intent.putExtra("position" ,position);
        startActivity(intent);
    }
    public void List(View view){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("day", dayTextView.getText());
        startActivity(intent);
    }

}