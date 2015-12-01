package com.minghui_liu.android.lemonweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kevin on 11/27/15.
 */
public class UserCityDataSource {
    private static UserCityDataSource sUserCityDataSource;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static UserCityDataSource get(Context context) {
        if (sUserCityDataSource == null) {
            sUserCityDataSource = new UserCityDataSource(context);
        }
        return sUserCityDataSource;
    }

    private String[] allColumns = { UserCityContract.FeedEntry._ID,
                                    UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME,
                                    UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID };

    public UserCityDataSource(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UserCityDbHelper(mContext).getWritableDatabase();
    }

    public void addCity(String name, int id) {
        ContentValues values = new ContentValues();
        values.put(UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID, id);
        values.put(UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME, name);
        mDatabase.insert(UserCityContract.FeedEntry.TABLE_NAME, null, values);
    }

    public void removeCity(String name) {
        String selection = UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME + " = ?";
        String[] selectionArgs = { name };
        mDatabase.delete(UserCityContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void removeCity(int id) {
        String selection = UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        mDatabase.delete(UserCityContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAllCities() {
        return mDatabase.query(UserCityContract.FeedEntry.TABLE_NAME, allColumns, null, null, null, null, null);
    }
}
