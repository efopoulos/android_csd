package com.example.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.animation.ObjectAnimator;

import androidx.fragment.app.Fragment;

public class TotalExpensesFragment extends Fragment {
    private TextView monthTextView;
    private TextView totalTextView;
    private EditText supermarketEditText;
    private EditText entertainmentEditText;
    private EditText homeEditText;

    private TextView supermarketPercentTextView;
    private TextView entertainmentPercentTextView;
    private TextView homePercentTextView;
    Boolean flag;

    DayValue dayValue;

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
    private Button processSupermarket;
    private Button processEntertainment;
    private Button processHome;


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

        supermarketEditText = view.findViewById(R.id.monthsupermarket_value);
        entertainmentEditText = view.findViewById(R.id.monthentertainment_value);
        homeEditText = view.findViewById(R.id.monthhome_value);

        supermarketPercentTextView = view.findViewById(R.id.supermarket_percent);
        entertainmentPercentTextView = view.findViewById(R.id.entertainment_percent);
        homePercentTextView = view.findViewById(R.id.home_percent);

        supermarketTextView = view.findViewById(R.id.monthsupermarket_value);
        entertainmentTextView = view.findViewById(R.id.monthentertainment_value);
        homeTextView = view.findViewById(R.id.monthhome_value);

        processSupermarket = view.findViewById(R.id.supermarket_process);
        processEntertainment = view.findViewById(R.id.entertainment_process);
        processHome = view.findViewById(R.id.home_process);

        //Ελέγχουμε αν το Fragment χρησιμοποιείται για τα έξοδα του μήνα ή για τα έξοδα της ημέρας
        if (flag) {
            processSupermarket.setVisibility(View.VISIBLE);
            processEntertainment.setVisibility(View.VISIBLE);
            processHome.setVisibility(View.VISIBLE);
            Process(view);
        } else {
            processSupermarket.setVisibility(View.GONE);
            processEntertainment.setVisibility(View.GONE);
            processHome.setVisibility(View.GONE);
        }

        monthTextView.setText(month);
        totalTextView.setText(String.valueOf(total));

        //Καθιστούμε τα EditText UnEdible για τα συνολικά έξοδα του μήνα
        supermarketEditText.setEnabled(false);
        entertainmentEditText.setEnabled(false);
        homeEditText.setEnabled(false);

        //υπολογισμός ποσοστού και δημιουργία processBar
        SupermarketProcess(view);
        EntertainmentProcess(view);
        HomeProcess(view);

