package com.minghui_liu.android.lemonweather.api;

import com.minghui_liu.android.lemonweather.model.forcast.Forecast;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kevin on 12/8/15.
 */
public interface ForcastAPI {
    @GET("/forcast")
    void getForecastByName(@Query("q") String city, @Query("units") String units, @Query("appid") String appid, Callback<Forecast> callBack);

    @GET("/forcast")
    void getForecastById(@Query("id") String id, @Query("units") String units, @Query("appid") String appid, Callback<Forecast> callBack);

}
