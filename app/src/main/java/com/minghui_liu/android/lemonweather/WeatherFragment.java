package com.minghui_liu.android.lemonweather;

import android.content.res.Configuration;
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
import com.minghui_liu.android.lemonweather.model.dailyforecast.DailyForecast;
import com.minghui_liu.android.lemonweather.model.hourlyforecast.HourlyForecast;
import com.minghui_liu.android.lemonweather.model.weather.Main;
import com.minghui_liu.android.lemonweather.model.weather.WeatherModel;

import java.util.Calendar;
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
    public static final String ARG_UNIT_STRING = "unit_string";
    public static final String APIURL = "http://api.openweathermap.org/data/2.5";
    public static final String APPID = "61da10c389e32199663fc074efd9b30b";
    private static final String TAG = "LemonWeather";

    private Date mDate;
    private int mCityId;
    private String mUnit;
    private String mTempUnit;
    private String mWindUnit;
    private int mOrientation;

    // Portrait orientation view members
    private TextView mDescriptionTextView;
    private TextView mTemperatureTextView;
    private ImageView mIconImageView;
    private TextView mDateTextView;
    private TextView mDayTextView;

    private TextView[] mDailyForecastTempTextViews;
//    private TextView[] mDailyForecastDescriptionTextViews;
    private ImageView[] mDailyForecastIconImageViews;
    private TextView[] mDailyForecastDateTextViews;

    // Landscape orientation view members
    private TextView mCloudTextView;
    private TextView mPressureTextView;
    private TextView mHumidityTextView;
    private TextView mWindTextView;
    private TextView mSunriseTextView;
    private TextView mSunsetTextView;

    private TextView[] mHourlyForecastTempTextViews;
    private ImageView[] mHourlyForecastIconImageViews;
    private TextView[] mHourlyForecastTimeTextViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        mCityId = getArguments().getInt(ARG_CITY_ID);
        String cityname = getArguments().getString(ARG_CITY_NAME);
        mUnit = getArguments().getString(ARG_UNIT_STRING);
        mDate = new Date();
        mTempUnit = mUnit.equals("metric") ? "°C" : "°F";
        mWindUnit = mUnit.equals("metric") ? " m/s" : " miles/h";
        mOrientation = getResources().getConfiguration().orientation;
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(APIURL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        WeatherAPI weatherService = restAdapter.create(WeatherAPI.class);

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mDescriptionTextView = (TextView)v.findViewById(R.id.description_text_view);
            mTemperatureTextView = (TextView)v.findViewById(R.id.temperature_text_view);
            mIconImageView = (ImageView)v.findViewById(R.id.icon_image_view);
            mDateTextView = (TextView)v.findViewById(R.id.date_text_view);
            mDayTextView = (TextView)v.findViewById(R.id.day_text_view);

            mDailyForecastTempTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_temp_text_view),
                    (TextView)v.findViewById(R.id.day2_temp_text_view),
                    (TextView)v.findViewById(R.id.day3_temp_text_view),
                    (TextView)v.findViewById(R.id.day4_temp_text_view)};

