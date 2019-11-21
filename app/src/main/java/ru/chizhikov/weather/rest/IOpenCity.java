package ru.chizhikov.weather.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.chizhikov.weather.rest.entities.CityRequestRestModel;

public interface IOpenCity {
    @GET("data/2.5/weather")
    Call<CityRequestRestModel> loadWeather(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("appid") String keyApi);
}