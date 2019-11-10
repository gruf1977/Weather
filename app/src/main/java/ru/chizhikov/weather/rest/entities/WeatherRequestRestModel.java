package ru.chizhikov.weather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("list") public ListRestModel[] listRestModels;
    @SerializedName("city") public CityRestModel city;
    @SerializedName("cod") public String cod;
    @SerializedName("message") public int message;
    @SerializedName("cnt") public int cnt;
}
