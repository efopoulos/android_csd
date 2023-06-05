package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class CalculatorDayFragment extends Fragment {

    //εισαγωγή τιμής
    EditText priceEditText;
    //αυτό που δείχνει την μέρα
    TextView dayTextView;
    DayValue dayValue;
    Spinner category_spinner;
    String day;
    private int position;
    public CalculatorDayFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dayValue = new DayValue();
        priceEditText = findViewById(R.id.price_edit_text);
        dayTextView = findViewById(R.id.day_textView);
        category_spinner = findViewById(R.id.category_spinner);

        Intent intent = getIntent();

        day = intent.getStringExtra("buttonText");
        position = intent.getIntExtra("position",0);
        dayTextView.setText(day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator_day, container, false);
    }

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

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("date", dayTextView.getText());
        intent.putExtra("value", total);
        intent.putExtra("position" ,position);
        startActivity(intent);
    }

    public void setData(String day) {
        this.day = day;

    }

}