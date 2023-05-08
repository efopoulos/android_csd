package com.example.wallet;

//Κλαση για προσωρινη φυλλαξη ζευγους μέρας-τιμής
public class DayValue {
    private String value;
    private String day;
    private String supermarket;


    public DayValue(CharSequence day, String value) {
        this.value = value;
        this.day = String.valueOf(day);
    }

    public DayValue() {
    }

    public void setDay(String day) { this.day = day;}
    public String getDay() {
        return this.day;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
    public String getSupermarket() {
        return this.supermarket;
    }
    public void setSupermarket(String supermarket) {this.supermarket = supermarket;}
}
