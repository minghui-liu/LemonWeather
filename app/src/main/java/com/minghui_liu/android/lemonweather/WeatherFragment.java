package com.minghui_liu.android.lemonweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minghui_liu.android.lemonweather.api.WeatherAPI;
import com.minghui_liu.android.lemonweather.model.WeatherModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kevin on 11/7/15.
 */
public class WeatherFragment extends Fragment {
    public static final String ARG_CITY_NUMBER = "city_number";
    public static final String APIURL = "http://api.openweathermap.org/data/2.5";
    public static final String APPID = "61da10c389e32199663fc074efd9b30b";
    private static final String TAG = "NavigationDrawer";

    private TextView mTextViewDescription;
    private TextView mTextViewTemperature;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        int i = getArguments().getInt(ARG_CITY_NUMBER);
        String city = getResources().getStringArray(R.array.city_list)[i];

        mTextViewDescription = (TextView)v.findViewById(R.id.description_text_view);
        mTextViewTemperature = (TextView)v.findViewById(R.id.temperature_text_view);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIURL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        WeatherAPI service = restAdapter.create(WeatherAPI.class);
        service.getWeatherCity(city, APPID, new Callback<WeatherModel>(){
            @Override
            public void success(WeatherModel weatherM, Response response) {
                double temp = weatherM.getMain().getTemp() - 273.15;
                String description = weatherM.getWeather().get(0).getDescription();

                mTextViewTemperature.setText(String.format("%d", (int)temp));
                mTextViewDescription.setText(description);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failed to get weather");
                Log.e(TAG, error.toString());
            }
        });

        getActivity().setTitle(city);

        return v;
    }
}
