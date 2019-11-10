package ru.chizhikov.weather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class CityRestModel {
    @SerializedName("id") public  long id;
    @SerializedName("name") public String nameCity;
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("country") public String country;
    @SerializedName("population") public long population;
    @SerializedName("timezone") public long timezone;
    @SerializedName("sunrise") public long sunrise;
    @SerializedName("sunset") public long sunset;
}
