package com.example.wallet;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthFragment extends Fragment {
    //αποθήκευση τιμής ημέρας και συνολου
    private String month;
    private int total;
    private static int supermarket;
    private static int entertainment;
    private static int home;
    private TextView monthTextView;
    private TextView totalTextView;
    private TextView supermarketTextView;
    private TextView entertainmentTextView;
    private TextView homeTextView;

    //κλειδια αποθήκευσης και ανάκτησης δεδομένων από Bundle
    private static final String MONTH = "month";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";

    public MonthFragment(){
    }

    //δημιουργεί και επιστρέφει ένα νέο αντικείμενο
    //monthFragment με τις παρεχόμενες
    //τιμές ημέρας και συνολικού ποσού ως ορίσματα
    public static MonthFragment newInstance(String month, int total, int supermarket,int entertainment,int home) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putString(MONTH, month);
        args.putInt(TOTAL, total);
        args.putInt(SUPERMARKET, supermarket);
        args.putInt(ENTERTAINMENT, entertainment);
        args.putInt(HOME, home);
        fragment.setArguments(args);
        return fragment;
    }

    // ανακτά αρχικές τιμές της ημέρας και συνόλου απο Bundle
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            month = getArguments().getString(MONTH);
            total = getArguments().getInt(TOTAL);
            supermarket = getArguments().getInt(SUPERMARKET);
            entertainment = getArguments().getInt(ENTERTAINMENT);
            home = getArguments().getInt(HOME);
        }
    }


    //Καλείται κατα την δημιουργία
    //ανακτά τις τιμές ημερας - ποσου από Bundle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        monthTextView = view.findViewById(R.id.month_id);
        totalTextView = view.findViewById(R.id.total_value);
        supermarketTextView = view.findViewById(R.id.supermarket_value);
        entertainmentTextView = view.findViewById(R.id.entertainment_value);
        homeTextView = view.findViewById(R.id.home_value);

        // Ανάκτηση των πληροφοριών από το Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String monthValue = bundle.getString(MONTH);
            String totalValue = String.valueOf(bundle.getInt(TOTAL));
            String supermarketValue = String.valueOf(bundle.getInt(SUPERMARKET));
            String entertainmentValue = String.valueOf(bundle.getInt(ENTERTAINMENT));
            String homeValue = String.valueOf(bundle.getInt(HOME));


            // Εμφάνιση των πληροφοριών στα TextView
            monthTextView.setText(monthValue);
            totalTextView.setText(totalValue);
            supermarketTextView.setText(supermarketValue);
            entertainmentTextView.setText(entertainmentValue);
            homeTextView.setText(homeValue);

        }

        return view;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Εδώ μπορείτε να τοποθετήσετε τον κώδικα που θέλετε να εκτελείται
            // όταν πατηθεί το βελάκι πίσω.
            // Μπορείτε να κάνετε τη μετάβαση σε ένα συγκεκριμένο Activity χρησιμοποιώντας Intent.

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}