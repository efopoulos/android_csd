package com.example.wallet;
/*
Δημιουργία ενός αντιεκιμένου Budget για αποθήκευση της τιμής Budget
κατά την λειτουργία της εφαρμογης
 */
public class BudgetManager {
    private static String budget ;

    public static String getBudget() {
        return budget;
    }
    public static void setBudget(String value) {
        budget = value;
    }
}
