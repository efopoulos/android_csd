package com.example.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TotalExpensesFragment extends Fragment {
    private TextView monthTextView;
    private TextView totalTextView;
    private TextView supermarketTextView;
    private TextView entertainmentTextView;
    private TextView homeTextView;

    private String month;
    private int total;
    private int supermarket;
    private int entertainment;
    private int home;

    private static final String MONTH = "month";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";

    public TotalExpensesFragment(){
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            String monthValue = bundle.getString(MONTH);
            int totalValue = bundle.getInt(TOTAL);
            int supermarketValue = bundle.getInt(SUPERMARKET);
            int entertainmentValue = bundle.getInt(ENTERTAINMENT);
            int homeValue = bundle.getInt(HOME);

            monthTextView.setText(monthValue);
            totalTextView.setText(String.valueOf(totalValue));
            supermarketTextView.setText(String.valueOf(supermarketValue));
            entertainmentTextView.setText(String.valueOf(entertainmentValue));
            homeTextView.setText(String.valueOf(homeValue));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_expenses, container, false);

        monthTextView = view.findViewById(R.id.month_id);
        totalTextView = view.findViewById(R.id.monthtotal_value);
        supermarketTextView = view.findViewById(R.id.monthsupermarket_value);
        entertainmentTextView = view.findViewById(R.id.monthentertainment_value);
        homeTextView = view.findViewById(R.id.monthhome_value);

        monthTextView.setText(month);
        totalTextView.setText(String.valueOf(total));
        supermarketTextView.setText(String.valueOf(supermarket));
        entertainmentTextView.setText(String.valueOf(entertainment));
        homeTextView.setText(String.valueOf(home));

        return view;
    }

    public void setData(String month, int total, int supermarket, int entertainment, int home) {
        this.month = month;
        this.total = total;
        this.supermarket = supermarket;
        this.entertainment = entertainment;
        this.home = home;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}