package com.ajayh.rainstats.network;

import com.ajayh.rainstats.datamodel.WeatherResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ajay.Harwani.
 */

public interface APIService {

    @GET("Rainfall-England.json")
    Call<ArrayList<WeatherResponse>> getWeatherForEngland();

    @GET("Rainfall-UK.json")
    Call<ArrayList<WeatherResponse>> getWeatherForUK();

    @GET("Rainfall-Scotland.json")
    Call<ArrayList<WeatherResponse>> getWeatherForScotland();

    @GET("Rainfall-Wales.json")
    Call<ArrayList<WeatherResponse>> getWeatherForWales();
}

