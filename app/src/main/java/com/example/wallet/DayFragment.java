package com.example.wallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DayFragment extends Fragment {
    //αποθήκευση τιμής ημέρας και συνολου
    private String day;
    private String total;
    private static String supermarket;
    private static String entertainment;
    private static String home;
    private TextView dayTextView;
    private TextView totalTextView;
    private TextView supermarketTextView;
    private TextView entertainmentTextView;
    private TextView homeTextView;

    //κλειδια αποθήκευσης και ανάκτησης δεδομένων από Bundle
    private static final String DAY = "day";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";

    public DayFragment(){
    }

    //δημιουργεί και επιστρέφει ένα νέο αντικείμενο
    //DayFragment με τις παρεχόμενες
    //τιμές ημέρας και συνολικού ποσού ως ορίσματα
    public static DayFragment newInstance(String day, String supermarket,String entertainment,String home, String total) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(DAY, day);
        args.putString(SUPERMARKET, supermarket);
        args.putString(ENTERTAINMENT, entertainment);
        args.putString(HOME, home);
        args.putString(TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    // ανακτά αρχικές τιμές της ημέρας και συνόλου απο Bundle
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            day = getArguments().getString(DAY);
            total = getArguments().getString(TOTAL);
            supermarket = getArguments().getString(SUPERMARKET);
            entertainment = getArguments().getString(ENTERTAINMENT);
            home = getArguments().getString(HOME);

        }
    }

    //Καλείται κατα την δημιουργία
    //ανακτά τις τιμές ημερας - ποσου από Bundle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        dayTextView = view.findViewById(R.id.month_id);
        totalTextView = view.findViewById(R.id.total_value);
        supermarketTextView = view.findViewById(R.id.supermarket_value);
        entertainmentTextView = view.findViewById(R.id.entertainment_value);
        homeTextView = view.findViewById(R.id.home_value);
        // Ανάκτηση των πληροφοριών από το Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String dayValue = bundle.getString(DAY);
            String totalValue = bundle.getString(TOTAL);
            String supermarketValue = bundle.getString(SUPERMARKET);
            String entertainmentValue = bundle.getString(ENTERTAINMENT);
            String homeValue = bundle.getString(HOME);

            // Εμφάνιση των πληροφοριών στα TextView
            dayTextView.setText(dayValue);
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