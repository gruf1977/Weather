package ru.chizhikov.weather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class CityRequestRestModel {
    @SerializedName("name")
    public String city;
}