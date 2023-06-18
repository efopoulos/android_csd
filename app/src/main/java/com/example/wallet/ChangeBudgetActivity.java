package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/*
Διεπαφή για την αλλαγή του διαθέσιμου Budget
 */
public class ChangeBudgetActivity extends MainActivity{
    Button BudgetButton;
    EditText MonthBudget;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebudget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BudgetButton = findViewById(R.id.button_change);
        MonthBudget = findViewById(R.id.month_budget);

        Intent intent = getIntent();
        intent.getStringExtra("budgetNow");
    }

    //Ανάκτηση νέας τιμής πουεισάγεται από τον χρήστη
    public void Change(View view) {
        String userInput = MonthBudget.getText().toString();
        if (!userInput.isEmpty() && userInput.matches("\\d+") ){
            BudgetManager.setBudget(userInput);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("MonthBudget", MonthBudget.getText().toString());
        super.onSaveInstanceState(outState);
    }
}