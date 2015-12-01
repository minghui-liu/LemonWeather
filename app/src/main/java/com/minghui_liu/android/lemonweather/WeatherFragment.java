package com.minghui_liu.android.lemonweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public static final String ARG_UNITS_STRING = "unit_string";
    public static final String APIURL = "http://api.openweathermap.org/data/2.5";
    public static final String APPID = "61da10c389e32199663fc074efd9b30b";
    private static final String TAG = "LemonWeather";

    private TextView mTextViewDescription;
    private TextView mTextViewTemperature;
    private ImageView mIconImageView;
    private ImageView mWindImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        int i = getArguments().getInt(ARG_CITY_NUMBER);
        String units = getArguments().getString(ARG_UNITS_STRING);
        String city = getResources().getStringArray(R.array.city_list)[i];

        mTextViewDescription = (TextView)v.findViewById(R.id.description_text_view);
        mTextViewTemperature = (TextView)v.findViewById(R.id.temperature_text_view);
        mIconImageView = (ImageView)v.findViewById(R.id.icon_image_view);
        mWindImageView = (ImageView)v.findViewById(R.id.wind_image_view);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIURL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        WeatherAPI service = restAdapter.create(WeatherAPI.class);
        service.getWeatherByName(city, units, APPID, new Callback<WeatherModel>(){
            @Override
            public void success(WeatherModel weatherM, Response response) {
                double temp = weatherM.getMain().getTemp();
                String description = weatherM.getWeather().get(0).getDescription();
                int condition = weatherM.getWeather().get(0).getId();

                mTextViewTemperature.setText(String.format("%d", (int)temp));
                mTextViewDescription.setText(description);

                switch (condition / 100) {
                    case 5:
                        mIconImageView.setBackgroundResource(R.drawable.ic_lemon_rainy);
                        break;
                    case 8:
                        switch (condition) {
                            case 800:
                                mIconImageView.setBackgroundResource(R.drawable.ic_lemon_sunny);
                                break;
                            case 801:
                                mIconImageView.setBackgroundResource(R.drawable.ic_lemon_partly_cloudy);
                                break;
                            case 802:
                                mIconImageView.setBackgroundResource(R.drawable.ic_lemon_cloudy);
                                break;
                        }
                }
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
