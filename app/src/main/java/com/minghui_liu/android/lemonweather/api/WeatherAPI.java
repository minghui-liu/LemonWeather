package com.minghui_liu.android.lemonweather.api;

import com.minghui_liu.android.lemonweather.model.dailyforecast.DailyForecast;
import com.minghui_liu.android.lemonweather.model.hourlyforecast.HourlyForecast;
import com.minghui_liu.android.lemonweather.model.weather.WeatherModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kevin on 10/29/15.
 */
public interface WeatherAPI {
    @GET("/weather")
    void getWeatherByName(@Query("q") String city, @Query("units") String units, @Query("appid") String appid, Callback<WeatherModel> callBack);

    @GET("/weather")
    void getWeatherById(@Query("id") String id, @Query("units") String units, @Query("appid") String appid, Callback<WeatherModel> callBack);

    @GET("/forecast")
    void getHourlyForecastByName(@Query("q") String city, @Query("units") String units, @Query("appid") String appid, Callback<HourlyForecast> callBack);

    @GET("/forecast")
    void getHourlyForecastById(@Query("id") String id, @Query("cnt") int count, @Query("units") String units, @Query("appid") String appid, Callback<HourlyForecast> callBack);

    @GET("/forecast/daily")
    void getDailyForecastByName(@Query("q") String city, @Query("units") String units, @Query("appid") String appid, Callback<DailyForecast> callBack);

    @GET("/forecast/daily")
    void getDailyForecastById(@Query("id") String id, @Query("cnt") int count, @Query("units") String units, @Query("appid") String appid, Callback<DailyForecast> callBack);
}
