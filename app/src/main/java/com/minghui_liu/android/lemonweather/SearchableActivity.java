package com.minghui_liu.android.lemonweather;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by kevin on 11/19/15.
 */
public class SearchableActivity extends ListActivity {
    private static final String TAG = "LemonWeather";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //searchCityDatabase(query);
            Log.d(TAG, "query: " + query);
        }
    }

}
