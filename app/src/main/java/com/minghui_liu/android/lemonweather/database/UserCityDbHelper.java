package com.minghui_liu.android.lemonweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kevin on 11/27/15.
 */
public class UserCityDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + UserCityContract.FeedEntry.TABLE_NAME + " (" +
            UserCityContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
            UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME + TEXT_TYPE + COMMA_SEP +
            UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID + INTEGER_TYPE +
            // Any other options for the CREATE command
            " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserCityContract.FeedEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserCityList.db";

    public UserCityDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade policy: simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}