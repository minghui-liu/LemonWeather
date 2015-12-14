package com.minghui_liu.android.lemonweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minghui_liu.android.lemonweather.model.City;

import java.util.ArrayList;

/**
 * Created by kevin on 11/27/15.
 */
public class UserCityDataSource {
    private static UserCityDataSource sUserCityDataSource;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private ArrayList<City> mCityList;

    public static UserCityDataSource get(Context context) {
        if (sUserCityDataSource == null) {
            sUserCityDataSource = new UserCityDataSource(context);
        }
        return sUserCityDataSource;
    }

    public UserCityDataSource(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UserCityDbHelper(mContext).getWritableDatabase();
        mCityList = new ArrayList<City>();
        sync();
    }

    public void addCity(City c) {
        if (!mCityList.contains(c)) {
            mCityList.add(c);
            ContentValues values = new ContentValues();
            values.put(UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID, c.getId());
            values.put(UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME, c.getName());
            values.put(UserCityContract.FeedEntry.COLUNN_NAME_CITY_COUNTRY, c.getCountry());
            mDatabase.insert(UserCityContract.FeedEntry.TABLE_NAME, null, values);
        }
    }

    public void removeCity(City c) {
        if (mCityList.contains(c)) {
            mCityList.remove(c);
            String selection = UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID + " = ?";
            String[] selectionArgs = { String.valueOf(c.getId()) };
            mDatabase.delete(UserCityContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        }
    }

    public City getCity(int pos) {
        return mCityList.get(pos);
    }

    public ArrayList<City> getAllCities() {
        return mCityList;
    }

    public void sync() {
        mCityList.clear();
        Cursor cursor = mDatabase.query(UserCityContract.FeedEntry.TABLE_NAME, null, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(UserCityContract.FeedEntry.COLUMN_NAME_CITY_NAME));
                int id = cursor.getInt(cursor.getColumnIndex(UserCityContract.FeedEntry.COLUMN_NAME_CITY_ID));
                String country = cursor.getString(cursor.getColumnIndex(UserCityContract.FeedEntry.COLUNN_NAME_CITY_COUNTRY));
                mCityList.add(new City(name, id, country));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }
}
