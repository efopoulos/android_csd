package com.example.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
/*
Δραστηριότητα για προβολή και διαχείρηση οικονομικών υποχρεώσεων
 */
public class CommitmentActivity extends MainActivity {
    Button add;
    LinearLayout layout;
    //Αποθήκευση δεδομένων για ανακατασκευή cardView
    SharedPreferences sharedPreferences;
    private ArrayList<String> savedCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitment);
        getSupportActionBar().hide();

        layout = findViewById(R.id.container);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        savedCards = new ArrayList<>();

        //Ανάκτηση αποθηκευμένων καρτών από SharedPreferences και αποθήκευση στο savedCards
        String savedCardsJson = sharedPreferences.getString("card_views", "");

        if (!savedCardsJson.isEmpty()) {
            //Οι αποθηκευμένες κάρτες μετατρέπονται σε λίστα αντικειμένων
            savedCards = new Gson().fromJson(savedCardsJson, new TypeToken<ArrayList<String>>() {}.getType());
        }

        //Ανακτούνται και δημιουργούνται οι αποθηκευμένες κάρτες
        rebuildCardView();

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });

        //Προσθήκη BottomNavigationView
        Intent intent = getIntent();
        String month = intent.getStringExtra("month");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_commitment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main:
                        Intent mainIntent = new Intent(CommitmentActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        return true;
                    case R.id.action_month_expenses:
                        Intent monthExpensesIntent = new Intent(CommitmentActivity.this, MonthExpenses.class);
                        monthExpensesIntent.putExtra("month", month);
                        startActivity(monthExpensesIntent);
                        return true;
                    case R.id.action_commitment:
                        Intent commitmentIntent = new Intent(CommitmentActivity.this, CommitmentActivity.class);
                        commitmentIntent.putExtra("month", month);
                        startActivity(commitmentIntent);
                        return true;
                }
                return false;
            }
        });
    }

    //Δημιουργία του παράθυρου διαλόγου (dialog) που εισάγει τις πληροφορίες
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);
        EditText amount = view.findViewById(R.id.amountEdit);
        Switch dialogSwitch = view.findViewById(R.id.DialogSwitch);

        //Ορισμός κουμπιών 'ΟΚ' και 'Cancel'
        builder.setView(view);
        builder.setTitle("Enter a new financial commitment")
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        //Ορισμός λειτουργίας σε περίπτωση που πατηθεί το 'ΟΚ'
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = name.getText().toString();
                String enteredAmount = amount.getText().toString();
                boolean switchState = dialogSwitch.isChecked();

                if (enteredName.isEmpty() || enteredAmount.isEmpty()) {
                    Toast.makeText(CommitmentActivity.this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                } else {
                    addCard(enteredName, enteredAmount, switchState);
                    dialog.dismiss();
                }
            }
        });
    }

    //Προσθέτει μία νέα κάρτα
    private void addCard(String name, String amount, boolean switchState) {
        addCardView(name, amount, switchState);
        //Μετατροπή της boolean τιμής του switch σε string
        String switchToString = String.valueOf(switchState);
        String cardData = name + "," + amount + "," + switchToString;
        savedCards.add(cardData);

        saveCardsToSharedPreferences();
    }

    //Αποθήκευση της λίστας savedCards στο SharedPreferences
    private void saveCardsToSharedPreferences() {
        String updatedCardsJson = new Gson().toJson(savedCards);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("card_views", updatedCardsJson);
        editor.apply();
    }


    //Προσθήκη νέας καρτέλας στο layout
    private void addCardView(String name, String amount, boolean switchState) {
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView amountView = view.findViewById(R.id.amount);
        Button delete = view.findViewById(R.id.delete);
        Switch cardSwitch = view.findViewById(R.id.CardSwitch);

        nameView.setText(name);
        amountView.setText(amount);
        cardSwitch.setChecked(switchState);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                removeCardFromSharedPreferences(name, amount);
            }
        });

        //Σε περίπτωση αλλαγής του switch καλείται η κλάση updateSwitch
        cardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSwitchState(name, amount, isChecked);
            }
        });
        layout.addView(view);
    }

    private void updateSwitchState(String name, String amount, boolean switchState) {
        for (int i = 0; i < savedCards.size(); i++) {
            String card = savedCards.get(i);
            String[] cardData = card.split(",");
            String cardName = cardData[0];
            String cardAmount = cardData[1];

            if (cardName.equals(name) && cardAmount.equals(amount)) {
                //Ενημέρωση της τιμής του switch για το συγκεκριμένο αντικείμενο
                String switchToString = String.valueOf(switchState);
                String updatedCardData = cardName + "," + cardAmount + "," + switchToString;
                savedCards.set(i, updatedCardData);
                saveCardsToSharedPreferences();
                break;
            }
        }
    }

    //Αφαίρεση μιας καρτέλας από το SharedPreferences
    private void removeCardFromSharedPreferences(String name, String amount) {
        //Ενημερώνονται τα δεδομένα της λίστας σε περίπτωση διαγραφής
        ArrayList<String> updatedCards = new ArrayList<>();
        for (String card : savedCards) {
            String[] cardData = card.split(",");
            String cardName = cardData[0];
            String cardAmount = cardData[1];
            if (!(cardName.equals(name) && cardAmount.equals(amount))) {
                updatedCards.add(card);
            }
        }
        savedCards = updatedCards;
        saveCardsToSharedPreferences();
    }

    //Ανακατασκευή των card views
    private void rebuildCardView() {
        layout.removeAllViews();
        for (String card : savedCards) {
            String[] cardData = card.split(",");
            String name = cardData[0];
            String amount = cardData[1];
            boolean switchState = Boolean.parseBoolean(cardData[2]);

            addCardView(name, amount, switchState);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savedCards.clear();
        layout.removeAllViews();
    }
}
