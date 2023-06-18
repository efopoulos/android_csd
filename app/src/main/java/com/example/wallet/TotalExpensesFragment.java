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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
/*
Framgent για την εμφάνιση και την επεξεργασία των καταγεγραμμένων δαπανών
 */
public class TotalExpensesFragment extends Fragment {
    private TextView monthTextView;
    private TextView totalTextView;
    private EditText supermarketEditText;
    private EditText entertainmentEditText;
    private EditText homeEditText;
    private EditText transportationEditText;
    private EditText otherEditText;
    private TextView supermarketPercentTextView;
    private TextView entertainmentPercentTextView;
    private TextView homePercentTextView;
    private TextView transportationPercentTextView;
    private TextView otherPercentTextView;
    Boolean flag;
    DayValue dayValue;
    private TextView supermarketTextView;
    private TextView entertainmentTextView;
    private TextView homeTextView;
    private TextView transportationTextView;
    private TextView otherTextView;
    private String month;
    private int total;
    private int supermarket;
    private int entertainment;
    private int home;
    private int transportation;
    private int other;
    private static final String MONTH = "month";
    private static final String TOTAL = "total";
    private static final String SUPERMARKET = "supermarket";
    private static final String ENTERTAINMENT = "entertainment";
    private static final String HOME = "home";
    private static final String TRANSPORTATION = "transportation";
    private static final String OTHER = "other";
    private Button processSupermarket;
    private Button processEntertainment;
    private Button processHome;
    private Button processTransportation;
    private Button processOther;

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
            int transportationValue = bundle.getInt(TRANSPORTATION);
            int otherValue = bundle.getInt(OTHER);

