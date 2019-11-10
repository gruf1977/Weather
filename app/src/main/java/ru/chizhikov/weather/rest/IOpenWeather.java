package ru.chizhikov.weather.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.chizhikov.weather.rest.entities.WeatherRequestRestModel;

public interface IOpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("lang") String lang,
                                              @Query("units") String units,
                                              @Query("appid") String keyApi);
}
