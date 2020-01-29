package com.logiic.weather.services;

import com.logiic.weather.models.darksky.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("{key}/{latitude},{longitude}")
    Call<Forecast> getCurrentWeather(
        @Path("key") String apiKey,
        @Path("latitude") double latitude,
        @Path("longitude") double longitude
    );

}
