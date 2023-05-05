package com.example.wallet;

import android.util.Log;

//Κλαση για προσωρινη φυλλαξη ζευγους μέρας-τιμής
public class DayValue {
    private String value;
    private String day;kkkk

    public DayValue(CharSequence day, String value) {
        this.value = value;
        this.day = String.valueOf(day);
        Log.d("day", String.valueOf(day));
        Log.d("value", value);
    }

    public DayValue() {
    }

    public void setDay(String day) {
        this.day = day;
    }
    public String getDay() {
        return this.day;
    }

    public void setValue(String value) {
        this.day = day;
    }
    public String getValue() {
        return this.value;
    }
}
