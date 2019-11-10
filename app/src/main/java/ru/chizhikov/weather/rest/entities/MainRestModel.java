package ru.chizhikov.weather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class MainRestModel {
    @SerializedName("temp") public float temp;
    @SerializedName("temp_min") public float temp_min;
    @SerializedName("temp_max") public float temp_max;
    @SerializedName("pressure") public int pressure;
    @SerializedName("sea_level") public int sea_level;
    @SerializedName("grnd_level") public int grnd_level;
    @SerializedName("humidity") public int humidity;
    @SerializedName("temp_kf") public float temp_kf;
}
