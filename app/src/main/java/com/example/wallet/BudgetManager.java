package com.example.wallet;

public class BudgetManager {
    private static String budget = String.valueOf(500);

    public static String getBudget() {
        return budget;
    }

    public static void setBudget(String value) {
        budget = value;
    }
}
