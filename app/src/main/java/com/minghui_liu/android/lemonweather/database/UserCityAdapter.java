package com.minghui_liu.android.lemonweather.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.minghui_liu.android.lemonweather.model.City;

import java.util.List;

/**
 * Created by kevin on 12/1/15.
 */
public class UserCityAdapter extends ArrayAdapter<City> {
    private List<City> mCityList;

    public UserCityAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        mCityList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        // Lookup view for data population
        TextView CityListEntryTitle = (TextView) convertView.findViewById(android.R.id.text1);
        TextView CityListEntrySubtitle = (TextView) convertView.findViewById(android.R.id.text2);
        // Populate the data into the template view using the data object
        CityListEntryTitle.setText(city.getName());
        CityListEntrySubtitle.setText(city.getCountry());
        // Return the completed view to render on screen
        return convertView;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public void setCityList(List<City> cityList) {
        mCityList = cityList;
    }
}
