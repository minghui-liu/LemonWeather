package com.minghui_liu.android.lemonweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by kevin on 11/27/15.
 */
public final class UserCityContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public UserCityContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_city_list";
        public static final String COLUMN_NAME_CITY_NAME = "cityname";
        public static final String COLUMN_NAME_CITY_ID = "cityid";
        public static final String COLUNN_NAME_CITY_COUNTRY = "citycountry";
    }
}



