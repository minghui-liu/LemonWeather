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
import com.minghui_liu.android.lemonweather.model.forcast.Forecast;
import com.minghui_liu.android.lemonweather.model.weather.WeatherModel;

import org.w3c.dom.Text;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kevin on 11/7/15.
 */
public class WeatherFragment extends Fragment {
    public static final String ARG_CITY_ID = "city_id";
    public static final String ARG_CITY_NAME = "city_name";
    public static final String ARG_UNITS_STRING = "unit_string";
    public static final String APIURL = "http://api.openweathermap.org/data/2.5";
    public static final String APPID = "61da10c389e32199663fc074efd9b30b";
    private static final String TAG = "LemonWeather";

    private TextView mTextViewDescription;
    private TextView mTextViewTemperature;
    private TextView mUnitTextView;
    private ImageView mIconImageView;
    private ImageView mWindImageView;

    private TextView[] mForcastTempTextViews;
    private TextView[] mForcastDescriptionTextViews;
    private ImageView[] mForcastIconImageViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        int cityid = getArguments().getInt(ARG_CITY_ID);
        String units = getArguments().getString(ARG_UNITS_STRING);
        String cityname = getArguments().getString(ARG_CITY_NAME);

        mTextViewDescription = (TextView)v.findViewById(R.id.description_text_view);
        mTextViewTemperature = (TextView)v.findViewById(R.id.temperature_text_view);
        mUnitTextView = (TextView)v.findViewById(R.id.unit_text_view);
        mIconImageView = (ImageView)v.findViewById(R.id.icon_image_view);
        mWindImageView = (ImageView)v.findViewById(R.id.wind_image_view);

        mForcastTempTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_temp),
                                                (TextView)v.findViewById(R.id.day2_temp),
                                                (TextView)v.findViewById(R.id.day3_temp),
                                                (TextView)v.findViewById(R.id.day4_temp)};

        mForcastDescriptionTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_desciption),
                                                        (TextView)v.findViewById(R.id.day2_description),
                                                        (TextView)v.findViewById(R.id.day3_description),
                                                        (TextView)v.findViewById(R.id.day4_description)};


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIURL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        WeatherAPI weatherService = restAdapter.create(WeatherAPI.class);
        weatherService.getWeatherByName(cityname, units, APPID, new Callback<WeatherModel>() {
            @Override
            public void success(WeatherModel weatherM, Response response) {
                double temp = weatherM.getMain().getTemp();
                String description = weatherM.getWeather().get(0).getDescription();
                int condition = weatherM.getWeather().get(0).getId();

                mTextViewTemperature.setText(String.format("%d", (int) temp));
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
        weatherService.getForecastByName(cityname, units, APPID, new Callback<Forecast>() {

            @Override
            public void success(Forecast forecast, Response response) {
                int count = forecast.getCnt();
                List<com.minghui_liu.android.lemonweather.model.forcast.List> days = forecast.getList();

                Log.d(TAG, "count = " + count);
                for (int i = 0; i < 4; i++) {
                    double min = days.get(i).getTemp().getMin();
                    double max = days.get(i).getTemp().getMax();
                    mForcastTempTextViews[i].setText(String.format("%d", (int) min) + " ~ " + String.format("%d", (int) max));
                    mForcastDescriptionTextViews[i].setText(days.get(i).getWeather().get(0).getDescription());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failed to get forcast");
                Log.e(TAG, error.toString());
            }
        });

        mUnitTextView.setText(units.equals("metric") ? "°C" : "°F");

        return v;
    }
}
