package com.minghui_liu.android.lemonweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
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

import java.util.Date;
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
    private TextView mTextViewUnit;
    private ImageView mIconImageView;
    private TextView mTextViewDate;
    private TextView mTextViewDay;

    private TextView[] mForcastTempTextViews;
    private TextView[] mForcastDescriptionTextViews;
    private ImageView[] mForcastIconImageViews;

    private Date mDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        int cityid = getArguments().getInt(ARG_CITY_ID);
        String units = getArguments().getString(ARG_UNITS_STRING);
        String cityname = getArguments().getString(ARG_CITY_NAME);

        mTextViewDescription = (TextView)v.findViewById(R.id.description_text_view);
        mTextViewTemperature = (TextView)v.findViewById(R.id.temperature_text_view);
        mTextViewUnit = (TextView)v.findViewById(R.id.unit_text_view);
        mIconImageView = (ImageView)v.findViewById(R.id.icon_image_view);
        mTextViewDate = (TextView)v.findViewById(R.id.date_text_view);
        mTextViewDay = (TextView)v.findViewById(R.id.day_text_view);

        mForcastTempTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_temp_text_view),
                                                (TextView)v.findViewById(R.id.day2_temp_text_view),
                                                (TextView)v.findViewById(R.id.day3_temp_text_view),
                                                (TextView)v.findViewById(R.id.day4_temp_text_view)};

        mForcastDescriptionTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_description_text_view),
                                                        (TextView)v.findViewById(R.id.day2_description_text_view),
                                                        (TextView)v.findViewById(R.id.day3_description_text_view),
                                                        (TextView)v.findViewById(R.id.day4_description_text_view)};

        mForcastIconImageViews = new ImageView[] {(ImageView)v.findViewById(R.id.day1_icon_image_view),
                                                    (ImageView)v.findViewById(R.id.day2_icon_image_view),
                                                    (ImageView)v.findViewById(R.id.day3_icon_image_view),
                                                    (ImageView)v.findViewById(R.id.day4_icon_image_view)};


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIURL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        WeatherAPI weatherService = restAdapter.create(WeatherAPI.class);
        weatherService.getWeatherById(String.valueOf(cityid), units, APPID, new Callback<WeatherModel>() {
            @Override
            public void success(WeatherModel weatherM, Response response) {
                double temp = weatherM.getMain().getTemp();
                String description = weatherM.getWeather().get(0).getDescription();
                int icon = Integer.parseInt(weatherM.getWeather().get(0).getIcon().substring(0, 2));

                mTextViewTemperature.setText(String.format("%.1f", temp));
                mTextViewDescription.setText(description);

                switch (icon) {
                    case 1:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_sunny);
                        break;
                    case 2:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_partly_cloudy);
                        break;
                    case 3:
                    case 4:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_cloudy);
                        break;
                    case 9:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_shower);
                        break;
                    case 10:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_rain);
                        break;
                    case 11:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_storm);
                        break;
                    case 13:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_snow);
                        break;
                    case 50:
                        mIconImageView.setImageResource(R.drawable.ic_lemon_fog);
                        break;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failed to get weather");
                Log.e(TAG, error.toString());
            }
        });

        weatherService.getForecastById(String.valueOf(cityid), units, APPID, new Callback<Forecast>() {

            @Override
            public void success(Forecast forecast, Response response) {
                List<com.minghui_liu.android.lemonweather.model.forcast.List> days = forecast.getList();

                for (int i = 0; i < 4; i++) {
                    double min = days.get(i).getTemp().getMin();
                    double max = days.get(i).getTemp().getMax();
                    int icon = Integer.parseInt(days.get(i).getWeather().get(0).getIcon().substring(0, 2));

                    mForcastTempTextViews[i].setText(String.format("%d", (int) min) + " ~ " + String.format("%d", (int) max));
                    mForcastDescriptionTextViews[i].setText(days.get(i).getWeather().get(0).getDescription());

                    switch (icon) {
                        case 1:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_sunny);
                            break;
                        case 2:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_partly_cloudy);
                            break;
                        case 3:
                        case 4:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_cloudy);
                            break;
                        case 9:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_shower);
                            break;
                        case 10:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_rain);
                            break;
                        case 11:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_storm);
                            break;
                        case 13:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_snow);
                            break;
                        case 50:
                            mForcastIconImageViews[i].setImageResource(R.drawable.ic_lemon_fog);
                            break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failed to get forcast");
                Log.e(TAG, error.toString());
            }
        });

        mDate = new Date();
        mTextViewUnit.setText(units.equals("metric") ? "°C" : "°F");
        mTextViewDate.setText(DateFormat.format("MMM dd, yyyy", mDate));
        mTextViewDay.setText(DateFormat.format("EEEE", mDate));

        return v;
    }
}
