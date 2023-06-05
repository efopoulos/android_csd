package com.example.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CommitmentActivity extends MainActivity {
    Button add;
    LinearLayout layout;
    SharedPreferences sharedPreferences;
    private ArrayList<String> savedCards = new ArrayList<>();

    Boolean selectedSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitment);
        layout = findViewById(R.id.container);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedCardsJson = sharedPreferences.getString("card_views", "");

        if (!savedCardsJson.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            savedCards = new Gson().fromJson(savedCardsJson, type);

            //εμφανίζει τα ήδη δημιουργημένα cardview
            for (String card : savedCards) {
                String[] cardData = card.split(",");
                String cardName = cardData[0];
                String cardAmount = cardData[1];
                addCardView(cardName, cardAmount);
            }

        }

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });

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

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);
        EditText amount = view.findViewById(R.id.amountEdit);
        Switch dialogSwitch = view.findViewById(R.id.DialogSwitch);

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
        name.setText("Name");
        amount.setText("Amount");

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = name.getText().toString();
                String enteredAmount = amount.getText().toString();

                if (enteredName.equals("Name") || enteredAmount.equals("Amount")) {
                    Toast.makeText(CommitmentActivity.this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                } else {
                    dialogSwitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(dialogSwitch.isChecked()){
                                selectedSwitch = true;
                            }else{
                                selectedSwitch = false;
                            }
                        }
                    });
                    addCard(enteredName, enteredAmount);
                    dialog.dismiss();
                }
            }
        });


    }


    private void addCard(String name, String amount) {
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView amountView = view.findViewById(R.id.amount);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);
        amountView.setText(amount);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);

                //ανάκτηση αποθηκευμένων καρτέλων από SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String savedCardsJson = sharedPreferences.getString("card_views", "");

                if (!savedCardsJson.isEmpty()) {
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    savedCards = new Gson().fromJson(savedCardsJson, type);
                }

                //προστίθονται όλες οι καρτέλες στο update εκτος από αυτην που
                //προκειται να διαγραφεί
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
                String updatedCardsJson = new Gson().toJson(savedCards);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("card_views", updatedCardsJson);
                editor.apply();
            }
        });

        layout.addView(view);

        String switchToString = String.valueOf(selectedSwitch);


        String cardData = name + "," + amount + "," ;
        savedCards.add(cardData);

        String updatedCardsJson = new Gson().toJson(savedCards);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("card_views", updatedCardsJson);
        editor.apply();
    }


    private void addCardView(String name, String amount) {
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView amountView = view.findViewById(R.id.amount);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);
        amountView.setText(amount);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);


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
                String updatedCardsJson = new Gson().toJson(savedCards);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("card_views", updatedCardsJson);
                editor.apply();
            }
        });

        layout.addView(view);
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