//            mDailyForecastDescriptionTextViews = new TextView[] {(TextView)v.findViewById(R.id.day1_description_text_view),
//                    (TextView)v.findViewById(R.id.day2_description_text_view),
//                    (TextView)v.findViewById(R.id.day3_description_text_view),
//                    (TextView)v.findViewById(R.id.day4_description_text_view)};

            mDailyForecastIconImageViews = new ImageView[] {(ImageView)v.findViewById(R.id.day1_icon_image_view),
                    (ImageView)v.findViewById(R.id.day2_icon_image_view),
                    (ImageView)v.findViewById(R.id.day3_icon_image_view),
                    (ImageView)v.findViewById(R.id.day4_icon_image_view)};

            mDailyForecastDateTextViews = new TextView[] { (TextView)v.findViewById(R.id.day1_date_text_view),
                    (TextView)v.findViewById(R.id.day2_date_text_view),
                    (TextView)v.findViewById(R.id.day3_date_text_view),
                    (TextView)v.findViewById(R.id.day4_date_text_view)};

            mDateTextView.setText(DateFormat.format("MMM dd, yyyy", mDate));
            mDayTextView.setText(DateFormat.format("EEEE", mDate));

            weatherService.getWeatherById(String.valueOf(mCityId), mUnit, APPID, new Callback<WeatherModel>() {
                @Override
                public void success(WeatherModel weatherM, Response response) {
                    double temp = weatherM.getMain().getTemp();
                    String description = weatherM.getWeather().get(0).getDescription();
                    int icon = Integer.parseInt(weatherM.getWeather().get(0).getIcon().substring(0, 2));

                    //mTemperatureTextView.setText(getString(R.string.weather_temperature, temp, mUnit.equals("metric") ? "°C" : "°F"));
                    mTemperatureTextView.setText(String.format("%.1f", temp) + mTempUnit);
                    mDescriptionTextView.setText(description);

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

            weatherService.getDailyForecastById(String.valueOf(mCityId), 4, mUnit, APPID, new Callback<DailyForecast>() {
                @Override
                public void success(DailyForecast forecast, Response response) {
                    List<com.minghui_liu.android.lemonweather.model.dailyforecast.List> days = forecast.getList();

                    for (int i = 0; i < 4; i++) {
                        double min = days.get(i).getTemp().getMin();
                        double max = days.get(i).getTemp().getMax();
                        int icon = Integer.parseInt(days.get(i).getWeather().get(0).getIcon().substring(0, 2));

                        //mDailyForecastTempTextViews[i].setText(getString(R.string.forecast_temperature, (int) min, (int) max, mUnit.equals("metric") ? "°C" : "°F"));
                        mDailyForecastTempTextViews[i].setText(String.format("%d", (int) min) + mTempUnit + " ~ " + String.format("%d", (int) max) + mTempUnit);
//                        mDailyForecastDescriptionTextViews[i].setText(days.get(i).getWeather().get(0).getDescription());
                        mDailyForecastDateTextViews[i].setText(DateFormat.format("MMM dd, EEE", getDayInFuture(mDate, i + 1)));

                        switch (icon) {
                            case 1:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_sunny);
                                break;
                            case 2:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_partly_cloudy);
                                break;
                            case 3:
                            case 4:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_cloudy);
                                break;
                            case 9:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_shower);
                                break;
                            case 10:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_rain);
                                break;
                            case 11:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_storm);
                                break;
                            case 13:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_snow);
                                break;
                            case 50:
                                mDailyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_fog);
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
        } else if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mCloudTextView = (TextView) v.findViewById(R.id.cloud_text);
            mPressureTextView = (TextView) v.findViewById(R.id.pressure_text);
            mHumidityTextView = (TextView) v.findViewById(R.id.humidity_text);
            mWindTextView = (TextView) v.findViewById(R.id.wind_text);
            mSunriseTextView = (TextView) v.findViewById(R.id.sunrise_text);
            mSunsetTextView = (TextView) v.findViewById(R.id.sunset_text);

            mHourlyForecastTempTextViews = new TextView[] {(TextView)v.findViewById(R.id.time1_temp),
                    (TextView)v.findViewById(R.id.time2_temp),
                    (TextView)v.findViewById(R.id.time3_temp),
                    (TextView)v.findViewById(R.id.time4_temp)};

            mHourlyForecastIconImageViews = new ImageView[] {(ImageView)v.findViewById(R.id.time1_icon),
                    (ImageView)v.findViewById(R.id.time2_icon),
                    (ImageView)v.findViewById(R.id.time3_icon),
                    (ImageView)v.findViewById(R.id.time4_icon)};

            mHourlyForecastTimeTextViews = new TextView[] { (TextView)v.findViewById(R.id.time1_time),
                    (TextView)v.findViewById(R.id.time2_time),
                    (TextView)v.findViewById(R.id.time3_time),
                    (TextView)v.findViewById(R.id.time4_time)};

            weatherService.getWeatherById(String.valueOf(mCityId), mUnit, APPID, new Callback<WeatherModel>() {
                @Override
                public void success(WeatherModel weatherModel, Response response) {
                    Main main = weatherModel.getMain();
                    double pressure = main.getPressure();
                    double humidity = main.getHumidity();
                    int clouds = weatherModel.getClouds().getAll();
                    Date sunrise = new Date(weatherModel.getSys().getSunrise() * 1000);
                    Date sunset = new Date(weatherModel.getSys().getSunset() * 1000);
                    double windspeed = weatherModel.getWind().getSpeed();
                    double winddeg = weatherModel.getWind().getDeg();

                    mCloudTextView.setText(clouds + "%");
                    mPressureTextView.setText(pressure + " hPa");
                    mHumidityTextView.setText(humidity + "%");
                    mWindTextView.setText((int)winddeg + "° " + windspeed + mWindUnit);
                    mSunriseTextView.setText(DateFormat.format("hh:mm a", sunrise));
                    mSunsetTextView.setText(DateFormat.format("hh:mm a", sunset));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failed to get weather");
                }
            });

            weatherService.getHourlyForecastById(String.valueOf(mCityId), 4, mUnit, APPID, new Callback<HourlyForecast>() {
                @Override
                public void success(HourlyForecast hourlyForecast, Response response) {
                    List<com.minghui_liu.android.lemonweather.model.hourlyforecast.List> times = hourlyForecast.getList();

                    for (int i = 0; i < 4; i++) {
                        double min = times.get(i).getMain().getTempMin();
                        double max = times.get(i).getMain().getTempMax();
                        int icon = Integer.parseInt(times.get(i).getWeather().get(0).getIcon().substring(0, 2));

                        mHourlyForecastTempTextViews[i].setText(String.format("%d", (int) min) + mTempUnit + " ~ " + String.format("%d", (int) max) + mTempUnit);
                        Date time = new Date(times.get(i).getDt() * 1000);
                        mHourlyForecastTimeTextViews[i].setText(DateFormat.format("hh:mm a", time));

                        switch (icon) {
                            case 1:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_sunny);
                                break;
                            case 2:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_partly_cloudy);
                                break;
                            case 3:
                            case 4:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_cloudy);
                                break;
                            case 9:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_shower);
                                break;
                            case 10:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_rain);
                                break;
                            case 11:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_storm);
                                break;
                            case 13:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_snow);
                                break;
                            case 50:
                                mHourlyForecastIconImageViews[i].setImageResource(R.drawable.ic_lemon_fog);
                                break;
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "failed to get hourly forecast");
                }
            });

        }


        return v;
    }

    private Date getDayInFuture(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, num);
        return calendar.getTime();
    }
}
