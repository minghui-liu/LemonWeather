package com.minghui_liu.android.lemonweather.api;

import com.minghui_liu.android.lemonweather.model.WeatherModel;

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

}
