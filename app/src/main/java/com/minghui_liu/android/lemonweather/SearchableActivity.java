package com.minghui_liu.android.lemonweather;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.minghui_liu.android.lemonweather.database.CityDatabase;
import com.minghui_liu.android.lemonweather.database.CityProvider;

/**
 * Created by kevin on 11/19/15.
 */
public class SearchableActivity extends ListActivity {
    private static final String TAG = "LemonWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion, log it
            Log.d(TAG, "Clicked suggestion: " + intent.getData().toString());
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "query: " + query);
            showResults(query);
        }
    }

    /**
     * Searches the dictionary and displays results for the given query.
     * @param query The search query
     */
    private void showResults(String query) {

        Cursor cursor = getContentResolver().query(CityProvider.CONTENT_URI, null, null,
                new String[] {query}, null);

        if (cursor == null) {
            // There are no results
//            mTextView.setText(getString(R.string.no_results, new Object[]{query}));
            Log.d(TAG, "no results");
        } else {
            // Display the number of results
            int count = cursor.getCount();
            String countString = getResources().getQuantityString(R.plurals.search_results,
                    count, new Object[] {count, query});
//            mTextView.setText(countString);
            Log.d(TAG, countString);

            // Specify the columns we want to display in the result
            String[] from = new String[] { CityDatabase.KEY_NAME,
                    CityDatabase.KEY_NAME };

            // Specify the corresponding layout elements where we want the columns to go
            int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter words = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2, cursor, from, to);
            getListView().setAdapter(words);

            // Define the on-click listener for the list items
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
//                    Intent wordIntent = new Intent(getApplicationContext(), WordActivity.class);
                    Uri data = Uri.withAppendedPath(CityProvider.CONTENT_URI,
                            String.valueOf(id));
//                    wordIntent.setData(data);
//                    startActivity(wordIntent);
                    Log.d(TAG, "Clicked: " + data.toString());
                }
            });
        }
    }
}