            monthTextView.setText(monthValue);
            totalTextView.setText(String.valueOf(totalValue));
            supermarketTextView.setText(String.valueOf(supermarketValue));
            entertainmentTextView.setText(String.valueOf(entertainmentValue));
            homeTextView.setText(String.valueOf(homeValue));
            transportationTextView.setText(String.valueOf(transportationValue));
            otherTextView.setText(String.valueOf(otherValue));
        }
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("flag", flag);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_expenses, container, false);

        if(savedInstanceState != null){
            flag = savedInstanceState.getBoolean("flag");
        }

        monthTextView = view.findViewById(R.id.month_id);
        totalTextView = view.findViewById(R.id.monthtotal_value);

        supermarketEditText = view.findViewById(R.id.monthsupermarket_value);
        entertainmentEditText = view.findViewById(R.id.monthentertainment_value);
        homeEditText = view.findViewById(R.id.monthhome_value);
        transportationEditText = view.findViewById(R.id.monthtransportation_value);
        otherEditText = view.findViewById(R.id.monthother_value);

        supermarketPercentTextView = view.findViewById(R.id.supermarket_percent);
        entertainmentPercentTextView = view.findViewById(R.id.entertainment_percent);
        homePercentTextView = view.findViewById(R.id.home_percent);
        transportationPercentTextView = view.findViewById(R.id.transportation_percent);
        otherPercentTextView = view.findViewById(R.id.other_percent);

        supermarketTextView = view.findViewById(R.id.monthsupermarket_value);
        entertainmentTextView = view.findViewById(R.id.monthentertainment_value);
        homeTextView = view.findViewById(R.id.monthhome_value);
        transportationTextView = view.findViewById(R.id.monthtransportation_value);
        otherTextView = view.findViewById(R.id.monthother_value);

        processSupermarket = view.findViewById(R.id.supermarket_process);
        processEntertainment = view.findViewById(R.id.entertainment_process);
        processHome = view.findViewById(R.id.home_process);
        processTransportation = view.findViewById(R.id.transportation_process);
        processOther = view.findViewById(R.id.other_process);

        //Έλεγχος της κατηογίας ποσών που θα εμφανίσει το fragment (ημερίσια ή μηνιαία)
        if (flag) {
            processSupermarket.setVisibility(View.VISIBLE);
            processEntertainment.setVisibility(View.VISIBLE);
            processHome.setVisibility(View.VISIBLE);
            processTransportation.setVisibility(View.VISIBLE);
            processOther.setVisibility(View.VISIBLE);
            Process(view);
        } else {
            processSupermarket.setVisibility(View.GONE);
            processEntertainment.setVisibility(View.GONE);
            processHome.setVisibility(View.GONE);
            processTransportation.setVisibility(View.GONE);
            processOther.setVisibility(View.GONE);
        }

        monthTextView.setText(month);
        totalTextView.setText(String.valueOf(total));

        //Καθιστούμε τα EditText Disabled για τα συνολικά έξοδα του μήνα
        supermarketEditText.setEnabled(false);
        entertainmentEditText.setEnabled(false);
        homeEditText.setEnabled(false);
        transportationEditText.setEnabled(false);
        otherEditText.setEnabled(false);

        //Υπολογισμός ποσοστού και δημιουργία processBar
        SupermarketProcess(view);
        EntertainmentProcess(view);
        HomeProcess(view);
        TransportationProcess(view);
        OtherProcess(view);

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

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);
                //Ορισμός της τρέχουσας ημερομηνίας στο DayValue, και την κατηγορία που μας ενδιαφέρει
                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Supermarket");

                //Αν η νέα τιμή διαφέρει από την παλια τότε
                if (!newTotal.equals(String.valueOf(supermarket))) {
                    //το νεο συνολικό ποσό ειναι η αφαίρεση της προηγούμενης τιμής από το αρχικό ποσο
                    //και το άρθροισμα της νέας τιμής
                    total = total - supermarket + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    //Ορισός του νέου συνολικού ποσού που υπολογίσαμε παραπάνω
                    dayValue.setValue(String.valueOf(total));
                    //Η παλιά τιμή αντικαθιστάται με την καινούργια
                    supermarket = Integer.parseInt(newTotal);
                    dayValue.setSupermarket(String.valueOf(supermarket));
                    //Στέλνουμε το αντικείμενο dayValue με όλα τα στοιχεία που μας ενδιαφέρουν στην
                    //κλάση της βάσης για ανανέωση της βάσης
                    dbHandler.updateValue(dayValue);
                }

                //Έλεγχος της ενεργοποίηση ή απενεργοποίηση ενός πεδίου κειμένου
                supermarketEditText.setEnabled(!supermarketEditText.isEnabled());
                if (supermarketEditText.isEnabled()) {
                    supermarketEditText.requestFocus();
                }
                //Επαναδημιουργίσ ταυ processBars με τα νέα δεδομένα
                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
                TransportationProcess(view);
                OtherProcess(view);
            }
        });

        //Η ίδια διαδικασία επαναλλαμβάνεται και για τις άλλες δύο κατηγορίες
        entertainmentEditText.setEnabled(false);
        processEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = entertainmentEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);

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
                TransportationProcess(view);
                OtherProcess(view);
            }
        });

        homeEditText.setEnabled(false);
        processHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = homeEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);

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
                TransportationProcess(view);
                OtherProcess(view);
            }
        });


        transportationEditText.setEnabled(false);
        processTransportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = transportationEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);

                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Transportation");

                if (!newTotal.equals(String.valueOf(transportation))) {
                    total = total - transportation + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    dayValue.setValue(String.valueOf(total));
                    transportation = Integer.parseInt(newTotal);
                    dayValue.setTransportation(String.valueOf(transportation));
                    dbHandler.updateValue(dayValue);
                }

                transportationEditText.setEnabled(!transportationEditText.isEnabled());
                if (transportationEditText.isEnabled()) {
                    transportationEditText.requestFocus();
                }

                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
                TransportationProcess(view);
                OtherProcess(view);
            }
        });

        otherEditText.setEnabled(false);
        processOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = otherEditText.getText().toString();

                DBHandler dbHandler = new DBHandler(getContext(), null, null, 5);

                dayValue.setDay(monthTextView.getText().toString());
                dayValue.setCategory("Other");

                if (!newTotal.equals(String.valueOf(other))) {
                    total = total - other + Integer.parseInt(newTotal);
                    totalTextView.setText(String.valueOf(total));
                    dayValue.setValue(String.valueOf(total));
                    other = Integer.parseInt(newTotal);
                    dayValue.setOther(String.valueOf(other));
                    dbHandler.updateValue(dayValue);
                }

                otherEditText.setEnabled(!otherEditText.isEnabled());
                if (otherEditText.isEnabled()) {
                    otherEditText.requestFocus();
                }

                SupermarketProcess(view);
                EntertainmentProcess(view);
                HomeProcess(view);
                TransportationProcess(view);
                OtherProcess(view);
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
    public void TransportationProcess(View view){
        int maxProgress = 100;

        transportationTextView.setText(String.valueOf(transportation));
        ProgressBar progressBarTransportation = view.findViewById(R.id.progressBarTransportation);
        int transportationValue = Integer.parseInt(transportationTextView.getText().toString());

        int transportationPercentage;
        if (total != 0) {
            transportationPercentage = (transportationValue * maxProgress) / total;
        } else {
            transportationPercentage = 0;
        }
        progressBarTransportation.setProgress(transportationPercentage);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarTransportation, "progress", 0, transportationPercentage);
        animation.setDuration(1000);
        animation.start();
        transportationPercentTextView.setText(transportationPercentage + "%");
    }

    public void OtherProcess(View view){
        int maxProgress = 100;

        otherTextView.setText(String.valueOf(other));
        ProgressBar progressBarOther = view.findViewById(R.id.progressBarOther);
        int otherValue = Integer.parseInt(otherTextView.getText().toString());

        int otherPercentage;
        if (total != 0) {
            otherPercentage = (otherValue * maxProgress) / total;
        } else {
            otherPercentage = 0;
        }
        progressBarOther.setProgress(otherPercentage);
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarOther, "progress", 0, otherPercentage);
        animation.setDuration(1000);
        animation.start();
        otherPercentTextView.setText(otherPercentage + "%");
    }

    public void setData(String month, int total, int supermarket, int entertainment, int home, int transportation,
            int other, boolean flag) {
        this.month = month;
        this.total = total;
        this.supermarket = supermarket;
        this.entertainment = entertainment;
        this.home = home;
        this.transportation = transportation;
        this.other = other;
        this.flag=flag;
    }
}