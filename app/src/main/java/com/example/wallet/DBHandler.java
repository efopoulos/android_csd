package com.example.wallet;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

public class DBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "valuesDB.db";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_VALUE = "value";
    public static final String TABLE_VALUES = "day_values";
    public static final String COLUMN_SUPERMARKET = "supermarket";
    public static final String COLUMN_ENTERTAINMENT = "entertainment";
    public static final String COLUMN_HOME = "home";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DAYS_TABLE = "CREATE TABLE " +
                TABLE_VALUES + "(" +
                COLUMN_DAYS + " TEXT PRIMARY KEY," +
                COLUMN_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_DAYS_TABLE);
        //Δημιουργια νεας στηλης
        String query = "ALTER TABLE " + TABLE_VALUES + " ADD COLUMN " + COLUMN_SUPERMARKET + " TEXT;";
        db.execSQL(query);
        query = "ALTER TABLE " + TABLE_VALUES + " ADD COLUMN " + COLUMN_ENTERTAINMENT + " TEXT;";
        db.execSQL(query);
        query = "ALTER TABLE " + TABLE_VALUES + " ADD COLUMN " + COLUMN_HOME + " TEXT;";
        db.execSQL(query);
    }

    //ενημέρωση βάσης
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALUES);
        onCreate(db);
    }
    public void addNewValue(DayValue dayValue) {
        String dayName = dayValue.getDay();
        String newValue = dayValue.getValue();
        String category = dayValue.getCategory();
        DayValue found = findDay(dayName);

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        if(found==null){
            values.put(COLUMN_DAYS, dayValue.getDay());
            values.put(COLUMN_VALUE, dayValue.getValue());
            if (category.equals("Supermarket")) {
                values.put(COLUMN_SUPERMARKET, newValue);
                values.put(COLUMN_ENTERTAINMENT, 0);
                values.put(COLUMN_HOME, 0);
            } else if (category.equals("Entertainment")) {
                values.put(COLUMN_SUPERMARKET, 0);
                values.put(COLUMN_ENTERTAINMENT, newValue);
                values.put(COLUMN_HOME, 0);
            }else{
                values.put(COLUMN_SUPERMARKET, 0);
                values.put(COLUMN_ENTERTAINMENT, 0);
                values.put(COLUMN_HOME, newValue);
            }
            db.insert(TABLE_VALUES, null, values);
        }
        else{
            String query = "UPDATE " + TABLE_VALUES + " SET " + COLUMN_VALUE + " = " + COLUMN_VALUE + " + '" + newValue + "' WHERE " + COLUMN_DAYS + " = '" + dayName + "'";
            db.execSQL(query);

            if (category.equals("Supermarket")) {
                query = "UPDATE " + TABLE_VALUES + " SET " + COLUMN_SUPERMARKET + " = " + COLUMN_SUPERMARKET + " + '" + newValue + "' WHERE " + COLUMN_DAYS + " = '" + dayName + "'";
            } else if (category.equals("Entertainment")) {
                query = "UPDATE " + TABLE_VALUES + " SET " + COLUMN_ENTERTAINMENT + " = " + COLUMN_ENTERTAINMENT + " + '" + newValue + "' WHERE " + COLUMN_DAYS + " = '" + dayName + "'";
            }else{
                query = "UPDATE " + TABLE_VALUES + " SET " + COLUMN_HOME + " = " + COLUMN_HOME + " + '" + newValue + "' WHERE " + COLUMN_DAYS + " = '" + dayName + "'";
            }
            db.execSQL(query);
        }
        //διαγράφει οτι εχει μεσα ο πινακας
        //db.delete(TABLE_VALUES, null, null);
    }

    public DayValue findDay(String day_name) {
        String query = "SELECT * FROM " + TABLE_VALUES +  " WHERE " +
                COLUMN_DAYS + " = '" + day_name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DayValue day = new DayValue();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            day.setDay(cursor.getString(0));
            day.setValue(String.valueOf(Integer.parseInt(cursor.getString(1))));
            cursor.close();
        } else {
            day = null;
        }
        db.close();
        return day;
    }
}
