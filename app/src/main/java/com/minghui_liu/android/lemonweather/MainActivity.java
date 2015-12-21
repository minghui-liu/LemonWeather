package com.minghui_liu.android.lemonweather;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.minghui_liu.android.lemonweather.database.CityDatabase;
import com.minghui_liu.android.lemonweather.database.CityProvider;
import com.minghui_liu.android.lemonweather.database.UserCityAdapter;
import com.minghui_liu.android.lemonweather.database.UserCityDataSource;
import com.minghui_liu.android.lemonweather.model.City;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LemonWeather";
    private static final String STATE_POSITION = "position";

    private String mUnits;
    private int mPosition;
    private boolean mIsCityListEmpty;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mLeftDrawer;
    private ListView mCityListView;
    private SearchView mSearchView;
    private UserCityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Set home button as toggle button for navigation drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSearchView = (SearchView) findViewById(R.id.city_search_view);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled (true);

        mCityListView = (ListView) findViewById(R.id.city_list_view);
        mCityListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        registerForContextMenu(mCityListView);

        updateCityList();

        // Get default city
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(STATE_POSITION);
        } else {
            mPosition = 0;
        }

        //set notification alarm
        setAlarmService();
    }

    @Override
    protected void onResume() {
        // Get display unit setting
        mUnits = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_unit", "imperial");
        if (!mIsCityListEmpty) {
            selectItem(mPosition);
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_action_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                UserCityDataSource.get(this).removeCity(mAdapter.getItem(info.position));
                updateCityList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion, add city to database
            Uri uri = intent.getData();
            Log.d(TAG, "Insert: " + uri.toString());
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int iIndex = cursor.getColumnIndexOrThrow(CityDatabase.KEY_ID);
                int nIndex = cursor.getColumnIndexOrThrow(CityDatabase.KEY_NAME);
                int cIndex = cursor.getColumnIndexOrThrow(CityDatabase.KEY_COUNTRY);

                int id = Integer.parseInt(cursor.getString(iIndex));
                String name = cursor.getString(nIndex);
                String country = cursor.getString(cIndex);
                UserCityDataSource userCityDataSource = UserCityDataSource.get(MainActivity.this);
                userCityDataSource.addCity(new City(name, id, country));
                updateCityList();
            }

            mSearchView.setQuery("", false);
            mSearchView.setIconified(true);
            ArrayList<City> citylist = UserCityDataSource.get(this).getAllCities();
            selectItem(citylist.size() - 1);

        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Query: " + query);
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
            Toast.makeText(this, getString(R.string.no_results, new Object[]{query}), Toast.LENGTH_LONG).show();
            Log.d(TAG, "no results");
        } else {
            // Display the number of results
            int count = cursor.getCount();
            String countString = getResources().getQuantityString(R.plurals.search_results,
                    count, new Object[] {count, query});
            Toast.makeText(this, countString, Toast.LENGTH_LONG).show();
            Log.d(TAG, countString);

            SearchResultDialogFragment fragment = new SearchResultDialogFragment();
            fragment.setCursor(cursor);

            fragment.show(getSupportFragmentManager(), "search_result");
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(WeatherFragment.ARG_CITY_ID, mAdapter.getItem(position).getId());
        args.putString(WeatherFragment.ARG_CITY_NAME, mAdapter.getItem(position).getName());
        args.putString(WeatherFragment.ARG_UNITS_STRING, mUnits);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mCityListView.setItemChecked(position, true);
        setTitle(mAdapter.getItem(position).getName());
        mDrawerLayout.closeDrawer(mLeftDrawer);
        mPosition = position;
    }

    private void updateCityList() {
        ArrayList<City> citylist = UserCityDataSource.get(this).getAllCities();
        mIsCityListEmpty = citylist.isEmpty();

        if (mAdapter == null) {
            mAdapter = new UserCityAdapter(this, android.R.layout.simple_list_item_2, citylist);
            mCityListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
     * Check and set notification alarm
     */
    private void setAlarmService() {
        AlarmService.setContext(this);
        boolean notificationOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_notification", true);
        //create alarm service with the stored time preference if none exists
        if (!AlarmService.isAlarmSet() && notificationOn) {
            //get stored time preference
            long time = PreferenceManager.getDefaultSharedPreferences(this)
                        .getLong("pref_time", AlarmService.getDefaultTime());

            AlarmService.updateAlarmTime(time);
        }
    }

}