package com.minghui_liu.android.lemonweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private String[] mCityList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mLeftDrawer;
    private LinearLayout mRightDrawer;
    private ListView mCityListView;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCityList = getResources().getStringArray(R.array.city_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCityListView = (ListView) findViewById(R.id.city_list_view);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        mRightDrawer = (LinearLayout) findViewById(R.id.right_drawer);
        mSearchView = (SearchView) findViewById(R.id.city_search_view);

        mSearchView.setIconifiedByDefault(false);

        mCityListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mCityList));
        mCityListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        registerForContextMenu(mCityListView);

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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                mDrawerToggle.onOptionsItemSelected(item);

                if (mDrawerLayout.isDrawerOpen(mLeftDrawer))
                    mDrawerLayout.closeDrawer(mLeftDrawer);

                if (!mDrawerLayout.isDrawerOpen(mRightDrawer))
                    mDrawerLayout.openDrawer(mRightDrawer);
                else
                    mDrawerLayout.closeDrawer(mRightDrawer);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(WeatherFragment.ARG_CITY_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mCityListView.setItemChecked(position, true);
        setTitle(mCityList[position]);
        mDrawerLayout.closeDrawer(mLeftDrawer);
    }
}

