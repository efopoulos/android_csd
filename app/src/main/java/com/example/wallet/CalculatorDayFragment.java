package com.example.wallet;

import static android.icu.text.DateFormat.DAY;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
Fragment για την καταχώρηση ημερίσιων δαπανών
 */
public class CalculatorDayFragment extends Fragment {
    EditText priceEditText;
    TextView dayTextView;
    DayValue dayValue;
    Spinner category_spinner;
    String day;
    private int position;

    public CalculatorDayFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            String dayValue = bundle.getString(DAY);
            dayTextView.setText(dayValue);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator_day, container, false);
        priceEditText = view.findViewById(R.id.price_edit_text);
        dayTextView = view.findViewById(R.id.day_textView);
        category_spinner = view.findViewById(R.id.category_spinner);
        Button calculateButton = view.findViewById(R.id.calculate_button);

        dayValue = new DayValue();
        dayTextView.setText(day);
       calculateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(priceEditText.getText().toString().trim().equals(""))
               {
                   Toast.makeText(getContext(), "Enter value", Toast.LENGTH_SHORT).show();
               }else {
                   String total = priceEditText.getText().toString();
                   String selectedCategory = category_spinner.getSelectedItem().toString();

                   DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);

                   //Ελέγχος για την μη εισαγηγή αρνητικής τιμής
                   if (Integer.parseInt(total) > 0 && total.matches("\\d+") ) {
                       //Ελέγχος κατηγορίας που επιλέχθηκε
                       if (selectedCategory.equals("Supermarket")) {
                           dayValue.setSupermarket(total);
                       } else if (selectedCategory.equals("Entertainment")) {
                           dayValue.setEntertainment(total);
                       } else if (selectedCategory.equals("Home")) {
                           dayValue.setHome(total);
                       } else if (selectedCategory.equals("Transportation")) {
                           dayValue.setTransportation(total);
                       } else {
                           dayValue.setOther(total);
                       }
                       dayValue.setDay(dayTextView.getText().toString());
                       dayValue.setValue(total);
                       dayValue.setCategory(selectedCategory);

                       if (!total.equals("") && !dayTextView.getText().equals("")) {
                           dbHandler.addNewValue(dayValue);
                       } else {
                           priceEditText.setText("");
                           Toast.makeText(getContext(), "Please enter valid values", Toast.LENGTH_SHORT).show();
                       }
                   }


                   //Μετάβαση στην MainActivity
                   Intent intent = new Intent(getActivity(), MainActivity.class);
                   intent.putExtra("date", dayTextView.getText().toString());
                   intent.putExtra("value", total);
                   intent.putExtra("position", position);
                   startActivity(intent);
               }
           }
       });

        return view;
    }

    //Κλάση που καλέιται από την CalculateActivity για ορισμό της ημέρας
    public void setData(String day) {
        this.day = day;
    }

}