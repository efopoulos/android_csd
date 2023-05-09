package com.example.wallet;

//Κλαση για προσωρινη φυλλαξη ζευγους μέρας-τιμής
public class DayValue {
    private String value;
    private String day;
    private String supermarket;

    private String entertainment;
    private String home;
    private String category;


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

    public void setCategory(String category) {this.category = category;}

    public String getCategory() {return this.category;}

    public void setEntertainment(String entertainment) {this.entertainment = entertainment; }
    public String getEntertainment() {return this.entertainment;}
    public void setHome(String home) {this.home = home; }
    public String getHome() {return this.home;}

}