        return view;
    }


    public void Process(View view){
        //Δημιουργία νέου αντικειμένου DayValue
        dayValue = new DayValue();
        supermarketEditText.setEnabled(false);

        processSupermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Εισαγωγή νεας τιμής από τον χρήστη
                String newTotal = supermarketEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 4);
                //Ορίζουμε την τρέχουσα ημερομηνία στο DayValue, και την κατηγορία που μας ενδιαφέρει
                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Supermarket");

                //Αν η νέα τιμή διαφέρει από την παλια τότε
                if (!newTotal.equals(String.valueOf(supermarket))) {
                    //το νεο συνολικό ποσό ειναι η αφαίρεση της προηγούμενης τιμής από το αρχικό ποσο
                    //και το άρθροισμα της νέας τιμής
                    total = total - supermarket + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    //ορίζουμε το νέο συνολικο ποσο που υπολογίσαμε παραπάνω
                    dayValue.setValue(String.valueOf(total));
                    //η παλιά τιμή αντικαθιστάται με την καινούργια
                    supermarket = Integer.parseInt(newTotal);
                    dayValue.setSupermarket(String.valueOf(supermarket));
                    //στέλνουμε το αντικείμενο dayValue με όλα τα στοιχεία που μας ενδιαφέρουν στην
                    // κλάση της βάσης για ανανέωση της βάσης
                    dbHandler.updateValue(dayValue);
                }

                //έλεγχος της ενεργοποίηση ή απενεργοποίηση ενός πεδίου κειμένου
                supermarketEditText.setEnabled(!supermarketEditText.isEnabled());
                if (supermarketEditText.isEnabled()) {
                    supermarketEditText.requestFocus();
                }
                //επαναδημιουργούμε τα processBars με τα νέα δεδομένα
                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
            }
        });

        //Η ίδια διαδικασία επαναλλαμβάνεται και για τις άλλες δύο κατηγορίες

        entertainmentEditText.setEnabled(false);
        processEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = entertainmentEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 4);

                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Entertainment");

                if (!newTotal.equals(String.valueOf(entertainment))) {
                    total = total - entertainment + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    dayValue.setValue(String.valueOf(total));
                    entertainment = Integer.parseInt(newTotal);
                    dayValue.setEntertainment(String.valueOf(entertainment));
                    dbHandler.updateValue(dayValue);
                }

                entertainmentEditText.setEnabled(!entertainmentEditText.isEnabled());
                if (entertainmentEditText.isEnabled()) {
                    entertainmentEditText.requestFocus();
                }
                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
            }
        });

        homeEditText.setEnabled(false);
        processHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = homeEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 4);

                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Home");

                if (!newTotal.equals(String.valueOf(home))) {
                    total = total - home + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    dayValue.setValue(String.valueOf(total));
                    home = Integer.parseInt(newTotal);
                    dayValue.setHome(String.valueOf(home));
                    dbHandler.updateValue(dayValue);
                }

                homeEditText.setEnabled(!homeEditText.isEnabled());
                if (homeEditText.isEnabled()) {
                    homeEditText.requestFocus();
                }

                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
            }
        });

    }

    //Κλάση που δημιουργεί και εμφανίζει το porcessBar του Supermarket
    public void SupermarketProcess(View view){
        int maxProgress = 100;

        supermarketTextView.setText(String.valueOf(supermarket));
        ProgressBar progressBarSupermarket = view.findViewById(R.id.progressBarSupermarket);

        int supermarketValue = Integer.parseInt(supermarketTextView.getText().toString());
        int supermarketPercentage;
        if (total != 0) {
            supermarketPercentage = (supermarketValue * maxProgress) / total;
        } else {
            supermarketPercentage = 0;
        }
        progressBarSupermarket.setProgress(supermarketPercentage);

        //Προσθέτει την κίνηση στην μπάρα κατα την δημιουργία της
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarSupermarket, "progress", 0, supermarketPercentage);
        animation.setDuration(1000);
        animation.start();

        supermarketPercentTextView.setText(supermarketPercentage + "%");
    }

    public void EntertainmentProcess(View view){
        int maxProgress = 100;

        entertainmentTextView.setText(String.valueOf(entertainment));
        ProgressBar progressBarEntertainment = view.findViewById(R.id.progressBarEntertainment);
        int entertainmentValue = Integer.parseInt(entertainmentTextView.getText().toString());
        int entertainmentPercentage;
        if (total != 0) {
            entertainmentPercentage = (entertainmentValue * maxProgress) / total;
        } else {
            entertainmentPercentage = 0;
        }
        progressBarEntertainment.setProgress(entertainmentPercentage);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarEntertainment, "progress", 0, entertainmentPercentage);
        animation.setDuration(1000);
        animation.start();
        entertainmentPercentTextView.setText(entertainmentPercentage + "%");
    }

    public void HomeProcess(View view){
        int maxProgress = 100;

        homeTextView.setText(String.valueOf(home));
        ProgressBar progressBarHome = view.findViewById(R.id.progressBarHome);
        int homeValue = Integer.parseInt(homeTextView.getText().toString());

        int homePercentage;
        if (total != 0) {
            homePercentage = (homeValue * maxProgress) / total;
        } else {
            homePercentage = 0;
        }
        progressBarHome.setProgress(homePercentage);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarHome, "progress", 0, homePercentage);
        animation.setDuration(1000);
        animation.start();
        homePercentTextView.setText(homePercentage + "%");
    }

    public void setData(String month, int total, int supermarket, int entertainment, int home, boolean flag) {
        this.month = month;
        this.total = total;
        this.supermarket = supermarket;
        this.entertainment = entertainment;
        this.home = home;
        this.flag=flag;
    }

}